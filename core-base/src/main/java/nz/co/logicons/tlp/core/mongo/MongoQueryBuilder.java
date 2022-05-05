package nz.co.logicons.tlp.core.mongo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import javax.validation.ValidationException;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;

import nz.co.logicons.tlp.core.common.QueryConstants;
import nz.co.logicons.tlp.core.enums.ChildSchemaType;
import nz.co.logicons.tlp.core.enums.SearchOperator;
import nz.co.logicons.tlp.core.genericmodels.RegularExpressionHelper;
import nz.co.logicons.tlp.core.genericmodels.nodes.Node;
import nz.co.logicons.tlp.core.genericmodels.schemas.ChildSchema;
import nz.co.logicons.tlp.core.genericmodels.schemas.DateSchema;
import nz.co.logicons.tlp.core.genericmodels.schemas.DateTimeSchema;
import nz.co.logicons.tlp.core.genericmodels.schemas.TimeSchema;
import nz.co.logicons.tlp.core.genericmodels.views.SearchParam;

public class MongoQueryBuilder
{
  private static final Logger LOGGER = LoggerFactory.getLogger(MongoQueryBuilder.class);

  public MongoQueryBuilder()
  {
    LOGGER.info("{} {}", this.getClass().getSimpleName(), this.hashCode());
  }

  public DBObject buildSearchQuery(List<SearchParam> searchParams)
  {
    QueryBuilder queryBuilder = new QueryBuilder();

    for (SearchParam searchParam : searchParams)
    {
      // skip if no child schema type or invalid search operator for child schema type
      if (searchParam.getChildschematype() == null
          || !Arrays.asList(searchParam.getChildschematype().getSearchOperators()).contains(
              searchParam.getSearchoperator()))
      {
        continue;
      }

      if (searchParam.getSearchparams() != null && searchParam.getSearchparams().size() > 0)
      {

        if (searchParam.getChildschematype() == ChildSchemaType.listinlineschema)
        {
          DBObject dbObject = buildSearchQuery(searchParam.getSearchparams());
          if (dbObject.keySet().size() > 0)
          {
            queryBuilder.put(searchParam.getFieldid());
            queryBuilder.elemMatch(dbObject);
          }
          continue;
        }

        if (searchParam.getChildschematype() == ChildSchemaType.linkedschema)
        {
          // let make sure that there is a subquery and not just blank fields
          if (buildSearchQuery(searchParam.getSearchparams()).keySet().size() > 0)
          {
            throw new ValidationException("mongo query builder error");
            // DocumentOperation documentOperation = documentOperationProvider.get();
            // Collection<DocumentNode> documentNodes = documentOperation.getDocuments(searchParam.getSubtype(),
            // searchParam.getSearchparams(), null, true, systemUser);
            // List<String> ids = new ArrayList<>();
            // for (DocumentNode documentNode : documentNodes)
            // {
            // ids.add(documentNode.getId());
            // }
            // queryBuilder.put(searchParam.getFieldid());
            // queryBuilder.in(ids);
            // continue;
          }
        }
      }

      // skip for null values
      if (searchParam.getValue() == null || searchParam.getValue().isNull())
      {
        continue;
      }
      if (searchParam.getSearchoperator() == SearchOperator.range)
      {
        processRangeSearchParam(queryBuilder, searchParam, SearchOperator.greaterthanequals);
        processRangeSearchParam(queryBuilder, searchParam, SearchOperator.lessthanequals);
        continue;
      }
      if (searchParam.getChildschematype() == ChildSchemaType.liststringschema
          || searchParam.getChildschematype() == ChildSchemaType.listlinkedschema)
      {
        processListStringOrLinked(queryBuilder, searchParam);
        continue;
      }

      processSearchParam(queryBuilder, searchParam);
    }

    DBObject dbObject = queryBuilder.get();
    return dbObject;
  }

  private void processListStringOrLinked(QueryBuilder queryBuilder, SearchParam searchParam)
  {
    Iterator<JsonNode> iterator = searchParam.getValue().elements();
    while (iterator.hasNext())
    {
      JsonNode jsonNode = iterator.next();
      if (jsonNode != null && !jsonNode.isNull())
      {
        ChildSchemaType childschematype = searchParam.getChildschematype() == ChildSchemaType.liststringschema
            ? ChildSchemaType.stringschema
            : ChildSchemaType.linkedschema;
        SearchParam searchParam2 = new SearchParam(searchParam.getFieldid(), searchParam.getSearchoperator(),
            childschematype, jsonNode);
        processSearchParam(queryBuilder, searchParam2);
      }
    }
  }

  private void processSearchParam(QueryBuilder queryBuilder, SearchParam searchParam)
  {
    ChildSchemaType schemaType = searchParam.getChildschematype();
    ChildSchema<?> childSchema = null;

    if (schemaType.getId().compareToIgnoreCase("numberschema") == 0)
    {
      childSchema = schemaType.getSchemaInstance();
      childSchema.setScale(3);
    }
    else
    {
      childSchema = schemaType.getSchemaInstance();
    }

    if (searchParam.getSearchoperator() == SearchOperator.notexists)
    {
      processGeneric(queryBuilder, searchParam, null);
      return;
    }

    Node<?> node = childSchema.deserializeNode(searchParam.getValue());

    if (node == null || node.getValue() == null)
    {
      return;
    }

    Object value = node.getValue();

    switch (searchParam.getChildschematype())
    {

      case idschema:
      case autoidschema:
      case stringschema:
      case linkedschema:
        queryBuilder = queryBuilder.put(searchParam.getFieldid());
        if (value == null || StringUtils.equalsIgnoreCase(value.toString(), QueryConstants.nil))
        {
          queryBuilder = queryBuilder.is(null);
        }
        else
        {
          Pair<Boolean, String> pair = RegularExpressionHelper.getRegularExpression(value.toString());
          if (pair.getKey())
          {
            queryBuilder = queryBuilder.regex(Pattern.compile(pair.getValue(), Pattern.CASE_INSENSITIVE));
          }
          else
          {
            queryBuilder = queryBuilder.is(pair.getValue());
          }
        }
        break;
      case dateschema:
        value = DateSchema.serialize((LocalDate) value);
        processGeneric(queryBuilder, searchParam, value);
        break;
      case timeschema:
        value = TimeSchema.serialize((LocalTime) value);
        processGeneric(queryBuilder, searchParam, value);
        break;
      case datetimeschema:
        value = DateTimeSchema.serialize((LocalDateTime) value);
        processGeneric(queryBuilder, searchParam, value);
        break;
      case sequenceidschema:
      case sequenceschema:
      case moneyschema:
      case numberschema:
        value = ((BigDecimal) value).doubleValue();
        processGeneric(queryBuilder, searchParam, value);
        break;
      case boolschema:
        if (!StringUtils.isBlank(searchParam.getValue().asText())
            && !StringUtils.equals("null", searchParam.getValue().asText()))
        {
          queryBuilder = queryBuilder.put(searchParam.getFieldid());
          queryBuilder = queryBuilder.is(value);
        }
        break;
      default:
        break;
    }
  }

  private void processGeneric(QueryBuilder queryBuilder, SearchParam searchParam, Object value)
  {
    DBObject notexists = null;
    if (StringUtils.contains(searchParam.getSearchoperator().getDisplaytext(), "!E"))
      notexists = new BasicDBObject(searchParam.getFieldid(), new BasicDBObject("$exists", false));
    else
      queryBuilder = queryBuilder.put(searchParam.getFieldid());

    switch (searchParam.getSearchoperator())
    {
      case greaterthan:
        queryBuilder = queryBuilder.greaterThan(value);
        break;
      case greaterthanequals:
        queryBuilder = queryBuilder.greaterThanEquals(value);
        break;
      case lessthan:
        queryBuilder = queryBuilder.lessThan(value);
        break;
      case lessthanequals:
        queryBuilder = queryBuilder.lessThanEquals(value);
        break;
      case exists:
        queryBuilder = queryBuilder.exists(value);
        break;
      case notequals:
        queryBuilder = queryBuilder.notEquals(value);
        break;
      case notexists:
        queryBuilder = queryBuilder.or(notexists, new BasicDBObject(searchParam.getFieldid(), null));
        break;
      case notexistsandequals:
        queryBuilder = queryBuilder.or(notexists, new BasicDBObject(searchParam.getFieldid(), value));
        break;
      case notexistsandlessthan:
        queryBuilder = queryBuilder.or(notexists,
            new BasicDBObject(searchParam.getFieldid(), new BasicDBObject("$lt", value)));
        break;
      case notexistsandgreaterthan:
        queryBuilder = queryBuilder.or(notexists,
            new BasicDBObject(searchParam.getFieldid(), new BasicDBObject("$gt", value)));
        break;
      case notexistsandlessthanequal:
        queryBuilder = queryBuilder.or(notexists,
            new BasicDBObject(searchParam.getFieldid(), new BasicDBObject("$lte", value)));
        break;
      case notexistsandgreaterthanequal:
        queryBuilder = queryBuilder.or(notexists,
            new BasicDBObject(searchParam.getFieldid(), new BasicDBObject("$gte", value)));
        break;
      default:
        queryBuilder = queryBuilder.is(value);
        break;
    }
  }

  private void processRangeSearchParam(QueryBuilder queryBuilder, SearchParam searchParam,
    SearchOperator searchOperator)
  {
    JsonNode value = null;
    switch (searchOperator)
    {
      case greaterthan:
      case greaterthanequals:
        value = searchParam.getValue().get("min");
        break;
      case lessthan:
      case lessthanequals:
        value = searchParam.getValue().get("max");
        break;
      default:
        break;
    }

    if (value != null && !value.isNull())
    {
      SearchParam subSearchParam = new SearchParam(searchParam.getFieldid(), searchOperator,
          searchParam.getChildschematype(), value);
      processSearchParam(queryBuilder, subSearchParam);
    }
  }

  public DBObject buildSubSearchQuery(List<SearchParam> searchParams, String listinlinefieldid)
  {
    List<SearchParam> subSearchParams = new ArrayList<SearchParam>();
    for (SearchParam searchParam : searchParams)
    {
      if (StringUtils.equals(listinlinefieldid, searchParam.getFieldid()))
      {
        for (SearchParam level2SearchParam : searchParam.getSearchparams())
        {
          SearchParam subSearchParam = new SearchParam(listinlinefieldid + "." + level2SearchParam.getFieldid(),
              level2SearchParam.getSearchoperator(), level2SearchParam.getChildschematype(),
              level2SearchParam.getValue());
          subSearchParams.add(subSearchParam);
        }
        return buildSearchQuery(subSearchParams);
      }
    }
    return new BasicDBObject();
  }

}

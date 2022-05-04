package nz.co.logicons.tlp.core.mongo;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.InjectableValues;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import nz.co.logicons.tlp.core.enums.JsonErrorType;
import nz.co.logicons.tlp.core.exceptions.ValidationException;
import nz.co.logicons.tlp.core.genericmodels.base.GenericSerializable;
import nz.co.logicons.tlp.core.genericmodels.jackson.DocumentNodeSerializer;
import nz.co.logicons.tlp.core.genericmodels.jackson.serializationprofile.SerializationProfile;
import nz.co.logicons.tlp.core.genericmodels.nodes.DocumentNode;
import nz.co.logicons.tlp.core.genericmodels.permissions.PermissionEvaluatorProvider;
import nz.co.logicons.tlp.core.genericmodels.schemas.ChildSchema;
import nz.co.logicons.tlp.core.genericmodels.schemas.DocumentSchema;
import nz.co.logicons.tlp.core.genericmodels.schemas.InlineSchema;
import nz.co.logicons.tlp.core.genericmodels.schemas.StringSchema;
import nz.co.logicons.tlp.core.genericmodels.views.SearchField;
import nz.co.logicons.tlp.core.genericmodels.views.SearchParam;
import nz.co.logicons.tlp.core.genericmodels.views.SortField;

public class TransformOperation
{
  private static Logger log = LoggerFactory.getLogger(TransformOperation.class);

  private static TypeReference<List<DocumentNode>> documentNodeListTypeReference = new TypeReference<List<DocumentNode>>()
  {
  };

  private static TypeReference<List<SearchField>> searchFieldListTypeReference = new TypeReference<List<SearchField>>()
  {
  };

  private static TypeReference<List<SearchParam>> searchParamListTypeReference = new TypeReference<List<SearchParam>>()
  {
  };

  private static TypeReference<List<SortField>> sortFieldListTypeReference = new TypeReference<List<SortField>>()
  {
  };

  private static TypeReference<List<JsonNode>> jsonNodeListTypeReference = new TypeReference<List<JsonNode>>()
  {
  };
  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private MongoDatastore datastore;

  @Autowired
  private PermissionEvaluatorProvider permissionEvaluatorProvider;

  public TransformOperation()
  {
    log.info("{} {}", this.getClass().getSimpleName(), this.hashCode());
  }

  public DocumentSchema createDocumentSchema(String json)
  {
    DocumentSchema documentSchema = create(json, DocumentSchema.class);
    documentSchema.init(datastore, getAuditInlineSchema(), getParentIdSchema(), getChildIdSchema(),
        permissionEvaluatorProvider);
    return documentSchema;
  }

  /**
   * 
   * @return Audit schema.
   * @throws JsonProcessingException .
   */
  private InlineSchema getAuditInlineSchema()
  {
    String json = "{\"_id\":\"audit\",\"type\":\"inlineschema\",\"subtype\":\"(audit)\"}";
    return (InlineSchema) create(json, ChildSchema.class);
  }

  /**
   * 
   * @return parentid schema.
   */
  private StringSchema getParentIdSchema()
  {
    String json = "{\"_id\":\"" + GenericSerializable.PARENT_ID_FIELD_NAME + "\",\"type\":\"stringschema\"}";
    return (StringSchema) create(json, ChildSchema.class);
  }

  /**
   * 
   * @return childid schema.
   */
  private StringSchema getChildIdSchema()
  {
    String json = "{\"_id\":\"" + GenericSerializable.CHILD_ID_FIELD_NAME + "\",\"type\":\"stringschema\"}";
    return (StringSchema) create(json, ChildSchema.class);
  }

  public DocumentNode createDocumentNode(String json, DocumentSchema documentSchema/* , User user */)
  {
    try
    {
      // DocumentSchema documentSchema = datastoreProvider.get().getSchema(schemaid);

      // inject the schema.
      InjectableValues injectableValues = new InjectableValues.Std()
          .addValue(DocumentSchema.class, documentSchema);

      DocumentNode documentNode = objectMapper.reader(DocumentNode.class).with(injectableValues)
          .readValue(json);
      return documentNode;
    }
    catch (Exception e)
    {
      log.error(e.getMessage(), e);
      throw new ValidationException(JsonErrorType.JsonTransformationError);
    }
  }

  public List<DocumentNode> createDocumentNodes(String jsons, DocumentSchema documentSchema)
  {
    try
    {
      // DocumentSchema documentSchema = datastoreProvider.get().getSchema(schemaid);
      // inject the schema.
      InjectableValues injectableValues = new InjectableValues.Std()
          .addValue(DocumentSchema.class, documentSchema);

      List<DocumentNode> documentNodes = objectMapper.reader(documentNodeListTypeReference)
          .with(injectableValues)
          .readValue(jsons);
      return documentNodes;
    }
    catch (IOException e)
    {
      log.error(e.getMessage(), e);
      throw new ValidationException(JsonErrorType.JsonTransformationError);
    }
  }

  public List<SearchParam> createSearchParams(String json)
  {
    try
    {
      if (StringUtils.isBlank(json))
      {
        return new ArrayList<SearchParam>();
      }
      List<SearchParam> searchParams = objectMapper.reader(searchParamListTypeReference)
          .readValue(json);
      return searchParams;
    }
    catch (IOException e)
    {
      log.error(e.getMessage(), e);
      throw new ValidationException(JsonErrorType.JsonTransformationError);
    }
  }

  public List<SortField> createSortFields(String sortjson)
  {
    try
    {
      List<SortField> sortFields = objectMapper.reader(sortFieldListTypeReference)
          .readValue(sortjson);
      return sortFields;
    }
    catch (IOException e)
    {
      log.error(e.getMessage(), e);
      throw new ValidationException(JsonErrorType.JsonTransformationError);
    }
  }

  /**
   * Create JsonNodes fields from json.
   * 
   * @param searchjson json representing search fields.
   * @return search fields.
   */
  public List<JsonNode> createJsonNodes(String json)
  {
    try
    {
      List<JsonNode> jsonNodes = objectMapper.reader(jsonNodeListTypeReference)
          .readValue(json);
      return jsonNodes;
    }
    catch (IOException e)
    {
      log.error(e.getMessage(), e);
      throw new ValidationException(JsonErrorType.JsonTransformationError);
    }
  }

  /**
   * Create JsonNode from json.
   * 
   * @param json .
   * @return .
   */
  public JsonNode createJsonNodeFromJsonString(String json)
  {
    try
    {
      if (StringUtils.isNotBlank(json))
      {
        return objectMapper.reader(JsonNode.class)
            .readValue(json);
      }
      else
      {
        return null;
      }
    }
    catch (IOException e)
    {
      log.error(e.getMessage(), e);
      throw new ValidationException(JsonErrorType.JsonTransformationError);
    }
  }

  public JsonNode createJsonNodeFromJsonStringAllowSingleQuote(String json)
  {
    try
    {
      if (StringUtils.isNotBlank(json))
      {
        ObjectMapper mapper = objectMapper;
        mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        return mapper.reader(JsonNode.class)
            .readValue(json);
      }
      else
      {
        return null;
      }
    }
    catch (IOException e)
    {
      log.error(e.getMessage(), e);
      throw new ValidationException(JsonErrorType.JsonTransformationError);
    }
  }

  /**
   * Create JsonNode from date time.
   * 
   * @param dateTime .
   * @return .
   */
  public JsonNode createJsonNode(DateTime dateTime)
  {
    return objectMapper.createObjectNode().numberNode(dateTime.getMillis());
  }

  /**
   * Create JsonNode boolean.
   * 
   * @param value .
   * @return .
   */
  public JsonNode createJsonNode(Boolean value)
  {
    return objectMapper.createObjectNode().booleanNode(value);
  }

  /**
   * Create JsonNode from date time.
   * 
   * @param dateTime .
   * @return .
   */
  public JsonNode createJsonNode(String value)
  {
    return objectMapper.createObjectNode().textNode(value);
  }

  /**
   * Create JsonNode from big decimal.
   * 
   * @param dateTime .
   * @return .
   */
  public JsonNode createJsonNode(BigDecimal bigDecimal)
  {
    return objectMapper.createObjectNode().numberNode(bigDecimal.doubleValue());
  }

  /**
   * Create JsonNode from double.
   * 
   * @param value .
   * @return .
   */
  public JsonNode createJsonNode(double value)
  {
    return objectMapper.createObjectNode().numberNode(value);
  }

  @SuppressWarnings("deprecation")
  public <T> T create(String json, /* User user, */ Class<T> clazz)
  {
    try
    {
      // ObjectMapper objectMapper = objectMapperProvider.get(user);
      T t = objectMapper.reader(clazz).readValue(json);
      return t;
    }
    catch (Exception e)
    {
      log.error(e.getMessage(), e);
      throw new ValidationException(JsonErrorType.JsonTransformationError);
    }
  }

  public String getJson(Object object/* , User user */)
  {
    return getJson(object, /* user, */ null);
  }
  public String getJson(Object object, /* User user, */SerializationProfile serializationProfile)
  {
    try
    {
      DocumentNodeSerializer.setSerializationProfile(serializationProfile);
      return objectMapper.writeValueAsString(object);
    }
    catch (JsonProcessingException e)
    {
      log.error(e.getMessage(), e);
      throw new ValidationException(JsonErrorType.JsonTransformationError);
    }
    finally
    {
      DocumentNodeSerializer.setSerializationProfile(null);
    }
  }
  // public MongoTemplate getMT()
  // {
  // return mongoTemplate;
  // }
}

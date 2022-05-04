package nz.co.logicons.tlp.core.mongo;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;
import com.mongodb.util.JSON;

import nz.co.logicons.tlp.core.business.operations.SequenceOperation;
import nz.co.logicons.tlp.core.enums.JsonErrorType;
import nz.co.logicons.tlp.core.exceptions.ValidationException;
import nz.co.logicons.tlp.core.genericmodels.DisplayTextComparator;
import nz.co.logicons.tlp.core.genericmodels.base.GenericInterface;
import nz.co.logicons.tlp.core.genericmodels.base.GenericSerializable;
import nz.co.logicons.tlp.core.genericmodels.nodes.DocumentNode;
import nz.co.logicons.tlp.core.genericmodels.nodevalue.Attachment;
import nz.co.logicons.tlp.core.genericmodels.operations.SchemaOperation;
import nz.co.logicons.tlp.core.genericmodels.operations.ValidateSchemaOperation;
import nz.co.logicons.tlp.core.genericmodels.permissions.Role;
import nz.co.logicons.tlp.core.genericmodels.schemas.DocumentSchema;
import nz.co.logicons.tlp.core.genericmodels.views.SearchParam;
import nz.co.logicons.tlp.core.genericmodels.views.SortField;

@Component
public class MongoDatastore
{
  private static Logger log = LoggerFactory.getLogger(MongoDatastore.class);
  @Autowired
  private MongoTemplate mongoTemplate;

  @Autowired
  private TransformOperation transformOperation;

  @Autowired
  private SchemaOperation schemaOperation;

  @Autowired
  private ValidateSchemaOperation validateSchemaOperation;

  @Autowired
  private MongoQueryBuilder mongoQueryBuilder;

  /** Keep schema in memory for quick access. */
  private Map<String, DocumentSchema> documentSchemas = new HashMap<>();

  /** These are the default schemas as specified in DefaultSchemas.json */
  // private Map<String, DocumentSchema> defaultDocumentSchemas = new HashMap<>();

  /** Keep schema in views for quick access. */
  // private Map<String, View> views = new HashMap<>();

  /** Keep schema in roles for quick access. */
  private Map<String, Role> roles = new HashMap<>();

  public Collection<DocumentNode> getDocuments(String schemaid, List<SearchParam> searchParams,
    List<SortField> sortFields, int skip, int limit)
  {

    if (searchParams == null)
    {
      searchParams = new ArrayList<>();
    }

    String jsons = search(schemaid, searchParams, sortFields, skip, limit);
    DocumentSchema schema = getPutDocumentSchema(schemaid);
    return transformOperation.createDocumentNodes(jsons, schema);
  }

  public String getDocumentsInStr(String schemaid, List<SearchParam> searchParams,
    List<SortField> sortFields, int skip, int limit)
  {

    if (searchParams == null)
    {
      searchParams = new ArrayList<>();
    }

    String jsons = search(schemaid, searchParams, sortFields, skip, limit);
    return jsons;
  }

  private String search(String schemaid, List<SearchParam> searchParams, List<SortField> sortFields, int skip,
    int limit)
  {
    DB db = mongoTemplate.getDb();
    DBCollection dbCollection = db.getCollection(schemaid);
    DBObject query = mongoQueryBuilder.buildSearchQuery(searchParams);
    DBObject orderBy = getSort(sortFields);
    DBCursor dbCursor = dbCollection.find(query).sort(orderBy).skip(skip).limit(limit);

    log.debug("Running search on {} using query {} sort {} skip {} limit {}", schemaid, query.toString(),
        orderBy.toString(), skip, limit);

    StringBuilder builder = new StringBuilder("[");
    while (dbCursor.hasNext())
    {
      DBObject dbObject = dbCursor.next();
      builder.append(dbObject.toString());
      if (dbCursor.hasNext())
      {
        builder.append(",");
      }
    }
    builder.append("]");

    return builder.toString();
  }

  private BasicDBObject getSort(List<SortField> sortFields)
  {
    BasicDBObject orderBy = new BasicDBObject();
    if (sortFields != null)
    {
      for (SortField sortField : sortFields)
      {
        orderBy.append(sortField.getId(), sortField.getOrder());
      }
    }
    return orderBy;
  }

  public final DocumentSchema getSchema(String schemaid)
  {
    return getSchema(schemaid, true);
  }

  public final DocumentSchema getSchema(String schemaid, boolean errorOnNotFound)
  {
    String json = get("schema", schemaid, false);
    if (json == null)
    {
      throw new ValidationException(JsonErrorType.SchemaNotFound);
    }
    DocumentSchema documentSchema = transformOperation.createDocumentSchema(json);
    if (documentSchema == null && errorOnNotFound)
    {
      throw new ValidationException(JsonErrorType.SchemaNotFound);
    }
    return documentSchema;
  }

  private String get(String schemaid, String documentid, boolean numericid)
  {
    DB db = mongoTemplate.getDb();

    DBObject query;
    if (numericid)
    {
      try
      {
        query = new BasicDBObject("_id", Long.valueOf(documentid));
      }
      catch (NumberFormatException e)
      {
        return null;
      }
    }
    else
    {
      QueryBuilder queryBuilder = new QueryBuilder();
      queryBuilder.put(GenericInterface.ID_FIELD_NAME);
      queryBuilder.is(documentid);
      query = queryBuilder.get();
    }

    DBCursor dbCursor = db.getCollection(schemaid).find(query);
    if (dbCursor.hasNext())
    {
      return dbCursor.next().toString();
    }
    else
    {
      return null;
    }
  }

  public DocumentNode getDocument(String schemaid, String documentid, boolean errorOnNotFound)
  {
    DocumentSchema documentSchema = getPutDocumentSchema(schemaid);
    String json = get(schemaid, documentid, documentSchema.isDocumentIdNumeric());
    if (json == null)
    {
      if (errorOnNotFound)
      {
        throw new ValidationException(JsonErrorType.DocumentNotFound);
      }
      return null;
    }

    return transformOperation.createDocumentNode(json, documentSchema);
  }

  public String getDocumentInStr(String schemaid, String documentid, boolean errorOnNotFound)
  {
    DocumentSchema documentSchema = getPutDocumentSchema(schemaid);
    String json = get(schemaid, documentid, documentSchema.isDocumentIdNumeric());
    if (json == null)
    {
      if (errorOnNotFound)
      {
        throw new ValidationException(JsonErrorType.DocumentNotFound);
      }
      return null;
    }

    return json;
  }

  private DocumentSchema getPutDocumentSchema(String schemaid)
  {
    DocumentSchema documentSchema = documentSchemas.get(schemaid);
    if (documentSchema == null)
    {
      documentSchema = getSchema(schemaid);
      documentSchemas.put(schemaid, documentSchema);
    }
    return documentSchema;
  }

  /**
   * Get role from
   * 
   * @param roleid .
   * @return .
   */
  public final Role getRole(String roleid)
  {
    return getRole(roleid, true);
  }

  /**
   * Get role from
   * 
   * @param roleid .
   *          errorOnNotFound when true then error if not found.
   * @return .
   */
  public final Role getRole(String roleid, boolean errorOnNotFound)
  {
    Role role = roles.get(roleid);
    if (role == null && errorOnNotFound)
    {
      throw new ValidationException(JsonErrorType.RoleNotFound);
    }
    return role;
  }

  /**
   * Add role to cache.
   * 
   * @param role .
   */
  public final void upsertRoleToCache(Role role)
  {
    roles.put(role.getId(), role);
  }

  @PostConstruct
  private void postConstruct()
  {
    initDefaultRoles();

    initSchemasFromDatastore();

    log.info("Document Schemas in map {} size ", documentSchemas.size());
  }

  private void initDefaultRoles()
  {
    upsertRoleToCache(Role.SYSTEM_ROLE);
    upsertRoleToCache(Role.SYSADMIN_ROLE);
    upsertRoleToCache(Role.SITEADMIN_ROLE);
    upsertRoleToCache(Role.FULLACCESS_ROLE);
    upsertRoleToCache(Role.READONLY_ROLE);
    upsertRoleToCache(Role.LOOKUP_ROLE);
  }

  /**
   * Initialize schemas from datastore.
   */
  private void initSchemasFromDatastore()
  {

    List<JsonNode> jsonNodes = transformOperation.createJsonNodes(search(SchemaOperation.SCHEMA_COLLECTION,
        new ArrayList<>(), null, 0, 0));

    for (JsonNode jsonNode : jsonNodes)
    {
      try
      {
        DocumentSchema documentSchema = transformOperation.createDocumentSchema(jsonNode.toString());
        log.debug("{} init schema ", documentSchema.getId());
        // schemaOperation.mergeSchemaFromDefault(documentSchema);
        // upsertSchema(documentSchema);
        documentSchemas.put(documentSchema.getId(), documentSchema);
        schemaOperation.initSequences(documentSchema);
        // schemaOperation.addGeneratedViews(documentSchema);
      }
      catch (Throwable t)
      {
        log.error("Unable to initialize schema: " + jsonNode.toString(), t);
      }
    }

    // Cannot validate till all schemas are loaded due to dependencies.
    for (DocumentSchema documentSchema : getSchemas())
    {
      try
      {
        validateSchemaOperation.validate(documentSchema);
      }
      catch (ValidationException e)
      {
        log.error("Invalid schema {}", documentSchema.getId());
        log.error(transformOperation.getJson(e.getJsonErrors()));
      }
    }
  }

  public MongoDatastore()
  {
    log.info("{} {}", this.getClass().getSimpleName(), this.hashCode());
  }

  private GridFS getGridFS()
  {
    return new GridFS((DB) mongoTemplate.getDb(), "attachment");
  }

  /**
   * Add or edit schema in datastore.
   * 
   * @param documentSchema .
   */
  public final void upsertSchema(DocumentSchema documentSchema)
  {
    documentSchemas.put(documentSchema.getId(), documentSchema);
    upsert(SchemaOperation.SCHEMA_COLLECTION, documentSchema.getId(), transformOperation.getJson(documentSchema));
  }

  private void upsert(String schemaid, String documentid, String json)
  {
    DB db = mongoTemplate.getDb();
    DBObject dbObject = (DBObject) JSON.parse(json);
    db.getCollection(schemaid).save(dbObject);

    /*
     * Log as error if audit.createdby is empty
     */
    ObjectMapper mapper = new ObjectMapper();
    Map<String, Object> map = new HashMap<String, Object>();
    try
    {
      map = mapper.readValue(json, new TypeReference<Map<String, String>>()
      {
      });
      boolean auditExists = map.containsKey("audit");
      boolean createdExists = map.containsKey("audit.createdby");

      /*
       * if (!auditExists && createdExists && !schemaid.contains("("))
       * log.warn("Audit.createdby is empty!");
       */
    }
    catch (IOException e)
    {
      // TODO Auto-generated catch block
      // e.printStackTrace();
      // log.error(e.toString());
    }
    /*
     * Log ends
     */
    catch (Exception e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  /**
   * Add or edit document in store.
   * 
   * @param documentNode .
   */
  public void upsertDocument(DocumentNode documentNode)
  {
    upsert(documentNode.getSchema().getId(), documentNode.getId(), transformOperation.getJson(documentNode));
  }

  /**
   * Get the next sequence.
   * 
   * @param sequenceid .
   * @return .
   */
  public DocumentNode nextSequenceDocument(String sequenceid)
  {
    String json = nextSequence(sequenceid);
    DocumentSchema documentSchema = getPutDocumentSchema(SequenceOperation.SCHEMA);
    return transformOperation.createDocumentNode(json, documentSchema);
  }

  public String nextSequence(String sequenceid)
  {
    DB db = mongoTemplate.getDb();
    DBObject query = new BasicDBObject(GenericSerializable.ID_FIELD_NAME, sequenceid);
    DBObject fields = null;
    DBObject sort = null;
    boolean remove = false;
    DBObject update = new BasicDBObject("$inc", new BasicDBObject(SequenceOperation.NEXT_FIELD, 1));
    boolean returnNew = true;
    boolean upsert = false;
    DBObject dbObject = db.getCollection(SequenceOperation.SCHEMA).findAndModify(query, fields, sort, remove, update,
        returnNew, upsert);
    return dbObject.toString();
  }

  public Attachment uploadAttachment(String fileName, String contentType, InputStream is)
  {
    GridFS gridFS = getGridFS();
    GridFSInputFile gridFSInputFile = gridFS.createFile(is);
    gridFSInputFile.setFilename(fileName);
    gridFSInputFile.setContentType(contentType);
    gridFSInputFile.save();

    Attachment attachment = new Attachment();
    attachment.setFilename(gridFSInputFile.getFilename());
    attachment.setContenttype(gridFSInputFile.getContentType());
    attachment.setAttachmentid(gridFSInputFile.getId().toString());
    attachment.setMD5(gridFSInputFile.getMD5());
    attachment.setLength(gridFSInputFile.getLength());
    return attachment;
  }

  /**
   * @return get all schemas.
   */
  public final Collection<DocumentSchema> getSchemas()
  {
    List<DocumentSchema> list = new ArrayList<>(this.documentSchemas.values());
    Collections.sort(list, new DisplayTextComparator());
    return list;
  }

  /**
   * @return get all schemas.
   */
  public final Collection<DocumentSchema> getSchemasNotInline()
  {
    List<DocumentSchema> list = this.documentSchemas.values().parallelStream()
        .filter(rec -> !rec.isInline() && !(rec.getChildrenMap().size() == 2) && !(rec.getId().startsWith("(") ||
            rec.getId().endsWith("_Card")))
        .collect(Collectors.toList());
    Collections.sort(list, new DisplayTextComparator());
    return list;
  }

  public Attachment getAttachment(String attachmentid)
  {
    Attachment attachment = null;
    try
    {

      GridFS gridFS = getGridFS();
      GridFSDBFile gridFSDBFile = gridFS.find(new ObjectId(attachmentid));
      if (gridFSDBFile != null)
      {
        attachment = new Attachment();
        attachment.setFilename(gridFSDBFile.getFilename());
        attachment.setContenttype(gridFSDBFile.getContentType());
        attachment.setAttachmentid(gridFSDBFile.getId().toString());
        attachment.setMD5(gridFSDBFile.getMD5());
        attachment.setLength(gridFSDBFile.getLength());
        attachment.setInputStream(gridFSDBFile.getInputStream());
      }

    }
    catch (Exception e)
    {
      // do nothing
    }
    return attachment;
  }

  public int getMaxSequence(String schemaid, String field)
  {
    DB db = mongoTemplate.getDb();

    BasicDBObject orderBy = new BasicDBObject();
    orderBy.append(field, -1);

    DBCursor dbCursor = db.getCollection(schemaid).find().sort(orderBy).limit(1);

    if (dbCursor.hasNext())
    {
      Object fieldObj = dbCursor.next().get(field);
      if (fieldObj == null)
      {
        log.warn("Invalid sequence field {} in schema {}", field, schemaid);
        log.warn("If the sequence field exists, check if it contains valid data in {}", schemaid);
      }
      String id = fieldObj.toString();
      try
      {
        // return Integer.valueOf(id);
        return new BigDecimal(id).intValue();
      }
      catch (NumberFormatException e)
      {
        log.error("Invalid numeric id {} in schema {}", id, schemaid);
        throw e;
      }
    }

    return 0;
  }
}

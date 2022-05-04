package nz.co.logicons.tlp.core.genericmodels.operations;

import java.util.Arrays;
import java.util.Collection;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import nz.co.logicons.tlp.core.business.operations.SequenceOperation;
import nz.co.logicons.tlp.core.enums.ChildSchemaType;
import nz.co.logicons.tlp.core.enums.JsonErrorType;
import nz.co.logicons.tlp.core.exceptions.ValidationException;
import nz.co.logicons.tlp.core.genericmodels.schemas.ChildSchema;
import nz.co.logicons.tlp.core.genericmodels.schemas.DocumentSchema;
import nz.co.logicons.tlp.core.genericmodels.schemas.SequenceIdSchema;
import nz.co.logicons.tlp.core.genericmodels.schemas.SequenceSchema;
import nz.co.logicons.tlp.core.mongo.MongoDatastore;
import nz.co.logicons.tlp.core.mongo.TransformOperation;

/**
 * Operations for schema.
 * 
 * @author bhambr
 * 
 */
public class SchemaOperation
{
  
  private static Logger log = LoggerFactory.getLogger(SchemaOperation.class);
  
  /** The name of collection in datastore that stores schemas. */
  public static String SCHEMA_COLLECTION = "schema";
  
  @Autowired
  private ValidateSchemaOperation validateSchemaOperation;

  @Autowired
  private MongoDatastore datastore;

  @Autowired
  private TransformOperation transformOperation;

  // @Autowired
  // private ViewOperation viewOperation;

  @Autowired
  private SequenceOperation sequenceOperation;
  
  // @Autowired
  // private MapDBSyncOperation mapDBSyncOperation;

  // public DocumentSchema getSchema(String schemaid)
  // {
  // return datastore.getSchema(schemaid);
  // }
  //
  // public DocumentSchema getSchema(String schemaid, boolean errorOnNotFound)
  // {
  // return datastore.getSchema(schemaid, errorOnNotFound);
  // }

  /**
   * Add a schema.
   * 
   * @param json schema as json
   * @param user user.
   */
  // public DocumentSchema addSchema(String json, User user)
  // {
  // DocumentSchema documentSchema = transformOperation.createDocumentSchema(json, user);
  // validateSchemaOperation.validate(documentSchema);
  // if (datastore.exists(SCHEMA_COLLECTION, documentSchema.getId()))
  // {
  // throw new ValidationException(JsonErrorType.DuplicateSchema);
  // }
  // initSequences(documentSchema);
  // datastore.upsertSchema(documentSchema);
  // addGeneratedViews(documentSchema);
  // try
  // {
  // mapDBSyncOperation.execute();
  // }
  // catch (ValidationException e)
  // {
  // log.error(e.toString());
  // }
  // return documentSchema;
  // }

  /**
   * Update a schema.
   * 
   * @param json schema as json
   * @param user user.
   */
  public DocumentSchema updateSchema(String json/* , User user */)
  {
    DocumentSchema documentSchema = transformOperation.createDocumentSchema(json);
    validateSchemaOperation.validate(documentSchema);
    //check if schema actually exists
    datastore.getSchema(documentSchema.getId());
    upsertSchemaNoValidation(documentSchema/* , user */);
    // try
    // {
    // mapDBSyncOperation.execute();
    // }
    // catch (ValidationException e)
    // {
    // log.error(e.toString());
    // }
    return documentSchema;
  }

  /**
   * Delete a schema.
   * 
   * @param json schema as json
   * @param user user.
   */
  // public void deleteSchema(String schemaid, User user)
  // {
  // DocumentSchema documentSchema = datastore.getSchema(schemaid);
  //
  // //cannot delete as document schema is in use.
  // if (StringUtils.isNotBlank(documentSchema.getUsage()))
  // {
  // throw new ValidationException(JsonErrorType.AccessDenied);
  // }
  //
  // DocumentSchema defaultDocumentSchema = datastore.getDefaultSchema(schemaid, false);
  // // cannot delete default schemas
  // if (defaultDocumentSchema != null)
  // {
  // throw new ValidationException(JsonErrorType.AccessDenied);
  // }
  // datastore.deleteSchema(documentSchema);
  // }

  /**
   * Add or edit a schema with no validation.
   * 
   * @param documentSchema schema.
   * @param user user.
   */
  public void upsertSchemaNoValidation(DocumentSchema documentSchema/* , User user */)
  {
    DocumentSchema currentDocumentSchema = datastore.getSchema(documentSchema.getId(), false);

    // mergeSchemaFromDefault(documentSchema);
    if (currentDocumentSchema != null)
    {
      // Once inline, always inline. Inline schemas are used as fields of other schemas. So dont permit change.
      if (currentDocumentSchema.isInline() && !documentSchema.isInline())
      {
        throw new ValidationException(JsonErrorType.InlineSchemaCannotHaveID);
      }
    }

    initSequences(documentSchema);
    datastore.upsertSchema(documentSchema);
    // addGeneratedViews(documentSchema);
  }

  /**
   * Add default views for schema.
   * @param documentSchema .
   */
  // public void addGeneratedViews(DocumentSchema documentSchema)
  // {
  // if (!documentSchema.isInline())
  // {
  // datastore.upsertViewToCache(viewOperation.generateView(documentSchema, SearchView.class.getSimpleName()
  // .toLowerCase()));
  // }
  // datastore.upsertViewToCache(viewOperation.generateView(documentSchema, DetailView.class.getSimpleName()
  // .toLowerCase()));
  // datastore.upsertViewToCache(viewOperation.generateView(documentSchema,
  // GridView.class.getSimpleName().toLowerCase()));
  // }

  /**
   * @return all schemas
   */
  public final Collection<DocumentSchema> getSchemas()
  {
    return datastore.getSchemas();
  }

  /**
   * Merge this schema with the default schema - there are certain properties and fields of a default schema that cannot be overridden/deleted.
   * @param documentSchema
   */
  // public void mergeSchemaFromDefault(DocumentSchema documentSchema)
  // {
  // DocumentSchema defaultDocumentSchema = datastore.getDefaultSchema(documentSchema.getId(), false);
  //
  // //only proceed if it is in fact a default schema.
  // if (defaultDocumentSchema != null)
  // {
  // //default schemas are system owned.
  // documentSchema.setSystemowned(true);
  //
  // documentSchema.setSystemlocked(defaultDocumentSchema.isSystemlocked());
  //
  // documentSchema.setSkipvalidation(defaultDocumentSchema.isSkipvalidation());
  //
  // //set all children in passed in schema to non system
  // for (ChildSchema<?> childSchema : documentSchema.getChildrenMap().values())
  // {
  // childSchema.setSystemlocked(false);
  // childSchema.setSystemowned(false);
  // }
  //
  // //loop through all default child schemas and place them in the input document schema (i.e override with defaults)
  // for (ChildSchema<?> defaultChildSchema : defaultDocumentSchema.getChildrenMap().values())
  // {
  //
  // ChildSchema<?> childSchema = documentSchema.getChildrenMap().get(defaultChildSchema.getId());
  //
  // if (!defaultChildSchema.isSystemowned())
  // {
  // defaultChildSchema.copyValidators(childSchema);
  // defaultChildSchema.copyPermissionEvaluator(childSchema);
  // }
  // documentSchema.addChild(defaultChildSchema);
  // }
  //
  // if (defaultDocumentSchema.isSystemlocked())
  // {
  // //copy the script across
  // documentSchema.getScripts().clear();
  // }
  //
  // //copy across all scripts as well
  // for (Script script : defaultDocumentSchema.getScripts())
  // {
  // if (!documentSchema.hasScript(script))
  // {
  // documentSchema.getScripts().add(script);
  // }
  // }
  // }
  // else
  // {
  // //its not a system schema (it may have been, but isnt anymore)
  // documentSchema.setSystemowned(false);
  // documentSchema.setSystemlocked(false);
  // }
  // }

  /**
   * Initialize sequence if schema contains a sequence as a child.
   * @param documentSchema .
   */
  public void initSequences(DocumentSchema documentSchema)
  {
    for (ChildSchema<?> schema : documentSchema.getChildrenMap().values())
    {
      if (schema.getType() == ChildSchemaType.sequenceschema)
      {
        sequenceOperation.init(((SequenceSchema) schema).getSequenceid());
      }

      if (schema.getType() == ChildSchemaType.sequenceidschema)
      {
        sequenceOperation.init(((SequenceIdSchema) schema).getSequenceid());
      }
    }
  }

  public ChildSchema<?> getChildSchemaByField(DocumentSchema documentSchema, String field)
  {
    if (StringUtils.indexOf(field, ".") > 0)
    {
      String[] keys = StringUtils.split(field, ".");
      ChildSchemaType childSchemaType = documentSchema.getChildrenMap().get(keys[0]).getType();
      if (childSchemaType == ChildSchemaType.inlineschema || childSchemaType == ChildSchemaType.listinlineschema)
      {
        DocumentSchema docSchema = datastore.getSchema(documentSchema.getChildrenMap().get(keys[0]).getSubtype());
        String[] remainingKeys = new String[keys.length - 1];
        System.arraycopy(keys, 1, remainingKeys, 0, remainingKeys.length);
        return getChildSchemaByField(docSchema, StringUtils.join(Arrays.asList(remainingKeys), "."));
      }
    }
    else
    {
      return documentSchema.getChildrenMap().get(field);
    }
    return null;
  }
}

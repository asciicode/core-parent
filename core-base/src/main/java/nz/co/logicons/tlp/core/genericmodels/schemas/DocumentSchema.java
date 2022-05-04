package nz.co.logicons.tlp.core.genericmodels.schemas;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.interceptor.CacheOperation;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;

import nz.co.logicons.tlp.core.enums.ChildSchemaType;
import nz.co.logicons.tlp.core.enums.PermissionType;
import nz.co.logicons.tlp.core.enums.ScriptTrigger;
import nz.co.logicons.tlp.core.genericmodels.base.GenericSerializable;
import nz.co.logicons.tlp.core.genericmodels.jackson.serializationprofile.CustomSerializer;
import nz.co.logicons.tlp.core.genericmodels.jackson.serializationprofile.SerializationProfile;
import nz.co.logicons.tlp.core.genericmodels.nodes.DocumentNode;
import nz.co.logicons.tlp.core.genericmodels.nodes.Node;
import nz.co.logicons.tlp.core.genericmodels.nodevalue.Document;
import nz.co.logicons.tlp.core.genericmodels.permissions.Permission;
import nz.co.logicons.tlp.core.genericmodels.permissions.PermissionEvaluatorProvider;
import nz.co.logicons.tlp.core.genericmodels.permissions.Permissions;
import nz.co.logicons.tlp.core.genericmodels.scripts.Script;
import nz.co.logicons.tlp.core.mongo.MongoDatastore;
import nz.co.logicons.tlp.core.scripting.ServerPermissionScript;

/**
 * Schema for a document.
 * Contains a schema for each child field ({@link #getChildrenMap()})
 * 
 * @author bhambr
 *
 */
@JsonPropertyOrder({ "_id", "displaytext", "inline", "systemowned", "systemlocked", "cached", "skipvalidation", "children",
    "permissions", "evaluatedpermissions" })
public class DocumentSchema
    extends Schema<Document>
{

  public static final String EXPANDED_SUFFIX = "__exp";
  
  /** schema for child fields. */
  private final Map<String, ChildSchema<?>> childrenMap = new LinkedHashMap<>();

  private List<ChildSchema<?>> childrenForJackson = new LinkedList<>();

  /** documents in cached schema are sent to the UI as part of the cache (see {@link CacheOperation#get()}) */
  private boolean cached;

  /** Permissions for schema. */
  private Permissions permissions = new Permissions();

  /** Evaluates permissions. */
  private PermissionEvaluatorProvider permissionEvaluatorProvider;

  private List<Script> scripts = new LinkedList<>();
  
  /** If set then do not validate documents on save. */
  private boolean skipvalidation;

  @JsonIgnore
  private List<ServerPermissionScript> permissionScripts;

  private boolean traceable;

  public boolean isSkipvalidation()
  {
    return skipvalidation;
  }

  public void setSkipvalidation(boolean skipvalidation)
  {
    this.skipvalidation = skipvalidation;
  }

  public DocumentSchema()
  {
    super();
  }

  /**
   * Invoked after deserialization. Performs object initialization.
   * 
   * @param datastore .
   */
  public void init(
    MongoDatastore datastore, InlineSchema auditInlineSchema, StringSchema parentidSchema,
    StringSchema childidSchema, PermissionEvaluatorProvider permissionEvaluatorProvider)
  {
    setDatastore(datastore);

    permissions.init(datastore);

    this.permissionEvaluatorProvider = permissionEvaluatorProvider;
    setPermissionEvaluator(permissionEvaluatorProvider.get(this));

    List<ChildSchema<?>> temp = new ArrayList<>(childrenForJackson);
    childrenForJackson.clear();

    for (ChildSchema<?> childSchema : temp)
    {
      addChild(childSchema);
    }

    if (!isInline())
    {
      addChild(auditInlineSchema);
      auditInlineSchema.setSystemowned(true);
      removeChild(parentidSchema.getId());
      removeChild(childidSchema.getId());
    }
    else
    {
      addChild(parentidSchema);
      parentidSchema.setSystemowned(true);
      addChild(childidSchema);
      childidSchema.setSystemowned(true);
      removeChild(auditInlineSchema.getId());
      cached = false;
    }

  }

  private DocumentNode getGlobalParametersDocument()
  {
    // DocumentNode documentNode = getDatastore().getDocument(GlobalParameters.SCHEMA, GlobalParameters.ID,
    // getDatastore().getSystemUser(), false); // don't throw exception
    return null;
  }

  /**
   * @return inline if no id field.
   */
  public boolean isInline()
  {
    return !getChildrenMap().containsKey(ID_FIELD_NAME);
  }

  public boolean isCached()
  {
    return cached;
  }
  

  public Permissions getPermissions()
  {
    return permissions;
  }

  /**
   * Makes this schema and all its children system owned.
   * 
   * @param systemOwned system ownded.
   */
  public void setSystemOwnedIncludingChildren(boolean systemOwned)
  {
    setSystemowned(systemOwned);
    for (ChildSchema<?> schema : getChildrenMap().values())
    {
      schema.setSystemowned(systemOwned);
    }
  }

  public void addChild(ChildSchema<?> schema)
  {
    schema.init(this, getDatastore());
    childrenMap.put(schema.getId(), schema);
    schema.setPermissionEvaluator(permissionEvaluatorProvider.get(this, schema));
    childrenForJackson = new LinkedList<>(childrenMap.values());
  }

  public void removeChild(String id)
  {
    ChildSchema<?> childSchema = childrenMap.get(id);
    if (childSchema != null)
    {
      childrenMap.remove(id);
      childrenForJackson.remove(childSchema);
    }
  }

  @JsonIgnore
  public Map<String, ChildSchema<?>> getChildrenMap()
  {
    return Collections.unmodifiableMap(childrenMap);
  }

  @JsonProperty(value = "children")
  public List<ChildSchema<?>> getChildrenForJackson()
  {
    return childrenForJackson;
  }

  @Override
  public Node<Document> deserializeNode(JsonNode jsonNode)
  {
    if (jsonNode == null)
    {
      return new DocumentNode(this);
    }
    else
    {
      return deserialize(jsonNode, this);
    }
  }

  protected Node<Document> deserialize(JsonNode jsonNode, DocumentSchema documentSchema)
  {
    DocumentNode document = new DocumentNode(documentSchema);

    if (jsonNode != null)
    {
      Iterator<Entry<String, JsonNode>> i = jsonNode.fields();
      while (i.hasNext())
      {

        Entry<String, JsonNode> entry = i.next();
        ChildSchema<?> childSchema = documentSchema.getChildrenMap().get(entry.getKey());
        if (childSchema != null)
        {

          document
              .addChildNode(entry.getKey(),
                  documentSchema.getChildrenMap().get(entry.getKey()).deserializeNode(entry.getValue()));
        }
      }
    }
    return document;
  }

  @Override
  public void serializeNode(Node<Document> node,
    JsonGenerator jsonGenerator, /* User user, */
    SerializationProfile serializationProfile, String parentPath)
    throws JsonGenerationException, IOException
  {

    for (CustomSerializer customSerializer : serializationProfile.getCustomSerializers())
    {
      if (customSerializer.applies(node, parentPath))
      {
        customSerializer.serialize(node, jsonGenerator, /* user, */ serializationProfile, parentPath);
        return;
      }
    }
    
    
    jsonGenerator.writeStartObject();

    serializeChildren(node.getValue(), jsonGenerator, /* user, */ serializationProfile, parentPath);
    
    if (!isInline() /* && !user.getRoles().isSystem() */)
    {
      DocumentNode globalNode = getGlobalParametersDocument();
      if (node.isPermitted(PermissionType.Update, /* user, */ globalNode))
      {
        jsonGenerator.writeBooleanField(Permission.CAN_UPDATE_FIELD_NAME, true);
      }

      if (node.isPermitted(PermissionType.Delete, /* user, */ globalNode))
      {
        jsonGenerator.writeBooleanField(Permission.CAN_DELETE_FIELD_NAME, true);
      }
    }

    jsonGenerator.writeEndObject();
  }
  
  public void serializeChildren(LinkedHashMap<String, Node<?>> children,
    JsonGenerator jsonGenerator/* , User user */,
    SerializationProfile serializationProfile, String parentPath) throws JsonGenerationException, IOException
  {
    for (Entry<String, Node<?>> entry : children.entrySet())
    {
      if (entry.getValue().isPermitted(PermissionType.Read,
          /* user, */ null)
          && entry.getValue().getValue() != null)
      {
        boolean serialize = false;

        if (entry.getValue().getSchema().getClass() == DocumentSchema.class)
        {
          // only serialize if document actually has children.
          serialize = ((Document) entry.getValue().getValue()).entrySet().size() > 0;
        }
        else
        {
          switch (entry.getValue().getSchema().getType())
          {
            case liststringschema:
            case listlinkedschema:
            case listinlineschema:
              // only serialize if lists contains values.
              serialize = ((List<?>) entry.getValue().getValue()).size() > 0;
              break;

            default:
              serialize = true;
              break;
          }

        }
        
        //a bit ugly but......
        if (entry.getValue().getSchema() instanceof ChildSchema)
        {
          ChildSchema<?> childSchema = (ChildSchema<?>) entry.getValue().getSchema();
          if (childSchema.isTransientfield())
          {
            serialize = false;
          }
        }
        
        if (serialize)
        {
          jsonGenerator.writeFieldName(entry.getKey());
          // create path based on parent path and id of child schema.
          String path = (StringUtils.isBlank(parentPath)) ? entry.getKey() : parentPath + "." + entry.getKey();
          entry.getValue().serialize(jsonGenerator, /* user, */ serializationProfile, path);
          
          if (serializationProfile.getSchemasToExpand().contains(path))
          {
            if (entry.getValue().getSchema().getType() == ChildSchemaType.linkedschema || entry.getValue().getSchema().getType() == ChildSchemaType.listlinkedschema || entry.getValue().getSchema().getType() == ChildSchemaType.deeplinkedschema)
            {
              jsonGenerator.writeFieldName(entry.getKey() + EXPANDED_SUFFIX);
              entry.getValue().serializeExpanded(jsonGenerator, /* user, */ serializationProfile, path);
            }
          }
          
        }
      }
    }
  }


  public String getUsage()
  {
    // Set<String> set = new HashSet<>();
    // for (DocumentSchema documentSchema : getDatastore().getSchemas())
    // {
    // for (ChildSchema<?> childSchema : documentSchema.getChildrenMap().values())
    // {
    // switch (childSchema.getType())
    // {
    //
    // case inlineschema:
    // case linkedschema:
    // case listinlineschema:
    // case listlinkedschema:
    // if (StringUtils.equals(getId(), childSchema.getSubtype()))
    // {
    // set.add(documentSchema.getId());
    // }
    // break;
    // case deeplinkedschema:
    // DeepLinkedSchema deepLinkedSchema = (DeepLinkedSchema) childSchema;
    // if (StringUtils.equals(getId(), deepLinkedSchema.getKeys()[0]))
    // {
    // set.add(documentSchema.getId());
    // }
    // default:
    // break;
    // }
    // }
    // }
    // if (set.size() > 0)
    // {
    // return set.toString();
    // }
    return "DocumentSchema getUsage()";
  }

  @Override
  public String toString()
  {
    return "DocumentSchema [children=" + getChildrenMap() + "]";
  }

  public List<Script> getScripts()
  {
    return scripts;
  }
  
  public boolean hasScript(Script script)
  {
    for (Script script2 : scripts)
    {
      if (script.getScripttrigger() == script2.getScripttrigger())
      {
        if (StringUtils.equals(script.getFunctionid(), script.getFunctionid()))
        {
          if (StringUtils.equals(script.getScript(), script.getScript()))
          {
            if (StringUtils.equals(script.getScriptletid(), script.getScriptletid()))
            {
              return true;
            }
          }
        }
      }
    }
    return false;
  }
  
  
  @JsonIgnore
  public boolean isDocumentIdNumeric()
  {
    ChildSchema<?> idSchema = getChildrenMap().get(GenericSerializable.ID_FIELD_NAME);
    return (idSchema != null && idSchema.getType() == ChildSchemaType.sequenceidschema);
  }
  
  @SuppressWarnings("unchecked")
  @JsonIgnore
  public synchronized List<ServerPermissionScript> getPermissionScripts()
  {
    if (permissionScripts == null)
    {
      permissionScripts = new ArrayList<>();
      for (Script script : getScripts())
      {
        if (script.getScripttrigger() == ScriptTrigger.PermissionServer)
        {
          try
          {
            Class<ServerPermissionScript> clazz = (Class<ServerPermissionScript>) Class.forName(script.getScriptletid());
            permissionScripts.add(clazz.getConstructor().newInstance());
          }
          catch (Exception e)
          {
            
          }
        }
      }
    }
    return permissionScripts;
  }

  @Override
  public void setScale(int scale)
  {
    // TODO Auto-generated method stub
    
  }

  public boolean isTraceable()
  {
    return traceable;
  }

  public void setTraceable(boolean traceable)
  {
    this.traceable = traceable;
  }
}

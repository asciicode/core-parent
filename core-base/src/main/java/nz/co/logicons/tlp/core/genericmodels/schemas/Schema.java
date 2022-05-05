package nz.co.logicons.tlp.core.genericmodels.schemas;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;

import nz.co.logicons.tlp.core.enums.ChildSchemaType;
import nz.co.logicons.tlp.core.genericmodels.base.GenericInterface;
import nz.co.logicons.tlp.core.genericmodels.jackson.serializationprofile.SerializationProfile;
import nz.co.logicons.tlp.core.genericmodels.nodes.Node;
import nz.co.logicons.tlp.core.genericmodels.permissions.PermissionEvaluator;
import nz.co.logicons.tlp.core.genericmodels.validators.Validator;
import nz.co.logicons.tlp.core.mongo.MongoDatastore;


/**
 * Base abstract class for all schemas.
 * @author bhambr
 *
 * @param <T> A document or any data type as per {@link ChildSchemaType}.
 */
public abstract class Schema<T>
    implements GenericInterface
{
  public static final String VALIDATORS_FIELD_NAME = "validators";
  
  /** The id. */
  private String id;

  /** The type of schema. Only set for child schemas not for document schema.*/
  private ChildSchemaType childSchemaType;

  /** Validators for this schema. */
  private final List<Validator<T>> validators = new ArrayList<>();

  /** Reference to datastore. Useful for looking up related items*/
  private MongoDatastore datastore;

  @JsonIgnore
  /** permissions evaluator for this schema. */
  private PermissionEvaluator permissionEvaluator;

  /** Schema is defined in DefaultSchemas.Json. It cannot be deleted/updated. */ 
  private boolean systemowned;

  /** Schema attributes such as validators and permissions evaluator may  not be overriden. They are defined in DefaultSchemas.Json.*/
  private boolean systemlocked;

  public Schema()
  {
    // DocumentSchema does not have a childSchemaType.
    if (getClass() == DocumentSchema.class)
    {
      childSchemaType = null;
    }
    else
    {
      childSchemaType = ChildSchemaType.valueOf(getClass().getSimpleName().toLowerCase());
    }
  }
  
  @JsonProperty("_id")
  public String getId()
  {
    return id;
  }

  public List<Validator<T>> getValidators()
  {
    return validators;
  }

  protected MongoDatastore getDatastore()
  {
    return datastore;
  }

  @JsonProperty("evaluatedpermissions")
  public PermissionEvaluator getPermissionEvaluator()
  {
    return permissionEvaluator;
  }
  
  
  @JsonIgnore
  public void setPermissionEvaluator(PermissionEvaluator permissionEvaluator)
  {
    this.permissionEvaluator = permissionEvaluator;
  }

  public boolean isSystemowned()
  {
    return systemowned;
  }

  public void setSystemowned(boolean systemowned)
  {
    this.systemowned = systemowned;
  }
  
  public void setSystemlocked(boolean systemlocked)
  {
    this.systemlocked = systemlocked;
  }

  /**
   * Add a validator (but avoid duplicates).
   * @param newValidator .
   */
  public void addValidator(Validator<T> newValidator)
  {
    for (Validator<T> validator : validators)
    {
      if (StringUtils.equals(validator.getType(), newValidator.getType()))
      {
        return;
      }
    }

    validators.add(newValidator);
  }

  /**
   * Deserialize the node (not the schema!)
   */
  public abstract Node<T> deserializeNode(JsonNode jsonNode);

  public abstract void setScale(int scale);
  
  /**
   * Serialize the node (not the schema!)
   */
  public void serializeNode(Node<T> node,
    JsonGenerator jsonGenerator/* , User user */,
    SerializationProfile serializationProfile, String parentPath)
    throws JsonGenerationException, IOException
  {
    jsonGenerator.writeObject(node.getValue());
  }
  
  /**
   * Serialize the node (not the schema!)
   */
  public void serializeNodeExpanded(Node<T> node,
    JsonGenerator jsonGenerator/* , User user */,
    SerializationProfile serializationProfile, String parentPath)
    throws JsonGenerationException, IOException
  {
    jsonGenerator.writeNull();
  }

  public final ChildSchemaType getType()
  {
    return childSchemaType;
  }
  
  @JsonIgnore
  public final boolean isDocumentSchema()
  {
    return getType() == null;
  }

  public String getSubtype()
  {
    return null;
  }

  public boolean isValueSet(T t)
  {
    return t != null;
  }

  @SuppressWarnings("unchecked")
  public void copyValidators(Schema<?> source)
  {
    if (source != null)
    {
      validators.clear();
      for (Validator<?> validator : source.getValidators())
      {
        validators.add((Validator<T>) validator);
      }
    }
  }

  public void copyPermissionEvaluator(Schema<?> source)
  {
    if (source != null)
    {
      setPermissionEvaluator(new PermissionEvaluator(source.getPermissionEvaluator().getPermissions(), source));
    }
  }

  // public boolean isPermitted(PermissionType permissionType, User user)
  // {
  // return getPermissionEvaluator().evaluate(permissionType, user, null) == PermissionResult.PERMITTED;
  // }

  public boolean isSystemlocked()
  {
    return systemowned && systemlocked;
  }
  
  protected void setDatastore(MongoDatastore datastore)
  {
    this.datastore = datastore;
  }

  
}

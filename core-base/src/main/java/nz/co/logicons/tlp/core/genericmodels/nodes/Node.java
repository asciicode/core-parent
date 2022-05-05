package nz.co.logicons.tlp.core.genericmodels.nodes;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;

import nz.co.logicons.tlp.core.enums.PermissionType;
import nz.co.logicons.tlp.core.genericmodels.base.GenericSerializable;
import nz.co.logicons.tlp.core.genericmodels.jackson.serializationprofile.SerializationProfile;
import nz.co.logicons.tlp.core.genericmodels.permissions.PermissionResult;
import nz.co.logicons.tlp.core.genericmodels.schemas.Schema;

/**
 * A node is a container for a schema and a value for that schema.
 * Basic building block for a document an its children.
 * 
 * @author bhambr
 * 
 */
public class Node<T>
    implements GenericSerializable, Cloneable
{
  /** The value of the node. */
  private T value;

  /** The schema for the value of the node. */
  private final Schema<T> schema;

  public Node(T value, Schema<T> schema)
  {
    super();
    this.value = value;
    this.schema = schema;
  }

  public T getValue()
  {
    return value;
  }

  public void setValue(T value)
  {
    this.value = value;
  }

  public Schema<T> getSchema()
  {
    return schema;
  }

  @Override
  public String toString()
  {
    if (value == null)
    {
      return null;
    }
    else
    {
      return value.toString();
    }
  }

  @Override
  public final void serialize(
    JsonGenerator jsonGenerator/* , User user */)
    throws JsonGenerationException, IOException
  {
    serialize(jsonGenerator, /* user, */ null, null);
  }
  
  public final void serialize(JsonGenerator jsonGenerator,
    /* User user, */ SerializationProfile serializationProfile, String parentPath)
    throws JsonGenerationException, IOException
  {
    // use the schema to serialize the node.
    getSchema().serializeNode(this, jsonGenerator, /* user, */ serializationProfile, parentPath);
  }
  
  public final void serializeExpanded(JsonGenerator jsonGenerator,
    /* User user, */ SerializationProfile serializationProfile, String parentPath)
      throws JsonGenerationException, IOException
    {
      // use the schema to serialize the node.
      getSchema().serializeNodeExpanded(this, jsonGenerator, /* user, */ serializationProfile, parentPath);
    }

  /**
   * Check if specified user has required permission.
   */
  public boolean isPermitted(PermissionType permissionType, /* User user, */ DocumentNode globalParam)
  {
    // Doesnt do much - just delegates check to schema.
    // Note: DocumentNode overrides this and uses the overall document for checking permissions (so that filters may be
    // taken into account)
    return getSchema().getPermissionEvaluator().evaluate(permissionType,
        /* user, */ globalParam) == PermissionResult.PERMITTED;
  }

  public Object clone() throws CloneNotSupportedException {
    return super.clone();
  }
  
}

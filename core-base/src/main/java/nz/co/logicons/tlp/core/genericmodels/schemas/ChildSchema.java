package nz.co.logicons.tlp.core.genericmodels.schemas;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver;

import nz.co.logicons.tlp.core.dbsync.model.FieldTypeAndLength;
import nz.co.logicons.tlp.core.enums.ChildSchemaType;
import nz.co.logicons.tlp.core.enums.DBType;
import nz.co.logicons.tlp.core.genericmodels.jackson.GenericInterfaceTypeIdResolver;
import nz.co.logicons.tlp.core.genericmodels.validators.Validator;
import nz.co.logicons.tlp.core.mongo.MongoDatastore;

@JsonTypeInfo(use = JsonTypeInfo.Id.CUSTOM,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type")
@JsonTypeIdResolver(GenericInterfaceTypeIdResolver.class)
public abstract class ChildSchema<T>
    extends Schema<T>
{
  /*
   * Transient fields are not store in data store. They provide a placeholder for client scripts and can be used to pass
   * information to server scripts or to provide summation in grids for calculated fields.
   */
  private boolean transientfield;
  
  private boolean upshiftfield;

  private boolean permissionexclude;

  public boolean isUpshiftfield()
  {
    return upshiftfield;
  }

  public void setUpshiftfield(boolean upshiftfield)
  {
    this.upshiftfield = upshiftfield;
  }

  public boolean isTransientfield()
  {
    return transientfield;
  }

  public void setTransientfield(boolean transientfield)
  {
    this.transientfield = transientfield;
  }

  public boolean isPermissionexclude()
  {
    return permissionexclude;
  }

  public void setPermissionexclude(boolean permissionexclude)
  {
    this.permissionexclude = permissionexclude;
  }

  public void init(DocumentSchema documentSchema, MongoDatastore datastore)
  {
    setDatastore(datastore);
    for (Validator<T> validator : getValidators())
    {
      validator.init();
    }
  }

  public Map<String, Boolean> getClassification()
  {
    Map<String, Boolean> map = new HashMap<String, Boolean>();
    if (ChildSchemaType.SimpleTypes.contains(this.getType()))
    {
      map.put("simple", true);
    }
    if (ChildSchemaType.NonListTypes.contains(this.getType()))
    {
      map.put("nonlist", true);
    }
    if (ChildSchemaType.NumericTypes.contains(this.getType()))
    {
      map.put("numeric", true);
    }
    if (ChildSchemaType.IdTypes.contains(this.getType()))
    {
      map.put("id", true);
    }
    if (ChildSchemaType.SystemAssignedTypes.contains(this.getType()))
    {
      map.put("systemassigned", true);
    }
    if (ChildSchemaType.SimpleAndListInlineTypes.contains(this.getType()))
    {
      map.put("simpleandlistinline", true);
    }
    return map;
  }

  public FieldTypeAndLength getFieldTypeAndLength(DBType dbType)
  {
    return null;
  }
}

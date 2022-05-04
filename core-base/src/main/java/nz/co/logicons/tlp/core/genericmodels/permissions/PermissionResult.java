package nz.co.logicons.tlp.core.genericmodels.permissions;

/**
 * Models the evaluation of a permission
 * 
 * @author bhambr
 * 
 */
public class PermissionResult
{
  
  private final boolean permitted;
  
  private final boolean permittedWithFilter;
  
  /** If set then this value in the user record must match value in document field schemaFieldId. */
  private final String userFieldId;

  /** See comment for userFieldId. */
  private final String schemaFieldId;
  
  public static final PermissionResult PERMITTED = new PermissionResult(true);
  
  public static final PermissionResult NOT_PERMITTED = new PermissionResult(false);
  
  
  public PermissionResult(boolean permitted)
  {
    this.permitted = permitted;
    this.permittedWithFilter = false;
    this.userFieldId = null;
    this.schemaFieldId = null;
  }
    
  public PermissionResult(Permission permission)
  {
    this.permitted = false;
    this.permittedWithFilter = true;
    this.userFieldId = permission.getUserfieldid();
    this.schemaFieldId = permission.getSchemafieldid();
  }

  public boolean isPermitted()
  {
    return permitted;
  }

  public boolean isPermittedWithFilter()
  {
    return permittedWithFilter;
  }

  public String getUserFieldId()
  {
    return userFieldId;
  }

  public String getSchemaFieldId()
  {
    return schemaFieldId;
  }

}

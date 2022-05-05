package nz.co.logicons.tlp.core.genericmodels.permissions;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import nz.co.logicons.tlp.core.enums.PermissionType;
import nz.co.logicons.tlp.core.genericmodels.base.GenericInterface;
import nz.co.logicons.tlp.core.mongo.MongoDatastore;


/**
 * Bean that holds access rights/permissions for a document.
 * @author bhambr
 *
 */
public class Permission implements GenericInterface
{
  public static final String CAN_CREATE_FIELD_NAME = "cancreate";
  
  public static final String CAN_UPDATE_FIELD_NAME = "canupdate";
  
  public static final String CAN_DELETE_FIELD_NAME = "candelete";

  /** The type of access. */
  private PermissionType permissiontype;

  /** The role that has access. */
  @JsonIgnore
  private Role role;

  /** Id of role. */
  private String roleid;

  /**
   * If set then this value in the user record must match value in document field schemaFieldId. Only applies at
   * document level (not field level as field inherits from document)
   */
  private String userfieldid;

  /** See comment for userFieldId. */
  private String schemafieldid;

  /** Does permission apply to all schema fields?. */
  private boolean appliestoallfields;
  
  /** Fields that permission applies to. Empty if appliesToAllFields == true. */
  private List<String> applicablefields = new ArrayList<>();
  
  public Permission()
  {
    
  }
  
  public Permission(PermissionType permissionType, Role role)
  {
    this.permissiontype = permissionType;
    this.role = role;
    this.appliestoallfields = false;
  }
  
  /**
   * Invoked after deserialization. Performs object initialization.
   * @param datastore .
   */
  protected void init(MongoDatastore datastore)
  {
    role = datastore.getRole(roleid, false);
    
    if (StringUtils.isEmpty(userfieldid) || StringUtils.isEmpty(schemafieldid))
    {
      userfieldid = null;
      schemafieldid = null;
    }
    
    if (appliestoallfields)
    {
      applicablefields.clear();
    }
  }
  
  
  public PermissionType getPermissiontype()
  {
    return permissiontype;
  }

  public String getUserfieldid()
  {
    return userfieldid;
  }

  public String getSchemafieldid()
  {
    return schemafieldid;
  }

  public boolean isAppliestoallfields()
  {
    return appliestoallfields;
  }

  public List<String> getApplicablefields()
  {
    return applicablefields;
  }
  
  public Role getRole()
  {
    return role;
  }
  
  @JsonProperty("role")
  public String getRoleId()
  {  
    if (role == null)
    {
      return null;
    }
    return role.getId();
  }
  
  @JsonProperty("role")
  public void setRoleid(String roleid)
  {
    this.roleid = roleid;
  }
  
  @JsonIgnore
  public boolean isFiltered()
  {
    return StringUtils.isNotBlank(userfieldid) && StringUtils.isNotBlank(schemafieldid); 
  }
  



}

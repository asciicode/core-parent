package nz.co.logicons.tlp.core.genericmodels.permissions;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import nz.co.logicons.tlp.core.enums.PermissionProfile;
import nz.co.logicons.tlp.core.enums.PermissionType;
import nz.co.logicons.tlp.core.mongo.MongoDatastore;


/**
 * Contains all permissions for a document or field.
 * 
 * @author bhambr
 * 
 */
public class Permissions
{
  private static final List<Permission> EMPTY_PERMISSIONS = Collections.unmodifiableList(new ArrayList<Permission>());
  
  private List<Permission> permissions = new ArrayList<>();

  private Map<Boolean, Map<PermissionType, List<Permission>>> permissionsMap;

  private PermissionProfile permissionprofile = PermissionProfile.PROFILE_4;

  public Permissions()
  {
    
  }
  
  
  public Permissions(List<Permission> permissions, PermissionProfile permissionProfile)
  {
    this.permissions.addAll(permissions);
    this.permissionprofile = permissionProfile;
    initPermissionsMap();
    
  }
  
  /**
   * Invoked after deserialization. Performs object initialization.
   * @param datastore .
   */
  public void init(MongoDatastore datastore)
  {
    // initialization list of permission
    Iterator<Permission> permissioniIterator = permissions.iterator();
    while (permissioniIterator.hasNext())
    {
      Permission permission = permissioniIterator.next();
      
      permission.init(datastore);
      
      // discard improperly specified permission
      if (permission.getPermissiontype() == null || permission.getRole() == null)
      {
        permissioniIterator.remove();
      }  
    }
    
    initPermissionsMap();
  }

  private void initPermissionsMap()
  {
    permissionsMap = new HashMap<>();

    // Construct the permissions map.
    for (Permission permission : permissions)
    {
      Map<PermissionType, List<Permission>> permissionsByFiltered = permissionsMap.get(permission.isFiltered());
      if (permissionsByFiltered == null)
      {
        permissionsByFiltered = new HashMap<>();
        permissionsMap.put(permission.isFiltered(), permissionsByFiltered);
      }

      List<Permission> permissionsByType = permissionsByFiltered.get(permission.getPermissiontype());
      if (permissionsByType == null)
      {
        permissionsByType = new ArrayList<>();
        permissionsByFiltered.put(permission.getPermissiontype(), permissionsByType);
      }
      permissionsByType.add(permission);
    }
  }

  public PermissionProfile getPermissionprofile()
  {
    return permissionprofile;
  }

  public List<Permission> getPermissions(boolean filteredPermissions, PermissionType permissionType)
  {
    Map<PermissionType, List<Permission>> permissionsByFiltered = permissionsMap.get(filteredPermissions);
    if (permissionsByFiltered == null)
    {
      return EMPTY_PERMISSIONS;
    }

    List<Permission> permissionsByType = permissionsByFiltered.get(permissionType);
    if (permissionsByType == null)
    {
      return EMPTY_PERMISSIONS;
    }

    return permissionsByType;
  }

  public List<Permission> getPermissions()
  {
    return permissions;
  }

}

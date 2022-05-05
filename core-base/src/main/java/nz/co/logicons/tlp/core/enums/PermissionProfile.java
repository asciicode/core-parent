package nz.co.logicons.tlp.core.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nz.co.logicons.tlp.core.genericmodels.base.EnumInterface;
import nz.co.logicons.tlp.core.genericmodels.permissions.Role;

/**
 * Types of permission profiles.
 * A schema has a single profile that sets the overall permissions.
 * The display text of a profile describes the roles and permissions.
 * For example "SYSADMIN[CRUD], SITEADMIN[CRUD], FULLACCESS[R], READONLY[R]" means
 * 1. SYSADMIN may [C]reate [R]ead [U]pdate [D]elete
 * 2. SITEADMIN may [C]reate [R]ead [U]pdate [D]elete
 * 3. FULLACCESS may [R]ead
 * 4. READONLY may [R]ead 
 * @author bhambr
 *
 */
public enum PermissionProfile implements EnumInterface
{
  PROFILE_1("SYSADMIN[CRUD], SITEADMIN[CRUD]"),
  PROFILE_2("SYSADMIN[CRUD], SITEADMIN[CRUD], FULLACCESS[R], READONLY[R]"),
  PROFILE_3("SYSADMIN[CRUD], SITEADMIN[CRUD], FULLACCESS[R], READONLY[R], LOOKUP[R]"),
  PROFILE_4("SYSADMIN[CRUD], SITEADMIN[CRUD], FULLACCESS[CRUD], READONLY[R]"),
  PROFILE_5("SYSADMIN[R], SITEADMIN[R], FULLACCESS[R], READONLY[R]"),
  PROFILE_6("SYSADMIN[CRUD], SITEADMIN[R], FULLACCESS[R], READONLY[R]");

  private final String displaytext;

  /** These roles will grant access irrespective of settings in permissions. */
  private Map<PermissionType, List<Role>> allowedRoles;
  
  static
  { 
	PROFILE_1.allowedRoles = new HashMap<>();
    addAccess(Role.SYSTEM_ROLE, PermissionType.values(), PROFILE_1.allowedRoles);
    addAccess(Role.SYSADMIN_ROLE, PermissionType.values(), PROFILE_1.allowedRoles);
    addAccess(Role.SITEADMIN_ROLE, PermissionType.values(), PROFILE_1.allowedRoles);
    
    PROFILE_2.allowedRoles = new HashMap<>();
    addAccess(Role.SYSTEM_ROLE, PermissionType.values(), PROFILE_2.allowedRoles);
    addAccess(Role.SYSADMIN_ROLE, PermissionType.values(), PROFILE_2.allowedRoles);
    addAccess(Role.SITEADMIN_ROLE, PermissionType.values(), PROFILE_2.allowedRoles);
    addAccess(Role.FULLACCESS_ROLE, new PermissionType[]{PermissionType.Read}, PROFILE_2.allowedRoles);
    addAccess(Role.READONLY_ROLE, new PermissionType[]{PermissionType.Read}, PROFILE_2.allowedRoles);
    
    PROFILE_3.allowedRoles = new HashMap<>();
    addAccess(Role.SYSTEM_ROLE, PermissionType.values(), PROFILE_3.allowedRoles);
    addAccess(Role.SYSADMIN_ROLE, PermissionType.values(), PROFILE_3.allowedRoles);
    addAccess(Role.SITEADMIN_ROLE, PermissionType.values(), PROFILE_3.allowedRoles);
    addAccess(Role.FULLACCESS_ROLE, new PermissionType[]{PermissionType.Read}, PROFILE_3.allowedRoles);
    addAccess(Role.READONLY_ROLE, new PermissionType[]{PermissionType.Read}, PROFILE_3.allowedRoles);
    addAccess(Role.LOOKUP_ROLE, new PermissionType[]{PermissionType.Read}, PROFILE_3.allowedRoles);
    
    PROFILE_4.allowedRoles = new HashMap<>();
    addAccess(Role.SYSTEM_ROLE, PermissionType.values(), PROFILE_4.allowedRoles);
    addAccess(Role.SYSADMIN_ROLE, PermissionType.values(), PROFILE_4.allowedRoles);
    addAccess(Role.SITEADMIN_ROLE, PermissionType.values(), PROFILE_4.allowedRoles);
    addAccess(Role.FULLACCESS_ROLE, PermissionType.values(), PROFILE_4.allowedRoles);
    addAccess(Role.READONLY_ROLE, new PermissionType[]{PermissionType.Read}, PROFILE_4.allowedRoles);
    
    PROFILE_5.allowedRoles = new HashMap<>();
    addAccess(Role.SYSTEM_ROLE, PermissionType.values(), PROFILE_5.allowedRoles);
    addAccess(Role.SYSADMIN_ROLE, new PermissionType[]{PermissionType.Read}, PROFILE_5.allowedRoles);
    addAccess(Role.SITEADMIN_ROLE, new PermissionType[]{PermissionType.Read}, PROFILE_5.allowedRoles);
    addAccess(Role.FULLACCESS_ROLE, new PermissionType[]{PermissionType.Read}, PROFILE_5.allowedRoles);
    addAccess(Role.READONLY_ROLE, new PermissionType[]{PermissionType.Read}, PROFILE_5.allowedRoles);

    PROFILE_6.allowedRoles = new HashMap<>();
    addAccess(Role.SYSTEM_ROLE, PermissionType.values(), PROFILE_6.allowedRoles);
    addAccess(Role.SYSADMIN_ROLE, PermissionType.values(), PROFILE_6.allowedRoles);
    addAccess(Role.SITEADMIN_ROLE, new PermissionType[]{PermissionType.Read}, PROFILE_6.allowedRoles);
    addAccess(Role.FULLACCESS_ROLE, new PermissionType[]{PermissionType.Read}, PROFILE_6.allowedRoles);
    addAccess(Role.READONLY_ROLE, new PermissionType[]{PermissionType.Read}, PROFILE_6.allowedRoles);
  }
  
  private static void addAccess(Role role, PermissionType[] permissionTypes, Map<PermissionType, List<Role>> allowedRoles)
  {
    for (PermissionType permissionType : permissionTypes)
    {
      List<Role> roles =  allowedRoles.get(permissionType);
      if (roles == null)
      {
        roles = new ArrayList<>();
        allowedRoles.put(permissionType, roles);
      }
      roles.add(role);
    }
  }
  
  private PermissionProfile(String displaytext)
  {
    this.displaytext = displaytext;
  }

  @Override
  public String getDisplaytext()
  {
    return displaytext;
  }
  
  public Map<PermissionType, List<Role>> getAllowedRoles()
  {
    return allowedRoles;
  }
}

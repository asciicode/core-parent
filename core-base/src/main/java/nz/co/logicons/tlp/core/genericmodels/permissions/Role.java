package nz.co.logicons.tlp.core.genericmodels.permissions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

import nz.co.logicons.tlp.core.genericmodels.JsonNodeHelper;
import nz.co.logicons.tlp.core.genericmodels.base.GenericSerializable;


/**
 * Models a role. Users are assigned roles, roles are assigned permissions. Hence a logged in users permissions are determined by their roles.
 * System defined (or predefined roles) such as SYSTEM, SYSADMIN and SITEADMIN cannot be modified and have special permissions
 * SYSTEM: Used by server to perform tasks on behalf of users (that users would not normally be able to perform) such as updating user records on login, loading the datastore on startup etc
 * SYSADMIN: Permits a system administrator to manage schemas, views, import, export etc
 * SITEADMIN: Permits an administrator at client site to manage users
 * @author bhambr
 *
 */
public class Role implements GenericSerializable, Comparable<Role>
{
  private static final String SYSTEM = "SYSTEM";
  
  private static final String SYSADMIN = "SYSADMIN";
  
  private static final String SITEADMIN = "SITEADMIN";
  
  private static final String FULLACCESS = "FULLACCESS";
  
  private static final String READONLY = "READONLY";
  
  public static final String LOOKUP = "LOOKUP";
  
  public static final String PREDEFINED_FIELD = "predefined";
  
  public static final String SYSTEM_FIELD = "system";
  
  public static final String SYSADMIN_FIELD = "sysadmin";
  
  public static final String SITEADMIN_FIELD = "siteadmin";

  public static final String SCHEMA = "(role)";
  
  private final String id;
  
  private boolean predefined;

  private boolean system;
  
  private boolean sysadmin;
  
  private boolean siteadmin;
  
  private static List<String> predefinedRoleIds;
  
  public static Role SYSTEM_ROLE = new Role(SYSTEM);
  
  public static Role SYSADMIN_ROLE = new Role(SYSADMIN);
  
  public static Role SITEADMIN_ROLE = new Role(SITEADMIN);
  
  public static Role FULLACCESS_ROLE = new Role(FULLACCESS);
  
  public static Role READONLY_ROLE = new Role(READONLY);
  
  public static Role LOOKUP_ROLE = new Role(LOOKUP);
  
  private synchronized static void init()
  {
    if (predefinedRoleIds == null)
    {
      //List of predefined role ids
      //Order is important! i.e SYSTEM > SYSADMIN > .......
      predefinedRoleIds = new ArrayList<>();
      predefinedRoleIds.add(SYSTEM);
      predefinedRoleIds.add(SYSADMIN);
      predefinedRoleIds.add(SITEADMIN);
      predefinedRoleIds.add(FULLACCESS);
      predefinedRoleIds.add(READONLY);
      predefinedRoleIds.add(LOOKUP);
      predefinedRoleIds = Collections.unmodifiableList(predefinedRoleIds);
    }
  }
  
  public Role(String id)
  {
    this.id = id;
    setup();
  }
  
  public Role(JsonNode jsonNode/* , User user *//* , Datastore datastore */)
  {
    this.id = JsonNodeHelper.getString(jsonNode, ID_FIELD_NAME);
    setup();
  }
  
  private void setup()
  {
    init();
    predefined = predefinedRoleIds.contains(id);
    system = SYSTEM.equals(id);
    sysadmin = SYSTEM.equals(id) || SYSADMIN.equals(id); //system is also sysadmin
    siteadmin = SYSTEM.equals(id) || SYSADMIN.equals(id) || SITEADMIN.equals(id); //system/sysadmin is also siteadmin
  }
  
  @Override
  public String getId()
  {
    return id;
  }
  
  public boolean isPredefined()
  {
    return predefined;
  }

  
  public boolean isSystem()
  {
    return system;
  }

  public boolean isSysadmin()
  {
    return sysadmin;
  }

  public boolean isSiteadmin()
  {
    return siteadmin;
  }

  @Override
  public void serializeSpecific(
    JsonGenerator jsonGenerator/* , User user */)
    throws IOException, JsonProcessingException
  {
    jsonGenerator.writeBooleanField(PREDEFINED_FIELD, isPredefined());
    jsonGenerator.writeBooleanField(SYSTEM_FIELD, isSystem());
    jsonGenerator.writeBooleanField(SYSADMIN_FIELD, isSysadmin());
    jsonGenerator.writeBooleanField(SITEADMIN_FIELD, isSiteadmin());
  }
  
  /**
   * Get the rank (its actually in reverse so a lower number is a higher rank)
   * @return
   */
  public Integer getPredefinedRank()
  {
    if (isPredefined())
    {
      return predefinedRoleIds.indexOf(getId());
    }
    return 10000;
  }

  @Override
  public int compareTo(Role o)
  {
    return this.getPredefinedRank().compareTo(o.getPredefinedRank());
  }
  
}

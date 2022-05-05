package nz.co.logicons.tlp.core.genericmodels.permissions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

import nz.co.logicons.tlp.core.genericmodels.base.GenericSerializable;
import nz.co.logicons.tlp.core.mongo.MongoDatastore;

/**
 * Models a collection of roles assigned to a user.
 * @author bhambr
 *
 */
public class Roles implements GenericSerializable
{

  public static final String ROLES_FIELD_NAME = "roles";
  
  public static final String ROLE_FIELD_NAME = "role";
  
  private List<Role> roles = new ArrayList<>();

  private boolean predefined;
  
  private boolean system;
  
  private boolean sysadmin;
  
  private boolean siteadmin;
  
  private Role predefinedRole;
  
  public Roles(Role role)
  {
    if (role != null)
    {
      roles.add(role);
    }
    
    calcPredefined();
  }
  
  public Roles(JsonNode jsonNode, MongoDatastore datastore)
  { 
    //create array of roles from json
    if (jsonNode != null)
    {
      JsonNode rolesJsonNode = jsonNode.get(ROLES_FIELD_NAME);
      
      if (rolesJsonNode != null && !rolesJsonNode.isNull())
      {
        Iterator<JsonNode> iterator = rolesJsonNode.elements();
        while (iterator.hasNext())
        {
          JsonNode childJsonNode = iterator.next();
          Role role = datastore.getRole(childJsonNode.asText(), false);
          if (role != null)
          {
            roles.add(role);
          }
        }
      }
    }
    
    if (!roles.contains(Role.LOOKUP_ROLE))
    {
      roles.add(Role.LOOKUP_ROLE);
    }
    
    calcPredefined();
  }
  
  private void calcPredefined()
  {
    
    
    if (roles.size() > 0)
    {
      Collections.sort(roles);
      if (roles.get(0).isPredefined())
      {
        this.predefinedRole = roles.get(0);
        this.predefined = this.predefinedRole.isPredefined();
        this.system = this.predefinedRole.isSystem();
        this.sysadmin = this.predefinedRole.isSysadmin();
        this.siteadmin = this.predefinedRole.isSiteadmin();
      }
    }
    
    //lock roles
    roles = Collections.unmodifiableList(roles);
  }

  public List<Role> getRoles()
  {
    return roles;
  }

  
  public boolean isPredefined()
  {
    return predefined;
  }
  
  public Role getPredefinedRole()
  {
    return predefinedRole;
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
  
  public void addRole(Role role)
  {
    this.roles = new ArrayList<>(this.roles);
    roles.add(role);
    calcPredefined();
  }
  
  public boolean isMemberOfRole(String roleName)
  {
    for (Role role : roles)
    {
     if (StringUtils.equals(roleName, role.getId()))
     {
       return true;
     }
    }
    return false;
  }
  
  @Override
  public void serializeSpecific(
    JsonGenerator jsonGenerator/* , User user */)
    throws IOException, JsonProcessingException
  {
    jsonGenerator.writeArrayFieldStart(ROLES_FIELD_NAME);
    for (Role role : getRoles())
    {
      jsonGenerator.writeString(role.getId());
    }
    jsonGenerator.writeEndArray();
    jsonGenerator.writeBooleanField(Role.PREDEFINED_FIELD, isPredefined());
    jsonGenerator.writeBooleanField(Role.SYSTEM_FIELD, isSystem());
    jsonGenerator.writeBooleanField(Role.SYSADMIN_FIELD, isSysadmin());
    jsonGenerator.writeBooleanField(Role.SITEADMIN_FIELD, isSiteadmin());
  }


}

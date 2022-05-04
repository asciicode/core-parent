package nz.co.logicons.tlp.core.business.models;

import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.LocalDateTime;

import com.fasterxml.jackson.databind.ObjectMapper;

import nz.co.logicons.tlp.core.genericmodels.nodes.DocumentNode;
import nz.co.logicons.tlp.core.genericmodels.nodes.Node;
import nz.co.logicons.tlp.core.genericmodels.permissions.Role;
import nz.co.logicons.tlp.core.genericmodels.permissions.Roles;

/**
 * Models instances of user schema.
 * 
 * @author bhambr
 * 
 */
public class User
{
  public static final String SCHEMA = "(user)";

  public static final String FIRST_NAME = "firstname";

  public static final String LAST_NAME = "lastname";

  public static final String EMAIL = "email";

  public static final String ROLE = "role";

  public static final String PASSWORD_HASH = "passwordhash";

  public static final String PASSWORD_SALT = "passwordsalt";

  public static final String REMEMBER_ME_HASH = "remembermehash";

  public static final String LAST_LOGIN = "lastlogin";

  public static final String LAST_REMEMBER_ME = "lastrememberme";

  public static final String ACTIVE = "active";

  public static final String CACHE_ID = "cacheid";

  public static final String APPLICATION_VERSION = "applicationVersion";

  public static final String DEFAULT_ADMIN_ACCOUNT_ID = "admin";
  
  public static final String DEFAULT_INTEGRATION_ACCOUNT_ID = "integration";

  public static final String USERNAME_FIELD_NAME = "username";

  public static final String PASSWORD_FIELD_NAME = "password";

  public static final String REMEMBER_ME_FIELD_NAME = "rememberme";

  public static final String NEW_PASSWORD_FIELD_NAME = "newpassword";

  public static final String NEW_PASSWORD_REPEAT_FIELD_NAME = "newpasswordrepeat";

  public static final String ADDRESS = "Address";
  
  public static final String RESTRICTED = "Restricted";
  
  public static final String TOBY = "TOBY";
  
  public static final String ENTITY = "Entity";
  
  public static final String PO_LIMIT = "PO_Limit";
  
  public static final String PO_SUPERVISOR = "PO_Supervisor";
  
  public static final String MENU = "Menu";

  public static final String DRIVER = "Driver";

  public static final String ID = "_id";

  public static final String FCM_TOKEN = "FCM_Token";

  public static final String WOBY = "WOBY";

  public static final String STOBY = "STOBY";

  public static final String KOBY = "KOBY";

  public boolean isWoby()
  {
    return document.getChildNodeValueAsBooleanDefault(WOBY);
  }

  public boolean isStoby()
  {
    return document.getChildNodeValueAsBooleanDefault(STOBY);
  }

  public boolean isKoby()
  {
    return document.getChildNodeValueAsBooleanDefault(KOBY);
  }

  public String getId()
  {
    return document.getChildNodeValueAsString(ID);
  }

  public void setId(String value)
  {
    document.setChildNodeValue(ID, value);
  }

  public String getDriver()
  {
    return document.getChildNodeValueAsString(DRIVER);
  }

  public void setDriver(String value)
  {
    document.setChildNodeValue(DRIVER, value);
  }

  /** The underlying document for user with schema (user). */
  private final DocumentNode document;

  /**
   * The object mapper used by jackson to serialize/deserialize json.
   * It is specific to the user as it takes the user's roles and fields in the user's document into account for
   * security/permissions purposes.
   */
  private ObjectMapper objectMapper;

  public DocumentNode getDocument()
  {
    return document;
  }

  public User(DocumentNode document)
  {
    this.document = document;
    // default admin account is always sysadmin (even if someone actually deletes the role via UI)
    if (document != null && DEFAULT_ADMIN_ACCOUNT_ID.equals(getUserName()))
    {
      if (!getRoles().getRoles().contains(Role.SYSADMIN_ROLE))
      {
        getRoles().addRole(Role.SYSADMIN_ROLE);
      }
    }
  }

  public String getUserName()
  {
    return document.getId();
  }

  public ObjectMapper getObjectMapper()
  {
    return objectMapper;
  }

  public void setObjectMapper(ObjectMapper objectMapper)
  {
    this.objectMapper = objectMapper;
  }

  public String getFcmToken()
  {
    return document.getChildNodeValueAsString(FCM_TOKEN);
  }

  public void setFcmToken(String accesstoken)
  {
    document.setChildNodeValue(FCM_TOKEN, accesstoken);
  }

  public String getPasswordHash()
  {
    return document.getChildNodeValueAsString(PASSWORD_HASH);
  }

  public void setPasswordHash(String passwordHash)
  {
    document.setChildNodeValue(PASSWORD_HASH, passwordHash);
  }

  public String getPasswordSalt()
  {
    return document.getChildNodeValueAsString(PASSWORD_SALT);
  }

  public void setPasswordSalt(String passwordSalt)
  {
    document.setChildNodeValue(PASSWORD_SALT, passwordSalt);
  }

  public String getRememberMeHash()
  {
    return document.getChildNodeValueAsString(REMEMBER_ME_HASH);
  }

  public void setRememberMeHash(String rememberMeHash)
  {
    document.setChildNodeValue(REMEMBER_ME_HASH, rememberMeHash);
  }

  public LocalDateTime getLastLogin()
  {
    return document.getChildNodeValueAsLocalDateTime(LAST_LOGIN);
  }

  public void setLastLogin(LocalDateTime lastLogin)
  {
    document.setChildNodeValue(LAST_LOGIN, lastLogin);
  }

  public LocalDateTime getLastRememberMe()
  {
    return document.getChildNodeValueAsLocalDateTime(LAST_REMEMBER_ME);
  }

  public void setLastRememberMe(LocalDateTime lastRememberMeLogin)
  {
    document.setChildNodeValue(LAST_REMEMBER_ME, lastRememberMeLogin);
  }

  public boolean isActive()
  {
    return document.getChildNodeValueAsBoolean(ACTIVE);
  }

  public void setActive(boolean active)
  {
    document.setChildNodeValue(ACTIVE, active);
  }

  public BigDecimal getcacheid()
  {
    return document.getChildNodeValueAsBigDecimal(CACHE_ID);
  }

  public void setcacheid(BigDecimal cacheid)
  {
    document.setChildNodeValue(CACHE_ID, cacheid);
  }

  public void setApplicationVersion(String applicationVersion)
  {
    document.setChildNodeValue(APPLICATION_VERSION, applicationVersion);
  }

  public String getApplicationVersion()
  {
    return document.getChildNodeValueAsString(APPLICATION_VERSION);
  }

  @SuppressWarnings("unchecked")
  public Roles getRoles()
  {
    return ((Node<Roles>) document.getChildNode(Roles.ROLES_FIELD_NAME)).getValue();
  }
  
  public boolean isMultiLogin()
  {
    return StringUtils.equals(getUserName(), DEFAULT_INTEGRATION_ACCOUNT_ID);
  }
  
  public String getEmail()
  {
    return document.getChildNodeValueAsString(EMAIL);
  }
  
  public Object getAddress()
  {
    return document.getChildNodeValueAsString(ADDRESS);
  }
  
  public boolean isRestricted()
  {
    return document.getChildNodeValueAsBooleanDefault(RESTRICTED);
  }

  public void setRestricted(boolean active)
  {
    document.setChildNodeValue(RESTRICTED, active);
  }
  
  public boolean isToby()
  {
    return document.getChildNodeValueAsBooleanDefault(TOBY);
  }
  
  public String getEntity()
  {
    return document.getChildNodeValueAsString(ENTITY);
  }

  public void setEntity(String value)
  {
    document.setChildNodeValue(ENTITY, value);
  }
  
  public BigDecimal getPOLimit()
  {
    return document.getChildNodeValueAsBigDecimal(PO_LIMIT);
  }

  public void setPOLimit(BigDecimal value)
  {
    document.setChildNodeValue(PO_LIMIT, value);
  }
  
  public String getPOSupervisor()
  {
    return document.getChildNodeValueAsString(PO_SUPERVISOR);
  }

  public void setPOSupervisor(String value)
  {
    document.setChildNodeValue(PO_SUPERVISOR, value);
  }

  public String getMenu()
  {
    return document.getChildNodeValueAsString(MENU);
  }

  public void setMenu(String value)
  {
    document.setChildNodeValue(MENU, value);
  }
}

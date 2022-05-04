package nz.co.logicons.tlp.core.genericmodels.permissions;

import java.util.List;

import nz.co.logicons.tlp.core.enums.PermissionType;
import nz.co.logicons.tlp.core.genericmodels.nodes.DocumentNode;
import nz.co.logicons.tlp.core.genericmodels.schemas.Schema;
import nz.co.logicons.tlp.core.scripting.ServerPermissionScript;

public class PermissionEvaluator
{
  private final Permissions permissions;

  private final Schema<?> schema;

  public PermissionEvaluator(Permissions permissions, Schema<?> schema)
  {
    this.permissions = permissions;
    this.schema = schema;
  }

  public Schema<?> getSchema()
  {
    return schema;
  }

  public Permissions getPermissions()
  {
    return permissions;
  }

  /**
   * Evaluate permissions.
   * Takes the document into account and applies permission filters.
   * Returned permission result will never have a filter on it (as it has already been evaluated)
   * IMPORTANT: Pass in the original document from the datastore!
   * 
   * @param permissionType
   * @param user
   * @param documentNode
   * @return
   */
  public PermissionResult evaluate(PermissionType permissionType,
    /* User user, */ DocumentNode documentNode, DocumentNode globalParam)
  {

    PermissionResult permissionResult = evaulatePermissionScripts(permissionType,
        /* user, */ documentNode, globalParam);
    if (permissionResult == null)
    {
      permissionResult = evaluate(permissionType, /* user, */ globalParam);
    }
    if (permissionResult.isPermittedWithFilter())
    {
      // check filter
      // Node<?> userField = user.getDocument().getChildNode(permissionResult.getUserFieldId());
      // if (userField != null)
      // {
      // Node<?> schemaField = documentNode.getChildNode(permissionResult.getSchemaFieldId());
      // if (schemaField != null)
      // {
      // if (userField.getValue() == null && schemaField.getValue() == null ||
      // (userField.getValue() != null && userField.getValue().equals(schemaField.getValue())))
      // {
      // return PermissionResult.PERMITTED;
      // }
      // }
      // }
    }

    return permissionResult;
  }

  private PermissionResult evaulatePermissionScripts(
    PermissionType permissionType, /* User user, */
    DocumentNode documentNode, DocumentNode globalParam)
  {

    for (ServerPermissionScript serverPermissionScript : documentNode.getSchema().getPermissionScripts())
    {
      PermissionResult permissionResult = serverPermissionScript.execute(permissionType,
          /* user, */ documentNode, globalParam);
      if (permissionResult != null)
      {
        return permissionResult;
      }
    }
    return null;
  }

  /**
   * Evaluate permissions.
   * Does not take the document into account (no filters are applied).
   * 
   * @param permissionType
   * @param user
   * @return
   */
  public PermissionResult evaluate(PermissionType permissionType, /* User user, */ DocumentNode globalParam)
  {
    // Check allowed roles first
    if (isInAllowedRoles(permissionType/* , user */))
    {
      return PermissionResult.PERMITTED;
    }

    // Now check permissions with no filter
    Permission permission = getMatchingPermission(/* user, */ permissions.getPermissions(false, permissionType));
    if (permission != null)
    {
      return PermissionResult.PERMITTED;
    }

    // Grab the first permission that has a filter (Doesnt at present support case where multiple permissions with
    // filters are matched)
    permission = getMatchingPermission(/* user, */ permissions.getPermissions(true, permissionType));
    if (permission != null)
    {
      return new PermissionResult(permission);
    }

    return PermissionResult.NOT_PERMITTED;
  }

  /**
   * Get permission whose role is in user's role.
   * 
   * @param user
   * @param permissions
   * @return
   */
  private Permission getMatchingPermission(/* User user, */ List<Permission> permissions)
  {
    for (Permission permission : permissions)
    {
      // if (user.getRoles().getRoles().contains(permission.getRole()))
      {
        return permission;
      }
    }
    return null;
  }

  private boolean isInAllowedRoles(PermissionType permissionType/* , User user */)
  {
    List<Role> roleByType = permissions.getPermissionprofile().getAllowedRoles().get(permissionType);
    if (roleByType != null)
    {
      // TODO: perhaps roleByType & user.getRoles().getRoles() should be sets and then if the intersection of the two
      // has any values then return true. That way dont have to rely on predefined role (which is determined by order)
      // return roleByType.contains(user.getRoles().getPredefinedRole());
      return true;
    }
    return false;
  }
}

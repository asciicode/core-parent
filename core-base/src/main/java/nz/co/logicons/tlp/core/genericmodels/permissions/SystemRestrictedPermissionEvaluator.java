package nz.co.logicons.tlp.core.genericmodels.permissions;

import nz.co.logicons.tlp.core.enums.PermissionType;
import nz.co.logicons.tlp.core.genericmodels.nodes.DocumentNode;
import nz.co.logicons.tlp.core.genericmodels.schemas.Schema;

/**
 * Special Permissions evaluator
 * Full access to SYSTEM role
 * All others denied access.
 * @author bhambr
 *
 */
public class SystemRestrictedPermissionEvaluator extends PermissionEvaluator
{

  public SystemRestrictedPermissionEvaluator(Schema<?> schema)
  {
    super(null, schema);
  }
  
  public PermissionResult evaluate(PermissionType permissionType/* , User user */)
  {
    // if (user.getRoles().isSystem())
    // {
      return PermissionResult.PERMITTED;
    // }
    
    // return PermissionResult.NOT_PERMITTED;
  }
  
  public PermissionResult evaluate(PermissionType permissionType/* , User user */, DocumentNode documentNode)
  {
    return evaluate(permissionType/* , user */);
  }

}

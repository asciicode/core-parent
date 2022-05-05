package nz.co.logicons.tlp.core.genericmodels.permissions;

import nz.co.logicons.tlp.core.enums.PermissionType;
import nz.co.logicons.tlp.core.genericmodels.nodes.DocumentNode;
import nz.co.logicons.tlp.core.genericmodels.schemas.Schema;

/**
 * Special Permissions evaluator
 * SYSTEM role can create, update and delete.
 * All other may read.
 * @author bhambr
 *
 */
public class SystemPermissionEvaluator extends PermissionEvaluator
{

  public SystemPermissionEvaluator(Schema<?> schema)
  {
    super(null, schema);
  }
  
  public PermissionResult evaluate(PermissionType permissionType/* , User user */)
  {
    switch (permissionType)
    {
      case Create:
      case Update:
      case Delete:
        // if (user.getRoles().isSystem())
        // {
          return PermissionResult.PERMITTED;
      // }
      // else
      // {
      // return PermissionResult.NOT_PERMITTED;
      // }
      default:
        return PermissionResult.PERMITTED;
    }
  }
  
  public PermissionResult evaluate(PermissionType permissionType/* , User user */, DocumentNode documentNode)
  {
    return evaluate(permissionType/* , user */);
  }

}

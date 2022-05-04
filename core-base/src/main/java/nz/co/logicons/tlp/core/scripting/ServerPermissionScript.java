package nz.co.logicons.tlp.core.scripting;

import nz.co.logicons.tlp.core.enums.PermissionType;
import nz.co.logicons.tlp.core.genericmodels.nodes.DocumentNode;
import nz.co.logicons.tlp.core.genericmodels.permissions.PermissionResult;

public interface ServerPermissionScript
{
  PermissionResult execute(
    PermissionType permissionType/* , User user */, DocumentNode documentNode, DocumentNode globalParam);
}

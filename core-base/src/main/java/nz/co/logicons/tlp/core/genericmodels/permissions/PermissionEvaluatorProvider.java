package nz.co.logicons.tlp.core.genericmodels.permissions;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import nz.co.logicons.tlp.core.enums.PermissionType;
import nz.co.logicons.tlp.core.genericmodels.schemas.ChildSchema;
import nz.co.logicons.tlp.core.genericmodels.schemas.DocumentSchema;

/**
 * Provider for {@link PermissionEvaluator}.
 * @author bhambr
 *
 */
public class PermissionEvaluatorProvider
{

  /**
   * Get {@link PermissionEvaluator} for a field in a schema.
   * @param documentSchema document schema.
   * @param childSchema field schema.
   * @return permissions evaluator.
   */
  public PermissionEvaluator get(DocumentSchema documentSchema, ChildSchema<?> childSchema)
  {
    switch (StringUtils.defaultString(documentSchema.getId()))
    {
      // case User.SCHEMA:
      // if (User.PASSWORD_HASH.equals(childSchema.getId()) || User.PASSWORD_SALT.equals(childSchema.getId()) ||
      // User.REMEMBER_ME_HASH.equals(childSchema.getId()))
      // {
      // return new SystemRestrictedPermissionEvaluator(childSchema);
      // }
      // else
      // {
      // return new SiteAdminPermissionEvaluator(childSchema);
      // }
      // case SequenceOperation.SCHEMA:
      // case BookmarkOperation.BOOKMARK_SCHEMA:
      // case Audit.SCHEMA:
      // return new SystemPermissionEvaluator(childSchema);
      default:
        return new PermissionEvaluator(getPermissionsForField(documentSchema.getPermissions(), childSchema.getId()), childSchema);
    }

  }

  /**
   * Get {@link PermissionEvaluator} for a schema.
   * @param documentSchema document schema.
   * @return permissions evaluator.
   */
  public PermissionEvaluator get(DocumentSchema documentSchema)
  {

    switch (StringUtils.defaultString(documentSchema.getId()))
    {
      // case User.SCHEMA:
      // return new SiteAdminPermissionEvaluator(documentSchema);
      // case SequenceOperation.SCHEMA:
      // case BookmarkOperation.BOOKMARK_SCHEMA:
      // case Audit.SCHEMA:
      // return new SystemPermissionEvaluator(documentSchema);
      default:
        return new PermissionEvaluator(documentSchema.getPermissions(), documentSchema);
    }
  }

  /**
   * Get the permissions for a field.
   * @param documentPermissions permissions for document.
   * @param fieldId id of field.
   * @return permissions for field.
   */
  private Permissions getPermissionsForField(Permissions documentPermissions, String fieldId)
  {

    List<Permission> fieldPermissions = new ArrayList<>();
    
    for (Permission permission : documentPermissions.getPermissions())
    {
      //field permissions only valid for read and update operations
      if (permission.getPermissiontype() == PermissionType.Read
          || permission.getPermissiontype() == PermissionType.Update)
      {
        if (permission.isAppliestoallfields() || permission.getApplicablefields().contains(fieldId))
        {
          fieldPermissions.add(new Permission(permission.getPermissiontype(), permission.getRole()));
          
          //replicate update permission as create permission
          if (permission.getPermissiontype() == PermissionType.Update)
          {
            fieldPermissions.add(new Permission(PermissionType.Create, permission.getRole()));
          }
        }
      }
    }
    
    return new Permissions(fieldPermissions, documentPermissions.getPermissionprofile());
  }

}

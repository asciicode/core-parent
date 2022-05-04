package nz.co.logicons.tlp.core.enums;

import nz.co.logicons.tlp.core.genericmodels.base.EnumInterface;

/**
 * Types of errors in json.
 * @author bhambr
 *
 */
public enum JsonErrorType implements EnumInterface
{
  FieldValidation("Field validation"),
  DocumentNotFound("Document not found"),
  SchemaNotFound("Schema not found"),
  RoleNotFound("Role not found"),
  AccessDenied("Access denied"),
  NotLoggedIn("Not logged in"),
  MalformedJson("Malformed json"),
  DuplicateSchema("Duplicate schema"),
  JsonTransformationError("Json transformation error"),
  JsonInvalidError("Json invalid"),
  InvalidUsernameOrPassword("Invalid username or password"),
  AccountLocked("Account locked"),
  AccountLockedDueToRepeatedFailedLogins("Account is temporarily locked due to repeated unsuccessful login attempts. Retry in 10 minutes."),
  InlineSchemaCannotHaveID("Inline schema cannot have ID"),
  InlineSchemaDocumentNotPermitted("Cannot create a document for inline schema"),
  DatasourceException("Datasource exception"),
  ViewNotFound("View not found"),
  UnknownViewType("Unknown view type"),
  DuplicateView("Duplicate view"),
  InvalidPassword("Invalid password"),
  DuplicateRole("Duplicate role"),
  ErrorInside("Error"),
  ServerScriptingError("Server scripting error"),
  ReportError("Error creating report"),
  ScriptletNotFound("Scriptlet not found"),
  DuplicateScriptlet("Duplicate scriptlet"),
  MapDBSyncError("Unable to map tables in dbsync database"),
  DBSyncError("dbsync error"),
  XMLInvalidError("XML invalid"),
  AttachmentNotFound("Attachment not found");

  private final String displaytext;
  
  private JsonErrorType(String displaytext)
  {
    this.displaytext = displaytext;
  }
  
  public String getDisplaytext()
  {
    return displaytext;
  }
}

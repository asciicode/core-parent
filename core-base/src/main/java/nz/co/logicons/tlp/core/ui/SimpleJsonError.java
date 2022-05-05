package nz.co.logicons.tlp.core.ui;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import nz.co.logicons.tlp.core.enums.JsonErrorType;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Models an error in the input (json).
 * @author bhambr
 *
 */
@JsonInclude(Include.NON_NULL)
public class SimpleJsonError implements JsonError
{
  /** The path within the json where the error occured. */
  private final String path;

  /** The error message. */
  private final String message;

  /** The validator that triggered the error. */
  private final String validator;

  /** Type of error. */
  private final JsonErrorType jsonErrorType;

  public SimpleJsonError(String path, String message, String validator)
  {
    super();
    this.path = path;
    this.message = message;
    this.validator = validator;
    this.jsonErrorType = JsonErrorType.FieldValidation;
  }

  public SimpleJsonError(JsonErrorType jsonErrorType)
  {
    this.path = null;
    this.validator = null;
    this.jsonErrorType = jsonErrorType;
    this.message = jsonErrorType.getDisplaytext();
  }
  
  public SimpleJsonError(String path, JsonErrorType jsonErrorType)
  {
    this.path = path;
    this.validator = null;
    this.jsonErrorType = jsonErrorType;
    this.message = jsonErrorType.getDisplaytext();
  }

  public String getPath()
  {
    return path;
  }

  public String getMessage()
  {
    return message;
  }

  public String getValidator()
  {
    return validator;
  }

  @JsonProperty("messagetype")
  public JsonErrorType getJsonErrorType()
  {
    return jsonErrorType;
  }

  @Override
  public String toString()
  {
    return "JsonError [path=" + path + ", message=" + message + ", validator=" + validator + ", jsonErrorType="
        + jsonErrorType + "]";
  }

}

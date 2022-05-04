package nz.co.logicons.tlp.core.exceptions;

import java.util.ArrayList;
import java.util.List;

import nz.co.logicons.tlp.core.enums.JsonErrorType;
import nz.co.logicons.tlp.core.ui.JsonError;
import nz.co.logicons.tlp.core.ui.SimpleJsonError;

/**
 * General exception when json is not valid for requested operation.
 * May be a fault in the json or in access or document not found or ..... (refer to {@link JsonErrorType} for more)
 * @author bhambr
 *
 */
public class ValidationException
    extends RuntimeException
{
  private static final long serialVersionUID = -95858657601833126L;

  private final List<JsonError> jsonErrors;

  public ValidationException(List<JsonError> jsonErrors)
  {
    super();
    this.jsonErrors = jsonErrors;
  }
  
  public ValidationException(String message)
  {
    super();
    this.jsonErrors = new ArrayList<>();
    jsonErrors.add(new SimpleJsonError(null, message, null));
  }
  
  public ValidationException(String message, Object...args)
  {
    super();
    this.jsonErrors = new ArrayList<>();
    jsonErrors.add(new SimpleJsonError(null, String.format(message, args), null));
  }
  
  public ValidationException(JsonErrorType jsonErrorType)
  {
    super();
    jsonErrors = new ArrayList<>();
    jsonErrors.add(new SimpleJsonError(jsonErrorType));
  }
  
  public List<JsonError> getJsonErrors()
  {
    return jsonErrors;
  }
  
  public boolean containsJsonErrorType(JsonErrorType jsonErrorType)
  {
    for (JsonError jsonError : jsonErrors)
    {
      if (jsonError.getJsonErrorType() == jsonErrorType)
      {
        return true;
      }
    }
    return false;
  }

  @Override
  public String toString()
  {
    return "ValidationException [jsonErrors=" + jsonErrors + "]";
  }

}

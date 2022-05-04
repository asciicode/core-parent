package nz.co.logicons.tlp.core.ui;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * Respose sent to client for all operations.
 * Either
 * 1. success or
 * 2. Simple errors for a single object (schema/view/document etc)
 * 3. errors during import operation.
 * @author bhambr
 *
 */
@JsonInclude(Include.NON_EMPTY)
public class JsonResponse
{
  
  /** was operation a success. */
  private final boolean success;

  /** errors that occurred during operation. */
  private final List<JsonError> jsonerrors = new ArrayList<>();

  public JsonResponse(List<JsonError> jsonErrors)
  {
    this.success = false;
    jsonerrors.addAll(jsonErrors);
  }

  public JsonResponse()
  {
    this.success = true;
  }

  public boolean isSuccess()
  {
    return success;
  }

  /**
   * @return simplified representation of jsonerrors for UI.
   */
  public Map<String, Set<String>> getErrors()
  {

    Map<String, Set<String>> temp = new TreeMap<>();
    for (JsonError jsonError : jsonerrors)
    {

      if (jsonError.getClass() == SimpleJsonError.class)
      {
        SimpleJsonError simpleJsonError = (SimpleJsonError) jsonError;
        String key = StringUtils.defaultString(simpleJsonError.getPath(), "_global");
        Set<String> set = temp.get(key);
        if (set == null)
        {
          set = new HashSet<>();
          temp.put(key, set);
        }
        set.add(simpleJsonError.getMessage());
      }
    }

    return temp;

  }
  
  /**
   * @return errors during import process.
   */
  public List<ImportJsonError> getImportErrors()
  {
    List<ImportJsonError> importJsonErrors = new ArrayList<>();
    for (JsonError jsonError : jsonerrors)
    {
      if (jsonError.getClass() == ImportJsonError.class)
      {
        importJsonErrors.add((ImportJsonError) jsonError);
      }
    }
    return importJsonErrors;
  }
}

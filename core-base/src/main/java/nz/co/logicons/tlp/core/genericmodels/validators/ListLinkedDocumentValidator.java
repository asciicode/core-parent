package nz.co.logicons.tlp.core.genericmodels.validators;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import nz.co.logicons.tlp.core.common.MessageConstants;
import nz.co.logicons.tlp.core.enums.JsonErrorType;
import nz.co.logicons.tlp.core.genericmodels.schemas.Schema;
import nz.co.logicons.tlp.core.mongo.MongoDatastore;
import nz.co.logicons.tlp.core.ui.JsonError;
import nz.co.logicons.tlp.core.ui.SimpleJsonError;

/**
 * Validates a list of linked document (i.e a documents with specified ids actually exists).
 * @author bhambr
 *
 */
public class ListLinkedDocumentValidator
    extends Validator<List<String>>
{
  public ListLinkedDocumentValidator()
  {
    
  }
  
  public ListLinkedDocumentValidator(Map<String, String> params)
  {
    super(params);
  }

  @Override
  public boolean validate(List<String> value,
    Schema<List<String>> schema, MongoDatastore datastore, String path, List<JsonError> jsonErrors)
  {
    boolean hasError = false;
    for (String id : value)
    {
      if (StringUtils.isBlank(id)/* || !datastore.exists(schema.getSubtype(), id) */)
      {
        jsonErrors.add(new SimpleJsonError(path + "." + value.indexOf(id), getParams().get(MESSAGE_PARAM_NAME), getType()));
        jsonErrors.add(new SimpleJsonError(path, JsonErrorType.ErrorInside));
        hasError = true;
      }
    }
    return hasError;
  }
  
  @Override
  public Map<String, String> getDefaultParams()
  {
    Map<String, String> map = new LinkedHashMap<>();
    map.put(MESSAGE_PARAM_NAME, MessageConstants.INVALID);
    return map;
  }

}

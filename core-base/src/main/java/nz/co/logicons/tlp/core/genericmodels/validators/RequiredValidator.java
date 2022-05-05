package nz.co.logicons.tlp.core.genericmodels.validators;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import nz.co.logicons.tlp.core.common.MessageConstants;
import nz.co.logicons.tlp.core.genericmodels.schemas.Schema;
import nz.co.logicons.tlp.core.mongo.MongoDatastore;
import nz.co.logicons.tlp.core.ui.JsonError;
import nz.co.logicons.tlp.core.ui.SimpleJsonError;

/**
 * Validates that field is not null.
 * @author bhambr
 *
 * @param <T>
 */
public class RequiredValidator<T> extends Validator<T>
{

  public RequiredValidator()
  {
    
  }
  
  public RequiredValidator(Map<String, String> params)
  {
    super(params);
  }

  @Override
  public boolean validate(T value, Schema<T> schema, MongoDatastore datastore, String path, List<JsonError> jsonErrors)
  {
    if (!schema.isValueSet(value))
    {
      jsonErrors.add(new SimpleJsonError(path, getParams().get(MESSAGE_PARAM_NAME), getType()));
      return true;
    }
    return false;
  }

  @Override
  public Map<String, String> getDefaultParams()
  {
    Map<String, String> map = new LinkedHashMap<>();
    map.put(MESSAGE_PARAM_NAME, MessageConstants.REQUIRED);
    return map;
  }

}

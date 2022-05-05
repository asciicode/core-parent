package nz.co.logicons.tlp.core.genericmodels.validators;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import nz.co.logicons.tlp.core.common.MessageConstants;
import nz.co.logicons.tlp.core.genericmodels.schemas.Schema;
import nz.co.logicons.tlp.core.mongo.MongoDatastore;
import nz.co.logicons.tlp.core.ui.JsonError;
import nz.co.logicons.tlp.core.ui.SimpleJsonError;

public class IDValidator extends Validator<String>
{

  public IDValidator()
  {
    
  }
  
  public IDValidator(Map<String, String> params)
  {
    super(params);
  }
  
  @Override
  public boolean validate(String value,
    Schema<String> schema, MongoDatastore datastore, String path,
    List<JsonError> jsonErrors)
  {
    if (StringUtils.startsWith(value, " ") || StringUtils.endsWith(value, " "))
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
    map.put(MESSAGE_PARAM_NAME, MessageConstants.INVALID);
    return map;
  }

}

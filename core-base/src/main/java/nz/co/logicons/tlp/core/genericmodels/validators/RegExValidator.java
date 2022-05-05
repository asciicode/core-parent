package nz.co.logicons.tlp.core.genericmodels.validators;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.RegexValidator;

import nz.co.logicons.tlp.core.common.MessageConstants;
import nz.co.logicons.tlp.core.genericmodels.schemas.Schema;
import nz.co.logicons.tlp.core.mongo.MongoDatastore;
import nz.co.logicons.tlp.core.ui.JsonError;
import nz.co.logicons.tlp.core.ui.SimpleJsonError;

/**
 * Validates a string against a regular expression.
 * @author bhambr
 *
 */
public class RegExValidator extends Validator<String>
{
  
  private static String REGEX_PARAM_NAME = "regex";
  
  private RegexValidator regexValidator;
  
  public RegExValidator()
  {
    
  }
  
  public RegExValidator(Map<String, String> params)
  {
    super(params);
  }
  
  private RegexValidator getRegexValidator()
  {
    if (regexValidator == null)
    {
      regexValidator = new RegexValidator(getParams().get(REGEX_PARAM_NAME));
    }
    return regexValidator;
  }
  

  @Override
  public boolean validate(String value,
    Schema<String> schema, MongoDatastore datastore, String path, List<JsonError> jsonErrors)
  {
    //Blank strings will not fail regex validation - if blank strings are not permitted then use required validator.
    if (StringUtils.isNotBlank(value))
    {
      if (!getRegexValidator().isValid(value))
      {
        jsonErrors.add(new SimpleJsonError(path, getParams().get(MESSAGE_PARAM_NAME), getType()));
        return true;
      }
    }
    return false;
  }
  

  @Override
  public Map<String, String> getDefaultParams()
  {
    Map<String, String> map = new LinkedHashMap<>();
    map.put(MESSAGE_PARAM_NAME, MessageConstants.INVALID);
    map.put(REGEX_PARAM_NAME, ".*");
    return map;
  }

}

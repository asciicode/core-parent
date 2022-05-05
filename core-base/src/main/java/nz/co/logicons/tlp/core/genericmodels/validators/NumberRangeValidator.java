package nz.co.logicons.tlp.core.genericmodels.validators;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import nz.co.logicons.tlp.core.common.MessageConstants;
import nz.co.logicons.tlp.core.genericmodels.schemas.Schema;
import nz.co.logicons.tlp.core.mongo.MongoDatastore;
import nz.co.logicons.tlp.core.ui.JsonError;
import nz.co.logicons.tlp.core.ui.SimpleJsonError;

/**
 * Validates that a number falls between a minimum and/or maximum threshold.
 * @author bhambr
 *
 */
public class NumberRangeValidator
    extends Validator<BigDecimal>
{
  private static String MIN_PARAM_NAME = "min";
  
  private static String MAX_PARAM_NAME = "max";
  
  private static String MIN_MESSAGE_PARAM_NAME = "minmessage";
  
  private static String MAX_MESSAGE_PARAM_NAME = "maxmessage";

  public NumberRangeValidator()
  {
    
  }
  
  public NumberRangeValidator(Map<String, String> params)
  {
    super(params);
  }

  @Override
  public boolean validate(BigDecimal value,
    Schema<BigDecimal> schema, MongoDatastore datastore, String path,
    List<JsonError> jsonErrors)
  {
    boolean hasError = false;
    if (value != null)
    {
      try
      {
        double min = Double.parseDouble(getParams().get(MIN_PARAM_NAME));
        if (value.compareTo(new BigDecimal(min).setScale(value.scale())) == -1)
        {
          jsonErrors.add(new SimpleJsonError(path, getParams().get(MIN_MESSAGE_PARAM_NAME), getType()));
          hasError = true;
        }
      }
      catch (Exception e)
      {
        //in case min is not a double
      }

      try
      {
        double max = Double.parseDouble(getParams().get(MAX_PARAM_NAME));
        if (value.compareTo(new BigDecimal(max).setScale(value.scale())) == 1)
        {
          jsonErrors.add(new SimpleJsonError(path, getParams().get(MAX_MESSAGE_PARAM_NAME), getType()));
          hasError = true;
        }
      }
      catch (Exception e)
      {
      //in case max is not a double
      }
      
    }

    return hasError;
  }

  @Override
  public Map<String, String> getDefaultParams()
  {
    Map<String, String> map = new LinkedHashMap<>();
    map.put(MIN_MESSAGE_PARAM_NAME, MessageConstants.TOO_SMALL);
    map.put(MAX_MESSAGE_PARAM_NAME, MessageConstants.TOO_BIG);
    map.put(MIN_PARAM_NAME, "");
    map.put(MAX_PARAM_NAME, "");
    return map;
  }

}

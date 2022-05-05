package nz.co.logicons.tlp.core.genericmodels.validators;

import java.util.HashMap;
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

public class ContainerNumberValidator extends Validator<String>
{

  private static RegexValidator regexValidator = new RegexValidator("^[A-Z]{3}[UJZ]{1}[0-9]{7}$");
  
  private static Map<String, Integer> lookup;
  
  static
  {
    lookup = new HashMap<String, Integer>();
    lookup.put("A", 10);
    lookup.put("B", 12);
    lookup.put("C", 13);
    lookup.put("D", 14);
    lookup.put("E", 15);
    lookup.put("F", 16);
    lookup.put("G", 17);
    lookup.put("H", 18);
    lookup.put("I", 19);
    lookup.put("J", 20);
    lookup.put("K", 21);
    lookup.put("L", 23);
    lookup.put("M", 24);
    lookup.put("N", 25);
    lookup.put("O", 26);
    lookup.put("P", 27);
    lookup.put("Q", 28);
    lookup.put("R", 29);
    lookup.put("S", 30);
    lookup.put("T", 31);
    lookup.put("U", 32);
    lookup.put("V", 34);
    lookup.put("W", 35);
    lookup.put("X", 36);
    lookup.put("Y", 37);
    lookup.put("Z", 38);
    lookup.put("0", 0);
    lookup.put("1", 1);
    lookup.put("2", 2);
    lookup.put("3", 3);
    lookup.put("4", 4);
    lookup.put("5", 5);
    lookup.put("6", 6);
    lookup.put("7", 7);
    lookup.put("8", 8);
    lookup.put("9", 9);
  }
  
  public ContainerNumberValidator()
  {
    
  }
  
  public ContainerNumberValidator(Map<String, String> params)
  {
    super(params);
  }
  
  @Override
  public boolean validate(String value,
    Schema<String> schema, MongoDatastore datastore, String path,
    List<JsonError> jsonErrors)
  {
    if (StringUtils.isNotBlank(value))
    {
      if (!regexValidator.isValid(value))
      {
        jsonErrors.add(new SimpleJsonError(path, getParams().get(MESSAGE_PARAM_NAME), getType()));
        return true;
      }
      
      //Note: Steps refer to calculation as listed at http://en.wikipedia.org/wiki/ISO_6346
      long stepA = 0;
      for (int i = 0; i < 10; i++)
      {
        long temp = lookup.get(value.substring(i, i + 1)) * (long) (Math.pow(2, i));
        stepA = stepA  + temp;
      }
      
      long stepBC = (long) Math.floor(stepA / 11);
      
      long stepD = stepBC * 11;
      
      long stepE = stepA - stepD;

      if (!StringUtils.equals(value.substring(10, 11), String.valueOf(stepE)))
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
    return map;
  }

}

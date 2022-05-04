package nz.co.logicons.tlp.core.genericmodels.validators;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver;

import nz.co.logicons.tlp.core.genericmodels.jackson.GenericInterfaceTypeIdResolver;
import nz.co.logicons.tlp.core.genericmodels.schemas.Schema;
import nz.co.logicons.tlp.core.ui.JsonError;

/**
 * Validates a field (or a document but isnt used for documents yet)
 * @author bhambr
 *
 * @param <T>
 */
@JsonPropertyOrder({ "type", "params" })
@JsonTypeInfo(use = JsonTypeInfo.Id.CUSTOM,
include = JsonTypeInfo.As.PROPERTY,
property = "type")
@JsonTypeIdResolver(GenericInterfaceTypeIdResolver.class)
public abstract class Validator<T>
{
  protected static String MESSAGE_PARAM_NAME = "message";
  
  /** validator parameters such as regular expression, min value, max value etc. */
  private Map<String, String> params = new HashMap<String, String>();

  public Validator()
  {
    
  }
  
  public void init()
  {
    Map<String, String> mergedParams = getDefaultParams();
    for (String key : mergedParams.keySet())
    {
      if (params.containsKey(key))
      {
        mergedParams.put(key,params.get(key));
      }
    }
    this.params = mergedParams;
  }
  
  public Validator(Map<String, String> params)
  {
    super();
    this.params = getDefaultParams();
    if (params != null)
    {
      for (String key : this.params.keySet())
      {
        if (params.containsKey(key))
        {
          this.params.put(key, params.get(key));
        }
      }
    }
  }

  
  public final Map<String, String> getParams()
  {
    return params;
  }

  // public abstract boolean validate(T value, Schema<T> schema, Datastore datastore, String path, List<JsonError>
  // jsonErrors);

  public abstract boolean validate(T value, Schema<T> schema, String path, List<JsonError> jsonErrors);

  @JsonIgnore
  public abstract Map<String, String> getDefaultParams();

  public final String getType()
  {
    return this.getClass().getSimpleName().toString().toLowerCase();
  }

  @Override
  public String toString()
  {
    return "Validator [getParams()=" + getParams() + ", getType()=" + getType()
        + "]";
  }

}

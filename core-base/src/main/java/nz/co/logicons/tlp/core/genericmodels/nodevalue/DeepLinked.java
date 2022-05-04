package nz.co.logicons.tlp.core.genericmodels.nodevalue;

import java.util.Map;

/**
 * Models a deep link within a document.
 * @author bhambr
 * 
 */
public class DeepLinked
{
  private final Map<String, String> properties;
  
  public Map<String, String> getProperties()
  {
    return properties;
  }

  public DeepLinked(Map<String, String> properties)
  {
    this.properties = properties;
  }
  
  public String getProperty(String key)
  {
    return properties.get(key);
  }
  

}

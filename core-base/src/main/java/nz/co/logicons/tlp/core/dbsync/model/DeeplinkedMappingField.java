package nz.co.logicons.tlp.core.dbsync.model;

/**
 * Mapping settings for a deepfield schema.
 * @author bhambr .
 *
 */
public class DeeplinkedMappingField
{
  /** The name of the deeplink field found in dbsync table.. */
  private final String fieldName;
  
  /** The keys for the deeplink field found in dbsync table (e.t TN_Job and Items). */
  private final String[] keys;

  public DeeplinkedMappingField(String fieldName, String[] keys)
  {
    super();
    this.fieldName = fieldName;
    this.keys = keys;
  }
  
  public String getFieldName()
  {
    return fieldName;
  }

  public String[] getKeys()
  {
    return keys;
  } 
}

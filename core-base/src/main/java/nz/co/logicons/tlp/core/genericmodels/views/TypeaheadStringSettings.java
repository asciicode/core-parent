package nz.co.logicons.tlp.core.genericmodels.views;

/**
 * Settings for a typeahead that applies to a field for StringSchema (not idschema!).
 * 
 * @author bhambr
 *
 */
public class TypeaheadStringSettings
{
  
  private String schemaid;

  private String fieldid;

  private Type type;
  
  public String getSchemaid()
  {
    return schemaid;
  }

  public void setSchemaid(String schemaid)
  {
    this.schemaid = schemaid;
  }

  public String getFieldid()
  {
    return fieldid;
  }

  public void setFieldid(String fieldid)
  {
    this.fieldid = fieldid;
  }

  public Type getType()
  {
    return type;
  }

  public void setType(Type type)
  {
    this.type = type;
  }

  public enum Type
  {
    //Type ahead returns selected string value.
    SelectStringValue,
    //Type ahead returns selected object/document.
    SelectObjectValue,
    //Type ahead merges selected object into parent of field.
    MergeObjectValue
  }
}

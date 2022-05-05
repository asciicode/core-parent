package nz.co.logicons.tlp.core.dbsync.model;


/**
 * Models mapping information (between child schema and database field) for simple fields such as string, boolean, date etc.
 * @author bhambr
 *
 */
public class SimpleFieldMapping
{
  private final String fieldName;
  
  private final FieldTypeAndLength fieldTypeAndLength;

  public SimpleFieldMapping(String fieldName, FieldTypeAndLength fieldTypeAndLength)
  {
    super();
    this.fieldName = fieldName;
    this.fieldTypeAndLength = fieldTypeAndLength;
  }

  public String getFieldName()
  {
    return fieldName;
  }

  public FieldTypeAndLength getFieldTypeAndLength()
  {
    return fieldTypeAndLength;
  }
  
}

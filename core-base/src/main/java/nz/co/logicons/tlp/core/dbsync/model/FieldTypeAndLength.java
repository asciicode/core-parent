package nz.co.logicons.tlp.core.dbsync.model;

import nz.co.logicons.tlp.core.enums.FieldType;

/**
 * Models the field type and field lenght used in dbsyc table fields.
 * @author bhambr
 *
 */
public class FieldTypeAndLength
{
  private final FieldType fieldType;

  /** Valid for varchar (where it is the length) and for decimal (where its is the scale i.e digits after decimal point). */
  private final int fieldLength;

  public FieldTypeAndLength(FieldType fieldType, int fieldLength)
  {
    super();
    this.fieldType = fieldType;
    this.fieldLength = fieldLength;
  }
  
  public FieldTypeAndLength(FieldType fieldType)
  {
    super();
    this.fieldType = fieldType;
    this.fieldLength = 0;
  }
  
  public FieldType getFieldType()
  {
    return fieldType;
  }

  public int getFieldLength()
  {
    return fieldLength;
  }
}

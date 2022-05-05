package nz.co.logicons.tlp.core.genericmodels.schemas;


import java.math.BigDecimal;
import java.math.RoundingMode;

import nz.co.logicons.tlp.core.dbsync.model.FieldTypeAndLength;
import nz.co.logicons.tlp.core.enums.DBType;
import nz.co.logicons.tlp.core.enums.FieldType;
import nz.co.logicons.tlp.core.genericmodels.nodes.Node;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Schema for a number field.
 * @author bhambr
 *
 */
public class NumberSchema
    extends ChildSchema<BigDecimal>
{
  /** Number of decimal places. */
  private int scale = 0;

  public int getScale()
  {
    return scale;
  }
  
  @Override
  public void setScale(int scale)
  {
    this.scale = scale;
  }

  @Override
  public Node<BigDecimal> deserializeNode(JsonNode jsonNode)
  {
    if (jsonNode == null || jsonNode.isNull())
    {
      return new Node<>(null, this);
    }
    else
    {
      return new Node<>(fixScale(new BigDecimal(jsonNode.asDouble())), this);
    }
  }
  
  @Override
  public FieldTypeAndLength getFieldTypeAndLength(DBType dbType)
  {
    if (getScale() == 0)
    {
     return new FieldTypeAndLength(FieldType.BIGINT); 
    }
    else
    {
      return new FieldTypeAndLength(FieldType.DECIMAL, getScale()); 
    }
  }
  
  public BigDecimal fixScale(BigDecimal input)
  {
    return input.setScale(getScale(), RoundingMode.HALF_UP);
  }
}

package nz.co.logicons.tlp.core.genericmodels.schemas;

import org.apache.commons.lang3.StringUtils;

import nz.co.logicons.tlp.core.dbsync.model.FieldTypeAndLength;
import nz.co.logicons.tlp.core.enums.DBType;
import nz.co.logicons.tlp.core.enums.FieldType;
import nz.co.logicons.tlp.core.genericmodels.base.GenericInterface;
import nz.co.logicons.tlp.core.genericmodels.nodes.Node;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Schema for a string field.
 * @author bhambr
 *
 */
public class StringSchema
    extends ChildSchema<String>
{
  
  @Override
  public Node<String> deserializeNode(JsonNode jsonNode)
  {
    if (jsonNode == null || jsonNode.isNull() || StringUtils.isBlank(jsonNode.asText()))
    {
      return new Node<>(null, this);
    }
    else
    {
      return new Node<>(jsonNode.asText(), this);
    }
  }
  
  @Override
  public boolean isValueSet(String t)
  {
    return StringUtils.isNotEmpty(t);
  }
  
  @Override
  public FieldTypeAndLength getFieldTypeAndLength(DBType dbType)
  {
    int fieldLength = 100;
    
    if (StringUtils.containsIgnoreCase(getId(), GenericInterface.DESCRIPTION_FIELD_NAME))
    {
      fieldLength = 1024;
    }
    
    return new FieldTypeAndLength(FieldType.VARCHAR, fieldLength);
  }

  @Override
  public void setScale(int scale)
  {
    // TODO Auto-generated method stub
    
  }
 
}

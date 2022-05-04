package nz.co.logicons.tlp.core.genericmodels.views;

import javax.swing.SortOrder;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.node.JsonNodeType;

import nz.co.logicons.tlp.core.genericmodels.jackson.JsonNodeDeserializer;
import nz.co.logicons.tlp.core.genericmodels.schemas.ChildSchema;

public class SortField
    extends Field
{

  private int order = 0;
  
  @JsonDeserialize(using =JsonNodeDeserializer.class)
  private JsonNode value;
  
  public SortField()
  {
    
  }
  
  public SortField(ChildSchema<?> schema, SortOrder sortOrder)
  {
    super(schema);
    this.order = (sortOrder == SortOrder.DESCENDING ) ? -1 : 1;
  }

  public int getOrder()
  {
    return order;
  }
  
  public Object getValue()
  {
    switch (getValueType())
    {
      case BOOLEAN:
        return value.asBoolean();
      case NUMBER:
        return value.asDouble();
      case STRING:
        return value.asText();
      case OBJECT:
        return value.asText();
      default:
        return null;
    }
  }
  
  
  public JsonNodeType getValueType()
  {
    if (value == null)
    {
      return JsonNodeType.NULL;
    }
    return value.getNodeType();
  }
}

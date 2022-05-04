package nz.co.logicons.tlp.core.genericmodels.schemas;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.databind.JsonNode;

import nz.co.logicons.tlp.core.genericmodels.nodes.Node;

/**
 * Schema for a field that contains a list of strings.
 * @author bhambr
 *
 */
public class ListStringSchema
    extends ChildSchema<List<String>>
{
  @Override
  public Node<List<String>> deserializeNode(JsonNode jsonNode)
  {
    List<String> list = new ArrayList<>();

    if (jsonNode != null)
    {
      Iterator<JsonNode> iterator = jsonNode.elements();
      while (iterator.hasNext())
      {
        JsonNode childJsonNode = iterator.next();
        list.add(childJsonNode.asText());
      }
    }

    return new Node<List<String>>(list, this);
  }

  @Override
  public boolean isValueSet(List<String> t)
  {
    if (t == null || t.size() == 0)
    {
      return false;
    }

    for (String string : t)
    {
      if (StringUtils.isNotBlank(string))
      {
        return true;
      }
    }
    return false;
  }

  @Override
  public void setScale(int scale)
  {
    // TODO Auto-generated method stub
    
  }
}

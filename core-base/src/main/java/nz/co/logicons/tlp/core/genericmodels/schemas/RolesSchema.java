package nz.co.logicons.tlp.core.genericmodels.schemas;

import com.fasterxml.jackson.databind.JsonNode;

import nz.co.logicons.tlp.core.genericmodels.nodes.Node;
import nz.co.logicons.tlp.core.genericmodels.permissions.Roles;

/**
 * Schema for a field that contains {@link Roles}.
 * @author bhambr
 *
 */
public class RolesSchema extends ChildSchema<Roles>
{

  @Override
  public Node<Roles> deserializeNode(JsonNode jsonNode)
  {
    Roles roles = new Roles(jsonNode, getDatastore());
    
    return new Node<Roles>(roles, this);
  }

  @Override
  public void setScale(int scale)
  {
    // TODO Auto-generated method stub
    
  }

}

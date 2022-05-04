package nz.co.logicons.tlp.core.genericmodels.schemas;


import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;

import nz.co.logicons.tlp.core.dbsync.model.FieldTypeAndLength;
import nz.co.logicons.tlp.core.enums.DBType;
import nz.co.logicons.tlp.core.enums.FieldType;
import nz.co.logicons.tlp.core.genericmodels.jackson.serializationprofile.SerializationProfile;
import nz.co.logicons.tlp.core.genericmodels.nodes.Node;

/**
 * Schema for a boolean field.
 * @author bhambr
 *
 */
public class BoolSchema
    extends ChildSchema<Boolean>
{
  @Override
  public Node<Boolean> deserializeNode(JsonNode jsonNode)
  {
    if (jsonNode == null)
    {
      return new Node<>(false, this);
    }
    else
    {
      return new Node<>(jsonNode.asBoolean(), this);
    }
  }

  @Override
  public void serializeNode(Node<Boolean> node,
    JsonGenerator jsonGenerator/* , User user */,
    SerializationProfile serializationProfile, String parentPath)
    throws JsonGenerationException, IOException
  {
    jsonGenerator.writeBoolean(node.getValue());
  }
  
  @Override
  public FieldTypeAndLength getFieldTypeAndLength(DBType dbType)
  {
      return new FieldTypeAndLength(FieldType.BIT);
  }

  @Override
  public void setScale(int scale)
  {
    // TODO Auto-generated method stub
    
  }

}

package nz.co.logicons.tlp.core.genericmodels.schemas;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;

import nz.co.logicons.tlp.core.genericmodels.jackson.serializationprofile.SerializationProfile;
import nz.co.logicons.tlp.core.genericmodels.nodes.Node;
import nz.co.logicons.tlp.core.genericmodels.nodevalue.Attachment;

public class ListAttachmentSchema
    extends ChildSchema<List<Attachment>>
{

  @Override
  public Node<List<Attachment>> deserializeNode(JsonNode jsonNode)
  {
    List<Attachment> list = new ArrayList<>();
    if (jsonNode != null && jsonNode.isArray())
    {
      Iterator<JsonNode> iterator = jsonNode.elements();
      while (iterator.hasNext())
      {
        Attachment attachment = AttachmentSchema.getAttachment(getDatastore(), iterator.next());
        if (attachment != null)
        {
          list.add(attachment);
        }
      }
    }
    return new Node<List<Attachment>>(list, this);
  }
  
  @Override
  public void serializeNode(Node<List<Attachment>> node,
    JsonGenerator jsonGenerator, /* User user, */
    SerializationProfile serializationProfile, String parentPath)
    throws JsonGenerationException, IOException
  {
    jsonGenerator.writeStartArray();
    for (Attachment attachment : node.getValue())
    {
      if (attachment != null)
      {
        AttachmentSchema.serialize(attachment, jsonGenerator);
      }
    }
    jsonGenerator.writeEndArray();
  }

  @Override
  public boolean isValueSet(List<Attachment> t)
  {
    if (t == null || t.size() == 0)
    {
      return false;
    }
    else
    {
      return true;
    }
  }

  @Override
  public void setScale(int scale)
  {
    // TODO Auto-generated method stub
    
  }

}

package nz.co.logicons.tlp.core.genericmodels.schemas;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;

import nz.co.logicons.tlp.core.genericmodels.JsonNodeHelper;
import nz.co.logicons.tlp.core.genericmodels.jackson.serializationprofile.SerializationProfile;
import nz.co.logicons.tlp.core.genericmodels.nodes.Node;
import nz.co.logicons.tlp.core.genericmodels.nodevalue.Attachment;
import nz.co.logicons.tlp.core.mongo.MongoDatastore;


public class AttachmentSchema
    extends ChildSchema<Attachment>
{

  public static final String ATTACHMENTID = "attachmentid";

  private static final String FILENAME = "filename";
  
  private static final String FORMATTEDFILENAME = "formattedfilename";
  
  private static final String CONTENTTYPE = "contenttype";
  
  private static final String LENGTH = "length";
  
  private static final String MD5 = "MD5";

  @Override
  public Node<Attachment> deserializeNode(JsonNode jsonNode)
  {
    return new Node<Attachment>(getAttachment(getDatastore(), jsonNode), this);
  }
  
  @Override
  public void serializeNode(Node<Attachment> node,
    JsonGenerator jsonGenerator/* , User user */,
    SerializationProfile serializationProfile, String parentPath)
    throws JsonGenerationException, IOException
  {
    if (node == null)
    {
      jsonGenerator.writeNull();
    }
    else
    {
      serialize(node.getValue(), jsonGenerator/* , user */);
    }
  }
  
  public static Attachment getAttachment(MongoDatastore datastore, JsonNode jsonNode)
  {
    Attachment attachment = null;
    if (jsonNode != null && jsonNode.isObject())
    {
      String attachmentid = JsonNodeHelper.getString(jsonNode, ATTACHMENTID);
      attachment = datastore.getAttachment(attachmentid);
    }
    return attachment;
  }
  
  public static void serialize(Attachment attachment,
    JsonGenerator jsonGenerator/* , User user */)
    throws JsonGenerationException, IOException
  {
    if (attachment == null)
    {
      jsonGenerator.writeNull();
    }
    else
    {
      jsonGenerator.writeStartObject();
      jsonGenerator.writeStringField(ATTACHMENTID, attachment.getAttachmentid());
      // if (!user.getRoles().isSystem())
      // {
        jsonGenerator.writeStringField(FILENAME, attachment.getFilename());
        jsonGenerator.writeStringField(FORMATTEDFILENAME, attachment.getFormattedfilename());
        jsonGenerator.writeStringField(CONTENTTYPE, attachment.getContenttype());
        jsonGenerator.writeNumberField(LENGTH, attachment.getLength());
        jsonGenerator.writeStringField(MD5, attachment.getMD5());
        // }
      jsonGenerator.writeEndObject();
    }
  }

  @Override
  public void setScale(int scale)
  {
    // TODO Auto-generated method stub
    
  }

}

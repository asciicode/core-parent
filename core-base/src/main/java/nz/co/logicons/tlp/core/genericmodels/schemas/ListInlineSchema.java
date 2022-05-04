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
import nz.co.logicons.tlp.core.genericmodels.nodevalue.Document;

/**
 * Schema for a field that contains a list of documents. 
 * Since the documents are not at root level they are called an inline documents.
 * @author bhambr
 *
 */
public class ListInlineSchema
    extends ChildSchema<List<Document>>
{

  /** The id of the schema for the inline documents. */
  private String subtype;

  @Override
  public String getSubtype()
  {
    return subtype;
  }

  @Override
  public Node<List<Document>> deserializeNode(JsonNode jsonNode)
  {
    if (jsonNode == null)
    {
      return new Node<List<Document>>(new ArrayList<Document>(), this);
    }

    DocumentSchema documentSchema = getDatastore().getSchema(subtype);
    List<Document> list = new ArrayList<>();

    if (jsonNode != null)
    {
      Iterator<JsonNode> iterator = jsonNode.elements();
      while (iterator.hasNext())
      {
        JsonNode childJsonNode = iterator.next();
        list.add(documentSchema.deserialize(childJsonNode, documentSchema).getValue());
      }
    }
    return new Node<List<Document>>(list, this);
  }

  @Override
  public void serializeNode(Node<List<Document>> node,
    JsonGenerator jsonGenerator, /* User user, */
    SerializationProfile serializationProfile, String parentPath)
    throws JsonGenerationException, IOException
  {
    DocumentSchema documentSchema = getDatastore().getSchema(subtype);
    jsonGenerator.writeStartArray();
    for (Document document : node.getValue())
    {
      new Node<>(document, documentSchema).serialize(jsonGenerator, serializationProfile, parentPath);
    }
    jsonGenerator.writeEndArray();
  }

  @Override
  public void setScale(int scale)
  {
    // TODO Auto-generated method stub
    
  }
}

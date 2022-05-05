package nz.co.logicons.tlp.core.genericmodels.schemas;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;

import nz.co.logicons.tlp.core.genericmodels.JsonNodeHelper;
import nz.co.logicons.tlp.core.genericmodels.base.GenericInterface;
import nz.co.logicons.tlp.core.genericmodels.jackson.serializationprofile.SerializationProfile;
import nz.co.logicons.tlp.core.genericmodels.nodes.DocumentNode;
import nz.co.logicons.tlp.core.genericmodels.nodes.Node;
import nz.co.logicons.tlp.core.genericmodels.nodevalue.DeepLinked;
import nz.co.logicons.tlp.core.genericmodels.nodevalue.Document;
import nz.co.logicons.tlp.core.genericmodels.validators.DeepLinkedValidator;
import nz.co.logicons.tlp.core.mongo.MongoDatastore;

/**
 * Schema for a deep link
 * 
 * @author bhambr
 *
 */
public class DeepLinkedSchema
    extends ChildSchema<DeepLinked>
{

  private String subtype;

  private String[] keys = new String[]{};

  @Override
  public void init(DocumentSchema documentSchema, MongoDatastore datastore)
  {
    super.init(documentSchema, datastore);
    addValidator(new DeepLinkedValidator());
    if (StringUtils.isNotBlank(subtype))
    {
      keys = StringUtils.split(subtype, ".");
    }
  }

  @Override
  public String getSubtype()
  {
    return subtype;
  }

  @Override
  public Node<DeepLinked> deserializeNode(JsonNode jsonNode)
  {
    if (jsonNode != null)
    {
      Map<String, String> properties = new LinkedHashMap<String, String>();

      for (String key : keys)
      {
        properties.put(key, JsonNodeHelper.getString(jsonNode, key));
      }
      return new Node<>(new DeepLinked(properties), this);
    }

    return new Node<>(null, this);
  }

  @Override
  public void serializeNode(Node<DeepLinked> node,
    JsonGenerator jsonGenerator, /* User user, */
    SerializationProfile serializationProfile, String parentPath)
    throws JsonGenerationException, IOException
  {
    if (node == null || node.getValue() == null)
    {
      jsonGenerator.writeNull();
    }
    else
    {
      jsonGenerator.writeStartObject();
      for (String key : keys)
      {
        String value = node.getValue().getProperty(key);
        if (StringUtils.isNotBlank(value))
        {
          jsonGenerator.writeStringField(key, value);
        }
      }
      jsonGenerator.writeEndObject();
    }
  }
  
  
  @Override
  public void serializeNodeExpanded(Node<DeepLinked> node,
    JsonGenerator jsonGenerator/* , User user */,
    SerializationProfile serializationProfile, String parentPath)
    throws JsonGenerationException, IOException
  {
    jsonGenerator.writeStartObject();
    DocumentNode documentNode = getDatastore().getDocument(keys[0], node.getValue().getProperty(keys[0]), false);
    if (documentNode != null)
    {
      jsonGenerator.writeFieldName(keys[0]);
      documentNode.serialize(jsonGenerator, serializationProfile, parentPath);

      for (int i = 1; i < keys.length; i++)
      {
        documentNode = find(documentNode.getValue(), keys[i], node.getValue().getProperty(keys[i]));
        if (documentNode != null)
        {
          jsonGenerator.writeFieldName(keys[i]);
          documentNode.serialize(jsonGenerator, serializationProfile, parentPath);
        }
      }
    }
    jsonGenerator.writeEndObject();
    
  }
  

  /**
   * Track down list of inline documents within current document based on provided id
   * Then track down document in above list based on provided value (childid)
   * 
   * @param currentDocument .
   * @param id .
   * @param value .
   * @return .
   */
  private DocumentNode find(Document currentDocument, String id, String value)
  {
    Node<List<Document>> node = (Node<List<Document>>) currentDocument.get(id);
    if (node == null)
    {
      return null;
    }

    for (Document document : node.getValue())
    {
      if (StringUtils.equals(value, document.get(GenericInterface.CHILD_ID_FIELD_NAME).getValue().toString()))
      {
        return new DocumentNode(document, getDatastore().getSchema(node.getSchema().getSubtype()));
      }
    }
    return null;
  }

  @JsonIgnore
  public String[] getKeys()
  {
    return keys;
  }

  @Override
  public void setScale(int scale)
  {
    // TODO Auto-generated method stub
    
  }
}

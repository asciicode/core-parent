package nz.co.logicons.tlp.core.genericmodels.schemas;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;

import nz.co.logicons.tlp.core.genericmodels.jackson.serializationprofile.SerializationProfile;
import nz.co.logicons.tlp.core.genericmodels.nodes.DocumentNode;
import nz.co.logicons.tlp.core.genericmodels.nodes.Node;
import nz.co.logicons.tlp.core.genericmodels.validators.ListLinkedDocumentValidator;
import nz.co.logicons.tlp.core.mongo.MongoDatastore;


/**
 * Schema for field that has a list of links to other documents.
 * Acts as foreign keys.
 * @author bhambr
 *
 */
public class ListLinkedSchema extends ListStringSchema
{
  /** The id of the schema that the field links to. */
  private String subtype;
  
  @Override
  public String getSubtype()
  {
    return subtype;
  }
  
  @Override
  public void init(DocumentSchema documentSchema, MongoDatastore datastore)
  {
    super.init(documentSchema, datastore);
    addValidator(new ListLinkedDocumentValidator());
  }
  
  @Override
  public void serializeNodeExpanded(Node<List<String>> node,
    JsonGenerator jsonGenerator, /* User user, */
    SerializationProfile serializationProfile, String parentPath)
    throws JsonGenerationException, IOException
  {
    
    jsonGenerator.writeStartArray();
    for (String documentid : node.getValue())
    {
      DocumentNode documentNode = getDatastore().getDocument(subtype, documentid, false);
      if (documentNode != null)
      {
        documentNode.serialize(jsonGenerator, serializationProfile, parentPath);
      }
    }
    jsonGenerator.writeEndArray();
  }
}

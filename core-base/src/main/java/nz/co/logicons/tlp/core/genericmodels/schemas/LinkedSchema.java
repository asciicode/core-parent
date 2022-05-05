package nz.co.logicons.tlp.core.genericmodels.schemas;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;

import nz.co.logicons.tlp.core.genericmodels.jackson.serializationprofile.SerializationProfile;
import nz.co.logicons.tlp.core.genericmodels.nodes.Node;
import nz.co.logicons.tlp.core.genericmodels.validators.LinkedDocumentValidator;
import nz.co.logicons.tlp.core.mongo.MongoDatastore;


/**
 * Schema for field that has a link to another document.
 * Acts as a foreign key.
 * @author bhambr
 *
 */
public class LinkedSchema extends StringSchema
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
    addValidator(new LinkedDocumentValidator(null));
  }
  
  @Override
  public void serializeNodeExpanded(Node<String> node, JsonGenerator jsonGenerator, /* User user, */
    SerializationProfile serializationProfile, String parentPath)
    throws JsonGenerationException, IOException
  {
    // DocumentNode documentNode = getDatastore().getDocument(getSubtype(), node.getValue(), user, false);
    // if (documentNode == null)
    // {
    // super.serializeNodeExpanded(node, jsonGenerator, user, serializationProfile, parentPath);
    // }
    // else
    // {
    // documentNode.serialize(jsonGenerator, user, serializationProfile, parentPath);
    // }
    
  }

}

package nz.co.logicons.tlp.core.genericmodels.schemas;


import com.fasterxml.jackson.databind.JsonNode;

import nz.co.logicons.tlp.core.genericmodels.nodes.DocumentNode;
import nz.co.logicons.tlp.core.genericmodels.nodes.Node;
import nz.co.logicons.tlp.core.genericmodels.nodevalue.Document;

/**
 * Schema for a field that contains a document. 
 * Since the document is not at root level it is called an inline document.
 * @author bhambr
 *
 */
public class InlineSchema extends ChildSchema<Document>
{
  /** The id of the schema for the inline document. */
  private String subtype;

  
  @Override
  public String getSubtype()
  {
    return subtype;
  }

  @Override
  public Node<Document> deserializeNode(JsonNode jsonNode)
  {
    DocumentSchema documentSchema = getDatastore().getSchema(subtype);
    if (jsonNode == null)
    {
      return new DocumentNode(documentSchema);
    }
    return documentSchema.deserialize(jsonNode, documentSchema);
  }

  @Override
  public void setScale(int scale)
  {
    // TODO Auto-generated method stub
    
  }

}

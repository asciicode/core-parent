package nz.co.logicons.tlp.core.business.models;


import org.apache.commons.lang3.StringUtils;

import nz.co.logicons.tlp.core.genericmodels.base.GenericInterface;
import nz.co.logicons.tlp.core.genericmodels.nodes.DocumentNode;

public abstract class Model
{
  private final DocumentNode documentNode;
  
  public Model(DocumentNode documentNode)
  {
    this.documentNode = documentNode;
    if (!StringUtils.equals(getSchemaId(), documentNode.getSchema().getId()))
    {
      throw new RuntimeException("Invalid schema");
    }
  }
  
  // public Model(Datastore datastore)
  // {
  // this.documentNode = new DocumentNode(datastore.getSchema(getSchemaId()));
  // if (!StringUtils.equals(getSchemaId(), documentNode.getSchema().getId()))
  // {
  // throw new RuntimeException("Invalid schema");
  // }
  // }
  
  public abstract String getSchemaId();
  
  public DocumentNode getDocumentNode()
  {
    return documentNode;
  }
  
  public String getId()
  {
    return getDocumentNode().getChildNodeValueAsString(GenericInterface.ID_FIELD_NAME);
  }

  public void setId(String value)
  {
    getDocumentNode().setChildNodeValue(GenericInterface.ID_FIELD_NAME, value);
  }
}

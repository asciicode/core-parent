package nz.co.logicons.tlp.core.business.models;

import org.joda.time.LocalDateTime;

import nz.co.logicons.tlp.core.genericmodels.nodes.DocumentNode;

/**
 * Models instances of audit schema.
 * @author bhambr
 *
 */
public class Audit extends Model
{
  public static final String SCHEMA = "(audit)";
  
  public static final String FIELD = "audit";
  
  public static final String CREATED_BY = "createdby";
  
  public static final String CREATED_ON = "createdon";
  
  public static final String CHANGED_BY = "changedby";
  
  public static final String CHANGED_ON = "changedon";
  
  public static final String LOGICAL_DELETE = "logicaldelete";
  
  public static final String AUDIT_LOGICAL_DELETE = FIELD + "." + LOGICAL_DELETE;
  
  
  public Audit(DocumentNode documentNode)
  {
    super(documentNode);
  }
  
  /**
   * Set createdby and createdon.
   * @param username .
   */
  public void setCreateFields(String username)
  {
    LocalDateTime dateTime = new LocalDateTime();
    getDocumentNode().setChildNodeValue(CREATED_BY, username);
    getDocumentNode().setChildNodeValue(CREATED_ON, dateTime);
  }
  
  /**
   * Set changedby and changedon.
   * @param username .
   */
  public void setChangeFields(String username)
  {
    LocalDateTime dateTime = new LocalDateTime();
    getDocumentNode().setChildNodeValue(CHANGED_BY, username);
    getDocumentNode().setChildNodeValue(CHANGED_ON, dateTime);
  }
  
  /**
   * Set logical deletion, changedby and changedon.
   * @param username .
   */
  public void setLogicalDeleteFields(String username)
  {
    LocalDateTime dateTime = new LocalDateTime();
    getDocumentNode().setChildNodeValue(CHANGED_BY, username);
    getDocumentNode().setChildNodeValue(CHANGED_ON, dateTime);
    getDocumentNode().setChildNodeValue(LOGICAL_DELETE, !Boolean.valueOf(getDocumentNode().getChildNode(LOGICAL_DELETE).toString()));
  }
  
  /**
   * Set logical deletion.
   * @param username .
   */
  public void setLogicalDeletion(Boolean logicalDeletion)
  {
    getDocumentNode().setChildNodeValue(LOGICAL_DELETE, logicalDeletion);
  }
  
  /**
   * Get logical deletion.
   */
  public Boolean getLogicalDeletion()
  {
    if (getDocumentNode().getChildNode(LOGICAL_DELETE) == null)
    {
      return false;
    }
    return Boolean.valueOf(getDocumentNode().getChildNode(LOGICAL_DELETE).toString());
  }

  @Override
  public String getSchemaId()
  {
    return SCHEMA;
  }
}

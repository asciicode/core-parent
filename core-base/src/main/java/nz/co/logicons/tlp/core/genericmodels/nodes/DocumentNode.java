package nz.co.logicons.tlp.core.genericmodels.nodes;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.TypeLiteral;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;

import nz.co.logicons.tlp.core.business.models.Audit;
import nz.co.logicons.tlp.core.business.models.Model;
import nz.co.logicons.tlp.core.enums.ChildSchemaType;
import nz.co.logicons.tlp.core.enums.PermissionType;
import nz.co.logicons.tlp.core.genericmodels.nodevalue.Attachment;
import nz.co.logicons.tlp.core.genericmodels.nodevalue.DeepLinked;
import nz.co.logicons.tlp.core.genericmodels.nodevalue.Document;
import nz.co.logicons.tlp.core.genericmodels.permissions.PermissionResult;
import nz.co.logicons.tlp.core.genericmodels.permissions.Roles;
import nz.co.logicons.tlp.core.genericmodels.schemas.ChildSchema;
import nz.co.logicons.tlp.core.genericmodels.schemas.DeepLinkedSchema;
import nz.co.logicons.tlp.core.genericmodels.schemas.DocumentSchema;
import nz.co.logicons.tlp.core.genericmodels.schemas.NumberSchema;
import nz.co.logicons.tlp.core.mongo.MongoDatastore;

/**
 * Node that contains {@link Document} as its value.
 * Container for all documents.
 * 
 * @author bhambr
 *
 */
public class DocumentNode
    extends Node<Document>
{

  public DocumentNode(DocumentSchema documentSchema)
  {
    super(new Document(), documentSchema);
  }

  public DocumentNode(Document document, DocumentSchema documentSchema)
  {
    super(document, documentSchema);
  }

  /**
   * Check if specified user has required permission.
   */
  @Override
  public boolean isPermitted(PermissionType permissionType, /* User user, */ DocumentNode globalParam)
  {
    // Doesnt do much - just delegates check to document schema.
    return getSchema().getPermissionEvaluator().evaluate(permissionType,
        /* user, */ this,
        globalParam) == PermissionResult.PERMITTED;
  }

  /** Get value of id child node. Inline documents do not have one. */
  @Override
  public String getId()
  {
    Object obj;
    if (getSchema().isInline())
    {
      obj = getChildNode(CHILD_ID_FIELD_NAME).getValue();
    }
    else
    {
      obj = getChildNode(ID_FIELD_NAME).getValue();
    }
    if (obj != null)
    {
      return obj.toString();
    }
    return null;
  }

  /**
   * Get node within this document with specified id.
   * id can contain dots to access nodes down a tree
   * But only links into inlineschema are supported at present.
   * 
   * @param id .
   * @return .
   */
  public Node<?> getChildNode(String id)
  {
    String[] keys = StringUtils.split(id, ".");

    Node<?> node = getValue().get(keys[0]);
    if (node == null)
    {
      ChildSchema<?> childSchema = getSchema().getChildrenMap().get(keys[0]);
      if (childSchema == null)
        return null;

      node = childSchema.deserializeNode(null);
      addChildNode(keys[0], node);
    }

    if (keys.length == 1)
    {
      return node;
    }
    else
    {
      if (getSchema().getChildrenMap().get(keys[0]).getType() == ChildSchemaType.inlineschema)
      {
        // strip first entry
        String[] subKeys = Arrays.copyOfRange(keys, 1, keys.length);
        String subId = StringUtils.join(subKeys, ".");
        return ((DocumentNode) node).getChildNode(subId);
      }
      else
      {
        throw new RuntimeException("Only inlineschema supported!");
      }

    }

  }

  /**
   * Add node to this document.
   * 
   * @param id id of child node schema.
   * @param node child node.
   */
  public void addChildNode(String id, Node<?> node)
  {
    getValue().put(id, node);
  }

  /**
   * @return get the Audit child node.
   */
  public Audit getAudit()
  {
    return new Audit((DocumentNode) getChildNode(Audit.FIELD));
  }

  /**
   * @return get the Logical Deletion(in Audit).
   */
  public Boolean isLogicalDeleted()
  {
    return getAudit().getLogicalDeletion();
  }

  public String getChildNodeValueAsString(String id)
  {
    Object value = null;
    if (getChildNode(id) != null)
      value = getChildNode(id).getValue();
    return (value == null) ? null : String.valueOf(value);
  }

  public String getChildNodeValueAsStringDefault(String id, String defaultId)
  {
    Object value = getChildNode(id);
    if (getChildNode(id) == null || getChildNode(id).getValue() == null)
      value = defaultId;
    return String.valueOf(value);
  }

  public String getChildNodeValueAsStringDefault(String id)
  {
    return getChildNodeValueAsStringDefault(id, StringUtils.EMPTY);
  }

  @SuppressWarnings("unchecked")
  public void setChildNodeValue(String id, String value)
  {
    ((Node<String>) getChildNode(id)).setValue(value);
  }

  @SuppressWarnings("unchecked")
  public void setChildNodeValue(String id, DeepLinked value)
  {
    ((Node<DeepLinked>) getChildNode(id)).setValue(value);
  }

  @SuppressWarnings("unchecked")
  public void setChildNodeValueAsListOfString(String id, List<String> value)
  {
    ((Node<List<String>>) getChildNode(id)).setValue(value);
  }

  @SuppressWarnings("unchecked")
  public void setChildNodeValue(String id, BigDecimal value)
  {
    ((Node<BigDecimal>) getChildNode(id)).setValue(value);
  }

  @SuppressWarnings("unchecked")
  public void setChildNodeValue(String id, double value)
  {
    ((Node<BigDecimal>) getChildNode(id)).setValue(new BigDecimal(value));
  }

  @SuppressWarnings("unchecked")
  public void setChildNodeValue(String id, Document value)
  {
    ((Node<Document>) getChildNode(id)).setValue(value);
  }

  /**
   * @SuppressWarnings("unchecked")
   * public <E extends Enum<E>> E getChildNodeValueAsEnum(String id, Class<E> enumClass)
   * {
   * return (E) getChildNode(id).getValue();
   * }
   **/

  @SuppressWarnings("unchecked")
  public void setChildNodeValue(String id, Roles value)
  {
    ((Node<Roles>) getChildNode(id)).setValue(value);
  }

  @SuppressWarnings("unchecked")
  public LocalDateTime getChildNodeValueAsLocalDateTime(String id)
  {
    return ((Node<LocalDateTime>) getChildNode(id)).getValue();
  }

  @SuppressWarnings("unchecked")
  public void setChildNodeValue(String id, LocalDateTime value)
  {
    ((Node<LocalDateTime>) getChildNode(id)).setValue(value);
  }

  @SuppressWarnings("unchecked")
  public LocalDate getChildNodeValueAsLocalDate(String id)
  {
    return ((Node<LocalDate>) getChildNode(id)).getValue();
  }

  @SuppressWarnings("unchecked")
  public void setChildNodeValue(String id, LocalTime value)
  {
    ((Node<LocalTime>) getChildNode(id)).setValue(value);
  }

  public void setChildNodeValue(String id, List<? extends Model> value)
  {
    List<Document> documents = getChildNodeValueAsDocumentList(id);
    documents.clear();
    for (Model model : value)
    {
      documents.add(model.getDocumentNode().getValue());
    }
  }

  public void setChildNodeValueAsDocumentNodeList(String id, List<DocumentNode> value)
  {
    List<Document> documents = getChildNodeValueAsDocumentList(id);
    documents.clear();
    for (DocumentNode docNode : value)
    {
      documents.add(docNode.getValue());
    }
  }

  @SuppressWarnings("unchecked")
  public LocalTime getChildNodeValueAsLocalTime(String id)
  {
    if (getChildNode(id) == null)
      return null;
    return ((Node<LocalTime>) getChildNode(id)).getValue();
  }

  @SuppressWarnings("unchecked")
  public void setChildNodeValue(String id, LocalDate value)
  {
    ((Node<LocalDate>) getChildNode(id)).setValue(value);
  }

  @SuppressWarnings("unchecked")
  public Boolean getChildNodeValueAsBoolean(String id)
  {
    return ((Node<Boolean>) getChildNode(id)).getValue();
  }

  @SuppressWarnings("unchecked")
  public Boolean getChildNodeValueAsBooleanDefault(String id)
  {
    if (getChildNode(id) != null)
      return ((Node<Boolean>) getChildNode(id)).getValue();
    return false;
  }

  @SuppressWarnings("unchecked")
  public BigDecimal getChildNodeValueAsBigDecimal(String id)
  {
    return ((Node<BigDecimal>) getChildNode(id)).getValue();
  }

  @SuppressWarnings("unchecked")
  public BigDecimal getChildNodeValueAsBigDecimalDefault(String id, BigDecimal defaultBd)
  {
    if (getChildNode(id) != null && getChildNode(id).getValue() != null)
      return ((Node<BigDecimal>) getChildNode(id)).getValue();
    return defaultBd;
  }

  public BigDecimal getChildNodeValueAsBigDecimalDefault(String id)
  {
    return getChildNodeValueAsBigDecimalDefault(id, BigDecimal.ZERO);
  }

  @SuppressWarnings("unchecked")
  public int getChildNodeNumberSchemaScale(String id)
  {
    Node<BigDecimal> node = ((Node<BigDecimal>) getChildNode(id));
    NumberSchema numberSchema = (NumberSchema) node.getSchema();
    return numberSchema.getScale();
  }

  @SuppressWarnings("unchecked")
  public double getChildNodeValueAsDouble(String id)
  {
    Node<BigDecimal> node = ((Node<BigDecimal>) getChildNode(id));
    NumberSchema numberSchema = (NumberSchema) node.getSchema();
    BigDecimal value = node.getValue();
    if (value == null)
    {
      value = BigDecimal.ZERO;
    }
    return numberSchema.fixScale(value).doubleValue();
  }

  @SuppressWarnings("unchecked")
  public int getChildNodeValueAsInt(String id)
  {
    BigDecimal value = ((Node<BigDecimal>) getChildNode(id)).getValue();
    if (value == null)
    {
      value = BigDecimal.ZERO;
    }
    return value.intValue();
  }

  @SuppressWarnings("unchecked")
  public void setChildNodeValue(String id, Boolean value)
  {
    ((Node<Boolean>) getChildNode(id)).setValue(value);
  }

  @SuppressWarnings("unchecked")
  public DeepLinked getChildNodeValueAsDeeplinked(String id)
  {
    Node<DeepLinked> deepLinkedNode = ((Node<DeepLinked>) getChildNode(id));
    if (deepLinkedNode.getValue() == null)
    {
      Map<String, String> properties = new LinkedHashMap<String, String>();
      String[] keys = ((DeepLinkedSchema) deepLinkedNode.getSchema()).getKeys();
      for (String key : keys)
      {
        properties.put(key, null);
      }

      deepLinkedNode.setValue(new DeepLinked(properties));
    }
    return deepLinkedNode.getValue();
  }

  @SuppressWarnings("unchecked")
  private <E> List<E> getChildNodeValueAsList(String id, TypeLiteral<E> listClass)
  {
    return getChildNode(id) != null ? (List<E>) getChildNode(id).getValue() : new ArrayList<E>();
  }

  public List<String> getChildNodeValueAsStringList(String id)
  {
    return getChildNodeValueAsList(id, new TypeLiteral<String>()
    {
    });
  }

  private List<Document> getChildNodeValueAsDocumentList(String id)
  {
    return getChildNodeValueAsList(id, new TypeLiteral<Document>()
    {
    });
  }

  public List<DocumentNode> getChildNodeValueAsDocumentNodeList(String id, MongoDatastore datastore)
  {
    DocumentSchema documentSchema = datastore.getSchema(getSchema().getChildrenMap().get(id)
        .getSubtype());

    List<Document> documents = getChildNodeValueAsDocumentList(id);
    List<DocumentNode> documentNodes = new ArrayList<DocumentNode>();
    for (Document document : documents)
    {
      documentNodes.add(new DocumentNode(document, documentSchema));
    }

    return documentNodes;
  }

  // @SuppressWarnings("unchecked")
  // public DocumentNode getChildNodeValueAsDocumentNode(String id, Datastore datastore)
  // {
  // DocumentSchema documentSchema = datastore.getSchema(getSchema().getChildrenMap().get(id)
  // .getSubtype());
  // Node<Document> documentNode = (Node<Document>) getChildNode(id);
  // return new DocumentNode(documentNode.getValue(), documentSchema);
  // }

  // public <T extends Model> List<T> getChildNodeValueAsModelList(Datastore datastore, Class<T> clazz, String
  // fieldName)
  // {
  // List<T> list = new ArrayList<>();
  // List<DocumentNode> documentNodes = getChildNodeValueAsDocumentNodeList(fieldName, datastore);
  // for (DocumentNode documentNode : documentNodes)
  // {
  // try
  // {
  // list.add(clazz.getConstructor(DocumentNode.class).newInstance(documentNode));
  // }
  // catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
  // | NoSuchMethodException | SecurityException e)
  // {
  // throw new ValidationException(JsonErrorType.ServerScriptingError);
  // }
  // }
  // // Unfortunately changes to list wont automatically go back to documentNodes
  // // Perhaps need to have our own implementation of list which is backed by documentNodes.
  // return list;
  // }

  // public <T extends Model> T getChildNodeValueAsModel(Datastore datastore, Class<T> clazz, String fieldName)
  // {
  // DocumentNode documentNode = getChildNodeValueAsDocumentNode(fieldName, datastore);
  // try
  // {
  // return clazz.getConstructor(DocumentNode.class).newInstance(documentNode);
  // }
  // catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
  // | NoSuchMethodException | SecurityException e)
  // {
  // throw new ValidationException(JsonErrorType.ServerScriptingError);
  // }
  // }

  @Override
  public DocumentSchema getSchema()
  {
    return (DocumentSchema) super.getSchema();
  }

  public String prettyPrintDocumentValues()
  {
    if (this.getValue() != null)
    {
      return this.getValue().prettyPrintValues();
    }

    return StringUtils.EMPTY;
  }

  @SuppressWarnings("unchecked")
  public void setChildNodeAsAttachmentValue(String id, Attachment value)
  {
    ((Node<Attachment>) getChildNode(id)).setValue(value);
  }

  @SuppressWarnings("unchecked")
  public void addChildNodeAsAttachmentToList(String id, Attachment value)
  {
    List<Attachment> attachments = getChildNodeValueAsList(id, new TypeLiteral<Attachment>()
    {
    });
    attachments.add(value);
    ((Node<List<Attachment>>) getChildNode(id)).setValue(attachments);
  }
}

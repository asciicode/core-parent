package nz.co.logicons.tlp.core.genericmodels.validators;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import nz.co.logicons.tlp.core.common.MessageConstants;
import nz.co.logicons.tlp.core.genericmodels.base.GenericInterface;
import nz.co.logicons.tlp.core.genericmodels.nodes.Node;
import nz.co.logicons.tlp.core.genericmodels.nodevalue.DeepLinked;
import nz.co.logicons.tlp.core.genericmodels.nodevalue.Document;
import nz.co.logicons.tlp.core.genericmodels.schemas.Schema;
import nz.co.logicons.tlp.core.ui.JsonError;

public class DeepLinkedValidator extends Validator<DeepLinked>
{

  @Override
  public boolean validate(DeepLinked value,
    Schema<DeepLinked> schema/* , Datastore datastore */, String path,
    List<JsonError> jsonErrors)
  {
    if (value != null)
    {
      //get the key/vale for the deep link (relies on order in map!)
      // Iterator<Entry<String, String>> iterator = value.getProperties().entrySet().iterator();
      
      //first entry is for master document
      // Entry<String, String> entry = iterator.next();
      // DocumentNode documentNode = datastore.getDocument(entry.getKey(), entry.getValue(), false);
      //
      // //check the master document
      // if (documentNode == null)
      // {
      // jsonErrors.add(new SimpleJsonError(path, getParams().get(MESSAGE_PARAM_NAME), getType()));
      // return true;
      // }
      //
      // Document document = documentNode.getValue();
      //
      // //now follow links and check them
      // while (iterator.hasNext())
      // {
      // entry = iterator.next();
      //
      // document = find(document, entry.getKey(), entry.getValue());
      // if (document == null)
      // {
      // jsonErrors.add(new SimpleJsonError(path, getParams().get(MESSAGE_PARAM_NAME), getType()));
      // return true;
      // }
      // }
    }
    return false;
  }

  @Override
  public Map<String, String> getDefaultParams()
  {
    Map<String, String> map = new LinkedHashMap<>();
    map.put(MESSAGE_PARAM_NAME, MessageConstants.INVALID);
    return map;
  }
  
  /**
   * Track down list of inline documents within current document based on provided id
   * Then track down document in above list based on provided value (childid)
   * @param currentDocument .
   * @param id .
   * @param value .
   * @return .
   */
  private Document find(Document currentDocument, String id, String value)
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
        return document;
      }
    }
    return null;
  }

}

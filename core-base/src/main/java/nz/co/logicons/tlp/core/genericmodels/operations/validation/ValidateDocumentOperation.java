package nz.co.logicons.tlp.core.genericmodels.operations.validation;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import nz.co.logicons.tlp.core.enums.JsonErrorType;
import nz.co.logicons.tlp.core.exceptions.ValidationException;
import nz.co.logicons.tlp.core.genericmodels.nodes.DocumentNode;
import nz.co.logicons.tlp.core.genericmodels.nodes.Node;
import nz.co.logicons.tlp.core.genericmodels.nodevalue.Document;
import nz.co.logicons.tlp.core.genericmodels.schemas.DocumentSchema;
import nz.co.logicons.tlp.core.genericmodels.schemas.ListInlineSchema;
import nz.co.logicons.tlp.core.genericmodels.schemas.Schema;
import nz.co.logicons.tlp.core.genericmodels.validators.RequiredValidator;
import nz.co.logicons.tlp.core.genericmodels.validators.Validator;
import nz.co.logicons.tlp.core.mongo.MongoDatastore;
import nz.co.logicons.tlp.core.ui.JsonError;
import nz.co.logicons.tlp.core.ui.SimpleJsonError;

/**
 * Validates a document.
 * Note: Written for generics but so far only used for document nodes.
 * @author bhambr
 *
 */
public class ValidateDocumentOperation
{
  @Autowired
  private MongoDatastore datastore;
  
  // @Autowired
  // private ScriptOperation scriptOperation;
  
  private static Logger log = LoggerFactory.getLogger(ValidateDocumentOperation.class);
  
  /**
   * Validate a node.
   * @param node node.
   */
  public <T> void validate(Node<T> node)
  {
    validate(node, false);
  }
  
  /**
   * Validates a node.
   * @param node node.
   * @param suspendRequiredValidation if set then required validator is not run.
   */
  public <T> void validate(Node<T> node, boolean suspendRequiredValidation)
  {
    List<JsonError> jsonErrors = new ArrayList<>();
    validate(null, node, jsonErrors, "", suspendRequiredValidation);
    if (jsonErrors.size() > 0)
    {
      throw new ValidationException(jsonErrors);
    }
  }
  
  
  private <T> boolean runValidators(String path, T value, Schema<T> schema, List<JsonError> jsonErrors,
    boolean suspendRequiredValidation)
  {
    boolean hasError = false;
    for (Validator<T> validator : schema.getValidators())
    {
      if (validator.getClass() == RequiredValidator.class && suspendRequiredValidation)
      {
        continue;
      }
      if (validator.validate(value, schema, datastore, path, jsonErrors))
      {
        hasError = true;
      }
    }
    return hasError;
  }

  private <T> boolean validate(String id, Node<T> node, List<JsonError> jsonErrors, String path,
    boolean suspendRequiredValidation)
  {
    try
    {
      path = appendIdToPath(id, path);

      boolean hasError = false;

      if (runValidators(path, node.getValue(), node.getSchema(), jsonErrors, suspendRequiredValidation))
      {
        hasError = true;
      }

      if (node.getSchema().getClass() == DocumentSchema.class && node.getValue() != null)
      {
        DocumentNode document = (DocumentNode) node;
        DocumentSchema documentSchema = document.getSchema();

        for (Schema<?> childSchema : documentSchema.getChildrenMap().values())
        {
          Node<?> childNode = document.getChildNode(childSchema.getId());
          if (validate(childSchema.getId(), childNode, jsonErrors, path, suspendRequiredValidation))
          {
            if (documentSchema.isInline())
            {
              hasError = true;
              jsonErrors.add(new SimpleJsonError(path, JsonErrorType.ErrorInside));
            }
          }

          if (childSchema.getClass() == ListInlineSchema.class)
          {
            ListInlineSchema listInlineSchema = (ListInlineSchema) childSchema;
            DocumentSchema listInlineSchemaSubType = datastore.getSchema(listInlineSchema.getSubtype());
            @SuppressWarnings("unchecked")
            List<Document> listDocuments = (List<Document>) childNode.getValue();
            if (listDocuments != null)
            {
              for (Document listDocumentItem : listDocuments)
              {
                Node<?> listItemNode = new DocumentNode(listDocumentItem, listInlineSchemaSubType);
                if (validate(String.valueOf(childSchema.getId() + "." + listDocuments.indexOf(listDocumentItem)),
                    listItemNode, jsonErrors, path, suspendRequiredValidation))
                {
                  hasError = true;
                  jsonErrors
                      .add(new SimpleJsonError(appendIdToPath(childSchema.getId(), path), JsonErrorType.ErrorInside));
                  jsonErrors.add(new SimpleJsonError(path, JsonErrorType.ErrorInside));
                }
              }
            }
          }

        }
        log.info("allen cats {} {} ", path, jsonErrors.size());
        // scriptOperation.executeOnValidationServerScript(document, null, path, jsonErrors);
        if (!jsonErrors.isEmpty())
        {
          log.info("allen cats 1 {} {} ", path, jsonErrors.size());
          log.info("allen cats 2 {} ", document.prettyPrintDocumentValues());
        }
      }
      return hasError;
    }
    catch (Exception e)
    {
      log.error(e.getMessage(), e);
      throw e;
    }

  }
  
  private String appendIdToPath(String id, String path)
  {
    if (StringUtils.isEmpty(path))
    {
      return id;
    }
    else
    {
      return path + "." + id;
    }
  }
}

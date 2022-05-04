package nz.co.logicons.tlp.core.genericmodels.operations;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import nz.co.logicons.tlp.core.common.MessageConstants;
import nz.co.logicons.tlp.core.enums.ChildSchemaType;
import nz.co.logicons.tlp.core.exceptions.ValidationException;
import nz.co.logicons.tlp.core.genericmodels.ChildSchemaSubTypeProvider;
import nz.co.logicons.tlp.core.genericmodels.ValidatorsProvider;
import nz.co.logicons.tlp.core.genericmodels.base.GenericSerializable;
import nz.co.logicons.tlp.core.genericmodels.operations.validation.ValidateOperation;
import nz.co.logicons.tlp.core.genericmodels.schemas.ChildSchema;
import nz.co.logicons.tlp.core.genericmodels.schemas.DocumentSchema;
import nz.co.logicons.tlp.core.genericmodels.validators.Validator;
import nz.co.logicons.tlp.core.ui.JsonError;
import nz.co.logicons.tlp.core.ui.SimpleJsonError;

/**
 * Validates a schema.
 * TODO: Need to go over rules. This class has been patched together with no serious thought on what the rules should
 * be.
 * 
 * @author bhambr
 *
 */
public class ValidateSchemaOperation
    extends ValidateOperation
{

  @Autowired
  private ChildSchemaSubTypeProvider childSchemaSubTypeProvider;

  @Autowired
  private ValidatorsProvider validatorsProvider;

  private static final String CHILDREN_FIELD_NAME = "children";

  private static final String SUB_TYPE_FIELD_NAME = "subtype";

  public void validate(DocumentSchema documentSchema)
  {
    List<JsonError> jsonErrors = new ArrayList<>();

    validateId(documentSchema, documentSchema.isSystemowned(), null, jsonErrors);

    Iterator<ChildSchema<?>> childSchemaIterator = documentSchema.getChildrenMap().values().iterator();
    int childSchemaIndex = 0;
    while (childSchemaIterator.hasNext())
    {
      ChildSchema<?> childSchema = childSchemaIterator.next();
      String path = CHILDREN_FIELD_NAME + "." + childSchemaIndex + ".";

      // check id
      validateId(childSchema, false, path, jsonErrors);

      // check id
      if (ChildSchemaType.IdTypes.contains(childSchema.getType())
          && !StringUtils.equals(childSchema.getId(), GenericSerializable.ID_FIELD_NAME))
      {
        jsonErrors.add(new SimpleJsonError(path + GenericSerializable.ID_FIELD_NAME, "Must be '"
            + GenericSerializable.ID_FIELD_NAME + "'", null));
      }
      
      if (ChildSchemaType.IdTypes.contains(childSchema.getType()))
      {
        childSchema.setTransientfield(false);
      }

      // check subtype
      List<String> validSubTypes = childSchemaSubTypeProvider.get(childSchema.getType());
      if (validSubTypes.size() > 0 && !validSubTypes.contains(childSchema.getSubtype()))
      {
        jsonErrors.add(new SimpleJsonError(path + SUB_TYPE_FIELD_NAME, MessageConstants.INVALID, null));
      }

      // check validators
      removeInvalidValidators(childSchema);

      childSchemaIndex++;
    }

    if (jsonErrors.size() > 0)
    {
      throw new ValidationException(jsonErrors);
    }
  }

  private <T> void removeInvalidValidators(ChildSchema<T> childSchema)
  {
    Iterator<Validator<T>> iterator = childSchema.getValidators().iterator();
    while (iterator.hasNext())
    {
      Validator<T> validator = iterator.next();
      if (!validatorsProvider.getAllTypes().get(childSchema.getType()).contains(validator.getType()))
      {
        iterator.remove();
      }
    }
  }
}

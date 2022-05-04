package nz.co.logicons.tlp.core.genericmodels.operations.validation;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.RegexValidator;

import nz.co.logicons.tlp.core.common.MessageConstants;
import nz.co.logicons.tlp.core.genericmodels.base.GenericInterface;
import nz.co.logicons.tlp.core.genericmodels.base.GenericSerializable;
import nz.co.logicons.tlp.core.ui.JsonError;
import nz.co.logicons.tlp.core.ui.SimpleJsonError;

public abstract class ValidateOperation
{
  public static RegexValidator systemNameValidator = new RegexValidator("^\\([a-zA-Z0-9_]*\\)$");

  private static RegexValidator nonSystemNameValidator = new RegexValidator("^[a-zA-Z0-9_]*$");

  protected void validateId(GenericInterface genericInterface, boolean systemowned, String parentPath, List<JsonError> jsonErrors)
  {
    String path = StringUtils.defaultString(parentPath) + GenericSerializable.ID_FIELD_NAME;
    
    if (StringUtils.isBlank(genericInterface.getId()))
    {
      jsonErrors.add(new SimpleJsonError(path, MessageConstants.REQUIRED, null));
    }
    else if (systemowned && !systemNameValidator.isValid(genericInterface.getId()))
    {
      //jsonErrors.add(new SimpleJsonError(path, "Invalid - Must start with '(' contain alphanumeric or underscore and end with ')'", null));
    }
    else if (!systemowned && !nonSystemNameValidator.isValid(genericInterface.getId()))
    {
      jsonErrors.add(new SimpleJsonError(path, "Invalid - Must only contain alphanumeric or underscore", null));
    }
  }
}

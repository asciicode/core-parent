package nz.co.logicons.tlp.core.ui;

import nz.co.logicons.tlp.core.enums.JsonErrorType;

/**
 * Models an error in the input (json).
 * @author bhambr
 *
 */
public interface JsonError
{
  public JsonErrorType getJsonErrorType();
}

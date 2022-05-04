package nz.co.logicons.tlp.core.ui;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import nz.co.logicons.tlp.core.enums.JsonErrorType;
import nz.co.logicons.tlp.core.genericmodels.base.GenericInterface;
import nz.co.logicons.tlp.core.genericmodels.nodes.DocumentNode;

/**
 * Models an error during the import process.
 * @author bhambr
 *
 */
@JsonPropertyOrder({ "type", "source", "jsonErrors" })
public class ImportJsonError implements JsonError
{
  /** The source of the error. */
  private final GenericInterface source;
  
  /** The underlying errors. */
  private final List<JsonError> jsonErrors;
  
  
  public ImportJsonError(GenericInterface source, List<JsonError> jsonErrors)
  {
    super();
    this.source = source;
    this.jsonErrors = new ArrayList<>(jsonErrors);
  }



  @Override
  public JsonErrorType getJsonErrorType()
  {
    return null;
  }


  public String getType()
  {
    if (source != null)
    {
      if (source.getClass() == DocumentNode.class)
      {
        return "Document " + source.getId() + " in schema" + ((DocumentNode) source).getSchema().getId();
      }
      else
      {
        return source.getClass().getSimpleName();
      }
    }
    return null;
  }
  
  public GenericInterface getSource()
  {
    return source;
  }

  public List<JsonError> getJsonErrors()
  {
    return jsonErrors;
  }
}

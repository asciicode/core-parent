package nz.co.logicons.tlp.core.enums;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;

import nz.co.logicons.tlp.core.genericmodels.base.EnumInterface;

/**
 * Indicates what level a script runs at.
 * 
 * @author bhambr
 *
 */
public enum ScriptLevel implements EnumInterface
{
  /* Category for scripts that run on a document schema. */
  DocumentSchema(new ScriptType[]{ ScriptType.groovy, ScriptType.java }, new ScriptTrigger[]{
      ScriptTrigger.BeforeSaveServer, ScriptTrigger.AfterSaveServer, ScriptTrigger.BeforeDeleteServer,
      ScriptTrigger.AfterDeleteServer, ScriptTrigger.OnValidationServer, ScriptTrigger.PermissionServer,
      ScriptTrigger.AfterCancelServer }),
  /* Category for scripts that run on a view. */
  LayoutView(new ScriptType[]{ ScriptType.javascript }, new ScriptTrigger[]{ ScriptTrigger.OnLoadBrowser,
      ScriptTrigger.ButtonBrowser, ScriptTrigger.FunctionButtonBrowser, ScriptTrigger.FileButtonBrowser,
          ScriptTrigger.BeforeAddNewClick }),
  LayoutViewField(new ScriptType[]{ ScriptType.javascript }, new ScriptTrigger[]{ ScriptTrigger.OnChangeBrowser,
      ScriptTrigger.AfterNewFromSearchBrowser, ScriptTrigger.AfterDeleteLineBrowser }),
  MultiSearchView(new ScriptType[]{ ScriptType.javascript }, new ScriptTrigger[]{ ScriptTrigger.OnLoadBrowser,
      ScriptTrigger.ButtonBrowser }),
  Grid(new ScriptType[]{ ScriptType.javascript }, new ScriptTrigger[]{ ScriptTrigger.OnRender }),
 GridView(
                                  new ScriptType[]{ ScriptType.javascript },
                                  new ScriptTrigger[]{ ScriptTrigger.OnLoadBrowser }),
  GridViewField(new ScriptType[]{ ScriptType.javascript }, new ScriptTrigger[]{ ScriptTrigger.OnChangeBrowser });

  private final ScriptTrigger[] scripttriggers;

  private final ScriptType[] scripttypes;

  private ScriptLevel(ScriptType[] scripttypes, ScriptTrigger[] scripttriggers)
  {
    this.scripttypes = scripttypes;
    this.scripttriggers = scripttriggers;
  }

  @Override
  public void serializeSpecific(
    JsonGenerator jsonGenerator/* , User user */)
    throws IOException, JsonProcessingException
  {
    jsonGenerator.writeArrayFieldStart("scripttypes");
    for (ScriptType scriptType : scripttypes)
    {
      jsonGenerator.writeString(scriptType.getId());
    }
    jsonGenerator.writeEndArray();

    jsonGenerator.writeArrayFieldStart("scripttriggers");
    for (ScriptTrigger scriptTrigger : scripttriggers)
    {
      scriptTrigger.serialize(jsonGenerator/* , user */);
    }
    jsonGenerator.writeEndArray();
  }

}

package nz.co.logicons.tlp.core.genericmodels.jackson;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import nz.co.logicons.tlp.core.genericmodels.scripts.ScriptLog;

public class ScriptLogSerilizer
    extends JsonSerializer<ScriptLog>
{

  @Override
  public void serialize(ScriptLog scriptLog, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
    throws IOException, JsonProcessingException
  {
    if (scriptLog.getParams() == null && scriptLog.getChildren().size() == 0 && !scriptLog.isRoot())
    {
      jsonGenerator.writeNull();
      return;
    }
    
    jsonGenerator.writeStartObject();
    
    if (scriptLog.isRoot())
    {
      jsonGenerator.writeStringField("title", scriptLog.getDisplayName());
    }
    
    int index = 1;
    for (ScriptLog child : scriptLog.getChildren())
    {
      jsonGenerator.writeFieldName(index + ". " + child.getDisplayName());
      jsonGenerator.writeObject(child);
      index++;
    }
    if (scriptLog.getParams() != null)
    {
      jsonGenerator.writeFieldName("params");
      jsonGenerator.writeObject(scriptLog.getParams());
    }
    jsonGenerator.writeEndObject();
  }

}

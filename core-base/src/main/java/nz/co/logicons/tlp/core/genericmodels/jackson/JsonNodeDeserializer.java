package nz.co.logicons.tlp.core.genericmodels.jackson;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import nz.co.logicons.tlp.core.genericmodels.views.SearchField;

/**
 * Deserialize {@link SearchField}.
 * 
 * @author bhambr
 *
 */
public class JsonNodeDeserializer
    extends JsonDeserializer<JsonNode>
{

  @Override
  public JsonNode deserialize(JsonParser jsonParser, DeserializationContext arg1)
    throws IOException, JsonProcessingException
  {
    // System.out.println(jsonParser.readValueAsTree());
    return jsonParser.readValueAsTree();
  }

}

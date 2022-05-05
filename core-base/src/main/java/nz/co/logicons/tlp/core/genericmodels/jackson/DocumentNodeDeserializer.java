package nz.co.logicons.tlp.core.genericmodels.jackson;

import java.io.IOException;

import nz.co.logicons.tlp.core.genericmodels.nodes.DocumentNode;
import nz.co.logicons.tlp.core.genericmodels.schemas.DocumentSchema;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * Deserialize {@link DocumentNode}.
 * @author bhambr
 *
 */
public class DocumentNodeDeserializer
    extends JsonDeserializer<DocumentNode>
{
  
  public DocumentNodeDeserializer()
  {
    super();
  }
  
  @Override
  public DocumentNode deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
    throws IOException, JsonProcessingException
  {
    //extract the schema for the document.
    DocumentSchema documentSchema = (DocumentSchema) deserializationContext.findInjectableValue(
        DocumentSchema.class.getName(), null, null);
    
    JsonNode jsonNode = jsonParser.readValueAsTree();

    //use the schema to deserialize
    return (DocumentNode) documentSchema.deserializeNode(jsonNode);
  }

}

package nz.co.logicons.tlp.core.genericmodels.jackson;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import nz.co.logicons.tlp.core.business.models.Model;

public class ModelSerializer extends JsonSerializer<Model>
{
  /** Derive permissions for serialization using this user's roles. */
  // private final User user;
  
  public ModelSerializer(/* User user */)
  {
    // this.user = user;
  }
  
  @Override
  public void serialize(Model model, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
    throws IOException, JsonProcessingException
  {
    model.getDocumentNode().serialize(jsonGenerator,
        /* user, */ DocumentNodeSerializer.getSerializationProfile(), null);
  }
  
  
}

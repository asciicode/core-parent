package nz.co.logicons.tlp.core.genericmodels.jackson;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import nz.co.logicons.tlp.core.genericmodels.base.GenericSerializable;

/**
 * Serialize {@link GenericSerializable}.
 * @author bhambr
 *
 */
public class GenericSerializer extends JsonSerializer<GenericSerializable>
{
  /** Derive permissions for serialization using this user's roles. */ 
  // private final User user;
  
  public GenericSerializer(/* User user */)
  {
    // this.user = user;
  }

  @Override
  public void serialize(GenericSerializable genericSerializable, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
    throws IOException, JsonProcessingException
  {
    //Not much to do - rely on GenericInterface to serialize itself.
    genericSerializable.serialize(jsonGenerator/* , user */);
  }

}

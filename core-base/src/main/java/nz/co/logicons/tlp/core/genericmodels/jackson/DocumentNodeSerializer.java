package nz.co.logicons.tlp.core.genericmodels.jackson;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import nz.co.logicons.tlp.core.genericmodels.jackson.serializationprofile.SerializationProfile;
import nz.co.logicons.tlp.core.genericmodels.nodes.DocumentNode;

/**
 * Serialize {@link DocumentNode}.
 * 
 * @author bhambr
 *
 */
public class DocumentNodeSerializer
    extends JsonSerializer<DocumentNode>
{

  /** Jackson doesnt support a way to pass in parameters during serialization. So Falling back on {@link ThreadLocal}. */
  private static ThreadLocal<SerializationProfile> threadLocalSerializationProfile = new ThreadLocal<>();

  /** Derive permissions for serialization using this user's roles. */
  // private final User user;

  public DocumentNodeSerializer(/* User user */)
  {
    // this.user = user;
  }

  @Override
  public void serialize(DocumentNode documentNode, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
    throws IOException, JsonProcessingException
  {
    documentNode.serialize(jsonGenerator/* , user */, getSerializationProfile(), null);
  }

  public static SerializationProfile getSerializationProfile()
  {
    SerializationProfile serializationProfile = threadLocalSerializationProfile.get();
    if (serializationProfile == null)
    {
      serializationProfile = new SerializationProfile();
      setSerializationProfile(serializationProfile);
    }
    return serializationProfile;
  }
  
  public static void setSerializationProfile(SerializationProfile serializationProfile)
  {
    threadLocalSerializationProfile.set(serializationProfile);
  }
}

package nz.co.logicons.tlp.core.genericmodels.jackson.serializationprofile;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;

import nz.co.logicons.tlp.core.genericmodels.nodes.Node;

public interface CustomSerializer
{
  public void serialize(Node<?> node,
    JsonGenerator jsonGenerator/* , User user */,
    SerializationProfile serializationProfile, String parentPath) throws JsonGenerationException, IOException;
  
  public boolean applies(Node<?> node, String parentPath);
}

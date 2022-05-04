package nz.co.logicons.tlp.core.enums;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;

import nz.co.logicons.tlp.core.genericmodels.base.EnumInterface;
import nz.co.logicons.tlp.core.genericmodels.base.GenericSerializable;

/**
 * The cache (sent to browser) contains lists of various enumerations.
 * This container class implements {@link GenericSerializable} and thus ensures that the id, display text and any
 * children are serialized and sent to cache.
 * 
 * Note: All other references to enumeratons are held in native form so that jackson's default serializer kicks in.
 * 
 * @author bhambr
 *
 */
public class EnumContainer
    implements GenericSerializable
{

  private EnumInterface enumInterface;

  public EnumContainer(EnumInterface enumInterface)
  {
    this.enumInterface = enumInterface;
  }

  @Override
  public String getId()
  {
    return enumInterface.getId();
  }

  @Override
  public String getDisplaytext()
  {
    return enumInterface.getDisplaytext();
  }

  @Override
  public void serializeSpecific(
    JsonGenerator jsonGenerator/* , User user */)
    throws IOException, JsonProcessingException
  {
    enumInterface.serializeSpecific(jsonGenerator/* , user */);
  }

  public static List<EnumContainer> getAsEnumContainers(EnumInterface[] enumInterfaces)
  {
    return getAsEnumContainers(Arrays.asList(enumInterfaces));
  }

  public static List<EnumContainer> getAsEnumContainers(List<EnumInterface> enumInterfaces)
  {
    List<EnumContainer> enumContainers = new ArrayList<EnumContainer>();
    for (EnumInterface enumInterface : enumInterfaces)
    {
      enumContainers.add(new EnumContainer(enumInterface));
    }
    return enumContainers;
  }
}

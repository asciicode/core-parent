package nz.co.logicons.tlp.core.genericmodels.jackson.serializationprofile;

import java.util.ArrayList;
import java.util.List;

import nz.co.logicons.tlp.core.genericmodels.schemas.DocumentSchema;
import nz.co.logicons.tlp.core.genericmodels.views.SearchParam;


public class SerializationProfile
{
  private List<String> schemasToExpand = new ArrayList<>();
  
  private List<CustomSerializer> customSerializers = new ArrayList<>();

  private List<SearchParam> searchParams = new ArrayList<>();

  public SerializationProfile()
  {

  }

  public SerializationProfile(List<String> schemasToExpand)
  {
    addSchemasToExpand(schemasToExpand);
  }

  public SerializationProfile(List<String> schemasToExpand, List<CustomSerializer> customSerializers,
    List<SearchParam> searchParams)
  {
    addSchemasToExpand(schemasToExpand);
    this.customSerializers = customSerializers;
    this.searchParams = searchParams;
  }

  private void addSchemasToExpand(List<String> schemasToExpand)
  {
    if (schemasToExpand != null)
    {
      for (String schemaToExpand : schemasToExpand)
      {
        this.schemasToExpand.add(schemaToExpand.replaceAll(DocumentSchema.EXPANDED_SUFFIX, ""));
      }
    }
  }
  
  public List<CustomSerializer> getCustomSerializers()
  {
    return customSerializers;
  }

  public List<String> getSchemasToExpand()
  {
    return schemasToExpand;
  }

  public List<SearchParam> getSearchParams()
  {
    return searchParams;
  }
}

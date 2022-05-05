package nz.co.logicons.tlp.core.rest.model;

public class DocumentNodePayloadModel
{
  public DocumentNodePayloadModel(String schemaid, String payload)
  {
    super();
    this.schemaid = schemaid;
    this.payload = payload;
  }

  private String schemaid;

  private String payload;

  public String getSchemaid()
  {
    return schemaid;
  }

  public void setSchemaid(String schemaid)
  {
    this.schemaid = schemaid;
  }

  public String getPayload()
  {
    return payload;
  }

  public void setPayload(String payload)
  {
    this.payload = payload;
  }

}

package nz.co.logicons.tlp.core.genericmodels;


import org.apache.commons.lang3.StringUtils;

import nz.co.logicons.tlp.core.genericmodels.base.GenericInterface;

public class SearchFieldMapping implements GenericInterface
{
  private String searchfield;

  private String detailfield;
  
  private String script;

  public SearchFieldMapping()
  {
    
  }
  
  public String getSearchfield()
  {
    return searchfield;
  }

  public String getDetailfield()
  {
    if (StringUtils.isBlank(getScript()))
    {
      return detailfield;
    }
    return null;
  }
  
  public String getScript()
  {
    return script;
  }

  public void setScript(String script)
  {
    this.script = script;
  }
  
}

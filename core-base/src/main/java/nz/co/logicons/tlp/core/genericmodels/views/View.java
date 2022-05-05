package nz.co.logicons.tlp.core.genericmodels.views;


import java.util.LinkedList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver;

import nz.co.logicons.tlp.core.genericmodels.base.GenericInterface;
import nz.co.logicons.tlp.core.genericmodels.jackson.GenericInterfaceTypeIdResolver;
import nz.co.logicons.tlp.core.genericmodels.scripts.Script;


/**
 * Base abstract class for all views and fields.
 * 
 * NOTE: At this point views and fields structures are held by server for consumption by client ad are not processed in
 * any way.
 * 
 * @author bhambr
 * 
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CUSTOM,
include = JsonTypeInfo.As.PROPERTY,
property = "type")
@JsonTypeIdResolver(GenericInterfaceTypeIdResolver.class)
public abstract class View
    implements GenericInterface
{
  private String id;
  
  private List<Script> scripts = new LinkedList<Script>();
  
  public void setId(String id)
  {
    this.id = id;
  }

  public String getId()
  {
    return id;
  }
  
  public abstract boolean isSystemowned();

  public abstract String getSchemaid();


  public String getType()
  {
    return getClass().getSimpleName().toLowerCase();
  }
  
  public List<Script> getScripts()
  {
    return scripts;
  }
  

  /**
   * Generate a nice name for the view (only used for system generated views).
   * 
   * @param id .
   * @param viewClass .
   * @return .
   */
  public static String getGeneratedViewId(String id, Class<? extends View> viewClass)
  {
    return "(" + id.replaceAll("\\(", "").replaceAll("\\)", "") + "_"
        + viewClass.getSimpleName().toLowerCase().replaceAll("view", "") + ")";
  }
}

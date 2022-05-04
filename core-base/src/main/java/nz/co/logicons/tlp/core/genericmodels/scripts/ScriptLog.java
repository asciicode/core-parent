package nz.co.logicons.tlp.core.genericmodels.scripts;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Logs execution of a script;
 * 
 * @author bhambr
 *
 */
public class ScriptLog
{

  protected boolean root = true;

  private String displayName;

  private List<ScriptLog> children = new ArrayList<ScriptLog>();

  private JsonNode params;

  public ScriptLog()
  {

  }

  public ScriptLog(String format, Object...args)
  {
    setDisplayName(format, args);
  }
  
  public ScriptLog(String displayName)
  {
    setDisplayName(displayName);
  }

  public boolean isRoot()
  {
    return root;
  }

  public JsonNode getParams()
  {
    return params;
  }

  public void setParams(JsonNode params)
  {
    this.params = params;
  }

  public String getDisplayName()
  {
    return displayName;
  }

  public void setDisplayName(String displayName)
  {
    this.displayName = displayName;
  }
  
  public void setDisplayName(String format, Object...args)
  {
    this.displayName = String.format(format, args);
  }

  public List<ScriptLog> getChildren()
  {
    return children;
  }

  public void setChildren(List<ScriptLog> children)
  {
    this.children = children;
  }

  public ScriptLog add(ScriptLog scriptLog)
  {
    scriptLog.root = false;
    getChildren().add(scriptLog);
    return scriptLog;
  }

  public ScriptLog add(String value)
  {
    return add(new ScriptLog(value));
  }
  
  public ScriptLog add(String format, Object...args)
  {
    return add(new ScriptLog(format, args));
  }

  public List<String> toLines()
  {
    return toLines(0);
  }
  
  protected List<String> toLines(int level)
  {
    List<String> list = new ArrayList<>();
    
    for (ScriptLog scriptLog : children)
    {
      
      list.add(String.format("%s %s", StringUtils.repeat(">", level), scriptLog.getDisplayName()));
      list.addAll(scriptLog.toLines(level + 1));
    }
    return list;
  }
}

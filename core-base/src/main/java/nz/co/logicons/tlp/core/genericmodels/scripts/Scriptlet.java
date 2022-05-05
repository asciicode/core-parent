package nz.co.logicons.tlp.core.genericmodels.scripts;


import nz.co.logicons.tlp.core.enums.ScriptType;
import nz.co.logicons.tlp.core.genericmodels.base.GenericInterface;

public class Scriptlet
    implements GenericInterface
{
  private String id;
  
  private ScriptType scripttype;
  
  private String script;

  private boolean systemowned;

  public Scriptlet()
  {
    
  }
  
  public Scriptlet(String id, String script)
  {
    this.scripttype = ScriptType.javascript;
    this.id = id;
    this.script = script;
    systemowned = true;
  }
  
  public Scriptlet(String id)
  {
    this.scripttype = ScriptType.java;
    this.id = id;
    systemowned = true;
  }
  
  public String getId()
  {
    return id;
  }

  public void setId(String id)
  {
    this.id = id;
  }

  public ScriptType getScripttype()
  {
    return scripttype;
  }

  public void setScripttype(ScriptType scripttype)
  {
    this.scripttype = scripttype;
  }

  
  public boolean isSystemowned()
  {
    return systemowned;
  }

  public void setSystemowned(boolean systemowned)
  {
    this.systemowned = systemowned;
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

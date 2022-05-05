package nz.co.logicons.tlp.core.genericmodels.scripts;


import nz.co.logicons.tlp.core.enums.ScriptTrigger;

public class Script
{
  private ScriptTrigger scripttrigger;

  private boolean scriptlet;

  private String scriptletid;

  private String functionid;

  private String script;
  
  private String buttontext;
  
  private String linetext;
  
  private String buttonglyph;
  
  public ScriptTrigger getScripttrigger()
  {
    return scripttrigger;
  }

  public void setScripttrigger(ScriptTrigger scripttrigger)
  {
    this.scripttrigger = scripttrigger;
  }

  public boolean isScriptlet()
  {
    return scriptlet;
  }

  public void setScriptlet(boolean scriptlet)
  {
    this.scriptlet = scriptlet;
  }

  public String getScriptletid()
  {
    return scriptletid;
  }

  public void setScriptletid(String scriptletid)
  {
    this.scriptletid = scriptletid;
  }

  public String getFunctionid()
  {
    return functionid;
  }

  public void setFunctionid(String functionid)
  {
    this.functionid = functionid;
  }

  public String getScript()
  {
    return script;
  }

  public void setScript(String script)
  {
    this.script = script;
  }

  public String getButtontext()
  {
    return buttontext;
  }

  public void setButtontext(String buttontext)
  {
    this.buttontext = buttontext;
  }

  public String getLinetext()
  {
    return linetext;
  }

  public void setLinetext(String linetext)
  {
    this.linetext = linetext;
  }
  
  public String getButtonglyph()
  {
    return buttonglyph;
  }

  public void setButtonglyph(String buttonglyph)
  {
    this.buttonglyph = buttonglyph;
  }
}

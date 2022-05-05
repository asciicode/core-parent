package nz.co.logicons.tlp.core.genericmodels.output;

import nz.co.logicons.tlp.core.genericmodels.scripts.ScriptLog;

public abstract class Output<T>
{
  private final ScriptLog scriptLog;

  private final T output;
  
  public Output(T output, ScriptLog scriptLog)
  {
    this.output = output;
    this.scriptLog = scriptLog;
  }
  
  public Output(T output)
  {
    this.output = output;
    this.scriptLog = null;
  }
  
  public ScriptLog getScriptLog()
  {
    return scriptLog;
  }

  public T getOutput()
  {
    return output;
  }
}

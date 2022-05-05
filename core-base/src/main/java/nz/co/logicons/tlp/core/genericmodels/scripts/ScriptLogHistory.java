package nz.co.logicons.tlp.core.genericmodels.scripts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ScriptLogHistory
{
  private static final int LIMIT = 10;
  
  private List<ScriptLog> scriptLogs = new ArrayList<ScriptLog>();
  
  public List<ScriptLog> getScriptLogs()
  {
    return Collections.unmodifiableList(scriptLogs);
  }

  public void add(ScriptLog scriptLog)
  {
    synchronized (scriptLogs)
    {
      scriptLogs.add(0, scriptLog);
      if (scriptLogs.size() > LIMIT)
      {
        scriptLogs.remove(scriptLogs.size() -1);
      }
    }
  }
}

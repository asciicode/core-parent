package nz.co.logicons.tlp.core.genericmodels.output;

import nz.co.logicons.tlp.core.genericmodels.nodes.DocumentNode;
import nz.co.logicons.tlp.core.genericmodels.scripts.ScriptLog;

public class DocumentOutput extends Output<DocumentNode>
{
  public DocumentOutput(DocumentNode output)
  {
    super(output, new ScriptLog());
  }

  public DocumentOutput(DocumentNode output, ScriptLog scriptLog)
  {
    super(output, scriptLog);
  }
}

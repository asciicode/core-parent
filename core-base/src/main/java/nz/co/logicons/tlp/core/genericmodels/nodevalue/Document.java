package nz.co.logicons.tlp.core.genericmodels.nodevalue;

import java.util.LinkedHashMap;

import nz.co.logicons.tlp.core.genericmodels.nodes.Node;

/**
 * Java representation of a json document.
 * Not much to it - just a Map of nodes.
 * @author bhambr
 *
 */
public class Document extends LinkedHashMap<String, Node<?>>
{
  private static final long serialVersionUID = -5307784919944467040L;

  public String prettyPrintValues()
  {
    StringBuilder sb = new StringBuilder();
    this.entrySet().forEach(entry -> {
      sb.append(entry.getKey() + " -> " + entry.getValue() + "\n");
    });
    return sb.toString();
  }
}

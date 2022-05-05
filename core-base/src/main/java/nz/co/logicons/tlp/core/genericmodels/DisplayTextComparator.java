package nz.co.logicons.tlp.core.genericmodels;

import java.util.Comparator;

import nz.co.logicons.tlp.core.genericmodels.base.GenericInterface;

public class DisplayTextComparator implements Comparator<GenericInterface>
{

  @Override
  public int compare(GenericInterface o1, GenericInterface o2)
  {
    return o1.getDisplaytext().compareTo(o2.getDisplaytext());
  }

}

package nz.co.logicons.tlp.core.enums;

import nz.co.logicons.tlp.core.genericmodels.base.EnumInterface;


/**
 * Types of currencies.
 * @author bhambr
 *
 */
public enum Currency implements EnumInterface
{
  NZD(2), AUD(2), USD(2), YEN(0), HKD(0);

  /** Number of decimal places for currency. */
  private final int scale;

  Currency(int scale)
  {
    this.scale = scale;
  }
  
  public int getScale()
  {
    return scale;
  }
}

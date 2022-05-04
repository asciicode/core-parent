package nz.co.logicons.tlp.core.genericmodels.schemas;

import nz.co.logicons.tlp.core.enums.Currency;


/**
 * Schema for a money field.
 * @author bhambr
 *
 */
public class MoneySchema
    extends NumberSchema
{
  
  /** The currency of the money. */
  private Currency currency = Currency.NZD;

  
  public Currency getCurrency()
  {
    return currency;
  }
  
  @Override
  public String getSubtype()
  {
    return currency.getId();
  }
  
  @Override
  public int getScale()
  {
    return getCurrency().getScale();
  }

}

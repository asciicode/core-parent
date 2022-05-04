package nz.co.logicons.tlp.core.enums;

import nz.co.logicons.tlp.core.genericmodels.base.EnumInterface;

/**
 * Types of search operators.
 * @author bhambr
 *
 */
public enum SearchOperator implements EnumInterface
{
  equals("=", "$eq"), 
  lessthan("<", "$lt"), 
  greaterthan(">", "$gt"), 
  lessthanequals("<=", "$lte"), 
  greaterthanequals(">=", "$gte"), 
  exists("exists", "$exists"),
  range("range", null), notequals("!=", "$ne"),
  
  notexists("!E", null), notexistsandequals("!E=", null), notexistsandlessthan("!E<", null), notexistsandgreaterthan(
      "!E>", null), notexistsandlessthanequal("!E<=", null), notexistsandgreaterthanequal("!E>=", null);

  /** Equivalent mongo operator. */
  private final String mongoOperator;
  
  private final String displaytext;
  
  private SearchOperator(String displaytext, String mongoOperator)
  {
    this.mongoOperator = mongoOperator;
    this.displaytext = displaytext;
  }
  
  public String getMongoOperator()
  {
    return mongoOperator;
  }
  
  public String getDisplaytext()
  {
    return displaytext;
  }
}

package nz.co.logicons.tlp.core.genericmodels;



import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.validator.routines.RegexValidator;

public class RegularExpressionHelper
{

  private static RegexValidator systemNameValidator = new RegexValidator("^\\([a-zA-Z0-9_]*\\)$");

  private static RegexValidator nonSystemNameValidator = new RegexValidator("^[a-zA-Z0-9_]*$");

  public static boolean isValidSystemName(String name)
  {
    return systemNameValidator.isValid(name);
  }

  public static boolean isValidNonSystemName(String name)
  {
    return nonSystemNameValidator.isValid(name);
  }

  /**
   * Converts search expression into a regular expression (if needed).
   * @param searchExpression search expression to convert.
   * @return First value in result indicates if it is a regular expression. Second value is the string/regular expression.
   */
  public static Pair<Boolean, String> getRegularExpression(String searchExpression)
  {
    if (searchExpression.startsWith("!")) // start a search with ! term is full regex
    {
      // search expression starts with ! so strip first character and treat as regular expression.
      String regularExpression = searchExpression.substring(1);
      return new MutablePair<Boolean, String>(true, regularExpression);
    }
    else if (StringUtils.contains(searchExpression, "*"))
    {
      // search expression contains * so convert to regular expression
      String regularExpression = StringUtils.join("^", searchExpression.replaceAll("\\*", ".*"), "$");
      return new MutablePair<Boolean, String>(true, regularExpression);
    }
    else
    {
      // not a regular expression
      return new MutablePair<Boolean, String>(false, searchExpression);
    }
  }
}

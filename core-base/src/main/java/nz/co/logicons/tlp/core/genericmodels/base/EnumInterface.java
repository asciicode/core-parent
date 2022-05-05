package nz.co.logicons.tlp.core.genericmodels.base;

/**
 * All enumerations implement this interface.
 * @author work
 *
 */
public interface EnumInterface extends GenericInterface
{
  /**
   * An enumeration's toString returns its name. Use that as an identifier.
   */
  default String getId()
  {
    return toString();
  }
}

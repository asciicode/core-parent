package nz.co.logicons.tlp.core.genericmodels.base;

import nz.co.logicons.tlp.core.genericmodels.jackson.GenericSerializer;

/**
 * Classes that implement this interface are serialized using {@link GenericSerializer}
 * They do not use jackson's default serializer.
 * @author bhambr.
 *
 */
public interface GenericSerializable extends GenericInterface
{
 
}

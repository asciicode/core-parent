package nz.co.logicons.tlp.core.genericmodels.schemas;

import nz.co.logicons.tlp.core.genericmodels.validators.IDValidator;
import nz.co.logicons.tlp.core.genericmodels.validators.RequiredValidator;
import nz.co.logicons.tlp.core.mongo.MongoDatastore;

/**
 * Schema for an id field.
 * Field is the primary key for document.
 * 
 * @author bhambr
 *
 */
public class IdSchema
    extends StringSchema
{
  @Override
  public void init(DocumentSchema documentSchema, MongoDatastore datastore)
  {
    super.init(documentSchema, datastore);
    addValidator(new RequiredValidator<>());
    addValidator(new IDValidator());
  }
}

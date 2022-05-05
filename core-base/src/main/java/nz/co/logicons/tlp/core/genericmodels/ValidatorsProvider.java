package nz.co.logicons.tlp.core.genericmodels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nz.co.logicons.tlp.core.enums.ChildSchemaType;
import nz.co.logicons.tlp.core.genericmodels.validators.ContainerNumberValidator;
import nz.co.logicons.tlp.core.genericmodels.validators.DeepLinkedValidator;
import nz.co.logicons.tlp.core.genericmodels.validators.EmailValidator;
import nz.co.logicons.tlp.core.genericmodels.validators.IDValidator;
import nz.co.logicons.tlp.core.genericmodels.validators.LinkedDocumentValidator;
import nz.co.logicons.tlp.core.genericmodels.validators.ListLinkedDocumentValidator;
import nz.co.logicons.tlp.core.genericmodels.validators.NumberRangeValidator;
import nz.co.logicons.tlp.core.genericmodels.validators.RegExValidator;
import nz.co.logicons.tlp.core.genericmodels.validators.RequiredValidator;
import nz.co.logicons.tlp.core.genericmodels.validators.Validator;

public class ValidatorsProvider
// implements Provider<Map<ChildSchemaType, List<Validator<?>>>>
{
  // list of validators that an admin can specify
  private static Map<ChildSchemaType, List<Validator<?>>> adminValidatorsMap = new HashMap<>();

  // list of validators that an admin or system can specify
  private static Map<ChildSchemaType, List<Validator<?>>> allValidatorsMap = new HashMap<>();
  
  private static Map<ChildSchemaType, List<String>> allValidatorTypesMap = new HashMap<>();

  static
  {
    for (ChildSchemaType childSchemaType : ChildSchemaType.values())
    {
      adminValidatorsMap.put(childSchemaType, getAdminValidators(childSchemaType));
      allValidatorsMap.put(childSchemaType, getAdminValidators(childSchemaType));
      allValidatorsMap.get(childSchemaType).addAll(getSystemValidators(childSchemaType));
      
      List<String> list = new ArrayList<>();
      for (Validator<?> validator : allValidatorsMap.get(childSchemaType))
      {
        list.add(validator.getType());
      }
      allValidatorTypesMap.put(childSchemaType, list);
    }
  }

  // @Override
  public Map<ChildSchemaType, List<Validator<?>>> get()
  {
    return adminValidatorsMap;
  }

  public Map<ChildSchemaType, List<String>> getAllTypes()
  {
    return allValidatorTypesMap;
  }

  private static List<Validator<?>> getAdminValidators(ChildSchemaType childSchemaType)
  {

    List<Validator<?>> list = new ArrayList<>();

    if (childSchemaType != ChildSchemaType.attachmentschema && childSchemaType != ChildSchemaType.listattachmentschema)
    {
      list.add(new RequiredValidator<>(null));
    }
    
    switch (childSchemaType)
    {
      case idschema:
      case stringschema:
        list.add(new RegExValidator(null));
        list.add(new EmailValidator(null));
        list.add(new ContainerNumberValidator(null));
        break;
      case numberschema:
      case moneyschema:
        list.add(new NumberRangeValidator(null));
        break;
      default:
        break;
    }

    return list;
  }

  private static List<Validator<?>> getSystemValidators(ChildSchemaType childSchemaType)
  {

    List<Validator<?>> list = new ArrayList<>();

    switch (childSchemaType)
    {
      case idschema:
        list.add(new IDValidator());
        break;
      case deeplinkedschema:
        list.add(new DeepLinkedValidator());
        break;
      case linkedschema:
        list.add(new LinkedDocumentValidator(null));
        break;
      case listlinkedschema:
        list.add(new ListLinkedDocumentValidator(null));
        break;
      default:
        break;
    }

    return list;
  }

}

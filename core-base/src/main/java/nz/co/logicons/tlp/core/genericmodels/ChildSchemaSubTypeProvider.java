package nz.co.logicons.tlp.core.genericmodels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import nz.co.logicons.tlp.core.enums.ChildSchemaType;
import nz.co.logicons.tlp.core.enums.Currency;
import nz.co.logicons.tlp.core.genericmodels.operations.SchemaOperation;
import nz.co.logicons.tlp.core.genericmodels.schemas.ChildSchema;
import nz.co.logicons.tlp.core.genericmodels.schemas.DocumentSchema;
import nz.co.logicons.tlp.core.mongo.MongoDatastore;

public class ChildSchemaSubTypeProvider
{
  
  @Autowired
  private SchemaOperation schemaOperation;
  
  @Autowired
  private MongoDatastore datastore;
  
  public Map<String, List<String>> get()
  {
    Map<String, List<String>> map = new HashMap<String, List<String>>();
    for (ChildSchemaType childSchemaType : ChildSchemaType.values())
    {
      map.put(childSchemaType.getId(), get(childSchemaType));
    }
    return map;
  }
  
  public List<String> get(ChildSchemaType childSchemaType)
  {
    List<String> list = new ArrayList<>();
    switch (childSchemaType)
    {
      case linkedschema:
      case listlinkedschema:
        list.addAll(getschemaids(true));
        break;
      case inlineschema:
      case listinlineschema:
        list.addAll(getschemaids(false));
        break;
      case moneyschema:
        list.addAll(getMoneySchemaSubTypes());
        break;
      case deeplinkedschema:
        list.addAll(getDeepLinkedSchemaSubTypes());
        break;
      default:
        break;
    }
    return list;
  }

  private List<String> getschemaids(boolean withId)
  {
    List<String> list = new ArrayList<>();
    for (DocumentSchema documentSchema : schemaOperation.getSchemas())
    {
      if (withId != (documentSchema.isInline()))
      {
        list.add(documentSchema.getId());
      }
    }
    return list;
  }
  
  public List<String> getDeepLinkedSchemaSubTypes()
  {
    List<String> deeplinks = new ArrayList<>();
    for (DocumentSchema documentSchema : datastore.getSchemas())
    {
      if (!documentSchema.isInline())
      {
        addDeepLinkedSchemaSubTypes(deeplinks, documentSchema, documentSchema.getId());
      }
    }
    return deeplinks;
  }
  
  private void addDeepLinkedSchemaSubTypes(List<String> deeplinks, DocumentSchema documentSchema, String parentpath)
  {
    for (ChildSchema<?> childSchema : documentSchema.getChildrenMap().values())
    {
      if (childSchema.getType() == ChildSchemaType.listinlineschema)
      {
        String path = parentpath + "." + childSchema.getId();
        deeplinks.add(path);
        addDeepLinkedSchemaSubTypes(deeplinks, datastore.getSchema(childSchema.getSubtype()), path);
      }
    }
  }
  
  public List<String> getMoneySchemaSubTypes()
  {
    List<String> list = new ArrayList<>();
    for (Currency currency : Currency.values())
    {
      list.add(currency.getId());
    }
    return list;
  }
}

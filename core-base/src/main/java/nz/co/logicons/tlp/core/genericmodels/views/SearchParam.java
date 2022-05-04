package nz.co.logicons.tlp.core.genericmodels.views;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import nz.co.logicons.tlp.core.enums.ChildSchemaType;
import nz.co.logicons.tlp.core.enums.SearchOperator;
import nz.co.logicons.tlp.core.genericmodels.jackson.JsonNodeDeserializer;

public class SearchParam
{
  String fieldid;

  private SearchOperator searchoperator = SearchOperator.equals;

  private ChildSchemaType childschematype;

  @JsonDeserialize(using = JsonNodeDeserializer.class)
  private JsonNode value;
  
  private List<SearchParam> searchparams = new ArrayList<SearchParam>();
  
  private String subtype;

  public SearchParam()
  {
    
  }
  
  public SearchParam(String fieldid, SearchOperator searchoperator, ChildSchemaType childschematype, JsonNode value)
  {
    this.fieldid = fieldid;
    this.searchoperator = searchoperator;
    this.childschematype = childschematype;
    this.value = value;
  }
  
  public String getFieldid()
  {
    return fieldid;
  }

  public SearchOperator getSearchoperator()
  {
    return searchoperator;
  }

  public ChildSchemaType getChildschematype()
  {
    return childschematype;
  }

  public JsonNode getValue()
  {
    return value;
  }
  
  public List<SearchParam> getSearchparams()
  {
    return searchparams;
  }

  public void setSearchparams(List<SearchParam> searchparams)
  {
    this.searchparams = searchparams;
  }
  public Object getDeserializeValue()
  {
    return getChildschematype().getSchemaInstance().deserializeNode(getValue());
  }
  
  public String getSubtype()
  {
    return subtype;
  }

  public void setSubtype(String subtype)
  {
    this.subtype = subtype;
  }

}

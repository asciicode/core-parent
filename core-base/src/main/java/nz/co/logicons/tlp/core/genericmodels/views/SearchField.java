package nz.co.logicons.tlp.core.genericmodels.views;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import nz.co.logicons.tlp.core.enums.SearchOperator;
import nz.co.logicons.tlp.core.genericmodels.jackson.JsonNodeDeserializer;
import nz.co.logicons.tlp.core.genericmodels.schemas.ChildSchema;

public class SearchField
    extends Field
{

  private SearchOperator searchoperator = SearchOperator.equals;
  
  @JsonDeserialize(using =JsonNodeDeserializer.class)
  private JsonNode value;
  
  public SearchField()
  {
    
  }
  
  public SearchField(ChildSchema<?> schema)
  {
    super(schema);
    this.searchoperator = SearchOperator.equals;
 
  }

  public void setSearchoperator(SearchOperator searchoperator)
  {
    this.searchoperator = searchoperator;
  }

  public SearchOperator getSearchoperator()
  {
    return searchoperator;
  }

  public String getSearchoperatordisplaytext()
  {
    if (searchoperator != SearchOperator.equals && searchoperator != SearchOperator.range)
    {
      return getSearchoperator().getDisplaytext();
    }
    return null;
  }
}

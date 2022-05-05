package nz.co.logicons.tlp.core.genericmodels.base;

import java.io.IOException;

//import nz.co.spikydev.base.business.models.User;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * Base interface for all types such as schemas, views, documents, nodes, enums etc.
 * Basically all data objects that the system deals with.
 * 
 * @author bhambr
 *
 */
public interface GenericInterface
{
  public static final String SYSTEM_OWNED_FIELD_NAME = "systemowned";

  public static final String SYSTEM_LOCKED_FIELD_NAME = "systemlocked";

  public static final String TYPE_FIELD_NAME = "type";

  public static final String ID_FIELD_NAME = "_id";

  public static final String PARENT_ID_FIELD_NAME = "parentid";

  public static final String CHILD_ID_FIELD_NAME = "childid";

  public static final String ID_DISPLAY_NAME = "ID";

  public static final String DISPLAY_TEXT_FIELD_NAME = "displaytext";

  public static final String SCHEMA_ID_FIELD_NAME = "schemaid";

  public static final String SCRIPTS_FIELD_NAME = "scripts";
  
  public static final String SCRIPT_FIELD_NAME = "script";
  
  public static final String LOGICAL_DELETE = "logicaldelete";
  
  public static final String DETAIL_VIEW_ID_FIELD_NAME = "detailviewid";
  
  public static final String GRID_VIEW_ID_FIELD_NAME = "gridviewid";
  
  public static final String SEARCH_VIEW_ID_FIELD_NAME = "searchviewid";
  
  public static final String EMBEDDED_DETAIL_VIEW_ID_FIELD_NAME = "embeddeddetailviewid";
  
  public static final String EMBEDDED_SEARCH_VIEW_ID_FIELD_NAME = "embeddedsearchviewid";
  
  public static final String EMBEDDED_GRID_VIEW_ID_FIELD_NAME = "embeddedgridviewid";
  
  public static final String GLYPH_TEXT_FIELD_NAME = "glyph";
  
  public static final String RANK_FIELD_NAME = "Rank";
  
  public static final String DESCRIPTION_FIELD_NAME = "Description";
  
  @JsonProperty("_id")
  default String getId()
  {
    return null;
  }

  default String getDisplaytext()
  {
    return getId();
  }
  
  /**
   * Serialize this object.
   * 
   * @param jsonGenerator The jackson generator
   * @param user use this user's security perform serialization.
   * @throws IOException .
   * @throws JsonProcessingException .
   */
  default void serialize(
    JsonGenerator jsonGenerator/* , User user */)
    throws IOException, JsonProcessingException
  {
    jsonGenerator.writeStartObject();
    if (StringUtils.isNotBlank(getId()))
    {
      jsonGenerator.writeStringField(GenericInterface.ID_FIELD_NAME, getId());
    }
    if (StringUtils.isNotBlank(getDisplaytext()))
    {
      jsonGenerator.writeStringField(GenericInterface.DISPLAY_TEXT_FIELD_NAME, getDisplaytext());
    }
    serializeSpecific(jsonGenerator);
    jsonGenerator.writeEndObject();
  }
  
  /**
   * Any class that implements this interface can serialize additional json fields specific to that class here.
   * 
   * @param jsonGenerator .
   * @param user .
   * @throws IOException .
   * @throws JsonProcessingException .
   */
//  default void serializeSpecific(JsonGenerator jsonGenerator, User user)
//    throws IOException, JsonProcessingException
//  {
//
//  }

default void serializeSpecific(JsonGenerator jsonGenerator)
  throws IOException, JsonProcessingException
{

}
}

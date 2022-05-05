package nz.co.logicons.tlp.core.genericmodels.views;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import nz.co.logicons.tlp.core.genericmodels.SearchFieldMapping;
import nz.co.logicons.tlp.core.genericmodels.base.GenericInterface;
import nz.co.logicons.tlp.core.genericmodels.jackson.JsonNodeDeserializer;
import nz.co.logicons.tlp.core.genericmodels.schemas.ChildSchema;
import nz.co.logicons.tlp.core.genericmodels.scripts.Script;

@JsonPropertyOrder({ "type", "_id", "displaytext", "startcollapsed", "newbutton", "dropdown", "readonly",
    "searchviewid", "detailviewid", "gridviewid", "embeddedgridviewid", "searchfieldmappings", "scripts" })
@JsonInclude(Include.ALWAYS)
public class Field
    implements GenericInterface
{

  private String id;

  private String displaytext;

  /** The tooltip for the view. */
  private String tooltip;

  private String searchviewid;

  private String detailviewid;

  private String embeddeddetailviewid;
  
  private String embeddedsearchviewid;

  private String gridviewid;

  private String embeddedgridviewid;

  private boolean startcollapsed;

  private boolean newbutton;

  private boolean dropdown;

  private boolean readonly;
  
  private boolean email;

  /** Lines for a string schema */
  private int lines = 1;

  private List<Script> scripts = new LinkedList<>();

  private List<SearchFieldMapping> searchfieldmappings = new ArrayList<>();

  private boolean defaultactive;

  private boolean defaultoverwrite;

  @JsonDeserialize(using = JsonNodeDeserializer.class)
  private JsonNode defaultvalue;
  
  //remove after all clients have been migrated to a version that has typeaheadStringSettings
  @Deprecated
  private String typeaheadschemaid;

  private TypeaheadStringSettings typeaheadStringSettings;

  public Field()
  {

  }

  public Field(ChildSchema<?> schema)
  {

    // this.id = schema.getId();
    //
    // if (StringUtils.equals(ID_FIELD_NAME, id))
    // {
    // // Don't set display text to _id - instead set it to ID.
    // this.displaytext = ID_DISPLAY_NAME;
    // }
    // else
    // {
    // this.displaytext = id.replaceAll("_", " ");
    // }
    //
    // this.displaytext = schema.getId();
    //
    // if (schema.getType() == ChildSchemaType.linkedschema)
    // {
    // searchviewid = View.getGeneratedViewId(((LinkedSchema) schema).getSubtype(), SearchView.class);
    // detailviewid = View.getGeneratedViewId(((LinkedSchema) schema).getSubtype(), DetailView.class);
    // newbutton = true;
    // }
    // if (schema.getType() == ChildSchemaType.listlinkedschema)
    // {
    // searchviewid = View.getGeneratedViewId(((ListLinkedSchema) schema).getSubtype(), SearchView.class);
    // detailviewid = View.getGeneratedViewId(((ListLinkedSchema) schema).getSubtype(), DetailView.class);
    // }
    //
    // if (schema.getType() == ChildSchemaType.inlineschema)
    // {
    // detailviewid = View.getGeneratedViewId(((InlineSchema) schema).getSubtype(), DetailView.class);
    // embeddeddetailviewid = View.getGeneratedViewId(((InlineSchema) schema).getSubtype(), DetailView.class);
    // }
    // if (schema.getType() == ChildSchemaType.listinlineschema)
    // {
    // detailviewid = View.getGeneratedViewId(((ListInlineSchema) schema).getSubtype(), DetailView.class);
    // gridviewid = View.getGeneratedViewId(((ListInlineSchema) schema).getSubtype(), GridView.class);
    // embeddedgridviewid = View.getGeneratedViewId(((ListInlineSchema) schema).getSubtype(), GridView.class);
    // }
    //
    // startcollapsed = true;
  }

  public String getSearchviewid()
  {
    return searchviewid;
  }

  public String getDetailviewid()
  {
    return detailviewid;
  }

  public String getEmbeddeddetailviewid()
  {
    return embeddeddetailviewid;
  }

  public boolean isStartcollapsed()
  {
    return startcollapsed;
  }

  public boolean isNewbutton()
  {
    return newbutton;
  }

  public boolean isDropdown()
  {
    return dropdown;
  }

  public boolean isReadonly()
  {
    return readonly;
  }

  public List<SearchFieldMapping> getSearchfieldmappings()
  {
    return searchfieldmappings;
  }

  public String getId()
  {
    return id;
  }

  public String getDisplaytext()
  {
    return displaytext;
  }

  public String getTooltip()
  {
    return tooltip;
  }

  public String getGridviewid()
  {
    return gridviewid;
  }

  public String getEmbeddedgridviewid()
  {
    return embeddedgridviewid;
  }

  public int getLines()
  {
    if (lines < 1)
    {
      lines = 1;
    }
    return lines;
  }

  public void setLines(int lines)
  {
    this.lines = lines;
  }

  public List<Script> getScripts()
  {
    return scripts;
  }

  public boolean isDefaultactive()
  {
    return defaultactive;
  }

  public boolean isDefaultoverwrite()
  {
    return defaultoverwrite;
  }

  public JsonNode getDefaultvalue()
  {
    return defaultvalue;
  }
  
  public String getTypeaheadschemaid()
  {
    return typeaheadschemaid;
  }
  
  public void setTypeaheadschemaid(String typeaheadschemaid)
  {
    this.typeaheadschemaid = typeaheadschemaid;
  }
  
  public String getEmbeddedsearchviewid()
  {
    return embeddedsearchviewid;
  }
  
  public boolean isEmail()
  {
    return email;
  }

  public void setEmail(boolean email)
  {
    this.email = email;
  }
  
  public TypeaheadStringSettings getTypeaheadStringSettings()
  {
    return typeaheadStringSettings;
  }

  public void setTypeaheadStringSettings(TypeaheadStringSettings typeaheadStringSettings)
  {
    this.typeaheadStringSettings = typeaheadStringSettings;
  }
}

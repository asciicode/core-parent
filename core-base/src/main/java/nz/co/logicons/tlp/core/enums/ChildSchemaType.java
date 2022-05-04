package nz.co.logicons.tlp.core.enums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import nz.co.logicons.tlp.core.genericmodels.base.EnumInterface;
import nz.co.logicons.tlp.core.genericmodels.permissions.Roles;
import nz.co.logicons.tlp.core.genericmodels.schemas.AttachmentSchema;
import nz.co.logicons.tlp.core.genericmodels.schemas.AutoIdSchema;
import nz.co.logicons.tlp.core.genericmodels.schemas.BoolSchema;
import nz.co.logicons.tlp.core.genericmodels.schemas.ChildSchema;
import nz.co.logicons.tlp.core.genericmodels.schemas.DateSchema;
import nz.co.logicons.tlp.core.genericmodels.schemas.DateTimeSchema;
import nz.co.logicons.tlp.core.genericmodels.schemas.DeepLinkedSchema;
import nz.co.logicons.tlp.core.genericmodels.schemas.IdSchema;
import nz.co.logicons.tlp.core.genericmodels.schemas.InlineSchema;
import nz.co.logicons.tlp.core.genericmodels.schemas.LinkedSchema;
import nz.co.logicons.tlp.core.genericmodels.schemas.ListAttachmentSchema;
import nz.co.logicons.tlp.core.genericmodels.schemas.ListInlineSchema;
import nz.co.logicons.tlp.core.genericmodels.schemas.ListLinkedSchema;
import nz.co.logicons.tlp.core.genericmodels.schemas.ListStringSchema;
import nz.co.logicons.tlp.core.genericmodels.schemas.MoneySchema;
import nz.co.logicons.tlp.core.genericmodels.schemas.NumberSchema;
import nz.co.logicons.tlp.core.genericmodels.schemas.RolesSchema;
import nz.co.logicons.tlp.core.genericmodels.schemas.SequenceIdSchema;
import nz.co.logicons.tlp.core.genericmodels.schemas.SequenceSchema;
import nz.co.logicons.tlp.core.genericmodels.schemas.StringSchema;
import nz.co.logicons.tlp.core.genericmodels.schemas.TimeSchema;

/**
 * Types of schemas.
 * 
 * @author bhambr
 *
 */
public enum ChildSchemaType implements EnumInterface
{
  idschema("ID", new SearchOperator[]{ SearchOperator.equals }, IdSchema.class),
  autoidschema("Auto ID", new SearchOperator[]{ SearchOperator.equals }, AutoIdSchema.class),
  sequenceidschema("Sequence ID", SearchOperator.values(), SequenceIdSchema.class),
  stringschema("String", new SearchOperator[]{ SearchOperator.equals }, StringSchema.class),
  boolschema("Boolean", new SearchOperator[]{ SearchOperator.equals }, BoolSchema.class),
  datetimeschema("Date & Time", new SearchOperator[]{ SearchOperator.equals }, DateTimeSchema.class),
  liststringschema("List of String", new SearchOperator[]{ SearchOperator.equals }, ListStringSchema.class),
  inlineschema("Inline", new SearchOperator[]{}, InlineSchema.class),
  linkedschema("Linked", new SearchOperator[]{ SearchOperator.equals }, LinkedSchema.class),
  numberschema("Number", SearchOperator.values(), NumberSchema.class),
  moneyschema("Money", SearchOperator.values(), MoneySchema.class),
  listinlineschema("List of Inline", new SearchOperator[]{SearchOperator.equals}, ListInlineSchema.class),
  listlinkedschema("List of Linked", new SearchOperator[]{ SearchOperator.equals }, ListLinkedSchema.class),
  dateschema("Date", SearchOperator.values(), DateSchema.class),
  timeschema("Time", SearchOperator.values(), TimeSchema.class),
  sequenceschema("Sequence", SearchOperator.values(), SequenceSchema.class),
  rolesschema(Roles.ROLES_FIELD_NAME, new SearchOperator[]{}, RolesSchema.class),
  deeplinkedschema("Deeplinked", new SearchOperator[]{}, DeepLinkedSchema.class),
  attachmentschema("Attachment", new SearchOperator[]{}, AttachmentSchema.class),
  listattachmentschema("List of Attachment", new SearchOperator[]{}, ListAttachmentSchema.class);

  public static final List<ChildSchemaType> SimpleTypes = Collections.unmodifiableList(Arrays.asList(new ChildSchemaType[]{ idschema, autoidschema, sequenceidschema, stringschema, boolschema, datetimeschema,
      linkedschema, numberschema, moneyschema, dateschema, timeschema, sequenceschema, attachmentschema, liststringschema, listlinkedschema }));
  
  public static final List<ChildSchemaType> NonListTypes = Collections.unmodifiableList(Arrays.asList(new ChildSchemaType[]{ idschema, autoidschema, sequenceidschema, stringschema, boolschema, datetimeschema,
      linkedschema, numberschema, moneyschema, dateschema, timeschema, sequenceschema, inlineschema, rolesschema, deeplinkedschema, attachmentschema }));
  
  public static final List<ChildSchemaType> NumericTypes = Collections.unmodifiableList(Arrays.asList(new ChildSchemaType[]{ sequenceidschema, numberschema, moneyschema, sequenceschema }));
  
  public static final List<ChildSchemaType> IdTypes = Collections.unmodifiableList(Arrays.asList(new ChildSchemaType[]{ idschema, autoidschema, sequenceidschema }));
  
  public static final List<ChildSchemaType> SystemAssignedTypes = Collections.unmodifiableList(Arrays.asList(new ChildSchemaType[]{autoidschema, sequenceidschema, sequenceschema }));
  
  public static List<ChildSchemaType> SimpleAndListInlineTypes;
  
  private final String displaytext;
  
  private final Class<? extends ChildSchema<?>> schemaClass;
  
  private ChildSchema<?> schemaInstance;

  /** The search operators valid for this schema type. */
  private final SearchOperator[] searchOperators;
  
  static
  {
    SimpleAndListInlineTypes = new ArrayList<ChildSchemaType>(SimpleTypes);
    SimpleAndListInlineTypes.add(ChildSchemaType.listinlineschema);
    SimpleAndListInlineTypes = Collections.unmodifiableList(SimpleAndListInlineTypes);
  }

  private ChildSchemaType(String displaytext, SearchOperator[] searchOperators, Class<? extends ChildSchema<?>> schemaClass)
  {
    this.displaytext = displaytext;
    this.searchOperators = searchOperators;
    this.schemaClass = schemaClass;
  }

  @Override
  public String getDisplaytext()
  {
    return displaytext;
  }

  public SearchOperator[] getSearchOperators()
  {
    return searchOperators;
  }
  
  public ChildSchema<?> getSchemaInstance()
  {
    if (schemaInstance == null)
    {
      try
      {
        this.schemaInstance = this.schemaClass.newInstance();
      }
      catch (Throwable t)
      {

      }
    }
    return schemaInstance;
  }
}

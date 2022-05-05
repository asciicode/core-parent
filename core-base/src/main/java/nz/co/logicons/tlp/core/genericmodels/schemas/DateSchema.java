package nz.co.logicons.tlp.core.genericmodels.schemas;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;

import nz.co.logicons.tlp.core.dbsync.model.FieldTypeAndLength;
import nz.co.logicons.tlp.core.enums.DBType;
import nz.co.logicons.tlp.core.enums.FieldType;
import nz.co.logicons.tlp.core.genericmodels.jackson.serializationprofile.SerializationProfile;
import nz.co.logicons.tlp.core.genericmodels.nodes.Node;

/**
 * Schema for a date field.
 * @author bhambr
 *
 */
public class DateSchema
    extends ChildSchema<LocalDate>
{
  
  public static DateTimeFormatter fmt = ISODateTimeFormat.dateTime();
  
  @Override
  public Node<LocalDate> deserializeNode(JsonNode jsonNode)
  {
    if (jsonNode == null || jsonNode.isNull())
    {
      return new Node<>(null, this);
    }
    else if (!jsonNode.isTextual())
    {
      //TODO: Knock this out and just return null once data has been transitioned across.
      return new Node<>(new LocalDate(jsonNode.asLong()), this);
    }
    else
    {
      return new Node<>(deserialize(jsonNode.asText()), this);
    }
  }

  @Override
  public void serializeNode(Node<LocalDate> node,
    JsonGenerator jsonGenerator/* , User user */,
    SerializationProfile serializationProfile, String parentPath)
    throws JsonGenerationException, IOException
  {
    if (node == null || node.getValue() == null)
    {
      jsonGenerator.writeNull();
    }
    else
    {
      jsonGenerator.writeString(serialize(node.getValue()));
    }

  }
  
  public static String serialize(LocalDate localDate)
  {
    return fmt.print(localDate.toLocalDateTime(LocalTime.MIDNIGHT));
  }
  
  public static LocalDate deserialize(String input)
  {
    if (StringUtils.isNotBlank(input))
    {
      return new DateTime(input).toLocalDate();
    }
    return null;
  }
  
  @Override
  public boolean isValueSet(LocalDate t)
  {
    return (t != null);
  }
  
  @Override
  public FieldTypeAndLength getFieldTypeAndLength(DBType dbType)
  {
    return new FieldTypeAndLength(FieldType.DATE);
  }

  @Override
  public void setScale(int scale)
  {
    // TODO Auto-generated method stub
    
  }
}

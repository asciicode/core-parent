package nz.co.logicons.tlp.core.genericmodels.schemas;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.LocalTime;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;

import nz.co.logicons.tlp.core.dbsync.model.FieldTypeAndLength;
import nz.co.logicons.tlp.core.enums.DBType;
import nz.co.logicons.tlp.core.enums.FieldType;
import nz.co.logicons.tlp.core.genericmodels.jackson.serializationprofile.SerializationProfile;
import nz.co.logicons.tlp.core.genericmodels.nodes.Node;

/**
 * Schema for a time field.
 * 
 * @author bhambr
 *
 */
public class TimeSchema
    extends ChildSchema<LocalTime>
{

  private static Pattern pattern = Pattern.compile("[0-9]{2,2}[:][0-9]{2,2}[:][0-9]{2,2}[.][0-9]{3,3}");

  @Override
  public Node<LocalTime> deserializeNode(JsonNode jsonNode)
  {
    if (jsonNode == null || jsonNode.isNull())
    {
      return new Node<>(null, this);
    }
    else if (!jsonNode.isTextual())
    {
      // TODO: Knock this out and just return null once data has been transitioned across.
      return new Node<>(new LocalTime().withMillisOfDay(jsonNode.asInt()), this);
    }
    else
    {
      return new Node<>(deserialize(jsonNode.asText()), this);
    }
  }

  @Override
  public void serializeNode(Node<LocalTime> node,
    JsonGenerator jsonGenerator, /* User user, */
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

  public static String serialize(LocalTime localTime)
  {
    return DateTimeSchema.serialize(new DateTime(0).withTime(localTime).toLocalDateTime());
  }

  public static LocalTime deserialize(String input)
  {
    if (StringUtils.isNotBlank(input))
    {
      Matcher matcher = pattern.matcher(input);
      if (matcher.find())
      {
        return new LocalTime(matcher.group());
      }
    }
    return null;
  }

  @Override
  public FieldTypeAndLength getFieldTypeAndLength(DBType dbType)
  {
    return new FieldTypeAndLength(FieldType.TIME);
  }

  @Override
  public void setScale(int scale)
  {
    // TODO Auto-generated method stub
    
  }
}

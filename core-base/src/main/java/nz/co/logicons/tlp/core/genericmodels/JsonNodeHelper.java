package nz.co.logicons.tlp.core.genericmodels;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

public class JsonNodeHelper
{
  public static String getString(JsonNode jsonNode, String id)
  {
    return getString(jsonNode, id, null);
  }

  public static String getString(JsonNode jsonNode, String id, String defaultValue)
  {
    try
    {
      if (jsonNode.get(id).isNull())
      {
        return defaultValue;
      }
      return jsonNode.get(id).asText();
    }
    catch (Exception e)
    {
      return defaultValue;
    }
  }

  public static boolean getBoolean(JsonNode jsonNode, String id)
  {
    return getBoolean(jsonNode, id, false);
  }

  public static boolean getBoolean(JsonNode jsonNode, String id, boolean defaultValue)
  {
    try
    {
      if (jsonNode.get(id).isNull())
      {
        return defaultValue;
      }
      return jsonNode.get(id).asBoolean();
    }
    catch (Exception e)
    {
      return defaultValue;
    }
  }

  public static long getLong(JsonNode jsonNode, String id)
  {
    try
    {
      if (jsonNode.get(id).isNull())
      {
        return 0;
      }
      return jsonNode.get(id).asLong();
    }
    catch (Exception e)
    {
      return 0;
    }
  }

  public static int getInt(JsonNode jsonNode, String id)
  {
    try
    {
      if (jsonNode.get(id).isNull())
      {
        return 0;
      }
      return jsonNode.get(id).asInt();
    }
    catch (Exception e)
    {
      return 0;
    }
  }

  public static Iterator<JsonNode> getIterator(JsonNode jsonNode, String id)
  {
    try
    {
      return jsonNode.get(id).elements();
    }
    catch (Exception e)
    {
      return new ArrayList<JsonNode>().iterator();
    }
  }

  public static Map<String, String> getMap(JsonNode jsonNode, String id)
  {
    try
    {
      LinkedHashMap<String, String> map = new LinkedHashMap<>();
      Iterator<String> iterator = jsonNode.get(id).fieldNames();
      while (iterator.hasNext())
      {
        String fieldName = iterator.next();
        map.put(fieldName, getString(jsonNode.get(id), fieldName));
      }
      return map;
    }
    catch (Exception e)
    {
      return null;
    }
  }

  public static Map<String, String> getMap(JsonNode jsonNode)
  {
    try
    {
      LinkedHashMap<String, String> map = new LinkedHashMap<>();
      Iterator<String> iterator = jsonNode.fieldNames();
      while (iterator.hasNext())
      {
        String fieldName = iterator.next();
        map.put(fieldName, getString(jsonNode, fieldName));
      }
      return map;
    }
    catch (Exception e)
    {
      return null;
    }
  }
  
  /*
  public static List<String> getStringList(JsonNode jsonNode, String id)
  {
    List<String> list = new ArrayList<>();
    ArrayNode arrayNode =  ((ArrayNode) jsonNode.get(id));
    Iterator<JsonNode> iterator = arrayNode.iterator();
    while (iterator.hasNext())
    {
      list.add(iterator.next().asText());
    }
    return list;
  }
  */
  
  public static List<String> getStringList(JsonNode jsonNode)
  {
    List<String> list = new ArrayList<>();
    ArrayNode arrayNode =  ((ArrayNode) jsonNode);
    Iterator<JsonNode> iterator = arrayNode.iterator();
    while (iterator.hasNext())
    {
      list.add(iterator.next().asText());
    }
    return list;
  }

}

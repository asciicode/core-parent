package nz.co.logicons.tlp.core.genericmodels.jackson;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import nz.co.logicons.tlp.core.enums.PermissionType;
import nz.co.logicons.tlp.core.genericmodels.permissions.Permission;
import nz.co.logicons.tlp.core.genericmodels.permissions.PermissionEvaluator;
import nz.co.logicons.tlp.core.genericmodels.permissions.PermissionResult;
import nz.co.logicons.tlp.core.genericmodels.schemas.DocumentSchema;

/**
 * Following permissions are calculated and sent to browser at a documentschema/schema level (via cache action)
 * 1. Inline DocumentSchema - canupdate
 * 2. Non inline/parent DocumentSchema - cancreate
 * 3. Child Schema/field in DocumentSchema - canupdate
 * These permissions are not dependent on document content and as such can be precalculated on a per user basis.
 * @author bhambr
 *
 */
public class PermissionEvaluatorSerializer extends JsonSerializer<PermissionEvaluator>
{
  /** Derive permissions for serialization using this user's roles. */ 
  // private final User user;
  
  public PermissionEvaluatorSerializer(/* User user */)
  {
    // this.user = user;
  }

  @Override
  public void serialize(PermissionEvaluator permissionEvaluator, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
    throws IOException, JsonProcessingException
  {
    
    jsonGenerator.writeStartObject();
    // if (!user.getRoles().isSystem())
    // {
      if (permissionEvaluator.getSchema().getClass() == DocumentSchema.class)
      {
        if (((DocumentSchema) permissionEvaluator.getSchema()).isInline())
        {
          //For inline document schema write canupdate
          if (permissionEvaluator.evaluate(PermissionType.Update, /* user, */null) == PermissionResult.PERMITTED)
          {
            jsonGenerator.writeBooleanField(Permission.CAN_UPDATE_FIELD_NAME, true);
          }
        }
        else
        {
          //For non inline document (i.e parent documents) write cancreate
          PermissionResult permissionResult = permissionEvaluator.evaluate(PermissionType.Create, /* user, */null);
          if (permissionResult != null)
          {
            if (permissionResult.isPermitted() || permissionResult.isPermittedWithFilter())
            {
              jsonGenerator.writeBooleanField(Permission.CAN_CREATE_FIELD_NAME, true);
            }
          }
        }
      }
      else
      {
        if (permissionEvaluator.evaluate(PermissionType.Update, /* user, */null) == PermissionResult.PERMITTED)
        {
          jsonGenerator.writeBooleanField(Permission.CAN_UPDATE_FIELD_NAME, true);
        }
      }
      // }
    
    jsonGenerator.writeEndObject();
  }

}

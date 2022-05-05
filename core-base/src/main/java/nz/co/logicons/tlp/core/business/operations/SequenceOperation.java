package nz.co.logicons.tlp.core.business.operations;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;

import nz.co.logicons.tlp.core.enums.ChildSchemaType;
import nz.co.logicons.tlp.core.exceptions.ValidationException;
import nz.co.logicons.tlp.core.genericmodels.base.GenericSerializable;
import nz.co.logicons.tlp.core.genericmodels.nodes.DocumentNode;
import nz.co.logicons.tlp.core.genericmodels.operations.SchemaOperation;
import nz.co.logicons.tlp.core.genericmodels.schemas.ChildSchema;
import nz.co.logicons.tlp.core.genericmodels.schemas.DocumentSchema;
import nz.co.logicons.tlp.core.genericmodels.schemas.SequenceSchema;
import nz.co.logicons.tlp.core.mongo.MongoDatastore;

/**
 * Performs sequence operations.
 * Sequences are auto incrementing numbers (usually used as _id/primary key)
 * @author bhambr
 *
 */
public class SequenceOperation
{
  public static final String SCHEMA = "(sequence)";
  
  public static final String NEXT_FIELD = "next";
  
  @Autowired
  private MongoDatastore datastore;
  
  // @Autowired
  // @System
  // private User systemUser;
  
  @Autowired
  private SchemaOperation schemaOperation;
  
  /**
   * Create a new sequence with specified id.
   * @param sequenceid .
   */
  public void init(String sequenceid)
  {
    init(sequenceid, 0, false);
  }
  
  /**
   * Set a sequence.
   * @param sequenceid Sequence to set.
   * @param next value of sequence.
   * @param error if set then error thrown if requested sequence value is less than current sequence value.
   */
  public void init(String sequenceid, int next, boolean error)
  {
    //get the sequence document.
    DocumentNode documentNode = datastore.getDocument(SCHEMA, sequenceid, false);
    
    //it does not exist so create it.
    if (documentNode == null) 
    {
      documentNode = new DocumentNode(datastore.getSchema(SCHEMA));
      documentNode.setChildNodeValue(GenericSerializable.ID_FIELD_NAME, sequenceid);
      documentNode.setChildNodeValue(NEXT_FIELD, BigDecimal.ZERO);
    }
    
    //set new value (if current value <= specified value) 
    if (documentNode.getChildNodeValueAsBigDecimal(NEXT_FIELD).intValue() <= next)
    {
      documentNode.setChildNodeValue(NEXT_FIELD, new BigDecimal(next));
      datastore.upsertDocument(documentNode);
    }
    else if (error && documentNode.getChildNodeValueAsBigDecimal(NEXT_FIELD).intValue() > next)
    {
      //throw error (if current value > specified value and param error == true)
      throw new ValidationException("Must be greater than " + documentNode.getChildNodeValueAsBigDecimal(NEXT_FIELD).intValue());
    }
  }
  
  /**
   * Increment specified sequence.
   * @param sequenceid .
   * @return .
   */
  public DocumentNode nextSequenceDocument(String sequenceid)
  {
    return datastore.nextSequenceDocument(sequenceid);
  }
  
  public void recalculateSequences()
  {
    for (DocumentSchema documentSchema : schemaOperation.getSchemas())
    {
      for (ChildSchema<?> childSchema : documentSchema.getChildrenMap().values())
      {
        if (childSchema.getType() == ChildSchemaType.sequenceidschema || childSchema.getType() == ChildSchemaType.sequenceschema)
        {
          
          SequenceSchema sequenceSchema = (SequenceSchema) childSchema;
          
          //figure out the highest sequence from collection
          int next =  datastore.getMaxSequence(documentSchema.getId(), sequenceSchema.getId());
          
          //no documents exists - nothing to do
          if (next == 0) 
          {
            break;
          }
          
          //get the sequence document.
          DocumentNode documentNode = datastore.getDocument(SCHEMA, sequenceSchema.getSequenceid(), false);
          
          //it does not exist so create it.
          if (documentNode == null) 
          {
            documentNode = new DocumentNode(datastore.getSchema(SCHEMA));
            documentNode.setChildNodeValue(GenericSerializable.ID_FIELD_NAME, sequenceSchema.getSequenceid());
            documentNode.setChildNodeValue(NEXT_FIELD, BigDecimal.ZERO);
          }
          
          //set new value (if current value <= specified value) 
          if (documentNode.getChildNodeValueAsBigDecimal(NEXT_FIELD).intValue() <= next)
          {
            documentNode.setChildNodeValue(NEXT_FIELD, new BigDecimal(next));
            datastore.upsertDocument(documentNode);
          }
        }
      }
    }
  }
}

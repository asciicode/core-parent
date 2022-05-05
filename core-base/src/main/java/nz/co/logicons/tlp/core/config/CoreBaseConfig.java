/**
 * 
 */
package nz.co.logicons.tlp.core.config;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.convert.CustomConversions;
import org.springframework.data.mongodb.core.convert.DbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import nz.co.logicons.tlp.core.business.models.Model;
import nz.co.logicons.tlp.core.business.operations.SequenceOperation;
import nz.co.logicons.tlp.core.genericmodels.ChildSchemaSubTypeProvider;
import nz.co.logicons.tlp.core.genericmodels.ValidatorsProvider;
import nz.co.logicons.tlp.core.genericmodels.base.GenericSerializable;
import nz.co.logicons.tlp.core.genericmodels.jackson.DocumentNodeDeserializer;
import nz.co.logicons.tlp.core.genericmodels.jackson.DocumentNodeSerializer;
import nz.co.logicons.tlp.core.genericmodels.jackson.GenericSerializer;
import nz.co.logicons.tlp.core.genericmodels.jackson.ModelSerializer;
import nz.co.logicons.tlp.core.genericmodels.jackson.PermissionEvaluatorSerializer;
import nz.co.logicons.tlp.core.genericmodels.jackson.ScriptLogSerilizer;
import nz.co.logicons.tlp.core.genericmodels.nodes.DocumentNode;
import nz.co.logicons.tlp.core.genericmodels.operations.SchemaOperation;
import nz.co.logicons.tlp.core.genericmodels.operations.ValidateSchemaOperation;
import nz.co.logicons.tlp.core.genericmodels.operations.validation.ValidateDocumentOperation;
import nz.co.logicons.tlp.core.genericmodels.permissions.PermissionEvaluator;
import nz.co.logicons.tlp.core.genericmodels.permissions.PermissionEvaluatorProvider;
import nz.co.logicons.tlp.core.genericmodels.scripts.ScriptLog;
import nz.co.logicons.tlp.core.mongo.MongoQueryBuilder;
import nz.co.logicons.tlp.core.mongo.TransformOperation;

/**
 * @author Allen
 *
 */
@Configuration
public class CoreBaseConfig
{
  @Bean
  @Primary
  public ObjectMapper objectMapper()
  {
    ObjectMapper objectMapper = new ObjectMapper();
    SimpleModule simpleModule = new SimpleModule("simpleModule", new Version(1, 0, 0, null, null, null));
    simpleModule.addSerializer(GenericSerializable.class, new GenericSerializer());
    simpleModule.addSerializer(DocumentNode.class, new DocumentNodeSerializer());
    simpleModule.addSerializer(PermissionEvaluator.class, new PermissionEvaluatorSerializer());
    simpleModule.addDeserializer(DocumentNode.class, new DocumentNodeDeserializer());
    simpleModule.addSerializer(Model.class, new ModelSerializer());
    simpleModule.addSerializer(ScriptLog.class, new ScriptLogSerilizer());

    objectMapper = objectMapper.registerModule(simpleModule);
    objectMapper.setSerializationInclusion(Include.NON_NULL);
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    return objectMapper;
  }

  @Bean
  public MappingMongoConverter mappingMongoConverter(MongoDbFactory factory, MongoMappingContext context,
    BeanFactory beanFactory)
  {
    DbRefResolver dbRefResolver = new DefaultDbRefResolver(factory);
    MappingMongoConverter mappingConverter = new MappingMongoConverter(dbRefResolver, context);
    try
    {
      mappingConverter.setCustomConversions(beanFactory.getBean(CustomConversions.class));
    }
    catch (NoSuchBeanDefinitionException ignore)
    {
    }

    // Don't save _class to mongo
    // comment for now need GrantedAuthority converter
    // mappingConverter.setTypeMapper(new DefaultMongoTypeMapper(null));

    return mappingConverter;
  }

  @Bean
  public TransformOperation transformOperation()
  {
    return new TransformOperation();
  }

  @Bean
  public MongoQueryBuilder mongoQueryBuilder()
  {
    return new MongoQueryBuilder();
  }

  @Bean
  public SchemaOperation schemaOperation()
  {
    return new SchemaOperation();
  }

  @Bean
  public SequenceOperation sequenceOperation()
  {
    return new SequenceOperation();
  }

  @Bean
  public PermissionEvaluatorProvider permissionEvaluatorProvider()
  {
    return new PermissionEvaluatorProvider();
  }

  @Bean
  public ValidateSchemaOperation validateSchemaOperation()
  {
    return new ValidateSchemaOperation();
  }

  @Bean
  public ChildSchemaSubTypeProvider childSchemaSubTypeProvider()
  {
    return new ChildSchemaSubTypeProvider();
  }

  @Bean
  public ValidatorsProvider validatorsProvider()
  {
    return new ValidatorsProvider();
  }

  @Bean
  public ValidateDocumentOperation validateDocumentOperation()
  {
    return new ValidateDocumentOperation();
  }
}

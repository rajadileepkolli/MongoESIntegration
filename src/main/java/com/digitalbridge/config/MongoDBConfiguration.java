package com.digitalbridge.config;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.MongoExceptionTranslator;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.core.WriteResultChecking;
import org.springframework.data.mongodb.core.convert.CustomConversions;
import org.springframework.data.mongodb.core.convert.DbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.convert.MongoConverter;

import com.digitalbridge.mongodb.audit.MongoAuditorProvider;
import com.digitalbridge.mongodb.convert.ObjectConverters;
import com.digitalbridge.mongodb.event.CascadeSaveMongoEventListener;
import com.digitalbridge.util.Constants;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.WriteConcern;

/**
 * <p>
 * MongoDBConfiguration class.
 * </p>
 *
 * @author rajakolli
 * @version 1:0
 */
@Configuration
public class MongoDBConfiguration extends AbstractMongoConfiguration {

    @Autowired
    Environment env;

    /**
     * <p>
     * Constructor for MongoDBConfiguration.
     * </p>
     */
    public MongoDBConfiguration() {
        super();
    }

    /** {@inheritDoc} */
    @Override
    protected String getDatabaseName() {
        return Constants.APPLICATIONNAME;
    }

    /** {@inheritDoc} */
    @Override
    public Mongo mongo() throws Exception {
        return getMongoClient();
    }

    /** {@inheritDoc} */
    @Override
    protected String getMappingBasePackage() {
        return "com.digitalbridge.domain";
    }

    /**
     * <p>
     * mongoClient.
     * </p>
     *
     * @return a {@link com.mongodb.MongoClient} object.
     */
    @Profile("local")
    @Bean(name = "mongoClient")
    public MongoClient localMongoClient() {
      List<MongoCredential> credentialsList = new ArrayList<MongoCredential>();
      credentialsList.add(MongoCredential.createCredential("digitalbridgeAdmin", Constants.APPLICATIONNAME, "password".toCharArray()));
      ServerAddress primary = new ServerAddress(new InetSocketAddress(Constants.LOCALHOST, Constants.PRIMARYPORT));
      ServerAddress secondary = new ServerAddress(new InetSocketAddress(Constants.LOCALHOST, Constants.SECONDARYPORT));
      ServerAddress teritory = new ServerAddress(new InetSocketAddress(Constants.LOCALHOST, Constants.TERITORYPORT));
      ServerAddress arbiterOnly = new ServerAddress(new InetSocketAddress(Constants.LOCALHOST, Constants.ARBITERPORT));
      List<ServerAddress> seeds = Arrays.asList(primary, secondary, teritory, arbiterOnly);
      MongoClientOptions mongoClientOptions = MongoClientOptions.builder().requiredReplicaSetName("digitalBridgeReplica")
          .build();
      return new MongoClient(seeds, credentialsList, mongoClientOptions);
    }

    @Profile("iLab")
    @Bean(name = "mongoClient")
    public MongoClient ilabMongoClient() {
      List<MongoCredential> credentialsList = new ArrayList<MongoCredential>();
      credentialsList.add(MongoCredential.createCredential("digitalbridgeAdmin", Constants.APPLICATIONNAME, "fD4Krim9".toCharArray()));
      ServerAddress primary = new ServerAddress("152.190.139.69", Constants.PRIMARYPORT);
      ServerAddress secondary = new ServerAddress("152.190.139.77", Constants.PRIMARYPORT);
      ServerAddress teritory = new ServerAddress("152.190.139.78", Constants.PRIMARYPORT);
      List<ServerAddress> seeds = Arrays.asList(primary, secondary, teritory);
      MongoClientOptions mongoClientOptions = MongoClientOptions.builder().requiredReplicaSetName("rs0").build();
      return new MongoClient(seeds, credentialsList, mongoClientOptions);
    }

    /**
     * <p>
     * mongoDbFactory.
     * </p>
     *
     * @return a {@link org.springframework.data.mongodb.MongoDbFactory} object.
     */
    @Bean
    public MongoDbFactory mongoDbFactory() {
        return new SimpleMongoDbFactory(getMongoClient(), getDatabaseName());
    }

    private MongoClient getMongoClient() {
        if (findProfile("iLab")) {
            return ilabMongoClient();
        } else {
            return localMongoClient();
        }
    }
    
    private boolean findProfile(String profileName) {
        return Arrays.asList(env.getActiveProfiles()).contains(profileName);
    }

    /**
     * <p>
     * mongoTemplate.
     * Creates a {@link org.springframework.data.mongodb.core.MongoTemplate}.
     * </p>
     * If we use REPLICA_ACKNOWLEDGED as WriteConcern -Exceptions are raised for network
     * issues, and server errors; waits for at least 2 servers for the write operation.
     *
     * @return a {@link org.springframework.data.mongodb.core.MongoTemplate} object.
     * @throws java.lang.Exception if any.
     */
    @Bean
    public MongoTemplate mongoTemplate() throws Exception {
        MongoTemplate mongoTemplate = new MongoTemplate(mongoDbFactory(),
                mongoConverter());
        mongoTemplate.setWriteConcern(WriteConcern.FSYNCED);
        mongoTemplate.setWriteResultChecking(WriteResultChecking.EXCEPTION);
        return mongoTemplate;
    }

    /**
     * Creates a {@link org.springframework.data.mongodb.core.convert.MappingMongoConverter} using the configured {@link #mongoDbFactory()} and
     * {@link #mongoMappingContext()}. Will get {@link #customConversions()} applied.
     *
     * @see #customConversions()
     * @see #mongoMappingContext()
     * @see #mongoDbFactory()
     * @return a {@link org.springframework.data.mongodb.core.convert.MongoConverter} object.
     * @throws java.lang.Exception if any.
     */
    @Bean
    public MongoConverter mongoConverter() throws Exception {
        DbRefResolver dbRefResolver = new DefaultDbRefResolver(mongoDbFactory());
        MappingMongoConverter converter = new MappingMongoConverter(dbRefResolver, mongoMappingContext());
        converter.setTypeMapper(new DefaultMongoTypeMapper(null));
        converter.setCustomConversions(customConversions());
        return converter;
    }

    /** {@inheritDoc} */
    @Override
    @Bean
    public CustomConversions customConversions() {
        List<Converter<?, ?>> converters = new ArrayList<Converter<?, ?>>();
        converters.addAll(ObjectConverters.getConvertersToRegister());
        return new CustomConversions(converters);
    }

    /**
     * <p>
     * exceptionTranslator.
     * </p>
     *
     * @return a {@link org.springframework.data.mongodb.core.MongoExceptionTranslator}
     * object.
     */
    @Bean
    public MongoExceptionTranslator exceptionTranslator() {
        return new MongoExceptionTranslator();
    }

    /**
     * <p>
     * auditorProvider.
     * </p>
     *
     * @return a {@link org.springframework.data.domain.AuditorAware} object.
     */
    @Bean
    public AuditorAware<String> auditorProvider() {
        return new MongoAuditorProvider<String>();
    }

    /**
     * <p>
     * cascadingMongoEventListener.
     * </p>
     *
     * @return a {@link com.digitalbridge.mongodb.event.CascadeSaveMongoEventListener}
     * object.
     */
    @Bean
    public CascadeSaveMongoEventListener cascadingMongoEventListener() {
        return new CascadeSaveMongoEventListener();
    }

}

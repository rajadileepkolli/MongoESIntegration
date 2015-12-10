package com.digitalbridge.config;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
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

import com.digitalbridge.mongodb.audit.MongoAuditorProvider;
import com.digitalbridge.mongodb.convert.ObjectConverters;
import com.digitalbridge.mongodb.event.CascadeSaveMongoEventListener;
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

    private static final String DATABASE = "digitalbridge";

    @Value("${mongo.primaryhost}")
    private String primaryhost;

    @Value("${mongo.secondaryhost}")
    private String secondaryhost;

    @Value("${mongo.teritoryhost}")
    private String teritoryhost;

    @Value("${mongo.replicasetname}")
    private String replicasetName;

    @Value("${mongo.superadminpassword}")
    private String superadminpassword;

    @Value("${mongo.primaryport}")
    private int primaryport;

    @Value("${mongo.secondaryport}")
    private int secondaryport;

    @Value("${mongo.teritoryport}")
    private int teritoryport;

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
        return DATABASE;
    }

    /** {@inheritDoc} */
    @Override
    public Mongo mongo() throws Exception {
        return mongoClient();
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
    @Bean
    public MongoClient mongoClient() {
        List<MongoCredential> credentialsList = new ArrayList<MongoCredential>();
        credentialsList.add(MongoCredential.createCredential("digitalbridgeAdmin",
                DATABASE, superadminpassword.toCharArray()));
        ServerAddress primary = new ServerAddress(
                new InetSocketAddress(primaryhost, primaryport));
        ServerAddress secondary = new ServerAddress(
                new InetSocketAddress(secondaryhost, secondaryport));
        ServerAddress teritory = new ServerAddress(
                new InetSocketAddress(teritoryhost, teritoryport));
        List<ServerAddress> seeds = Arrays.asList(primary, secondary, teritory);
        MongoClientOptions mongoClientOptions = MongoClientOptions.builder()
                .socketKeepAlive(true) // Enable for only above JDK 7 and above
                .requiredReplicaSetName(replicasetName).build();
        return new MongoClient(seeds, credentialsList, mongoClientOptions);
    }

    /**
     * <p>
     * mongoDbFactory.
     * </p>
     *
     * @return a {@link org.springframework.data.mongodb.MongoDbFactory} object.
     */
    public MongoDbFactory mongoDbFactory() {
        return new SimpleMongoDbFactory(mongoClient(), getDatabaseName());
    }

    /**
     * <p>
     * mongoTemplate.
     * </p>
     * If we use REPLICA_ACKNOWLEDGED as WriteConcern -Exceptions are raised for network
     * issues, and server errors; waits for at least 2 servers for the write operation.
     *
     * @return a {@link org.springframework.data.mongodb.core.MongoTemplate} object.
     * @throws java.lang.ClassNotFoundException if any.
     */
    @Bean
    public MongoTemplate mongoTemplate() throws ClassNotFoundException {
        MongoTemplate mongoTemplate = new MongoTemplate(mongoDbFactory(),
                mongoConverter());
        mongoTemplate.setWriteConcern(WriteConcern.JOURNALED);
        mongoTemplate.setWriteResultChecking(WriteResultChecking.EXCEPTION);
        return mongoTemplate;
    }

    /**
     * <p>
     * mongoConverter.
     * </p>
     *
     * @return a
     * {@link org.springframework.data.mongodb.core.convert.MappingMongoConverter} object.
     * @throws java.lang.ClassNotFoundException if any.
     */
    @Bean
    public MappingMongoConverter mongoConverter() throws ClassNotFoundException {
        DbRefResolver dbRefResolver = new DefaultDbRefResolver(mongoDbFactory());
        MappingMongoConverter converter = new MappingMongoConverter(dbRefResolver,
                mongoMappingContext());
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

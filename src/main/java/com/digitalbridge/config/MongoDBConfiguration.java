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
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.MongoExceptionTranslator;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.core.convert.CustomConversions;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

import com.digitalbridge.mongodb.audit.MongoAuditorProvider;
import com.digitalbridge.mongodb.convert.ObjectConverters;
import com.digitalbridge.mongodb.event.CascadeSaveMongoEventListener;
import com.digitalbridge.util.Constants;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;

/**
 * <p>MongoDBConfiguration class.</p>
 *
 * @author rajakolli
 * @version 1:0
 */
@Configuration
public class MongoDBConfiguration extends AbstractMongoConfiguration {

//  private static final Logger LOGGER = LoggerFactory.getLogger(MongoDBConfiguration.class);

  private static final String DATABASE = "digitalbridge";

  @Autowired private Mongo mongoClient;

//  private String bindIp = InetAddress.getLoopbackAddress().getHostAddress();

/*  private IFeatureAwareVersion version = PRODUCTION;

  private Integer port;

  private IDirectory artifactStorePath = new FixedPath(
      "C:\\workspace-sts-3.7.0.RELEASE" + "\\embeddedMongodbCustomPath");

  private ITempNaming executableNaming = new UUIDTempNaming();*/

  /** {@inheritDoc} */
  @Override
  protected String getDatabaseName() {
    return DATABASE;
  }

  /** {@inheritDoc} */
  @Override
  public Mongo mongo() throws Exception {
    return null;
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
    credentialsList.add(MongoCredential.createCredential("digitalbridgeAdmin", DATABASE, "password".toCharArray()));
    ServerAddress primary = new ServerAddress(new InetSocketAddress(Constants.LOCALHOST, Constants.PRIMARYPORT));
    ServerAddress secondary = new ServerAddress(new InetSocketAddress(Constants.LOCALHOST, Constants.SECONDARYPORT));
    ServerAddress teritory = new ServerAddress(new InetSocketAddress(Constants.LOCALHOST, Constants.TERITORYPORT));
    ServerAddress arbiterOnly = new ServerAddress(new InetSocketAddress(Constants.LOCALHOST, Constants.ARBITERPORT));
    List<ServerAddress> seeds = Arrays.asList(primary, secondary, teritory, arbiterOnly);
    MongoClientOptions mongoClientOptions = MongoClientOptions.builder().requiredReplicaSetName("digitalBridgeReplica")
        .build();
    return new MongoClient(seeds, credentialsList, mongoClientOptions);
  }

/*  @Profile("test")
  @Bean(name = "mongoClient", destroyMethod = "close")
  public MongoClient testMongoClient() throws IOException {
    LOGGER.info("Initializing embedded MongoDB instance");
    MongodStarter runtime = MongodStarter.getInstance(buildRuntimeConfig());
    MongodExecutable mongodExe = runtime.prepare(buildMongodConfig());
    LOGGER.info("Starting embedded MongoDB instance");
    mongodExe.start();

    return new MongoClient(bindIp, getPort());
  }

  private Integer getPort() {
    if (port == null) {
      try {
        port = Network.getFreeServerPort();
      } catch (IOException ex) {
        LOGGER.error("Could not get free server port");
      }
    }
    return port;
  }

  private IMongodConfig buildMongodConfig() throws IOException {
    Storage replication = new Storage("C:\\embeddedMongodbCustomPath", "rs0", 0);
    return new MongodConfigBuilder().version(version).net(new Net(bindIp, getPort(), Network.localhostIsIPv6()))
        .replication(replication).build();
  }

  private IRuntimeConfig buildRuntimeConfig() {
    return new RuntimeConfigBuilder().defaults(Command.MongoD).processOutput(buildOutputConfig())
        .artifactStore(buildArtifactStore()).build();
  }

  private ProcessOutput buildOutputConfig() {
    Logger logger = LoggerFactory.getLogger(MongodProcess.class);

    return new ProcessOutput(new Slf4jStreamProcessor(logger, Slf4jLevel.TRACE),
        new Slf4jStreamProcessor(logger, Slf4jLevel.WARN), new Slf4jStreamProcessor(logger, Slf4jLevel.INFO));
  }

  private IArtifactStore buildArtifactStore() {
    Logger logger = LoggerFactory.getLogger(Downloader.class);

    return new ExtractedArtifactStoreBuilder()
        .defaults(Command.MongoD).download(new DownloadConfigBuilder().defaultsForCommand(Command.MongoD)
            .artifactStorePath(artifactStorePath).progressListener(new Slf4jProgressListener(logger)).build())
        .executableNaming(executableNaming).build();
  }*/

  /**
   * <p>ilabMongoClient.</p>
   *
   * @return a {@link com.mongodb.MongoClient} object.
   */
  @Profile("iLab")
  @Bean(name = "mongoClient")
  public MongoClient ilabMongoClient() {
    List<MongoCredential> credentialsList = new ArrayList<MongoCredential>();
    credentialsList.add(MongoCredential.createCredential("digitalbridgeAdmin", DATABASE, "fD4Krim9".toCharArray()));
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
  @SuppressWarnings("deprecation")
  @Bean
  public MongoDbFactory mongoDbFactory() {
    return new SimpleMongoDbFactory(mongoClient, getDatabaseName());
  }

  /**
   * <p>
   * mongoTemplate.
   * </p>
   *
   * @return a {@link org.springframework.data.mongodb.core.MongoTemplate} object.
   */
  @Bean
  public MongoTemplate mongoTemplate() {
    return new MongoTemplate(mongoDbFactory(), mongoConverter());
  }

  /**
   * <p>
   * mongoConverter.
   * </p>
   *
   * @return a {@link org.springframework.data.mongodb.core.convert.MappingMongoConverter} object.
   */
  @Bean
  public MappingMongoConverter mongoConverter() {
    MongoMappingContext mappingContext = new MongoMappingContext();
    DefaultDbRefResolver dbRefResolver = new DefaultDbRefResolver(mongoDbFactory());
    MappingMongoConverter mongoConverter = new MappingMongoConverter(dbRefResolver, mappingContext);
    mongoConverter.setTypeMapper(new DefaultMongoTypeMapper(null));
    mongoConverter.setCustomConversions(customConversions());
    return mongoConverter;
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
   * @return a {@link org.springframework.data.mongodb.core.MongoExceptionTranslator} object.
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
   * @return a {@link com.digitalbridge.event.CascadeSaveMongoEventListener} object.
   */
  @Bean
  public CascadeSaveMongoEventListener cascadingMongoEventListener() {
    return new CascadeSaveMongoEventListener();
  }

}

package com.digitalbridge.config;

import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.mongodb.MongoClient;

import io.searchbox.client.http.JestHttpClient;

@Configuration
public class PreDestroyConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(PreDestroyConfiguration.class);

    @Autowired
    JestHttpClient jestClient;

    @Autowired
    MongoClient mongoClient;

    @PreDestroy
    public void cleanup() {
        LOGGER.info("Spring Container is destroy! Customer clean up");
        try {
            jestClient.shutdownClient();
            mongoClient.close();
        } catch (Exception e) {
            LOGGER.error("Exception while closing {}", e.getMessage(), e);
        }
        LOGGER.info("Spring Container is destroy! Customer clean up completed");
    }
}

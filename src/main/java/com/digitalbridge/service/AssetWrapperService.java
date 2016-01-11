package com.digitalbridge.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.digitalbridge.domain.AssetWrapper;
import com.digitalbridge.mongodb.repository.AssetWrapperRepository;
import com.digitalbridge.request.UpdateRequest;
import com.digitalbridge.util.Constants;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoException;

/**
 * <p>
 * AssetWrapperService class.
 * </p>
 *
 * @author rajakolli
 * @version 1:0
 */
@RequestMapping(value = Constants.BASE_MAIN_URL)
@RestController
public class AssetWrapperService {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(AssetWrapperService.class);

    /*
     * https://github.com/spring-projects/spring-data-mongodb/blob/master/spring -data-
     * mongodb/src/test/java/org/springframework/data/mongodb/core/query/ UpdateTests.java
     * 
     * For Update OperationsTest
     */
    @Autowired
    AssetWrapperRepository assetWrapperRepository;

    @Autowired
    MongoTemplate mongoTemplate;

    /**
     * <p>
     * getAll.
     * </p>
     *
     * @return a {@link org.springframework.data.domain.Page} object.
     */
    @Secured({ "ROLE_USER" })
    @RequestMapping(value = "/getAll", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Page<AssetWrapper> getAll() {
        return assetWrapperRepository
                .findAll(new PageRequest(Constants.ZERO, Constants.PAGESIZE));
    }

    /**
     * <p>
     * geospatialsearch.
     * </p>
     *
     * @return a {@link org.springframework.data.domain.Page} object.
     */
    @Secured({ "ROLE_USER" })
    @RequestMapping(value = "/geospatialsearch")
    public Page<AssetWrapper> geospatialsearch() {
        Point point = new Point(-74.0014541, 40.7408231);
        Distance distance = new Distance(1, Metrics.MILES);
        Pageable pageable = new PageRequest(Constants.ZERO, Constants.PAGESIZE);
        Page<AssetWrapper> result = assetWrapperRepository.findByLocationNear(point,
                distance, pageable);
        return result;
    }

    /**
     * <p>
     * updateIndividualFields.
     * </p>
     *
     * @param assetID a {@link java.lang.String} object.
     * @param updateRequestList a {@link java.util.List} object.
     * @return a {@link com.digitalbridge.domain.AssetWrapper} object.
     */
    @Secured({ "ROLE_USER" })
    @RequestMapping(value = "/update/arrayvalue", method = { RequestMethod.GET,
            RequestMethod.PUT })
    public AssetWrapper updateIndividualFields(@RequestParam("assetID") String assetID,
            @RequestBody List<UpdateRequest> updateRequestList) {
        LOGGER.debug("received update request for assetID {}", assetID);
        Query query = new Query(Criteria.where("_id").is(assetID));
        Update update = new Update();
        for (UpdateRequest updateRequest : updateRequestList) {
            update.set(updateRequest.getKey(), updateRequest.getValue());
        }
        AssetWrapper result = mongoTemplate.findAndModify(query, update,
                new FindAndModifyOptions().returnNew(true).upsert(false),
                AssetWrapper.class);
        LOGGER.debug("Result : {}", result);
        return result;
    }

    /**
     * <p>
     * addToFieldArray.
     * </p>
     *
     * @param assetID a {@link java.lang.String} object.
     * @param updateRequestList a {@link java.util.List} object.
     * @return a {@link com.digitalbridge.domain.AssetWrapper} object.
     */
    @Secured({ "ROLE_USER" })
    @RequestMapping(value = "/update/updateArrayValue", method = { RequestMethod.PUT,
            RequestMethod.GET }, headers = "Accept=application/json")
    public AssetWrapper addToFieldArray(@RequestParam("assetID") String assetID,
            @RequestBody List<UpdateRequest> updateRequestList) {
        LOGGER.debug("received update request for assetID {}", assetID);
        Query query = new Query(Criteria.where("id").is(assetID));
        Update update = new Update();
        for (UpdateRequest updateRequest : updateRequestList) {
            update.push(updateRequest.getKey(), updateRequest.getValue());
        }
        AssetWrapper result = mongoTemplate.findAndModify(query, update,
                new FindAndModifyOptions().returnNew(true).upsert(false),
                AssetWrapper.class);
        LOGGER.debug("Result : {}", result);
        return result;
    }

    /**
     * <p>
     * removeFromFieldArray.
     * </p>
     *
     * @param assetID a {@link java.lang.String} object.
     * @param list a {@link java.util.List} object.
     * @return a {@link com.digitalbridge.domain.AssetWrapper} object.
     */
    @Secured({ "ROLE_USER" })
    @RequestMapping(value = "/update/removeFromFieldArray", method = { RequestMethod.PUT,
            RequestMethod.GET }, headers = "Accept=application/json")
    public AssetWrapper removeFromFieldArray(@RequestParam("assetID") String assetID,
            @RequestBody List<UpdateRequest> list) {
        LOGGER.debug("received update request for assetID {}", assetID);
        Query query = new Query(Criteria.where("id").is(assetID));

        Update update = new Update();
        for (UpdateRequest entry : list) {
            update.pull(entry.getKey(), entry.getValue());
        }
        AssetWrapper result = mongoTemplate.findAndModify(query, update,
                new FindAndModifyOptions().returnNew(true).upsert(false),
                AssetWrapper.class);
        LOGGER.debug("Result : {}", result);
        return result;
    }

    /**
     * <p>
     * findOne.
     * </p>
     *
     * @param assetID a {@link java.lang.String} object.
     * @return a {@link com.digitalbridge.domain.AssetWrapper} object.
     */
    @Secured({ "ROLE_USER" })
    @RequestMapping(value = "/findAsset")
    public AssetWrapper findOne(@RequestParam("assetID") String assetID) {
        return assetWrapperRepository.findOne(assetID);
    }

    /**
     * <p>
     * createGeoSpatialIndex.
     * </p>
     */
    @Secured({ "ROLE_ADMIN" })
    @RequestMapping(value = "/createGeoSpatialIndex")
    public void createGeoSpatialIndex() {
        DBCollection collection = mongoTemplate.getCollection("address");
        DBObject key = new BasicDBObject("location", "2dsphere");
        collection.dropIndexes();
        try {
            collection.createIndex(key, "geospatialIdx");
        } catch (MongoException e) {
            LOGGER.error("MongoException :: {}", e.getMessage(), e);
        }
    }

}

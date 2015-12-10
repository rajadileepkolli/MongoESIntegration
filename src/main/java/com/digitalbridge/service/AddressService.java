package com.digitalbridge.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.NearQuery;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.digitalbridge.domain.Address;
import com.digitalbridge.mongodb.repository.AssetWrapperRepository;
import com.digitalbridge.request.UpdateRequest;
import com.digitalbridge.util.Constants;

/**
 * <p>
 * AddressService class.
 * </p>
 *
 * @author rajakolli
 * @version 1:0
 */
@RestController
@RequestMapping(value = Constants.BASE_MAIN_URL)
public class AddressService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AddressService.class);

    @Autowired
    AssetWrapperRepository assetWrapperRepository;
    @Autowired
    MongoTemplate mongoTemplate;

    /**
     * <p>
     * updateSetValue.
     * </p>
     *
     * @param assetID a {@link java.lang.String} object.
     * @return a {@link com.digitalbridge.domain.Address} object.
     * @param updateRequest a {@link com.digitalbridge.request.UpdateRequest} object.
     */
    @Secured({ "ROLE_USER" })
    @RequestMapping(value = "/update/addressValue", method = { RequestMethod.GET,
            RequestMethod.PUT }, produces = MediaType.APPLICATION_JSON_VALUE)
    public Address updateSetValue(@RequestParam("assetID") String assetID,
            @RequestParam("updateValues") UpdateRequest updateRequest) {
        LOGGER.debug("received update request for assetID {}", assetID);
        String addressID = assetWrapperRepository.findOne(assetID).getAddress().getId();
        Query query = new Query(Criteria.where("_id").is(addressID));
        Update update = new Update();
        update.set(updateRequest.getKey(), updateRequest.getValue());
        Address result = mongoTemplate.findAndModify(query, update,
                new FindAndModifyOptions().returnNew(true).upsert(false), Address.class);
        LOGGER.debug("Result : {}", result);
        Assert.isTrue(result.getId().equalsIgnoreCase(addressID), "Address Matched");
        return result;
    }

    /**
     * <p>
     * geoNear.
     * </p>
     *
     * @param point a {@link org.springframework.data.geo.Point} object.
     * @param maxdistance a int.
     * @return a {@link org.springframework.data.geo.GeoResults} object.
     */
    public GeoResults<Address> geoNear(Point point, int maxdistance) {
        /**
         * if we want to limit number of results use num(int) which Configures the maximum
         * number of results to return.
         */
        NearQuery geoNear = NearQuery.near(new GeoJsonPoint(point), Metrics.MILES)
                .maxDistance(maxdistance);
        GeoResults<Address> result = mongoTemplate.geoNear(geoNear, Address.class);
        return result;
    }
}

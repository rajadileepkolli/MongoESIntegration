package com.digitalbridge.controller;

import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.digitalbridge.exception.DigitalBridgeException;
import com.digitalbridge.request.AggregationSearchRequest;
import com.digitalbridge.response.AggregationSearchResponse;
import com.digitalbridge.service.AggregationSearchService;
import com.digitalbridge.util.Constants;

/**
 * AggregationSearch class.
 * <p>
 *
 * @author rajakolli
 * @version 1:0
 */
@RestController
@RequestMapping(value = Constants.BASE_MAIN_URL)
public class AggregationSearchController {

    @Autowired
    AggregationSearchService aggregationSearchService;

    /**
     * performBasicAggregationSearch.
     * <p>
     *
     * @param refresh a boolean.
     * @param sortOrder a {@link java.lang.String} object.
     * @param searchKeyword a {@link java.lang.String} object.
     * @param fieldNames a {@link java.util.List} object.
     * @param sortFields a {@link java.lang.String} object.
     * @return a {@link com.digitalbridge.response.AggregationSearchResponse} object.
     * @throws com.digitalbridge.exception.DigitalBridgeException if any.
     */
    @Secured({ "ROLE_USER" })
    @RequestMapping(value = "/performBasicAggregationSearch", method = {
            RequestMethod.POST,
            RequestMethod.GET }, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public AggregationSearchResponse performBasicAggregationSearch(
            @RequestParam(required = false, defaultValue = "false", name = "refresh") boolean refresh,
            @RequestParam(required = false, defaultValue = "DESC", name = "sortOrder") String sortOrder,
            @RequestParam(required = true, name = "searchKeyword") String searchKeyword,
            @RequestParam(required = true, name = "fieldNames") List<String> fieldNames,
            @RequestParam(required = false, defaultValue = "createdDate", name = "sortFields") String... sortFields)
                    throws DigitalBridgeException {
        Direction direction;
        if (StringUtils.endsWithIgnoreCase(sortOrder, "ASC")) {
            direction = Direction.ASC;
        } else {
            direction = Direction.DESC;
        }

        if (null == sortFields || sortFields.length == 0) {
            sortFields = new String[] { "createdDate" };
        }
        return aggregationSearchService.performBasicAggregationSearch(searchKeyword,
                fieldNames, refresh, direction, sortFields);
    }

    /**
     * performIconicSearch.
     * <p>
     *
     * @param searchKeyword a {@link java.lang.String} object.
     * @param fieldName a {@link java.lang.String} object.
     * @param refresh a boolean.
     * @return a {@link org.springframework.http.ResponseEntity} object.
     */
    @Secured({ "ROLE_USER" })
    @RequestMapping(value = "/performIconicSearch", method = { RequestMethod.POST,
            RequestMethod.GET }, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Set<String>> performIconicSearch(
            @RequestParam(required = true, name = "searchKeyword") String searchKeyword,
            @RequestParam(required = true, name = "fieldName") String fieldName,
            @RequestParam(required = false, defaultValue = "false", name = "refresh") boolean refresh) {
        try {
            Set<String> response = aggregationSearchService
                    .performIconicSearch(searchKeyword, fieldName, refresh);
            return new ResponseEntity<Set<String>>(response, HttpStatus.OK);
        } catch (DigitalBridgeException e) {
            return new ResponseEntity<Set<String>>(HttpStatus.NOT_FOUND);
        }

    }

    /**
     * performAdvancedSearch.
     * <p>
     *
     * @throws com.digitalbridge.exception.DigitalBridgeException if any.
     * @param refresh a boolean.
     * @param aggregationSearchRequest a
     * {@link com.digitalbridge.request.AggregationSearchRequest} object.
     * @return a {@link com.digitalbridge.response.AggregationSearchResponse} object.
     */
    @Secured({ "ROLE_USER" })
    @RequestMapping(value = "/performAdvancedAggregationSearch", method = {
            RequestMethod.POST,
            RequestMethod.GET }, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public AggregationSearchResponse performAdvancedAggregationSearch(
            @RequestParam(required = false, defaultValue = "false", name = "refresh") boolean refresh,
            @RequestBody AggregationSearchRequest aggregationSearchRequest)
                    throws DigitalBridgeException {
        if (null == aggregationSearchRequest.getSortFields()) {
            aggregationSearchRequest.setSortFields(new String[] { "createdDate" });
        }

        Direction direction = null;
        if (StringUtils.equalsIgnoreCase(aggregationSearchRequest.getSortDirection(),
                "asc")) {
            direction = Direction.ASC;
        } else {
            direction = Direction.DESC;
        }

        return aggregationSearchService.performAdvancedAggregationSearch(refresh,
                aggregationSearchRequest, direction);
    }

}

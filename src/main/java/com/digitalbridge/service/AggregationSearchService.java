package com.digitalbridge.service;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Sort.Direction;

import com.digitalbridge.exception.DigitalBridgeException;
import com.digitalbridge.request.AggregationSearchRequest;
import com.digitalbridge.response.AggregationSearchResponse;

/**
 * <p>AggregationSearchService interface.</p>
 *
 * @author rajakolli
 * @version 1:0
 */
public interface AggregationSearchService
{

    /**
     * <p>performBasicAggregationSearch.</p>
     *
     * @param searchKeyword a {@link java.lang.String} object.
     * @param fieldNames a {@link java.util.List} object.
     * @param refresh a boolean.
     * @param direction a {@link org.springframework.data.domain.Sort.Direction} object.
     * @param sortFields a {@link java.lang.String} object.
     * @return a {@link com.digitalbridge.response.AggregationSearchResponse} object.
     * @throws com.digitalbridge.exception.DigitalBridgeException if any.
     */
    AggregationSearchResponse performBasicAggregationSearch(String searchKeyword, List<String> fieldNames,
            boolean refresh, Direction direction, String... sortFields) throws DigitalBridgeException;

    /**
     * <p>performIconicSearch.</p>
     *
     * @param searchKeyword a {@link java.lang.String} object.
     * @param fieldName a {@link java.lang.String} object.
     * @param refresh a boolean.
     * @return a {@link java.util.Set} object.
     * @throws com.digitalbridge.exception.DigitalBridgeException if any.
     */
    Set<String> performIconicSearch(String searchKeyword, String fieldName, boolean refresh) throws DigitalBridgeException;

    /**
     * <p>performAdvancedAggregationSearch.</p>
     *
     * @param refresh a boolean.
     * @param aggregationSearchRequest a {@link com.digitalbridge.request.AggregationSearchRequest} object.
     * @param direction a {@link org.springframework.data.domain.Sort.Direction} object.
     * @return a {@link com.digitalbridge.response.AggregationSearchResponse} object.
     * @throws com.digitalbridge.exception.DigitalBridgeException if any.
     */
    AggregationSearchResponse performAdvancedAggregationSearch(boolean refresh,
            AggregationSearchRequest aggregationSearchRequest, Direction direction) throws DigitalBridgeException;

}

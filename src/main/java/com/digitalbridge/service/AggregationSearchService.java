package com.digitalbridge.service;

import java.util.Set;

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
     * @param fieldNames an array of {@link java.lang.String} objects.
     * @param refresh a boolean.
     * @param sortField a {@link java.lang.String} object.
     * @param sortOrder a {@link java.lang.String} object.
     * @return a {@link com.digitalbridge.response.AggregationSearchResponse} object.
     * @throws com.digitalbridge.exception.DigitalBridgeException if any.
     */
    AggregationSearchResponse performBasicAggregationSearch(String searchKeyword, String[] fieldNames,
            boolean refresh, String sortField, String sortOrder) throws DigitalBridgeException;

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
     * @return a {@link com.digitalbridge.response.AggregationSearchResponse} object.
     * @throws com.digitalbridge.exception.DigitalBridgeException if any.
     */
    AggregationSearchResponse performAdvancedAggregationSearch(boolean refresh,
            AggregationSearchRequest aggregationSearchRequest) throws DigitalBridgeException;

}

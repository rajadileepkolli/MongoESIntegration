package com.digitalbridge.service;

import com.digitalbridge.exception.DigitalBridgeException;
import com.digitalbridge.request.SearchResponse;

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
     * @return a {@link com.digitalbridge.request.SearchResponse} object.
     * @throws com.digitalbridge.exception.DigitalBridgeException if any.
     */
    SearchResponse performBasicAggregationSearch(String searchKeyword, String[] fieldNames,
            boolean refresh, String sortField, String sortOrder) throws DigitalBridgeException;

}

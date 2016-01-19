package com.digitalbridge.response;

import java.util.HashMap;
import java.util.Map;

import org.springframework.data.domain.Page;

import lombok.Data;

/**
 * <p>
 * SearchResponse class.
 * </p>
 *
 * @author rajakolli
 * @version 1:0
 */
@Data
public class AggregationSearchResponse {

    private Map<String, Map<String, Long>> aggregations = new HashMap<>();
    private Page<?> searchResult;
    private long totalElements = 0;

}

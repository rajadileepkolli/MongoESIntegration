package com.digitalbridge.response;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.data.domain.Page;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * <p>
 * SearchResponse class.
 * </p>
 *
 * @author rajakolli
 * @version 1:0
 */
@XmlRootElement(name = "aggregationSearchResponse")
public class AggregationSearchResponse {

    private Map<String, Map<String, Long>> aggregations = new HashMap<>();
    private Page<?> searchResult;
    private long totalElements = 0;

    /**
     * <p>
     * Constructor for AggregationSearchResponse.
     * </p>
     */
    public AggregationSearchResponse() {
    }

    /**
     * <p>
     * Constructor for AggregationSearchResponse.
     * </p>
     *
     * @param aggregations a {@link java.util.Map} object.
     * @param searchResult a {@link org.springframework.data.domain.Page} object.
     * @param totalElements a long.
     */
    @JsonCreator
    public AggregationSearchResponse(
            @JsonProperty("aggregations") Map<String, Map<String, Long>> aggregations,
            @JsonProperty("searchResult") Page<?> searchResult,
            @JsonProperty("totalElements") long totalElements) {
        super();
        this.aggregations = aggregations;
        this.searchResult = searchResult;
        this.totalElements = totalElements;
    }

    /**
     * <p>
     * Getter for the field <code>aggregations</code>.
     * </p>
     *
     * @return a {@link java.util.Map} object.
     */
    @XmlElement
    public Map<String, Map<String, Long>> getAggregations() {
        return aggregations;
    }

    /**
     * <p>
     * Setter for the field <code>aggregations</code>.
     * </p>
     *
     * @param aggregations a {@link java.util.Map} object.
     */
    public void setAggregations(Map<String, Map<String, Long>> aggregations) {
        this.aggregations = aggregations;
    }

    /**
     * <p>
     * Getter for the field <code>searchResult</code>.
     * </p>
     *
     * @return a {@link org.springframework.data.domain.Page} object.
     */
    @XmlElement
    public Page<?> getSearchResult() {
        return searchResult;
    }

    /**
     * <p>
     * Setter for the field <code>searchResult</code>.
     * </p>
     *
     * @param searchResult a {@link org.springframework.data.domain.Page} object.
     */
    public void setSearchResult(Page<?> searchResult) {
        this.searchResult = searchResult;
    }

    /**
     * <p>
     * setCount.
     * </p>
     *
     * @param totalElements a long.
     */
    public void setCount(long totalElements) {
        this.totalElements = totalElements;
    }

    /**
     * <p>
     * Getter for the field <code>totalElements</code>.
     * </p>
     *
     * @return a long.
     */
    @XmlElement
    public long getTotalElements() {
        return totalElements;
    }

}

package com.digitalbridge.request;

import java.util.HashMap;
import java.util.Map;

import org.springframework.data.domain.Page;

/**
 * <p>SearchResponse class.</p>
 *
 * @author rajakolli
 * @version 1:0
 */
public class SearchResponse {
	
	Map<String, Map<String, Long>> aggregations = new HashMap<>();
	Page<?> searchResult;
	private long totalElements = 0;

	/**
	 * <p>Getter for the field <code>aggregations</code>.</p>
	 *
	 * @return a {@link java.util.Map} object.
	 */
	public Map<String, Map<String, Long>> getAggregations() {
		return aggregations;
	}

	/**
	 * <p>Setter for the field <code>aggregations</code>.</p>
	 *
	 * @param aggregations a {@link java.util.Map} object.
	 */
	public void setAggregations(Map<String, Map<String, Long>> aggregations) {
		this.aggregations = aggregations;
	}

	/**
	 * <p>Getter for the field <code>searchResult</code>.</p>
	 *
	 * @return a {@link org.springframework.data.domain.Page} object.
	 */
	public Page<?> getSearchResult() {
		return searchResult;
	}

	/**
	 * <p>Setter for the field <code>searchResult</code>.</p>
	 *
	 * @param searchResult a {@link org.springframework.data.domain.Page} object.
	 */
	public void setSearchResult(Page<?> searchResult) {
		this.searchResult = searchResult;
	}

	/**
	 * <p>setCount.</p>
	 *
	 * @param totalElements a long.
	 */
	public void setCount(long totalElements) {
		this.totalElements = totalElements;
	}

	/**
	 * <p>Getter for the field <code>totalElements</code>.</p>
	 *
	 * @return a long.
	 */
	public long getTotalElements() {
		return totalElements;
	}

}

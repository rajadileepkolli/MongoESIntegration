package com.digitalbridge.request;

import java.util.HashMap;
import java.util.Map;

import org.springframework.data.domain.Page;

public class SearchResponse {
	
	Map<String, Map<String, Long>> aggregations = new HashMap<>();
	Page<?> searchResult;
	private long totalElements = 0;

	public Map<String, Map<String, Long>> getAggregations() {
		return aggregations;
	}

	public void setAggregations(Map<String, Map<String, Long>> aggregations) {
		this.aggregations = aggregations;
	}

	public Page<?> getSearchResult() {
		return searchResult;
	}

	public void setSearchResult(Page<?> searchResult) {
		this.searchResult = searchResult;
	}

	public void setCount(long totalElements) {
		this.totalElements = totalElements;
	}

	public long getTotalElements() {
		return totalElements;
	}

	public void setTotalElements(long totalElements) {
		this.totalElements = totalElements;
	}
}

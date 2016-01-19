package com.digitalbridge.request;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

/**
 * AggregationTermRequest class.
 * <p>
 *
 * @author rajakolli
 * @version 1:0
 */
@Data
public class AggregationTermRequest {
    private List<SearchParameters> termsFilters = new ArrayList<>();

    private List<FacetDateRange> dateTermsFilters = new ArrayList<>();

    private String facetFieldId;
}

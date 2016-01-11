package com.digitalbridge.controller;

import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.search.sort.SortOrder;

import com.digitalbridge.request.AggregationSearchRequest;
import com.digitalbridge.request.LocationSearchRequest;
import com.digitalbridge.request.SearchParameters;

/**
 * <p>AggregationSearchControllerData class.</p>
 *
 * @author rajakolli
 * @version $Id: $Id
 */
public class AggregationSearchControllerData {

    /**
     * <p>getAggregationSearchRequest.</p>
     *
     * @return a {@link com.digitalbridge.request.AggregationSearchRequest} object.
     */
    public AggregationSearchRequest getAggregationSearchRequest() {

        AggregationSearchRequest aggregationSearchRequest = new AggregationSearchRequest();
        List<SearchParameters> searchParametersList = new ArrayList<>();
        SearchParameters searchParameters = new SearchParameters();
        searchParameters.setFieldId("cuisine");
        searchParameters.setSearchValue("indian");
        SearchParameters searchParameters2 = new SearchParameters();
        searchParameters2.setFieldId("borough");
        searchParameters2.setSearchValue("manhattan");
        searchParametersList.add(searchParameters);
        searchParametersList.add(searchParameters2);
        aggregationSearchRequest.setSearchParametersList(searchParametersList);
        aggregationSearchRequest.setSortFields(new String[]{"aName"});
        aggregationSearchRequest.setSortDirection(SortOrder.DESC.toString());
        aggregationSearchRequest.setLocationSearch(true);
        LocationSearchRequest locationSearchRequest = new LocationSearchRequest();
        locationSearchRequest.setLongitude(-74.0014541);
        locationSearchRequest.setLatitude(40.7408231);
        locationSearchRequest.setRadius(10);
        aggregationSearchRequest.setLocationSearchRequest(locationSearchRequest);
        return aggregationSearchRequest;
    
    }
    
    /**
     * <p>getAggregationSearchRequest1.</p>
     *
     * @return a {@link com.digitalbridge.request.AggregationSearchRequest} object.
     */
    public AggregationSearchRequest getAggregationSearchRequest1() {

        AggregationSearchRequest aggregationSearchRequest = new AggregationSearchRequest();
        List<SearchParameters> searchParametersList = new ArrayList<>();
        SearchParameters searchParameters = new SearchParameters();
        searchParameters.setFieldId("borough");
        searchParameters.setSearchValue("Brooklyn");
        searchParametersList.add(searchParameters);
        aggregationSearchRequest.setSearchParametersList(searchParametersList);
        aggregationSearchRequest.setSortFields(new String[]{"cuisine","borough"});
        aggregationSearchRequest.setSortDirection(SortOrder.DESC.toString());
        return aggregationSearchRequest;
    
    }

}

package com.digitalbridge.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.common.Base64;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder.Operator;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.range.date.DateRangeBuilder;
import org.elasticsearch.search.aggregations.bucket.terms.Terms.Order;
import org.elasticsearch.search.aggregations.bucket.terms.TermsBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.ISODateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.stereotype.Service;

import com.digitalbridge.domain.Address;
import com.digitalbridge.domain.AssetWrapper;
import com.digitalbridge.exception.DigitalBridgeException;
import com.digitalbridge.exception.DigitalBridgeExceptionBean;
import com.digitalbridge.mongodb.repository.AddressRepository;
import com.digitalbridge.mongodb.repository.AssetWrapperRepository;
import com.digitalbridge.request.AggregationSearchRequest;
import com.digitalbridge.request.FacetDateRange;
import com.digitalbridge.request.LocationSearchRequest;
import com.digitalbridge.request.SearchParameters;
import com.digitalbridge.response.AggregationSearchResponse;
import com.digitalbridge.service.AggregationSearchService;
import com.digitalbridge.util.Constants;
import com.digitalbridge.util.MapUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import io.searchbox.action.Action;
import io.searchbox.client.JestResult;
import io.searchbox.client.http.JestHttpClient;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import io.searchbox.core.search.aggregation.DateRangeAggregation;
import io.searchbox.core.search.aggregation.DateRangeAggregation.DateRange;
import io.searchbox.core.search.aggregation.TermsAggregation;
import io.searchbox.params.SearchType;

/**
 * <p>
 * AggregationSearchServiceImpl class.
 * </p>
 *
 * @author rajakolli
 * @version 1:0
 */
@Service
public class AggregationSearchServiceImpl implements AggregationSearchService {
    private static final Logger LOGGER = LoggerFactory
            .getLogger(AggregationSearchServiceImpl.class);
    private static final int SIZE = Integer.MAX_VALUE;
    private static final String INDEX_NAME = "digitalbridge";
    private static final String TYPE = "assetwrapper";

    @Autowired
    JestHttpClient jestClient;

    @Autowired
    AssetWrapperRepository assetWrapperRepository;
    @Autowired
    AddressRepository addressRepository;

    /** {@inheritDoc} */
    @Override
    public AggregationSearchResponse performBasicAggregationSearch(String searchKeyword,
            List<String> fieldNames, boolean refresh, Direction direction,
            String... sortFields) throws DigitalBridgeException {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders
                .multiMatchQuery(searchKeyword,
                        fieldNames.stream().toArray(String[]::new))
                .operator(Operator.OR));
        searchSourceBuilder.size(SIZE);

        return performAggregationSearch(refresh, searchSourceBuilder, direction,
                sortFields);
    }

    private AggregationSearchResponse performAggregationSearch(boolean refresh,
            SearchSourceBuilder searchSourceBuilder, Direction direction,
            String... sortFields) throws DigitalBridgeException {
        TermsBuilder cuisineTermsBuilder = AggregationBuilders.terms("MyCuisine")
                .field("cuisine").size(SIZE).order(Order.count(false));
        TermsBuilder boroughTermsBuilder = AggregationBuilders.terms("MyBorough")
                .field("borough").size(SIZE).order(Order.count(false));
        DateRangeBuilder dateRangeBuilder = AggregationBuilders.dateRange("MyDateRange")
                .field("lDate");
        addDateRange(dateRangeBuilder);

        searchSourceBuilder.aggregation(cuisineTermsBuilder);
        searchSourceBuilder.aggregation(boroughTermsBuilder);
        searchSourceBuilder.aggregation(dateRangeBuilder);

        LOGGER.debug("Query : {}", searchSourceBuilder.toString());
        Search search = new Search.Builder(searchSourceBuilder.toString())
                .addIndex(INDEX_NAME).addType(TYPE).setHeader(getHeader())
                .refresh(refresh).setSearchType(SearchType.DFS_QUERY_THEN_FETCH).build();

        SearchResult searchResult = (SearchResult) handleResult(search);
        AggregationSearchResponse response = new AggregationSearchResponse();
        Page<AssetWrapper> assetDetails = null;
        List<String> assetIds = Collections.emptyList();
        if (searchResult != null && searchResult.isSucceeded()) {
            /* Get AssetIds */
            JsonArray hits = searchResult.getJsonObject().getAsJsonObject("hits")
                    .getAsJsonArray("hits");
            assetIds = new ArrayList<>();
            for (JsonElement jsonElement : hits) {
                assetIds.add(jsonElement.getAsJsonObject().get("_id").getAsString());
            }

            if (assetIds != null && !assetIds.isEmpty()) {
                assetDetails = findAssetsDetailsByAssetIds(assetIds, direction,
                        sortFields);

                if (assetDetails.getTotalElements() > 0) {
                    response.setSearchResult(assetDetails);
                    response.setAggregations(extractTermFiltersCount(searchResult));
                    response.setCount(assetDetails.getTotalElements());
                }
            }
        }

        return response;
    }

    private Map<String, Map<String, Long>> extractTermFiltersCount(
            SearchResult searchResult) {
        Map<String, Map<String, Long>> resultMap = new LinkedHashMap<>(Constants.THREE);
        TermsAggregation cuisineTerm = searchResult.getAggregations()
                .getTermsAggregation("MyCuisine");
        Collection<io.searchbox.core.search.aggregation.TermsAggregation.Entry> cusineBuckets = cuisineTerm
                .getBuckets();
        Map<String, Long> cuisineMap = new LinkedHashMap<>(cusineBuckets.size());
        for (io.searchbox.core.search.aggregation.TermsAggregation.Entry bucket : cusineBuckets) {
            Long count = bucket.getCount();
            if (count > 0) {
                cuisineMap.put(bucket.getKey(), bucket.getCount());
            }
        }

        TermsAggregation boroughTerm = searchResult.getAggregations()
                .getTermsAggregation("MyBorough");
        Collection<io.searchbox.core.search.aggregation.TermsAggregation.Entry> boroughBuckets = boroughTerm
                .getBuckets();
        Map<String, Long> boroughMap = new LinkedHashMap<>(boroughBuckets.size());
        for (io.searchbox.core.search.aggregation.TermsAggregation.Entry bucket : boroughBuckets) {
            long count = bucket.getCount();
            if (count > 0) {
                boroughMap.put(bucket.getKey(), bucket.getCount());
            }
        }

        DateRangeAggregation dateRangeTerm = searchResult.getAggregations()
                .getDateRangeAggregation("MyDateRange");
        List<DateRange> dateRangeBuckets = dateRangeTerm.getBuckets();
        Map<String, Long> dateRangeMap = new LinkedHashMap<>(dateRangeBuckets.size());
        for (DateRange dateRange : dateRangeBuckets) {
            long count = dateRange.getCount();
            if (count > 0) {
                FacetDateRange facetDateRange = new FacetDateRange();
                if (StringUtils.isNotEmpty(dateRange.getFromAsString())) {
                    facetDateRange.setStartDate(DateTime.parse(
                            dateRange.getFromAsString(),
                            ISODateTimeFormat.dateTimeParser().withOffsetParsed()));
                }
                if (StringUtils.isNotEmpty(dateRange.getToAsString())) {
                    facetDateRange.setEndDate(DateTime.parse(dateRange.getToAsString(),
                            ISODateTimeFormat.dateTimeParser().withOffsetParsed()));
                }
                dateRangeMap.put(facetDateRange.toString(), dateRange.getCount());
            }
        }

        if (MapUtils.isNotEmpty(cuisineMap)) {
            resultMap.put(cuisineTerm.getName(), cuisineMap);
        }
        if (MapUtils.isNotEmpty(boroughMap)) {
            resultMap.put(boroughTerm.getName(), boroughMap);
        }
        if (MapUtils.isNotEmpty(dateRangeMap)) {
            resultMap.put(dateRangeTerm.getName(), dateRangeMap);
        }
        return resultMap;
    }

    private Page<AssetWrapper> findAssetsDetailsByAssetIds(List<String> assetIds,
            Direction direction, String... sortField) {
        return assetWrapperRepository.findByIdIn(assetIds, new PageRequest(Constants.ZERO,
                Constants.PAGESIZE, direction, sortField));
    }

    /**
     * @param dateRangeBuilder
     */
    private void addDateRange(DateRangeBuilder dateRangeBuilder) {
        DateTime startMonthDate = new DateTime(DateTimeZone.UTC)
                .withDayOfMonth(Constants.ONE).withTimeAtStartOfDay();
        dateRangeBuilder.addUnboundedTo(startMonthDate.minusMonths(Constants.TWELVE));
        for (int i = Constants.TWELVE; i > Constants.ZERO; i--) {
            dateRangeBuilder.addRange(startMonthDate.minusMonths(i),
                    startMonthDate.minusMonths(i - 1).minusMillis(Constants.ONE));
        }
        dateRangeBuilder.addUnboundedFrom(startMonthDate);
    }

    private Map<String, Object> getHeader() {
        return Collections.singletonMap("Authorization",
                "Basic " + Base64.encodeBytes("admin:admin_pw".getBytes()));
    }

    /**
     * handleResult.
     * <p>
     *
     * @param action a {@link io.searchbox.action.Action} object.
     * @return a {@link io.searchbox.client.JestResult} object.
     * @throws com.digitalbridge.exception.DigitalBridgeException if any.
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    protected JestResult handleResult(Action action) throws DigitalBridgeException {
        JestResult jestResult = null;
        try {
            jestResult = jestClient.execute(action);
            if (!jestResult.isSucceeded()) {
                if (jestResult.getResponseCode() == Constants.CLUSTERBLOCKEXCEPTIONCODE) {
                    LOGGER.error(jestResult.getErrorMessage());
                    DigitalBridgeExceptionBean bean = new DigitalBridgeExceptionBean();
                    bean.setFaultCode("1011");
                    bean.setFaultString("ClusterBlockException");
                    throw new DigitalBridgeException(bean);
                } else if (jestResult.getResponseCode() == Constants.INDEXMISSINGCODE) {
                    LOGGER.error(jestResult.getErrorMessage());
                    DigitalBridgeExceptionBean bean = new DigitalBridgeExceptionBean();
                    bean.setFaultCode("1012");
                    bean.setFaultString("IndexMissingException");
                    throw new DigitalBridgeException(bean);
                } else {
                    LOGGER.error(jestResult.getJsonString());
                    LOGGER.error("Error :{}", jestResult.getErrorMessage());
                }
            }
        } catch (IOException e) {
            LOGGER.error(
                    "IOException occured while attempting to perform ElasticSearch Operation : {}",
                    e.getMessage(), e);
        }
        return jestResult;
    }

    /** {@inheritDoc} */
    @Override
    public Set<String> performIconicSearch(String searchKeyword, String fieldName,
            boolean refresh) throws DigitalBridgeException {

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.size(SIZE);
        searchSourceBuilder.query(QueryBuilders.boolQuery()
                .must(QueryBuilders.matchQuery(fieldName, searchKeyword)));
        Search search = new Search.Builder(searchSourceBuilder.toString())
                .addIndex(INDEX_NAME).addType(TYPE).setHeader(getHeader())
                .refresh(refresh).build();
        Set<String> returnValues = Collections.emptySet();
        SearchResult searchResult = (SearchResult) handleResult(search);
        JsonArray hits = searchResult.getJsonObject().getAsJsonObject("hits")
                .getAsJsonArray("hits");
        returnValues = new HashSet<>();
        for (JsonElement jsonElement : hits) {
            returnValues.add(jsonElement.getAsJsonObject().get("_source")
                    .getAsJsonObject().get(fieldName).getAsString());
        }
        return returnValues;
    }

    /** {@inheritDoc} */
    @Override
    public AggregationSearchResponse performAdvancedAggregationSearch(boolean refresh,
            AggregationSearchRequest aggregationSearchRequest, Direction direction)
                    throws DigitalBridgeException {

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder queryFilters = new BoolQueryBuilder();
        for (SearchParameters searchParameter : aggregationSearchRequest
                .getSearchParametersList()) {
            queryFilters.must(QueryBuilders.termsQuery(searchParameter.getFieldId(),
                    searchParameter.getSearchValue()));
        }
        if (null != aggregationSearchRequest.getAssetIds() && !aggregationSearchRequest.getAssetIds().isEmpty()) {
            queryFilters.must(QueryBuilders.termsQuery("_id", aggregationSearchRequest.getAssetIds()));
        }
        BoolQueryBuilder filterQuery = new BoolQueryBuilder();
        filterQuery.must(queryFilters);
        searchSourceBuilder.query(filterQuery);
        searchSourceBuilder.size(SIZE);
        return performAggregationSearch(refresh, searchSourceBuilder, direction,
                aggregationSearchRequest.getSortFields());

    }

    /** {@inheritDoc} */
    @Override
    public List<String> performLocationSearch(LocationSearchRequest locationSearchRequest) {
        Distance distance = new Distance(locationSearchRequest.getRadius(), Metrics.MILES);
        Point point = new Point(locationSearchRequest.getLatitude(), locationSearchRequest.getLongitude());
        int loopvalue = Constants.ZERO;
        Page<Address> result = null;
        List<String> addressIds = new ArrayList<>();
        do {
            result = addressRepository.findByLocationNear(point, distance, new PageRequest(loopvalue, 1000));
            for (Address address : result) {
                addressIds.add(address.getId());
            }
            loopvalue++;
        }
        while (result.hasNext());
        List<String> assetIds = new ArrayList<>();
        try {
            if (null != addressIds && !addressIds.isEmpty()) {
                Page<AssetWrapper> assetWrapperResult = null;
                int i = Constants.ZERO;
                do {
                    assetWrapperResult = assetWrapperRepository
                            .findByAddressIdIn(addressIds, new PageRequest(i, 10000));
                    for (AssetWrapper assetWrapper : assetWrapperResult) {
                        assetIds.add(assetWrapper.getId());
                    }
                    i++;
                }
                while (assetWrapperResult.hasNext());
            }
        } catch (Exception e) {
            LOGGER.error("Exception :{}", e.getMessage(), e);
        }
        return assetIds;
    }

}

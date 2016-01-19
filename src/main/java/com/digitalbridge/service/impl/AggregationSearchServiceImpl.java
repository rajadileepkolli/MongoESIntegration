package com.digitalbridge.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.ClearScrollResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.Base64;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.GeoDistanceQueryBuilder;
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
import org.springframework.stereotype.Service;

import com.digitalbridge.domain.AssetWrapper;
import com.digitalbridge.exception.DigitalBridgeException;
import com.digitalbridge.exception.DigitalBridgeExceptionBean;
import com.digitalbridge.mongodb.repository.AssetWrapperRepository;
import com.digitalbridge.request.AggregationSearchRequest;
import com.digitalbridge.request.FacetDateRange;
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
import io.searchbox.core.SearchScroll;
import io.searchbox.core.search.aggregation.DateRangeAggregation;
import io.searchbox.core.search.aggregation.DateRangeAggregation.DateRange;
import io.searchbox.core.search.aggregation.TermsAggregation;
import io.searchbox.core.search.aggregation.TermsAggregation.Entry;
import io.searchbox.params.Parameters;
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
    private static final int PAGE_SIZE = 10000;
    /** Constant <code>SCROLL_ID="_scroll_id"</code> */
    public static final String SCROLL_ID = "_scroll_id";

    @Autowired
    JestHttpClient jestClient;
    
    @Autowired
    Client elasticSearchClient;

    @Autowired
    AssetWrapperRepository assetWrapperRepository;

    /** {@inheritDoc} */
    @Override
    public AggregationSearchResponse performBasicAggregationSearch(String searchKeyword,
            List<String> fieldNames, boolean refresh, Direction direction,
            String... sortFields) throws DigitalBridgeException {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder
                .query(QueryBuilders
                        .multiMatchQuery(searchKeyword,
                                fieldNames.parallelStream()
                                        .toArray(size -> new String[size]))
                        .operator(Operator.OR));
        searchSourceBuilder.noFields();
        searchSourceBuilder.size(SIZE);

        return performAggregationSearch(refresh, searchSourceBuilder, direction,
                sortFields);
    }

    private AggregationSearchResponse performAggregationSearch(boolean refresh,
            SearchSourceBuilder searchSourceBuilder, Direction direction,
            String... sortFields) throws DigitalBridgeException {
        TermsBuilder cuisineTermsBuilder = AggregationBuilders.terms("MyCuisine")
                .field("cuisine").size(PAGE_SIZE).order(Order.count(false));
        TermsBuilder boroughTermsBuilder = AggregationBuilders.terms("MyBorough")
                .field("borough").size(PAGE_SIZE).order(Order.count(false));
        DateRangeBuilder dateRangeBuilder = AggregationBuilders.dateRange("MyDateRange")
                .field("lDate");
        addDateRange(dateRangeBuilder);

        searchSourceBuilder.aggregation(cuisineTermsBuilder);
        searchSourceBuilder.aggregation(boroughTermsBuilder);
        searchSourceBuilder.aggregation(dateRangeBuilder);

        LOGGER.debug("Query : {}", searchSourceBuilder.toString());
        Search search = new Search.Builder(searchSourceBuilder.toString())
                .addIndex(INDEX_NAME).addType(TYPE)
                .setParameter(Parameters.SEARCH_TYPE, SearchType.SCAN)
                .setParameter(Parameters.SIZE, PAGE_SIZE)
                .setParameter(Parameters.SCROLL, "5m")
                .setHeader(getHeader())
                .refresh(refresh).setSearchType(SearchType.QUERY_THEN_FETCH).build();

        JestResult searchResult = (SearchResult) handleResult(search);
        AggregationSearchResponse response = new AggregationSearchResponse();
        Page<AssetWrapper> assetDetails = null;
        List<String> assetIds = new ArrayList<>();
        
        String scrollId = searchResult.getJsonObject().get(SCROLL_ID).getAsString();
        int pageNumber = 1;
        while (true) {
            if (searchResult != null && searchResult.isSucceeded()) {
                JsonArray hits = searchResult.getJsonObject().getAsJsonObject("hits")
                        .getAsJsonArray("hits");
                if (null != hits && hits.size() == 0) {
                    break;
                }

                /* Get AssetIds */
                hits.forEach(jsonElement -> assetIds
                        .add(jsonElement.getAsJsonObject().get("_id").getAsString()));

                if (assetIds != null && !assetIds.isEmpty()) {
                    assetDetails = findAssetsDetailsByAssetIds(assetIds, direction,
                            sortFields);

                    if (assetDetails.getTotalElements() > 0) {
                        response.setSearchResult(assetDetails);
                        response.setAggregations(
                                extractTermFiltersCount((SearchResult) searchResult));
                        response.setCount(assetDetails.getTotalElements());
                    }
                }
                SearchScroll scroll = new SearchScroll.Builder(scrollId, "5m")
                        .setHeader(getHeader()).build();
                searchResult = handleResult(scroll);
                scrollId = searchResult.getJsonObject().get(SCROLL_ID).getAsString();
                LOGGER.info("finished scrolling page # " + pageNumber++ + " which had "
                        + hits.size() + " results.");
            }
        }
        
        clearScroll(scrollId);

        return response;
    }

    private Map<String, Map<String, Long>> extractTermFiltersCount(
            SearchResult searchResult) {
        Map<String, Map<String, Long>> resultMap = new LinkedHashMap<>(Constants.THREE);
        
        List<Entry> cusineBuckets = searchResult.getAggregations()
                .getTermsAggregation("MyCuisine").getBuckets();
        Map<String, Long> cuisineMap = new LinkedHashMap<>(cusineBuckets.size());
        cusineBuckets.stream().filter(hasBucket -> hasBucket.getCount() > 0)
                .forEach(bucket -> cuisineMap.put(bucket.getKey(), bucket.getCount()));

        TermsAggregation boroughTerm = searchResult.getAggregations()
                .getTermsAggregation("MyBorough");
        Collection<io.searchbox.core.search.aggregation.TermsAggregation.Entry> boroughBuckets = boroughTerm
                .getBuckets();
        Map<String, Long> boroughMap = new LinkedHashMap<>(boroughBuckets.size());
        boroughBuckets.stream().filter(bucket -> bucket.getCount() > 0)
                .forEach(bucket -> boroughMap.put(bucket.getKey(), bucket.getCount()));

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
            resultMap.put("MyCuisine", cuisineMap);
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
                .addIndex(INDEX_NAME).addType(TYPE)
                .setParameter(Parameters.SEARCH_TYPE, SearchType.SCAN)
                .setParameter(Parameters.SIZE, PAGE_SIZE)
                .setParameter(Parameters.SCROLL, "1m").setHeader(getHeader())
                .refresh(refresh).build();
        Set<String> returnValues = new HashSet<>();
        LOGGER.debug(search.getData(null));
        JestResult searchResult = handleResult(search);
        String scrollId = searchResult.getJsonObject().get(SCROLL_ID).getAsString();
        int pageNumber = 1;
        while (true) {
            SearchScroll scroll = new SearchScroll.Builder(scrollId, "1m")
                    .setHeader(getHeader()).build();
            searchResult = handleResult(scroll);
            JsonArray hits = searchResult.getJsonObject().getAsJsonObject("hits")
                    .getAsJsonArray("hits");
            if (hits.size() == 0) {
                break;
            }
            for (JsonElement jsonElement : hits) {
                returnValues.add(jsonElement.getAsJsonObject().get("_source")
                        .getAsJsonObject().get(fieldName).getAsString());
            }
            scrollId = searchResult.getJsonObject().get(SCROLL_ID).getAsString();
            LOGGER.info("finished scrolling page # " + pageNumber++ + " which had "
                    + hits.size() + " results.");

        }
        
        clearScroll(scrollId);

        return returnValues;
    }
    
    private void clearScroll(String... scrollIds) {
        ClearScrollResponse clearResponse = elasticSearchClient.prepareClearScroll().setScrollIds(Arrays.asList(scrollIds)).get();
        clearResponse.isSucceeded();
    }

    /** {@inheritDoc} */
    @Override
    public AggregationSearchResponse performAdvancedAggregationSearch(boolean refresh,
            AggregationSearchRequest aggregationSearchRequest, Direction direction)
                    throws DigitalBridgeException {

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.noFields();
        BoolQueryBuilder queryFilters = new BoolQueryBuilder();
        for (SearchParameters searchParameter : aggregationSearchRequest
                .getSearchParametersList()) {
            queryFilters.must(QueryBuilders.termsQuery(searchParameter.getFieldId(),
                    searchParameter.getSearchValue()));
        }
        if (aggregationSearchRequest.isLocationSearch()) {
            GeoDistanceQueryBuilder geoDistanceQueryBuilder = new GeoDistanceQueryBuilder(
                    "location.coordinates");
            geoDistanceQueryBuilder.point(
                    aggregationSearchRequest.getLocationSearchRequest().getLatitude(),
                    aggregationSearchRequest.getLocationSearchRequest().getLongitude());
            geoDistanceQueryBuilder.distance(
                    aggregationSearchRequest.getLocationSearchRequest().getRadius(),
                    DistanceUnit.MILES);
            queryFilters.must(geoDistanceQueryBuilder);
        }
        BoolQueryBuilder filterQuery = new BoolQueryBuilder();
        filterQuery.must(queryFilters);
        searchSourceBuilder.query(filterQuery);
        searchSourceBuilder.size(SIZE);
        return performAggregationSearch(refresh, searchSourceBuilder, direction,
                aggregationSearchRequest.getSortFields());

    }

}

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
import org.elasticsearch.index.query.MatchQueryBuilder.Operator;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.range.date.DateRangeBuilder;
import org.elasticsearch.search.aggregations.bucket.terms.Terms.Order;
import org.elasticsearch.search.aggregations.bucket.terms.TermsBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
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
import com.digitalbridge.request.FacetDateRange;
import com.digitalbridge.request.SearchResponse;
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
 * <p>AggregationSearchServiceImpl class.</p>
 *
 * @author rajakolli
 * @version 1:0
 */
@Service
public class AggregationSearchServiceImpl implements AggregationSearchService
{
    private static final Logger LOGGER = LoggerFactory
            .getLogger(AggregationSearchServiceImpl.class);
    private static final int SIZE = Integer.MAX_VALUE;
    private static final String INDEX_NAME = "digitalbridge";
    private static final String TYPE = "assetwrapper";

    @Autowired
    JestHttpClient jestClient;

    @Autowired
    AssetWrapperRepository assetWrapperRepository;

    /** {@inheritDoc} */
    @Override
    public SearchResponse performBasicAggregationSearch(String searchKeyword,
            String[] fieldNames, boolean refresh, String sortField, String sortOrder)
                    throws DigitalBridgeException
    {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.multiMatchQuery(searchKeyword, fieldNames)
                .operator(Operator.OR));
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
        searchSourceBuilder.size(SIZE);
        if (StringUtils.isNotBlank(sortField))
        {
            FieldSortBuilder sort = new FieldSortBuilder(sortField);
            if (sortOrder.equalsIgnoreCase("DESC"))
            {
                sort.order(SortOrder.DESC);
            }
            searchSourceBuilder.sort(sort);
        }

        LOGGER.debug("Query : {}", searchSourceBuilder.toString());
        Search search = new Search.Builder(searchSourceBuilder.toString()).addIndex(INDEX_NAME)
                .addType(TYPE).setHeader(getHeader()).refresh(refresh)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH).build();

        SearchResult searchResult = (SearchResult) handleResult(search);
        SearchResponse response = new SearchResponse();
        Page<AssetWrapper> res = null;
        Map<String, Map<String, Long>> resultMap = new LinkedHashMap<>(Constants.THREE);
        List<String> assetIds = Collections.emptyList();
        if (searchResult != null && searchResult.isSucceeded())
        {
            /* Get AssetIds */
            JsonArray hits = searchResult.getJsonObject().getAsJsonObject("hits")
                    .getAsJsonArray("hits");
            assetIds = new ArrayList<>();
            for (JsonElement jsonElement : hits)
            {
                assetIds.add(jsonElement.getAsJsonObject().get("_id").getAsString());
            }

            if (assetIds != null && !assetIds.isEmpty())
            {
                res = assetWrapperRepository.findByIdIn(assetIds, new PageRequest(
                        Constants.ZERO, Constants.PAGESIZE, Direction.ASC, "aName"));

                if (res.getTotalElements() > 0)
                {
                    TermsAggregation cuisineTerm = searchResult.getAggregations()
                            .getTermsAggregation("MyCuisine");
                    Collection<io.searchbox.core.search.aggregation.TermsAggregation.Entry> cusineBuckets = cuisineTerm
                            .getBuckets();
                    Map<String, Long> cuisineMap = new LinkedHashMap<>(cusineBuckets.size());
                    for (io.searchbox.core.search.aggregation.TermsAggregation.Entry bucket : cusineBuckets)
                    {
                        Long count = bucket.getCount();
                        if (count > 0)
                        {
                            cuisineMap.put(bucket.getKey(), bucket.getCount());
                        }
                    }

                    TermsAggregation boroughTerm = searchResult.getAggregations()
                            .getTermsAggregation("MyBorough");
                    Collection<io.searchbox.core.search.aggregation.TermsAggregation.Entry> boroughBuckets = boroughTerm
                            .getBuckets();
                    Map<String, Long> boroughMap = new LinkedHashMap<>(boroughBuckets.size());
                    for (io.searchbox.core.search.aggregation.TermsAggregation.Entry bucket : boroughBuckets)
                    {
                        long count = bucket.getCount();
                        if (count > 0)
                        {
                            boroughMap.put(bucket.getKey(), bucket.getCount());
                        }
                    }

                    DateRangeAggregation dateRangeTerm = searchResult.getAggregations()
                            .getDateRangeAggregation("MyDateRange");
                    List<DateRange> dateRangeBuckets = dateRangeTerm.getBuckets();
                    Map<String, Long> dateRangeMap = new LinkedHashMap<>(
                            dateRangeBuckets.size());
                    for (DateRange dateRange : dateRangeBuckets)
                    {
                        long count = dateRange.getCount();
                        if (count > 0)
                        {
                            FacetDateRange facetDateRange = new FacetDateRange();
                            if (StringUtils.isNotEmpty(dateRange.getFromAsString()))
                            {
                                facetDateRange.setStartDate(DateTime
                                        .parse(dateRange.getFromAsString(), ISODateTimeFormat
                                                .dateTimeParser().withOffsetParsed()));
                            }
                            if (StringUtils.isNotEmpty(dateRange.getToAsString()))
                            {
                                facetDateRange.setEndDate(DateTime
                                        .parse(dateRange.getToAsString(), ISODateTimeFormat
                                                .dateTimeParser().withOffsetParsed()));
                            }
                            dateRangeMap.put(facetDateRange.toString(), dateRange.getCount());
                        }
                    }

                    if (MapUtils.isNotEmpty(cuisineMap))
                    {
                        resultMap.put(cuisineTerm.getName(), cuisineMap);
                    }
                    if (MapUtils.isNotEmpty(boroughMap))
                    {
                        resultMap.put(boroughTerm.getName(), boroughMap);
                    }
                    if (MapUtils.isNotEmpty(dateRangeMap))
                    {
                        resultMap.put(dateRangeTerm.getName(), dateRangeMap);
                    }
                }
            }
        }

        response.setSearchResult(res);
        response.setAggregations(resultMap);
        response.setCount(res.getTotalElements());

        return response;

    }

    /**
     * @param dateRangeBuilder
     */
    private void addDateRange(DateRangeBuilder dateRangeBuilder)
    {
        DateTime startMonthDate = new DateTime(DateTimeZone.UTC).withDayOfMonth(Constants.ONE)
                .withTimeAtStartOfDay();
        dateRangeBuilder.addUnboundedTo(startMonthDate.minusMonths(Constants.TWELVE));
        for (int i = Constants.TWELVE; i > Constants.ZERO; i--)
        {
            dateRangeBuilder.addRange(startMonthDate.minusMonths(i),
                    startMonthDate.minusMonths(i - 1).minusMillis(Constants.ONE));
        }
        dateRangeBuilder.addUnboundedFrom(startMonthDate);
    }

    private Map<String, Object> getHeader()
    {
        return Collections.singletonMap("Authorization",
                "Basic " + Base64.encodeBytes("admin:admin_pw".getBytes()));
    }

    /**
     * handleResult.
     * <p>
     *
     * @param action
     *            a {@link io.searchbox.action.Action} object.
     * @return a {@link io.searchbox.client.JestResult} object.
     * @throws com.digitalbridge.exception.DigitalBridgeException
     *             if any.
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    protected JestResult handleResult(Action action) throws DigitalBridgeException
    {
        JestResult jestResult = null;
        try
        {
            jestResult = jestClient.execute(action);
            if (!jestResult.isSucceeded())
            {
                if (jestResult.getResponseCode() == Constants.CLUSTERBLOCKEXCEPTIONCODE)
                {
                    LOGGER.error(jestResult.getErrorMessage());
                    DigitalBridgeExceptionBean bean = new DigitalBridgeExceptionBean();
                    bean.setFaultCode("1011");
                    bean.setFaultString("ClusterBlockException");
                    throw new DigitalBridgeException(bean);
                }
                else if (jestResult.getResponseCode() == Constants.INDEXMISSINGCODE)
                {
                    LOGGER.error(jestResult.getErrorMessage());
                    DigitalBridgeExceptionBean bean = new DigitalBridgeExceptionBean();
                    bean.setFaultCode("1012");
                    bean.setFaultString("IndexMissingException");
                    throw new DigitalBridgeException(bean);
                }
                else
                {
                    LOGGER.error(jestResult.getJsonString());
                    LOGGER.error("Error :{}", jestResult.getErrorMessage());
                }
            }
        }
        catch (IOException e)
        {
            LOGGER.error(
                    "IOException occured while attempting to perform ElasticSearch Operation : {}",
                    e.getMessage(), e);
        }
        return jestResult;
    }

    /** {@inheritDoc} */
    @Override
    public Set<String> performIconicSearch(String searchKeyword, String fieldName,
            boolean refresh) throws DigitalBridgeException
    {

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.size(SIZE);
        searchSourceBuilder.query(QueryBuilders.boolQuery()
                .must(QueryBuilders.matchQuery(fieldName, searchKeyword)));
        Search search = new Search.Builder(searchSourceBuilder.toString()).addIndex(INDEX_NAME)
                .addType(TYPE).setHeader(getHeader()).refresh(refresh).build();
        Set<String> returnValues = Collections.emptySet();
        SearchResult searchResult = (SearchResult) handleResult(search);
        JsonArray hits = searchResult.getJsonObject().getAsJsonObject("hits")
                .getAsJsonArray("hits");
        returnValues = new HashSet<>();
        for (JsonElement jsonElement : hits)
        {
            returnValues.add(jsonElement.getAsJsonObject().get("_source").getAsJsonObject()
                    .get(fieldName).getAsString());
        }
        return returnValues;
    }

}

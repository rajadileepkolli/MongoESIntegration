package com.digitalbridge.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.digitalbridge.exception.DigitalBridgeException;
import com.digitalbridge.request.AggregationSearchRequest;
import com.digitalbridge.util.Constants;

@RestController
/**
 * <p>LoadTestController class.</p>
 *
 * @author rajakolli
 * @version $Id: $Id
 */
@RequestMapping(value = Constants.BASE_MAIN_URL)
public class LoadTestController {

    @Autowired
    AggregationSearchController aggregationSearchController;

    /**
     * <p>performLoadTest.</p>
     *
     * @param maxHits a int.
     * @return a {@link java.lang.String} object.
     */
    @Secured({ "ROLE_ADMIN" })
    @RequestMapping(value = "/performLoadTest", method = RequestMethod.GET)
    public String performLoadTest(
            @RequestParam(required = false, defaultValue = "10", name = "maxHits") int maxHits) {
        AggregationSearchControllerData searchControllerData = new AggregationSearchControllerData();
        AggregationSearchRequest aggregationSearchRequest = searchControllerData
                .getAggregationSearchRequest();
        AggregationSearchRequest aggregationSearchRequest1 = searchControllerData
                .getAggregationSearchRequest1();
        String searchKeyword = "garden";
        List<String> fieldNames = new ArrayList<>();
        fieldNames.add("aName");
        fieldNames.add("cuisine");
        int i = 0;
        do {
            try {

                aggregationSearchController.performAdvancedAggregationSearch(true,
                        i % 2 == 0 ? aggregationSearchRequest
                                : aggregationSearchRequest1);
                aggregationSearchController.performBasicAggregationSearch(true, null,
                        searchKeyword, fieldNames);
                aggregationSearchController.performIconicSearch(searchKeyword,
                        i % 2 == 0 ? fieldNames.get(0) : fieldNames.get(1), true);
                i++;
            }
            catch (DigitalBridgeException e) {

            }
        }
        while (i < maxHits);
        return "Completed loadTesting";
    }
    
}

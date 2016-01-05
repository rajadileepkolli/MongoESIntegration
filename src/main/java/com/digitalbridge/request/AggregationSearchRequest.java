package com.digitalbridge.request;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import lombok.Data;

/**
 * <p>
 * AggregationSearchRequest class.
 * </p>
 *
 * @author rajakolli
 * @version 1:0
 */
@XmlRootElement(name = "AggregationSearchRequest")
@Data
public class AggregationSearchRequest {
    private List<SearchParameters> searchParametersList = new ArrayList<>();
    private List<String> assetIds = new ArrayList<>();
    private String[] sortFields;
    private String sortDirection;
    private boolean locationSearch;
    private LocationSearchRequest locationSearchRequest;

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

}

package com.digitalbridge.request;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * <p>
 * AggregationSearchRequest class.
 * </p>
 *
 * @author rajakolli
 * @version 1:0
 */
@XmlRootElement(name = "AggregationSearchRequest")
public class AggregationSearchRequest {
    private List<SearchParameters> searchParametersList = new ArrayList<>();
    private List<String> assetIds = new ArrayList<>();
    private String[] sortFields;
    private String sortDirection;

    /**
     * <p>
     * Getter for the field <code>searchParametersList</code>.
     * </p>
     *
     * @return a {@link java.util.List} object.
     */
    public List<SearchParameters> getSearchParametersList() {
        return searchParametersList;
    }

    /**
     * <p>
     * Setter for the field <code>searchParametersList</code>.
     * </p>
     *
     * @param searchParametersList a {@link java.util.List} object.
     */
    public void setSearchParametersList(List<SearchParameters> searchParametersList) {
        this.searchParametersList = searchParametersList;
    }

    /**
     * <p>
     * Getter for the field <code>assetIds</code>.
     * </p>
     *
     * @return a {@link java.util.List} object.
     */
    public List<String> getAssetIds() {
        return assetIds;
    }

    /**
     * <p>
     * Setter for the field <code>assetIds</code>.
     * </p>
     *
     * @param assetIds a {@link java.util.List} object.
     */
    public void setAssetIds(List<String> assetIds) {
        this.assetIds = assetIds;
    }

    /**
     * <p>
     * Getter for the field <code>sortFields</code>.
     * </p>
     *
     * @return an array of {@link java.lang.String} objects.
     */
    public String[] getSortFields() {
        return sortFields;
    }

    /**
     * <p>
     * Setter for the field <code>sortFields</code>.
     * </p>
     *
     * @param sortFields an array of {@link java.lang.String} objects.
     */
    public void setSortFields(String[] sortFields) {
        this.sortFields = sortFields;
    }

    /**
     * <p>
     * Getter for the field <code>sortDirection</code>.
     * </p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getSortDirection() {
        return sortDirection;
    }

    /**
     * <p>
     * Setter for the field <code>sortDirection</code>.
     * </p>
     *
     * @param sortDirection a {@link java.lang.String} object.
     */
    public void setSortDirection(String sortDirection) {
        this.sortDirection = sortDirection;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

}

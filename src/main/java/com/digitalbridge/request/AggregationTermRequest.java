package com.digitalbridge.request;

import java.util.ArrayList;
import java.util.List;

/**
 * AggregationTermRequest class.
 * <p>
 *
 * @author rajakolli
 * @version 1:0
 */
public class AggregationTermRequest {
	List<SearchParameters> termsFilters = new ArrayList<>();

	List<FacetDateRange> dateTermsFilters = new ArrayList<>();

	String facetFieldId;

	/**
	 * <p>
	 * Getter for the field <code>termsFilters</code>.
	 * </p>
	 *
	 * @return a {@link java.util.List} object.
	 */
	public List<SearchParameters> getTermsFilters() {
		return termsFilters;
	}

	/**
	 * <p>
	 * Setter for the field <code>termsFilters</code>.
	 * </p>
	 *
	 * @param termsFilters a {@link java.util.List} object.
	 */
	public void setTermsFilters(List<SearchParameters> termsFilters) {
		this.termsFilters = termsFilters;
	}

	/**
	 * <p>
	 * Getter for the field <code>dateTermsFilters</code>.
	 * </p>
	 *
	 * @return a {@link java.util.List} object.
	 */
	public List<FacetDateRange> getDateTermsFilters() {
		return dateTermsFilters;
	}

	/**
	 * <p>
	 * Setter for the field <code>dateTermsFilters</code>.
	 * </p>
	 *
	 * @param dateTermsFilters a {@link java.util.List} object.
	 */
	public void setDateTermsFilters(List<FacetDateRange> dateTermsFilters) {
		this.dateTermsFilters = dateTermsFilters;
	}

	/**
	 * <p>
	 * Getter for the field <code>facetFieldId</code>.
	 * </p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getFacetFieldId() {
		return facetFieldId;
	}

	/**
	 * <p>
	 * Setter for the field <code>facetFieldId</code>.
	 * </p>
	 *
	 * @param facetFieldId a {@link java.lang.String} object.
	 */
	public void setFacetFieldId(String facetFieldId) {
		this.facetFieldId = facetFieldId;
	}
}

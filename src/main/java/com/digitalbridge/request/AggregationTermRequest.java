package com.digitalbridge.request;

import java.util.ArrayList;
import java.util.List;

public class AggregationTermRequest
{
    List<SearchParameters> termsFilters = new ArrayList<>();
    
    List<FacetDateRange> dateTermsFilters = new ArrayList<>();
    
    String facetFieldId;

    public List<SearchParameters> getTermsFilters()
    {
        return termsFilters;
    }

    public void setTermsFilters(List<SearchParameters> termsFilters)
    {
        this.termsFilters = termsFilters;
    }

    public List<FacetDateRange> getDateTermsFilters()
    {
        return dateTermsFilters;
    }

    public void setDateTermsFilters(List<FacetDateRange> dateTermsFilters)
    {
        this.dateTermsFilters = dateTermsFilters;
    }

    public String getFacetFieldId()
    {
        return facetFieldId;
    }

    public void setFacetFieldId(String facetFieldId)
    {
        this.facetFieldId = facetFieldId;
    }
 }

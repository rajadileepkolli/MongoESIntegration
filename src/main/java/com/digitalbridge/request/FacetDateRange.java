package com.digitalbridge.request;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.joda.time.DateTime;

import com.digitalbridge.service.impl.JsonDateSerializer;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * FacetDateRange class.
 * <p>
 *
 * @author rajakolli
 * @version 1:0
 */
@JsonInclude(Include.NON_NULL)
public class FacetDateRange
{
    @JsonProperty
    private DateTime startDate;
    @JsonProperty
    private DateTime endDate;

    @JsonSerialize(using = JsonDateSerializer.class)
    public DateTime getStartDate()
    {
        return startDate;
    }

    public void setStartDate(DateTime startDate)
    {
        this.startDate = startDate;
    }

    @JsonSerialize(using = JsonDateSerializer.class)
    public DateTime getEndDate()
    {
        return endDate;
    }

    public void setEndDate(DateTime endDate)
    {
        this.endDate = endDate;
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}

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
public class FacetDateRange {
    @JsonProperty
    private DateTime startDate;
    @JsonProperty
    private DateTime endDate;

    /**
     * <p>
     * Getter for the field <code>startDate</code>.
     * </p>
     *
     * @return a {@link org.joda.time.DateTime} object.
     */
    @JsonSerialize(using = JsonDateSerializer.class)
    public DateTime getStartDate() {
        return startDate;
    }

    /**
     * <p>
     * Setter for the field <code>startDate</code>.
     * </p>
     *
     * @param startDate a {@link org.joda.time.DateTime} object.
     */
    public void setStartDate(DateTime startDate) {
        this.startDate = startDate;
    }

    /**
     * <p>
     * Getter for the field <code>endDate</code>.
     * </p>
     *
     * @return a {@link org.joda.time.DateTime} object.
     */
    @JsonSerialize(using = JsonDateSerializer.class)
    public DateTime getEndDate() {
        return endDate;
    }

    /**
     * <p>
     * Setter for the field <code>endDate</code>.
     * </p>
     *
     * @param endDate a {@link org.joda.time.DateTime} object.
     */
    public void setEndDate(DateTime endDate) {
        this.endDate = endDate;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}

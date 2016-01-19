package com.digitalbridge.request;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import lombok.Data;

/**
 * <p>
 * SearchParameters class.
 * </p>
 *
 * @author rajakolli
 * @version 1:0
 */
@Data
public class SearchParameters {
    private String fieldId;
    private String searchValue;

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}

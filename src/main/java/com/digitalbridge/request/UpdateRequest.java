package com.digitalbridge.request;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * UpdateRequest class.
 * <p>
 *
 * @author rajakolli
 * @version 1: 0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateRequest {

    private String key;
    private Object value;
    
    /** {@inheritDoc} */
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}

package com.digitalbridge.request;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import lombok.Data;

/**
 * UpdateRequest class.
 * <p>
 *
 * @author rajakolli
 * @version 1: 0
 */
@Data
public class UpdateRequest {

    private String key;
    private Object value;
    
    /**
     * <p>Constructor for UpdateRequest.</p>
     *
     * @param key a {@link java.lang.String} object.
     * @param value a {@link java.lang.Object} object.
     */
    public UpdateRequest(String key, Object value) {
        super();
        this.key = key;
        this.value = value;
    }
    
    /**
     * <p>Constructor for UpdateRequest.</p>
     */
    public UpdateRequest(){
        super();
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}

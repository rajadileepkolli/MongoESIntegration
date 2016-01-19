package com.digitalbridge.request;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * UpdateRequest class.
 * <p>
 *
 * @author rajakolli
 * @version 1: 0
 */
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

    /**
     * <p>
     * Getter for the field <code>key</code>.
     * </p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getKey() {
        return key;
    }

    /**
     * <p>
     * Setter for the field <code>key</code>.
     * </p>
     *
     * @param key a {@link java.lang.String} object.
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * <p>
     * Getter for the field <code>value</code>.
     * </p>
     *
     * @return a {@link java.lang.Object} object.
     */
    public Object getValue() {
        return value;
    }

    /**
     * <p>
     * Setter for the field <code>value</code>.
     * </p>
     *
     * @param value a {@link java.lang.Object} object.
     */
    public void setValue(Object value) {
        this.value = value;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}

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
public class UpdateRequest
{

    private String key;
    private Object value;

    public String getKey()
    {
        return key;
    }

    public void setKey(String key)
    {
        this.key = key;
    }

    public Object getValue()
    {
        return value;
    }

    public void setValue(Object value)
    {
        this.value = value;
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}

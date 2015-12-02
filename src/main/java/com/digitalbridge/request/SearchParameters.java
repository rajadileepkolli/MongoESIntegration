package com.digitalbridge.request;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * <p>SearchParameters class.</p>
 *
 * @author rajakolli
 * @version 1:0
 */
public class SearchParameters
{
    private String fieldId;
    private String searchValue;
    
    /**
     * <p>Getter for the field <code>fieldId</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getFieldId()
    {
        return fieldId;
    }
    /**
     * <p>Setter for the field <code>fieldId</code>.</p>
     *
     * @param fieldId a {@link java.lang.String} object.
     */
    public void setFieldId(String fieldId)
    {
        this.fieldId = fieldId;
    }
    /**
     * <p>Getter for the field <code>searchValue</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getSearchValue()
    {
        return searchValue;
    }
    /**
     * <p>Setter for the field <code>searchValue</code>.</p>
     *
     * @param searchValue a {@link java.lang.String} object.
     */
    public void setSearchValue(String searchValue)
    {
        this.searchValue = searchValue;
    }
    
    /** {@inheritDoc} */
    @Override
    public String toString() {
      return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}

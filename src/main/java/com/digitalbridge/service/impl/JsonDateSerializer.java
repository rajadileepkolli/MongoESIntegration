package com.digitalbridge.service.impl;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * <p>JsonDateSerializer class.</p>
 *
 * @author rajakolli
 * @version 1:0
 */
@Component
public class JsonDateSerializer extends JsonSerializer<Date>
{

    private static final DateFormat DATEFORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.sss'Z'", Locale.US);

    /** {@inheritDoc} */
    @Override
    public void serialize(Date date, JsonGenerator gen, SerializerProvider serializers)
            throws IOException, JsonProcessingException
    {
        gen.writeString(getISO8601StringForDate(date));
    }
    
    /**
     * Return an ISO 8601 combined date and time string for specified date/time
     * 
     * @param date
     *            Date
     * @return String with format "yyyy-MM-dd'T'HH:mm:ss.sss'Z'"
     */
    private static String getISO8601StringForDate(Date date) {
        /* dateFormat.setTimeZone(TimeZone.getTimeZone("UTC")); */
        DATEFORMAT.setTimeZone(TimeZone.getDefault());
        return DATEFORMAT.format(date);
    }
}

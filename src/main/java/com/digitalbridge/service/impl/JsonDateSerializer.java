package com.digitalbridge.service.impl;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.joda.time.DateTime;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * JsonDateSerializer class.
 * <p>
 *
 * @author rajakolli
 * @version 1:0
 * @param <T>
 */
@Component
public class JsonDateSerializer<T> extends JsonSerializer<T> {

    private static final DateFormat DATEFORMAT = new SimpleDateFormat(
            "yyyy-MM-dd'T'HH:mm:ss.sss'Z'", Locale.US);

    /** {@inheritDoc} */
    @Override
    public void serialize(T value, JsonGenerator gen, SerializerProvider serializers)
            throws IOException {
        Date castedDate = null;
        if (value instanceof DateTime) {
            castedDate = ((DateTime) value).toDate();
        } else {
            castedDate = (Date) value;
        }

        gen.writeString(getISO8601StringForDate(castedDate));
    }

    /**
     * Return an ISO 8601 combined date and time string for specified date/time
     * 
     * @param date Date
     * @return String with format "yyyy-MM-dd'T'HH:mm:ss.sss'Z'"
     */
    private String getISO8601StringForDate(Date castedDate) {
        DATEFORMAT.setTimeZone(TimeZone.getDefault());
        return DATEFORMAT.format(castedDate);
    }
}

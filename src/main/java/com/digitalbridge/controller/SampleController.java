package com.digitalbridge.controller;

import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.digitalbridge.request.Sample;
import com.digitalbridge.util.Constants;


/**
 * <p>SampleController class.</p>
 *
 * @author rajakolli
 * @version 1:0
 */
@RequestMapping(value = Constants.BASE_MAIN_URL)
@RestController
public class SampleController
{
    /**
     * <p>testxml.</p>
     *
     * @param a a {@link com.digitalbridge.request.Sample} object.
     * @return a {@link com.digitalbridge.request.Sample} object.
     */
    @Secured({ "ROLE_USER" })
    @RequestMapping(value = "/test", method = RequestMethod.POST, consumes = {
            MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE,
            MediaType.TEXT_XML_VALUE }, produces = { MediaType.APPLICATION_JSON_UTF8_VALUE,
                    MediaType.APPLICATION_XML_VALUE })
    public Sample testxml(@RequestBody Sample a)
    {
        a.setUsername("Welcome ".concat(a.getUsername()));
        a.setId(2);
        return a;
    }
}

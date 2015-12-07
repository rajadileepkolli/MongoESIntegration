package com.digitalbridge.controller;

import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.digitalbridge.request.Sample;
import com.digitalbridge.util.Constants;

@RestController
@RequestMapping(value = Constants.BASE_MAIN_URL)
public class SampleController
{
    @Secured({ "ROLE_USER" })
    @RequestMapping(value = "/test", method = RequestMethod.POST, consumes = {
            "application/json", "application/xml", "text/xml" }, produces = {
                    MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE })
    public Sample testxml(@RequestBody Sample a)
    {
        a.setUsername("Raja");
        a.setId(2);
        return a;
    }
}

package com.digitalbridge.util;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * AuthorizationHeader class.
 * <p>
 *
 * @author rajakolli
 * @version 1:0
 */
@RequestMapping(value = "/restapi/digitalbridge/search")
@RepositoryRestController
public class AuthorizationHeader {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(AuthorizationHeader.class);

    /**
     * <p>
     * getBasicHeader.
     * </p>
     *
     * @param userName a {@link java.lang.String} object.
     * @param password a {@link java.lang.String} object.
     * @return a {@link org.springframework.http.ResponseEntity} object.
     */
    @RequestMapping(value = "/getEncoded/{username}/{password}", method = RequestMethod.GET, headers = {
            "Accept=application/json", "Accept=application/xml" })
    public String getBasicHeader(
            @PathVariable("username") String userName,
            @PathVariable("password") String password) {
        String usernameappendedwithPassword = new StringBuilder().append(userName)
                .append(":").append(password).toString();
        String finalString = null;
        try {
            finalString = "Basic " + new String(
                    Base64.encode(usernameappendedwithPassword.getBytes("UTF-8")));
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("UnsupportedEncodingException : {}", e.getMessage(), e);
            return "Unable to Encode";
        }
        return finalString;
    }

}

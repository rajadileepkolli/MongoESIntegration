package com.digitalbridge.util;

import java.io.UnsupportedEncodingException;

import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * AuthorizationHeader class.
 * </p>
 *
 * @author rajakolli
 * @version 1:0
 */
@RequestMapping(value = "/restapi/digitalbridge/search")
@RepositoryRestController
public class AuthorizationHeader
{

    /**
     * getBasicHeader.
     * </p>
     *
     * @param userName
     *            a {@link java.lang.String} object.
     * @param password
     *            a {@link java.lang.String} object.
     * @return a {@link java.lang.String} object.
     * @throws java.io.UnsupportedEncodingException
     *             is any
     */
    @RequestMapping(value = "/getEncoded/{username}/{password}", method = RequestMethod.GET)
    public String getBasicHeader(@PathVariable("username") String userName,
            @PathVariable("password") String password) throws UnsupportedEncodingException
    {
        String usernameappendedwithPassword = new StringBuilder().append(userName).append(":")
                .append(password).toString();
        return "Basic "
                + new String(Base64.encode(usernameappendedwithPassword.getBytes("UTF-8")));
    }

}

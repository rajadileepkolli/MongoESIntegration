package com.digitalbridge.util;

import java.io.UnsupportedEncodingException;

import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @RequestMapping(value = "/getEncoded/{username}/{password}", method = RequestMethod.GET, headers = {
            "Accept=application/json", "Accept=application/xml" })
    public ResponseEntity<String> getBasicHeader(@PathVariable("username") String userName,
            @PathVariable("password") String password)
    {
        String usernameappendedwithPassword = new StringBuilder().append(userName).append(":")
                .append(password).toString();
        String finalString = null;
        try
        {
            finalString = "Basic " + new String(
                    Base64.encode(usernameappendedwithPassword.getBytes("UTF-8")));
        }
        catch (UnsupportedEncodingException e)
        {
            return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(finalString, HttpStatus.OK);
    }

}

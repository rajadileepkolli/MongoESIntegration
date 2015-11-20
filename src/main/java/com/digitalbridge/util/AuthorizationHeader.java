package com.digitalbridge.util;

import java.util.Collections;
import java.util.Map;

import org.elasticsearch.common.Base64;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>AuthorizationHeader class.</p>
 *
 * @author rajakolli
 * @version 1:0
 */
@RequestMapping(value = "/digitalbridge/search")
@RestController
public class AuthorizationHeader {
  
  /**
   * <p>getHeader.</p>
   *
   * @param userName a {@link java.lang.String} object.
   * @param password a {@link java.lang.String} object.
   * @return a {@link java.util.Map} object.
   */
  public Map<String, Object> getHeader(String userName, String password) {
    String value = new StringBuilder().append(userName).append(":").append(password).toString();
    return Collections.singletonMap("Authorization", "Basic " + Base64.encodeBytes(value.getBytes()));
  }

  /**
   * <p>getBasicHeader.</p>
   *
   * @param userName a {@link java.lang.String} object.
   * @param password a {@link java.lang.String} object.
   * @return a {@link java.lang.String} object.
   */
//  @Secured(value = { "ROLE_ANONYMOUS" })
  @RequestMapping(value = "/getEncoded/{username}/{password}")
  public String getBasicHeader(@PathVariable("username") String userName,@PathVariable("password") String password) {
    String appendString = new StringBuilder().append(userName).append(":").append(password).toString();
    return "Basic " + Base64.encodeBytes(appendString.getBytes());
  }

}

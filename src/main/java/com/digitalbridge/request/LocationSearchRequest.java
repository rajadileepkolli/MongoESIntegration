package com.digitalbridge.request;

import lombok.Data;


/**
 * <p>LocationSearchRequest class.</p>
 *
 * @author rajakolli
 * @version 1:0
 */
@Data
public class LocationSearchRequest {
    double radius;
    double latitude;
    double longitude;
}

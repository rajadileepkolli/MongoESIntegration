package com.digitalbridge.request;

import lombok.Data;

@Data
public class LocationSearchRequest {
    double radius;
    double latitude;
    double longitude;
}

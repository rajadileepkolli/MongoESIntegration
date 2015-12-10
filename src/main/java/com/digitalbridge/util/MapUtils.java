package com.digitalbridge.util;

import java.util.Map;

/**
 * <p>
 * MapUtils class.
 * </p>
 *
 * @author rajakolli
 * @version 1:0
 */
public class MapUtils {

    private MapUtils() {
    }

    /**
     * Null-safe check if the specified map is not empty.
     * <p>
     * Null returns false.
     *
     * @param map the map to check, may be null
     * @return true if non-null and non-empty
     */
    @SuppressWarnings("rawtypes")
    public static boolean isNotEmpty(Map map) {
        return !MapUtils.isEmpty(map);
    }

    /**
     * Null-safe check if the specified map is empty.
     * <p>
     * Null returns true.
     *
     * @param map the map to check, may be null
     * @return true if empty or null
     */
    @SuppressWarnings("rawtypes")
    public static boolean isEmpty(Map map) {
        return (map == null || map.isEmpty());
    }
}

package com.doordash.utils;

import java.util.Map;

/**
 * Utility function for Maps.
 */

public class MapUtils {

    public static boolean isEmpty(final Map<?,?> map) {
        return (map == null || map.size() == 0);
    }
}
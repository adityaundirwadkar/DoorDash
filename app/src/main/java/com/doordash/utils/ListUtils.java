package com.doordash.utils;

import java.util.List;

/**
 * Utility class to implement common functions used on List collection.
 */
public class ListUtils {

    public static boolean isEmpty(List list) {
        return list == null || list.size() == 0;
    }

    public static boolean equalSize(List list1, List list2) {
        return (list1 == null && list2 == null) || (list1 != null && list2 != null && list1.size() == list2.size());
    }
}


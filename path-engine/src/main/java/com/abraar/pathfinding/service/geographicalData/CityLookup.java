package com.abraar.pathfinding.service.geographicalData;

import ch.qos.logback.core.util.StringUtil;

import java.util.HashMap;
import java.util.Map;


public class CityLookup {
    private static Map<String, String> lookup;

    static
    {
        lookup = new HashMap<>();
        lookup.put("Leicester", "leicestershire");
    }

    public static String lookup(String key)
    {
        String value = lookup.get(key);
        return StringUtil.isNullOrEmpty(value) ? key : value;
    }

}

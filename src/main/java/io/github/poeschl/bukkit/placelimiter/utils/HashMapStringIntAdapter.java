package io.github.poeschl.bukkit.placelimiter.utils;


import com.squareup.moshi.FromJson;
import com.squareup.moshi.ToJson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class HashMapStringIntAdapter {

    @FromJson
    HashMap<String, Integer> hashMapFromJson(List<String> stringMap) {
        HashMap<String, Integer> map = new HashMap<>();

        for (int i = 0; i < stringMap.size(); i++) {
            String combined = stringMap.get(i);
            String key = combined.substring(0, combined.indexOf('|'));
            int number = Integer.valueOf(combined.substring(combined.indexOf('|') + 1, combined.length()));
            map.put(key, number);
        }

        return map;
    }

    @ToJson
    List<String> hashMapToJson(HashMap<String, Integer> map) {
        List<String> stringList = new ArrayList<>();

        for (Iterator<String> iterator = map.keySet().iterator(); iterator.hasNext(); ) {
            String currentKey = iterator.next();
            int number = map.get(currentKey);

            stringList.add(currentKey + '|' + number);
        }

        return stringList;
    }
}

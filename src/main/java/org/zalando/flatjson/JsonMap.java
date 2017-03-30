package org.zalando.flatjson;

import java.util.LinkedHashMap;
import java.util.Map;

class JsonMap extends LinkedHashMap<String, Json> {

    @Override public String toString() {
        StringBuilder result = new StringBuilder("{");
        int count = 0;
        for (Map.Entry<String, Json> entry : entrySet()) {
            if (count > 0) result.append(",");
            String key = StringCodec.escape(entry.getKey());
            result.append(String.format("\"%s\":%s", key, entry.getValue()));
            count++;
        }
        result.append("}");
        return result.toString();
    }

    @Override public JsonMap clone() {
        JsonMap map = new JsonMap();
        for (Map.Entry<String, Json> entry : this.entrySet()) {
            String key = entry.getKey();
            Json value = entry.getValue();
            if (value.isObject() || value.isArray()) {
                map.put(key, value.clone());
            } else {
                map.put(key, value);
            }
        }
        return map;
    }
}

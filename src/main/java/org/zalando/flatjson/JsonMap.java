package org.zalando.flatjson;

import java.util.LinkedHashMap;
import java.util.Map;

class JsonMap<K, V> extends LinkedHashMap<K, V> {

    @Override public String toString() {
        StringBuilder result = new StringBuilder("{");
        int count = 0;
        for (Map.Entry entry : entrySet()) {
            if (count > 0) result.append(",");
            String key = StringCodec.escape((String) entry.getKey());
            result.append(String.format("\"%s\":%s", key, entry.getValue()));
            count++;
        }
        result.append("}");
        return result.toString();
    }

}

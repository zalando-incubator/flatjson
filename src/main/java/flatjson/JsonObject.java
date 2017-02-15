package flatjson;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public class JsonObject extends JsonValue {

    private Map<String, JsonValue> values;

    JsonObject(Json json, int element) {
        super(json, element);
    }

    @Override public boolean isObject() {
        return true;
    }

    @Override public synchronized Map<String, JsonValue> asObject() {
        if (values == null) {
            values = Collections.unmodifiableMap(createValues());
        }
        return values;
    }

    private Map<String, JsonValue> createValues() {
        Map<String, JsonValue> result = new HashMap<>();
        int e = element + 1;
        while (e <= element + json.getContained(element)) {
            String key = json.getRawString(e);
            result.put(key, json.createValue(e + 1));
            e += json.getContained(e + 1) + 2;
        }
        return result;
    }
}

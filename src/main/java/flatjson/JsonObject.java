package flatjson;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public class JsonObject extends JsonValue {

    private Map<String, JsonValue> values;

    JsonObject(Json json, int element) {
        super(json, element);
    }

    int size() {
        return json.getContained(element) / 2;
    }

    synchronized Map<String, JsonValue> getValues() {
        if (values == null) values = Collections.unmodifiableMap(createValues());
        return values;
    }

    private Map<String, JsonValue> createValues() {
        Map<String, JsonValue> result = new HashMap<>(size());
        for (int i = 0; i < size(); i++) {
            String key = json.getRawString(element + 2 * i + 1);
            JsonValue value = json.createValue(element + 2 * i + 2);
            result.put(key, value);
        }
        return result;
    }
}

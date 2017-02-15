package flatjson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class JsonArray extends JsonValue {

    private List<JsonValue> values;

    JsonArray(Json json, int element) {
        super(json, element);
    }

    @Override public boolean isArray() {
        return true;
    }

    @Override public synchronized List<JsonValue> asArray() {
        if (values == null) {
            values = Collections.unmodifiableList(createValues());
        }
        return values;
    }

    private List<JsonValue> createValues() {
        List<JsonValue> result = new ArrayList<>(json.getContained(element));
        for (int i = 0; i < json.getContained(element); i++) {
            result.add(json.createValue(element + i + 1));
        }
        return result;
    }
}

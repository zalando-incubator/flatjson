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
        List<JsonValue> result = new ArrayList<>();
        int e = element + 1;
        while (e <= element + json.getContained(element)) {
            result.add(json.createValue(e));
            e += json.getContained(e) + 1;
        }
        return result;
    }
}

package flatjson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class JsonArray extends JsonValue {

    private List<JsonValue> values;

    JsonArray(Json json, int element) {
        super(json, element);
    }

    int size() {
        return element().contained;
    }

    synchronized List<JsonValue> getValues() {
        if (values == null) values = Collections.unmodifiableList(createValues());
        return values;
    }

    private List<JsonValue> createValues() {
        List<JsonValue> result = new ArrayList<>(size());
        for (int i = 0; i < size(); i++) {
            result.add(json.createValue(element + i + 1));
        }
        return result;
    }
}

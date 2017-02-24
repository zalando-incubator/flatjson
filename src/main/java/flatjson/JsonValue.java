package flatjson;

import java.util.*;

import static flatjson.Json.Token;
import static flatjson.Json.Token.*;

public class JsonValue {

    public static JsonValue create(Json json, int element) {
        switch (json.getToken(element)) {
            case ARRAY: return new Array(json, element);
            case OBJECT: return new Object(json, element);
            case STRING_ESCAPED:
            case STRING: return new Strng(json, element);
            default: return new JsonValue(json, element);
        }
    }

    public static class JsonList<E> extends ArrayList<E> {
        @Override public String toString() {
            StringBuilder result = new StringBuilder("[");
            for (int i = 0; i < size(); i++) {
                if (i > 0) result.append(",");
                result.append(get(i).toString());
            }
            result.append("]");
            return result.toString();
        }
    }

    public static class JsonMap<K,V> extends LinkedHashMap<K,V> {
        @Override public String toString() {
            StringBuilder result = new StringBuilder("{");
            int count = 0;
            for (Map.Entry entry : entrySet()) {
                if (count > 0) result.append(",");
                String key = Json.escape((String) entry.getKey());
                result.append(String.format("\"%s\":%s", key, entry.getValue()));
                count++;
            }
            result.append("}");
            return result.toString();
        }
    }

    public static class Object extends JsonValue {

        private Map<String, JsonValue> values;

        Object(Json json, int element) {
            super(json, element);
        }

        @Override public boolean isObject() {
            return true;
        }

        @Override public Map<String, JsonValue> asObject() {
            if (values == null) values = createValues();
            return values;
        }

        private Map<String, JsonValue> createValues() {
            Map<String, JsonValue> result = new JsonMap<>();
            int e = element + 1;
            while (e <= element + json.getNested(element)) {
                String key = json.getStringValue(e);
                result.put(key, create(json, e + 1));
                e += json.getNested(e + 1) + 2;
            }
            return result;
        }
    }

    public static class Array extends JsonValue {

        private List<JsonValue> values;

        Array(Json json, int element) {
            super(json, element);
        }

        @Override public boolean isArray() {
            return true;
        }

        @Override public List<JsonValue> asArray() {
            if (values == null) values = createValues();
            return values;
        }

        private List<JsonValue> createValues() {
            List<JsonValue> result = new ArrayList<>();
            int e = element + 1;
            while (e <= element + json.getNested(element)) {
                result.add(create(json, e));
                e += json.getNested(e) + 1;
            }
            return result;
        }
    }

    public static class Strng extends JsonValue {

        private String value;

        Strng(Json json, int element) {
            super(json, element);
        }

        @Override public boolean isString() {
            return true;
        }

        @Override public String asString() {
            if (value == null) {
                value = json.getStringValue(element);
            }
            return value;
        }

    }

    protected final Json json;
    protected final int element;

    JsonValue(Json json, int element) {
        this.json = json;
        this.element = element;
    }

    public boolean isNull() {
        return hasToken(NULL);
    }

    public boolean isBoolean() {
        return hasToken(TRUE) || hasToken(FALSE);
    }

    public boolean isNumber() {
        return hasToken(NUMBER);
    }

    public boolean isString() {
        return false;
    }

    public boolean isArray() {
        return false;
    }

    public boolean isObject() {
        return false;
    }

    public boolean asBoolean() {
        if (!isBoolean()) throw new IllegalStateException("not a boolean");
        return Boolean.valueOf(json.getRaw(element));
    }

    public long asLong() {
        if (!isNumber()) throw new IllegalStateException("not a number");
        return Long.valueOf(json.getRaw(element));
    }

    public double asDouble() {
        if (!isNumber()) throw new IllegalStateException("not a number");
        return Double.valueOf(json.getRaw(element));
    }

    public String asString() {
        throw new IllegalStateException("not a string");
    }

    public List<JsonValue> asArray() {
        throw new IllegalStateException("not an array");
    }

    public Map<String, JsonValue> asObject() {
        throw new IllegalStateException("not an object");
    }

    protected boolean hasToken(Token token) {
        return json.getToken(element) == token;
    }

    @Override public String toString() {
        return json.getRaw(element);
    }
}

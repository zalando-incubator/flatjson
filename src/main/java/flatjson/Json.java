package flatjson;

import java.util.*;

import static flatjson.Token.*;

public class Json {

    public static Json parse(String raw) {
        if (raw == null) throw new ParseException("cannot parse null");
        return new Overlay(raw).parse();
    }

    static Json create(Overlay overlay, int element) {
        switch (overlay.getToken(element)) {
            case ARRAY: return new Array(overlay, element);
            case OBJECT: return new Object(overlay, element);
            case STRING_ESCAPED:
            case STRING: return new Strng(overlay, element);
            default: return new Json(overlay, element);
        }
    }

    static class JsonList<E> extends ArrayList<E> {
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

    static class JsonMap<K,V> extends LinkedHashMap<K,V> {
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

    static class Object extends Json {

        private Map<String, Json> values;

        Object(Overlay overlay, int element) {
            super(overlay, element);
        }

        @Override public boolean isObject() {
            return true;
        }

        @Override public Map<String, Json> asObject() {
            if (values == null) values = createValues();
            return values;
        }

        private Map<String, Json> createValues() {
            Map<String, Json> result = new JsonMap<>();
            int e = element + 1;
            while (e <= element + overlay.getNested(element)) {
                String key = overlay.getStringValue(e);
                result.put(key, create(overlay, e + 1));
                e += overlay.getNested(e + 1) + 2;
            }
            return result;
        }
    }

    static class Array extends Json {

        private List<Json> values;

        Array(Overlay overlay, int element) {
            super(overlay, element);
        }

        @Override public boolean isArray() {
            return true;
        }

        @Override public List<Json> asArray() {
            if (values == null) values = createValues();
            return values;
        }

        private List<Json> createValues() {
            List<Json> result = new ArrayList<>();
            int e = element + 1;
            while (e <= element + overlay.getNested(element)) {
                result.add(create(overlay, e));
                e += overlay.getNested(e) + 1;
            }
            return result;
        }
    }

    static class Strng extends Json {

        private String value;

        Strng(Overlay overlay, int element) {
            super(overlay, element);
        }

        @Override public boolean isString() {
            return true;
        }

        @Override public String asString() {
            if (value == null) {
                value = overlay.getStringValue(element);
            }
            return value;
        }

    }

    protected final Overlay overlay;
    protected final int element;

    Json(Overlay overlay, int element) {
        this.overlay = overlay;
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
        return Boolean.valueOf(overlay.getRaw(element));
    }

    public long asLong() {
        if (!isNumber()) throw new IllegalStateException("not a number");
        return Long.valueOf(overlay.getRaw(element));
    }

    public double asDouble() {
        if (!isNumber()) throw new IllegalStateException("not a number");
        return Double.valueOf(overlay.getRaw(element));
    }

    public String asString() {
        throw new IllegalStateException("not a string");
    }

    public List<Json> asArray() {
        throw new IllegalStateException("not an array");
    }

    public Map<String, Json> asObject() {
        throw new IllegalStateException("not an object");
    }

    protected boolean hasToken(Token token) {
        return overlay.getToken(element) == token;
    }

    @Override public String toString() {
        return overlay.getRaw(element);
    }
}

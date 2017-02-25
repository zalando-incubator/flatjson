package flatjson;

import java.util.*;

import static flatjson.Token.*;

public abstract class Json {

    public static Json parse(String raw) {
        if (raw == null) throw new ParseException("cannot parse null");
        return new Overlay(raw).parse();
    }

    public static Json nil() {
        return new Literal.Null();
    }

    public static Json bool(boolean value) {
        return new Literal.Boolean(value);
    }

    public static Json number(long value) {
        return new Literal.Number(value);
    }

    public static Json number(double value) {
        return new Literal.Number(value);
    }

    public static Json string(String string) {
        return (string == null) ? new Literal.Null() : new Literal.Strng(string);
    }

    public static Map<String, Json> object() {
        return new JsonMap<>();
    }

    public static List<Json> array() {
        return new JsonList<>();
    }

    static Json create(Overlay overlay, int element) {
        switch (overlay.getToken(element)) {
            case ARRAY: return new Array(overlay, element);
            case OBJECT: return new Object(overlay, element);
            case STRING_ESCAPED:
            case STRING: return new Strng(overlay, element);
            default: return new Value(overlay, element);
        }
    }

    static class Object extends Value {

        private Map<String, Json> map;

        Object(Overlay overlay, int element) {
            super(overlay, element);
        }

        @Override public boolean isObject() {
            return true;
        }

        @Override public Map<String, Json> asObject() {
            if (map == null) map = createMap();
            return map;
        }

        private Map<String, Json> createMap() {
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

    static class Array extends Value {

        private List<Json> array;

        Array(Overlay overlay, int element) {
            super(overlay, element);
        }

        @Override public boolean isArray() {
            return true;
        }

        @Override public List<Json> asArray() {
            if (array == null) array = createArray();
            return array;
        }

        private List<Json> createArray() {
            List<Json> result = new ArrayList<>();
            int e = element + 1;
            while (e <= element + overlay.getNested(element)) {
                result.add(create(overlay, e));
                e += overlay.getNested(e) + 1;
            }
            return result;
        }
    }

    static class Strng extends Value {

        private String string;

        Strng(Overlay overlay, int element) {
            super(overlay, element);
        }

        @Override public boolean isString() {
            return true;
        }

        @Override public String asString() {
            if (string == null) string = overlay.getStringValue(element);
            return string;
        }

    }

    static class Value extends Json {

        protected final Overlay overlay;
        protected final int element;

        Value(Overlay overlay, int element) {
            this.overlay = overlay;
            this.element = element;
        }

        @Override public boolean isNull() {
            return hasToken(NULL);
        }

        @Override public boolean isBoolean() {
            return hasToken(TRUE) || hasToken(FALSE);
        }

        @Override public boolean isNumber() {
            return hasToken(NUMBER);
        }

        @Override public boolean asBoolean() {
            if (!isBoolean()) throw new IllegalStateException("not a boolean");
            return java.lang.Boolean.valueOf(overlay.getRaw(element));
        }

        @Override public long asLong() {
            if (!isNumber()) throw new IllegalStateException("not a number");
            return Long.valueOf(overlay.getRaw(element));
        }

        @Override public double asDouble() {
            if (!isNumber()) throw new IllegalStateException("not a number");
            return Double.valueOf(overlay.getRaw(element));
        }

        protected boolean hasToken(Token token) {
            return overlay.getToken(element) == token;
        }

        @Override public String toString() {
            return overlay.getRaw(element);
        }
    }

    public boolean isNull() {
        return false;
    }

    public boolean isBoolean() {
        return false;
    }

    public boolean isNumber() {
        return false;
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
        throw new IllegalStateException("not a boolean");
    }

    public long asLong() {
        throw new IllegalStateException("not a number");
    }

    public double asDouble() {
        throw new IllegalStateException("not a number");
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

}

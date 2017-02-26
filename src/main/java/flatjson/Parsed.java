package flatjson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static flatjson.Token.*;

class Parsed extends Json {

    static class Value extends Parsed {

        protected final Overlay overlay;
        protected final int element;

        Value(Overlay overlay, int element) {
            this.overlay = overlay;
            this.element = element;
        }

        protected boolean hasToken(Token token) {
            return overlay.getToken(element) == token;
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
            return Boolean.valueOf(overlay.getRaw(element));
        }

        @Override public long asLong() {
            if (!isNumber()) throw new IllegalStateException("not a number");
            return Long.valueOf(overlay.getRaw(element));
        }

        @Override public double asDouble() {
            if (!isNumber()) throw new IllegalStateException("not a number");
            return Double.valueOf(overlay.getRaw(element));
        }

        @Override public String toString() {
            return overlay.getRaw(element);
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
}

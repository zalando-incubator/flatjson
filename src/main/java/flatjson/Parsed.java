package flatjson;

import java.util.List;
import java.util.Map;

import static flatjson.Json.Type.NULL;

class Parsed extends Json {

    static class Value extends Parsed {

        protected final Overlay overlay;
        protected final int element;

        Value(Overlay overlay, int element) {
            this.overlay = overlay;
            this.element = element;
        }

        @Override public boolean isNull() {
            return overlay.getType(element) == NULL;
        }

        @Override public String toString() {
            return overlay.getJson(element);
        }
    }

    static class Bool extends Value {

        Bool(Overlay overlay, int element) {
            super(overlay, element);
        }

        @Override public boolean isBoolean() {
            return true;
        }

        @Override public boolean asBoolean() {
            return Boolean.valueOf(overlay.getJson(element));
        }
    }

    static class Number extends Value {

        Number(Overlay overlay, int element) {
            super(overlay, element);
        }

        @Override public boolean isNumber() {
            return true;
        }

        @Override public long asLong() {
            return Long.valueOf(overlay.getJson(element));
        }

        @Override public double asDouble() {
            return Double.valueOf(overlay.getJson(element));
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
            if (string == null) string = overlay.getUnescapedString(element);
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

        @Override public String toString() {
            return (array == null) ? super.toString() : array.toString();
        }

        private List<Json> createArray() {
            List<Json> result = new JsonList<>();
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

        @Override public String toString() {
            return (map == null) ? super.toString() : map.toString();
        }

        private Map<String, Json> createMap() {
            Map<String, Json> result = new JsonMap<>();
            int e = element + 1;
            while (e <= element + overlay.getNested(element)) {
                String key = overlay.getUnescapedString(e);
                result.put(key, create(overlay, e + 1));
                e += overlay.getNested(e + 1) + 2;
            }
            return result;
        }
    }
}

package org.zalando.flatjson;

import java.util.List;
import java.util.Map;

class Parsed extends Json {

    static class Strng extends Parsed {

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

    static class Array extends Parsed {

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

    static class Object extends Parsed {

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

    protected final Overlay overlay;
    protected final int element;

    Parsed(Overlay overlay, int element) {
        this.overlay = overlay;
        this.element = element;
    }

    @Override public String toString() {
        return overlay.getJson(element);
    }
}

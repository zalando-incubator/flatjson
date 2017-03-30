package org.zalando.flatjson;

import java.util.List;
import java.util.Map;

abstract class Parsed extends Json {

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

        @Override public boolean equals(java.lang.Object obj) {
            return  this == obj || obj != null && obj.equals(asString());
        }

        @Override public int hashCode() {
            return asString().hashCode();
        }
    }

    static class Array extends Parsed {

        private JsonList array;

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
            return asArray().toString();
        }

        private JsonList createArray() {
            JsonList result = new JsonList();
            int e = element + 1;
            while (e <= element + overlay.getNested(element)) {
                result.add(create(overlay, e));
                e += overlay.getNested(e) + 1;
            }
            return result;
        }

        @Override public boolean equals(java.lang.Object obj) {
            return this == obj || obj != null && obj.equals(asArray());
        }

        @Override public int hashCode() {
            return asArray().hashCode();
        }

        @Override public Array clone() {
            Array copy = new Array(overlay, element);
            copy.array = (array != null) ? array.clone() : null;
            return copy;
        }
    }

    static class Object extends Parsed {

        private JsonMap map;

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

        private JsonMap createMap() {
            JsonMap result = new JsonMap();
            int e = element + 1;
            while (e <= element + overlay.getNested(element)) {
                String key = overlay.getUnescapedString(e);
                result.put(key, create(overlay, e + 1));
                e += overlay.getNested(e + 1) + 2;
            }
            return result;
        }

        @Override public boolean equals(java.lang.Object obj) {
            return this == obj || obj != null && obj.equals(asObject());
        }

        @Override public int hashCode() {
            return asObject().hashCode();
        }

        @Override public Object clone() {
            Object copy = new Object(overlay, element);
            copy.map = (map != null) ? map.clone() : null;
            return copy;
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

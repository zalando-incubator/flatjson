package org.zalando.flatjson;

import java.util.List;
import java.util.Map;

class Literal extends Json {

    static class Null extends Literal {

        @Override public boolean isNull() {
            return true;
        }

        @Override public void accept(Visitor visitor) {
            visitor.visitNull();
        }

        @Override public String toString() {
            return "null";
        }
    }

    static class Bool extends Literal {

        private final boolean value;

        Bool(boolean value) {
            this.value = value;
        }

        @Override public boolean isBoolean() {
            return true;
        }

        @Override public boolean asBoolean() {
            return value;
        }

        @Override public void accept(Visitor visitor) {
            visitor.visitBoolean(value);
        }
        @Override public String toString() {
            return Boolean.toString(value);
        }
    }

    static class Number extends Literal {

        private final String value;

        Number(long value) {
            this.value = Long.toString(value);
        }

        Number(double value) {
            this.value = Double.toString(value);
        }

        @Override public boolean isNumber() {
            return true;
        }

        @Override public long asLong() {
            return Long.valueOf(value);
        }

        @Override public double asDouble() {
            return Double.valueOf(value);
        }

        @Override public void accept(Visitor visitor) {
            visitor.visitNumber(value);
        }

        @Override public String toString() {
            return value;
        }
    }

    static class Strng extends Literal {

        private final String string;

        Strng(String string) {
            this.string = string;
        }

        @Override public boolean isString() {
            return true;
        }

        @Override public String asString() {
            return string;
        }

        @Override public void accept(Visitor visitor) {
            visitor.visitString(string);
        }

        @Override public String toString() {
            return String.format("\"%s\"", StringCodec.escape(string));
        }
    }

    static class Array extends Literal {

        private final List<Json> list;

        Array(List<Json> values) {
            this.list = new JsonList<>(values);
        }

        @Override public boolean isArray() {
            return true;
        }

        @Override public List<Json> asArray() {
            return list;
        }

        @Override public void accept(Visitor visitor) {
            visitor.beginArray();
            for (Json value : list) value.accept(visitor);
            visitor.endArray();
        }

        @Override public String toString() {
            return list.toString();
        }
    }

    static class Object extends Literal {

        private final Map<String, Json> map;

        Object() {
            this.map = new JsonMap<>();
        }

        @Override public boolean isObject() {
            return true;
        }

        @Override public Map<String, Json> asObject() {
            return map;
        }

        @Override public void accept(Visitor visitor) {
            visitor.beginObject();
            for (Map.Entry<String, Json> entry : map.entrySet()) {
                visitor.visitString(entry.getKey());
                entry.getValue().accept(visitor);
            }
            visitor.endObject();
        }

        @Override public String toString() {
            return map.toString();
        }
    }
}

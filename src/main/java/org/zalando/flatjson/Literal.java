package org.zalando.flatjson;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

class Literal extends Json {

    static class Null extends Literal {

        @Override public boolean isNull() {
            return true;
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

        @Override public String toString() {
            return Boolean.toString(value);
        }
    }

    static class Number extends Literal {

        private final String value;

        Number(String value) {
            this.value = value;
        }

        @Override public boolean isNumber() {
            return true;
        }

        @Override public int asInt() {
            return Integer.valueOf(value);
        }

        @Override public long asLong() {
            return Long.valueOf(value);
        }

        @Override public float asFloat() {
            return Float.valueOf(value);
        }

        @Override public double asDouble() {
            return Double.valueOf(value);
        }

        @Override public BigInteger asBigInteger() {
            return new BigInteger(value);
        }

        @Override public BigDecimal asBigDecimal() {
            return new BigDecimal(value);
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

        @Override public String toString() {
            return map.toString();
        }
    }
}

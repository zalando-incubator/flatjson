package org.zalando.flatjson;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

abstract class Literal extends Json {

    static class Null extends Literal {

        @Override public boolean isNull() {
            return true;
        }

        @Override public String asString() {
            return null;
        }

        @Override public BigInteger asBigInteger() {
            return null;
        }

        @Override public BigDecimal asBigDecimal() {
            return null;
        }

        @Override public Map<String, Json> asObject() {
            return null;
        }

        @Override public List<Json> asArray() {
            return null;
        }

        @Override public String toString() {
            return "null";
        }

        @Override public boolean equals(java.lang.Object obj) {
            return obj == null || obj.equals(null);
        }

        @Override public int hashCode() {
            return 0;
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

        @Override public boolean equals(java.lang.Object obj) {
            return obj != null && obj.equals(Boolean.valueOf(value));
        }

        @Override public int hashCode() {
            return Boolean.valueOf(value).hashCode();
        }
    }

    static class Number extends Literal {

        private final BigDecimal value;

        Number(String value) {
            this.value = new BigDecimal(value);
        }

        @Override public boolean isNumber() {
            return true;
        }

        @Override public int asInt() {
            return value.intValue();
        }

        @Override public long asLong() {
            return value.longValue();
        }

        @Override public float asFloat() {
            return value.floatValue();
        }

        @Override public double asDouble() {
            return value.doubleValue();
        }

        @Override public BigInteger asBigInteger() {
            return value.toBigInteger();
        }

        @Override public BigDecimal asBigDecimal() {
            return value;
        }

        @Override public String toString() {
            return value.toString();
        }

        @Override public boolean equals(java.lang.Object obj) {
            return obj != null && obj.equals(value);
        }

        @Override public int hashCode() {
            return value.hashCode();
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

        @Override public boolean equals(java.lang.Object obj) {
            return obj != null && obj.equals(string);
        }

        @Override public int hashCode() {
            return string.hashCode();
        }
    }

    static class Array extends Literal {

        private final JsonList list;

        Array(List<Json> values) {
            this.list = new JsonList(values);
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

        @Override public boolean equals(java.lang.Object obj) {
            return obj != null && obj.equals(list);
        }

        @Override public int hashCode() {
            return list.hashCode();
        }

        @Override public Array clone() {
            return new Array(list.clone());
        }
    }

    static class Object extends Literal {

        private final JsonMap map;

        Object(JsonMap map) {
            this.map = map;
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

        @Override public boolean equals(java.lang.Object obj) {
            return obj != null && obj.equals(map);
        }

        @Override public int hashCode() {
            return map.hashCode();
        }

        @Override public Object clone() {
            return new Object(map.clone());
        }
    }
}

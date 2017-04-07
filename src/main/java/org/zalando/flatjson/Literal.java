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

        @Override public void accept(Visitor visitor) {
            visitor.visitNull();
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

        @Override public void accept(Visitor visitor) {
            visitor.visitBoolean(value);
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

        private final String value;
        private BigDecimal number;

        Number(String value) {
            this.value = value;
        }

        Number(BigDecimal number) {
            this.value = number.toString();
            this.number = number;
        }

        @Override public boolean isNumber() {
            return true;
        }

        private BigDecimal getNumber() {
            if (number != null) {
                return number;
            } else {
                return number = new BigDecimal(value);
            }
        }

        @Override public int asInt() {
            return getNumber().intValue();
        }

        @Override public long asLong() {
            return getNumber().longValue();
        }

        @Override public float asFloat() {
            return getNumber().floatValue();
        }

        @Override public double asDouble() {
            return getNumber().doubleValue();
        }

        @Override public BigInteger asBigInteger() {
            return getNumber().toBigInteger();
        }

        @Override public BigDecimal asBigDecimal() {
            return getNumber();
        }

        @Override public void accept(Visitor visitor) {
            visitor.visitNumber(value);
        }

        @Override public String toString() {
            return value;
        }

        @Override public boolean equals(java.lang.Object obj) {
            return obj != null && obj.equals(getNumber());
        }

        @Override public int hashCode() {
            return getNumber().hashCode();
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

        @Override public void accept(Visitor visitor) {
            visitor.beginArray();
            for (Json value : list) value.accept(visitor);
            visitor.endArray();
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

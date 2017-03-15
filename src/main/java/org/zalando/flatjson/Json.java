package org.zalando.flatjson;

import java.math.BigDecimal;
import java.util.*;

public class Json {

    enum Type {
        NULL,
        TRUE,
        FALSE,
        NUMBER,
        STRING,
        STRING_ESCAPED,
        ARRAY,
        OBJECT
    }

    protected static Json create(Overlay overlay, int element) {
        switch (overlay.getType(element)) {
            case TRUE:
            case FALSE: return new Parsed.Bool(overlay, element);
            case NUMBER: return new Parsed.Number(overlay, element);
            case STRING_ESCAPED:
            case STRING: return new Parsed.Strng(overlay, element);
            case ARRAY: return new Parsed.Array(overlay, element);
            case OBJECT: return new Parsed.Object(overlay, element);
            default: return new Parsed.Value(overlay, element);
        }
    }

    public static Json parse(String raw) {
        return create(new Overlay(raw), 0);
    }

    public static Json value(boolean value) {
        return new Literal.Bool(value);
    }

    public static Json value(int value) {
        return new Literal.Number(Integer.toString(value));
    }

    public static Json value(long value) {
        return new Literal.Number(Long.toString(value));
    }

    public static Json value(float value) {
        return new Literal.Number(Float.toString(value));
    }

    public static Json value(double value) {
        return new Literal.Number(Double.toString(value));
    }

    public static Json value(BigDecimal value) {
        return (value == null) ? new Literal.Null() : new Literal.Number(value.toString());
    }

    public static Json value(String value) {
        return (value == null) ? new Literal.Null() : new Literal.Strng(value);
    }

    public static Json array(Json... values) {
        return new Literal.Array(Arrays.asList(values));
    }

    public static Json object() {
        return new Literal.Object();
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

    public int asInt() {
        throw new IllegalStateException("not a number");
    }

    public long asLong() {
        throw new IllegalStateException("not a number");
    }

    public float asFloat() {
        throw new IllegalStateException("not a number");
    }

    public double asDouble() {
        throw new IllegalStateException("not a number");
    }

    public BigDecimal asBigDecimal() {
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

    @Override public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof Json)) return false;
        return toString().equals(other.toString());
    }

    @Override public int hashCode() {
        return toString().hashCode();
    }
}

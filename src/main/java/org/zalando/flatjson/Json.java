package org.zalando.flatjson;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public abstract class Json implements Cloneable {

    public static final String DEFAULT_INDENT = "  ";

    public static final Json NULL = new Literal.Null();
    public static final Json TRUE = new Literal.Bool(true);
    public static final Json FALSE = new Literal.Bool(false);

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
        Type type = overlay.getType(element);
        switch (type) {
            case NULL: return NULL;
            case TRUE:
            case FALSE: return new Literal.Bool(Boolean.valueOf(overlay.getJson(element)));
            case NUMBER: return new Literal.Number(overlay.getJson(element));
            case STRING_ESCAPED:
            case STRING: return new Parsed.Strng(overlay, element);
            case ARRAY: return new Parsed.Array(overlay, element);
            case OBJECT: return new Parsed.Object(overlay, element);
            default: throw new ParseException("unknown type: " + type);
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

    public static Json value(BigInteger value) {
        return (value == null) ? NULL : new Literal.Number(value.toString());
    }

    public static Json value(BigDecimal value) {
        return (value == null) ? NULL : new Literal.Number(value.toString());
    }

    public static Json value(String value) {
        return (value == null) ? NULL : new Literal.Strng(value);
    }

    public static Json value() {
        return NULL;
    }

    public static Json array(List<Json> values) {
        return new Literal.Array(values);
    }

    public static Json array(Json... values) {
        return new Literal.Array(Arrays.asList(values));
    }

    public static Json object() {
        return new Literal.Object(new JsonMap());
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

    public BigInteger asBigInteger() {
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

    public void accept(Visitor visitor) {
        throw new IllegalStateException("not implemented");
    }

    public String prettyPrint() {
        return prettyPrint(DEFAULT_INDENT);
    }

    public String prettyPrint(String indent) {
        PrettyPrinter printer = new PrettyPrinter(indent);
        accept(printer);
        return printer.toString();
    }

    @Override public Json clone() {
        return this;
    }
}

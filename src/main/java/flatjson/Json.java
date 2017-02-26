package flatjson;

import java.util.*;

public class Json {

    protected static Json create(Overlay overlay, int element) {
        switch (overlay.getToken(element)) {
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

    public static Json nil() {
        return new Literal.Null();
    }

    public static Json bool(boolean value) {
        return new Literal.Bool(value);
    }

    public static Json number(long value) {
        return new Literal.Number(value);
    }

    public static Json number(double value) {
        return new Literal.Number(value);
    }

    public static Json string(String value) {
        return (value == null) ? nil() : new Literal.Strng(value);
    }

    public static List<Json> array() {
        return new JsonList<>();
    }

    public static Map<String, Json> object() {
        return new JsonMap<>();
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

    public long asLong() {
        throw new IllegalStateException("not a number");
    }

    public double asDouble() {
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

}

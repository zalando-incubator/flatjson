package flatjson;

public class JsonValue {

    protected final Json json;
    protected final int element;

    JsonValue(Json json, int element) {
        this.json = json;
        this.element = element;
    }

    public boolean isNull() {
        return hasToken(Json.Token.NULL);
    }

    public boolean isBoolean() {
        return hasToken(Json.Token.TRUE) || hasToken(Json.Token.FALSE);
    }

    public boolean isNumber() {
        return hasToken(Json.Token.NUMBER);
    }

    public boolean isString() {
        return hasToken(Json.Token.STRING);
    }

    public boolean isArray() {
        return hasToken(Json.Token.ARRAY);
    }

    public boolean isObject() {
        return hasToken(Json.Token.OBJECT);
    }

    public boolean asBoolean() {
        if (!isBoolean()) throw new IllegalStateException("not a boolean");
        return Boolean.valueOf(element().getRaw());
    }

    public long asLong() {
        if (!isNumber()) throw new IllegalStateException("not a number");
        return Long.valueOf(element().getRaw());
    }

    public double asDouble() {
        if (!isNumber()) throw new IllegalStateException("not a number");
        return Double.valueOf(element().getRaw());
    }

    public String asString() {
        if (!isString()) throw new IllegalStateException("not a string");
        return element().getRawString(); // todo: convert escaped chars
    }

    public JsonArray asArray() {
        if (!isArray()) throw new IllegalStateException("not an array");
        return (JsonArray) this;
    }

    public JsonObject asObject() {
        if (!isObject()) throw new IllegalStateException("not an object");
        return (JsonObject) this;
    }

    protected boolean hasToken(Json.Token token) {
        return element().token == token;
    }

    protected Json.Element element() {
        return json.getElement(element);
    }
}

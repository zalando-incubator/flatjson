package flatjson;

import java.util.List;
import java.util.Map;

public class JsonValue {

    public static String decodeString(String raw) {
        StringBuffer result = new StringBuffer(raw.length());
        int i = 0;
        while (i < raw.length()) {
            if (raw.charAt(i) == '\\') {
                i++;
                if (raw.charAt(i) == '"') {
                    result.append('"');
                } else if (raw.charAt(i) == '\\') {
                    result.append('\\');
                } else if (raw.charAt(i) == '/') {
                    result.append('/');
                } else if (raw.charAt(i) == 'b') {
                    result.append('\b');
                } else if (raw.charAt(i) == 'f') {
                    result.append('\f');
                } else if (raw.charAt(i) == 'n') {
                    result.append('\n');
                } else if (raw.charAt(i) == 'r') {
                    result.append('\r');
                } else if (raw.charAt(i) == 't') {
                    result.append('\t');
                } else if (raw.charAt(i) == 'u') {
                    result.append(Character.toChars(Integer.parseInt(raw.substring(i+1, i+5), 16)));
                    i += 4;
                }
            } else {
                result.append(raw.charAt(i));
            }
            i++;
        }
        return result.toString();
    }

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
        return false;
    }

    public boolean isArray() {
        return false;
    }

    public boolean isObject() {
        return false;
    }

    public boolean asBoolean() {
        if (!isBoolean()) throw new IllegalStateException("not a boolean");
        return Boolean.valueOf(json.getRaw(element));
    }

    public long asLong() {
        if (!isNumber()) throw new IllegalStateException("not a number");
        return Long.valueOf(json.getRaw(element));
    }

    public double asDouble() {
        if (!isNumber()) throw new IllegalStateException("not a number");
        return Double.valueOf(json.getRaw(element));
    }

    public String asString() {
        throw new IllegalStateException("not a string");
    }

    public List<JsonValue> asArray() {
        throw new IllegalStateException("not an array");
    }

    public Map<String, JsonValue> asObject() {
        throw new IllegalStateException("not an object");
    }

    protected boolean hasToken(Json.Token token) {
        return json.getToken(element) == token;
    }

    @Override public String toString() {
        return json.getRaw(element);
    }
}

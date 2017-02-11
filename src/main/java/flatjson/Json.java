package flatjson;


import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Json {

    public static class ParseException extends RuntimeException {
        public ParseException(String message) {
            super(message);
        }

        public ParseException(Token token) {
            super("illegal token: " + token);
        }

        public ParseException(char c) {
            super("illegal char: '" + c + "'");
        }
    }

    public enum Token {
        NULL,
        TRUE,
        FALSE,
        NUMBER,
        STRING,
        ARRAY,
        OBJECT,
        OBJECT_VALUE
    }

    private static final char[] HEX_CHARS = "0123456789abcdefABCDEF".toCharArray();
    private static final char[] ESCAPED_CHARS = "\"\\/bfnrt".toCharArray();
    private static final char[] WHITESPACE_CHARS = " \t\r\n".toCharArray();

    private static class State {
        State consume(Json json, int index, char c, char next) {
            throw new ParseException(c);
        }
    }

    private static class SkipWhitespace extends State {
        @Override State consume(Json json, int index, char c, char next) {
            for (char w : WHITESPACE_CHARS) if (c == w) return this;
            return super.consume(json, index, c, next);
        }
    }

    private static class Value extends SkipWhitespace {
        @Override State consume(Json json, int index, char c, char next) {
            if (c == 'n') return json.begin(index, Token.NULL);
            if (c == 't') return json.begin(index, Token.TRUE);
            if (c == 'f') return json.begin(index, Token.FALSE);
            if (c == '"') return json.begin(index, Token.STRING);
            if (c == '[') return json.begin(index, Token.ARRAY);
            if (c == '{') return json.begin(index, Token.OBJECT);
            if (c == '0') {
                if (next >= '0' && next <= '9') throw new ParseException(c);
                if (next == '.') return json.begin(index, Token.NUMBER);
                if (next == 'e' || next == 'E') {
                    json.begin(index, Token.NUMBER);
                    return EXPONENT;
                }
                json.begin(index, Token.NUMBER);
                return json.end(index, Token.NUMBER);
            }
            if (c >= '1' && c <= '9') {
                if (next >= '0' && next <= '9') return json.begin(index, Token.NUMBER);
                if (next == '.') return json.begin(index, Token.NUMBER);
                if (next == 'e' || next == 'E') {
                    json.begin(index, Token.NUMBER);
                    return EXPONENT;
                }
                json.begin(index, Token.NUMBER);
                return json.end(index, Token.NUMBER);
            }
            if (c == '-') {
                json.begin(index, Token.NUMBER);
                return NUMBER_NEGATIVE;
            }
            return super.consume(json, index, c, next);
        }
    }

    private static final State VALUE = new Value();

    private static final State END = new SkipWhitespace();

    private static final State NULL_1 = new State() {
        @Override State consume(Json json, int index, char c, char next) {
            if (c == 'u') return NULL_2;
            return super.consume(json, index, c, next);
        }
    };

    private static final State NULL_2 = new State() {
        @Override State consume(Json json, int index, char c, char next) {
            if (c == 'l') return NULL_3;
            return super.consume(json, index, c, next);
        }
    };

    private static final State NULL_3 = new State() {
        @Override State consume(Json json, int index, char c, char next) {
            if (c == 'l') return json.end(index, Token.NULL);
            return super.consume(json, index, c, next);
        }
    };

    private static final State TRUE_1 = new State() {
        @Override State consume(Json json, int index, char c, char next) {
            if (c == 'r') return TRUE_2;
            return super.consume(json, index, c, next);
        }
    };

    private static final State TRUE_2 = new State() {
        @Override State consume(Json json, int index, char c, char next) {
            if (c == 'u') return TRUE_3;
            return super.consume(json, index, c, next);
        }
    };

    private static final State TRUE_3 = new State() {
        @Override State consume(Json json, int index, char c, char next) {
            if (c == 'e') return json.end(index, Token.TRUE);
            return super.consume(json, index, c, next);
        }
    };

    private static final State FALSE_1 = new State() {
        @Override State consume(Json json, int index, char c, char next) {
            if (c == 'a') return FALSE_2;
            return super.consume(json, index, c, next);
        }
    };

    private static final State FALSE_2 = new State() {
        @Override State consume(Json json, int index, char c, char next) {
            if (c == 'l') return FALSE_3;
            return super.consume(json, index, c, next);
        }
    };

    private static final State FALSE_3 = new State() {
        @Override State consume(Json json, int index, char c, char next) {
            if (c == 's') return FALSE_4;
            return super.consume(json, index, c, next);
        }
    };

    private static final State FALSE_4 = new State() {
        @Override State consume(Json json, int index, char c, char next) {
            if (c == 'e') return json.end(index, Token.FALSE);
            return super.consume(json, index, c, next);
        }
    };

    private static final State NUMBER_NEGATIVE = new State() {
        @Override State consume(Json json, int index, char c, char next) {
            if (c == '0') {
                if (next >= '0' && next <= '9') return super.consume(json, index, c, next);
                if (next == '.') return NUMBER_DOT;
                if (next == 'e' || next == 'E') return EXPONENT;
                return json.end(index, Token.NUMBER);
            }
            if (c >= '1' && c <= '9') {
                if (next >= '0' && next <= '9') return NUMBER_START;
                if (next == '.') return NUMBER_DOT;
                if (next == 'e' || next == 'E') return EXPONENT;
                return json.end(index, Token.NUMBER);

            }
            return super.consume(json, index, c, next);
        }
    };

    private static final State NUMBER_START = new State() {
        @Override State consume(Json json, int index, char c, char next) {
            if (c == '.') return NUMBER_FRACTION;
            if (c >= '0' && c <= '9') {
                if (next >= '0' && next <= '9') return NUMBER_START;
                if (next == '.') return NUMBER_DOT;
                if (next == 'e' || next == 'E') return EXPONENT;
                return json.end(index, Token.NUMBER);
            }
            return super.consume(json, index, c, next);
        }
    };

    private static final State NUMBER_DOT = new State() {
        @Override State consume(Json json, int index, char c, char next) {
            return NUMBER_FRACTION;
        }
    };

    private static final State NUMBER_FRACTION = new State() {
        @Override State consume(Json json, int index, char c, char next) {
            if (c >= '0' && c <= '9') {
                if (next >= '0' && next <= '9') return NUMBER_FRACTION;
                if (next == 'e' || next == 'E') return EXPONENT;
                return json.end(index, Token.NUMBER);
            }
            return super.consume(json, index, c, next);
        }
    };

    private static final State EXPONENT = new State() {
        @Override State consume(Json json, int index, char c, char next) {
            if (next == '+' || next == '-') return EXPONENT_SIGN;
            return EXPONENT_DIGITS;
        }
    };

    private static final State EXPONENT_SIGN = new State() {
        @Override State consume(Json json, int index, char c, char next) {
            return EXPONENT_DIGITS;
        }
    };

    private static final State EXPONENT_DIGITS = new State() {
        @Override State consume(Json json, int index, char c, char next) {
            if (c >= '0' && c <= '9') {
                if (next >= '0' && next <= '9') return EXPONENT_DIGITS;
                return json.end(index, Token.NUMBER);
            }
            return super.consume(json, index, c, next);
        }
    };

    private static final State ARRAY_START = new Value() {
        @Override State consume(Json json, int index, char c, char next) {
            if (c == ']') return json.end(index, Token.ARRAY);
            return super.consume(json, index, c, next);
        }
    };

    private static final State ARRAY_NEXT = new SkipWhitespace() {
        @Override State consume(Json json, int index, char c, char next) {
            if (c == ']') return json.end(index, Token.ARRAY);
            if (c == ',') return VALUE;
            return super.consume(json, index, c, next);
        }
    };

    private static final State OBJECT_START = new SkipWhitespace() {
        @Override State consume(Json json, int index, char c, char next) {
            if (c == '}') return json.end(index, Token.OBJECT);
            if (c == '"') return json.begin(index, Token.STRING);
            return super.consume(json, index, c, next);
        }
    };

    private static final State OBJECT_KEY = new SkipWhitespace() {
        @Override State consume(Json json, int index, char c, char next) {
            if (c == '"') return json.begin(index, Token.STRING);
            return super.consume(json, index, c, next);
        }
    };

    private static final State OBJECT_COLON = new SkipWhitespace() {
        @Override State consume(Json json, int index, char c, char next) {
            if (c == ':') return json.begin(index, Token.OBJECT_VALUE);
            return super.consume(json, index, c, next);
        }
    };

    private static final State OBJECT_NEXT = new SkipWhitespace() {
        @Override State consume(Json json, int index, char c, char next) {
            if (c == '}') return json.end(index, Token.OBJECT);
            if (c == ',') return OBJECT_KEY;
            return super.consume(json, index, c, next);
        }
    };

    private static final State STRING = new State() {
        @Override State consume(Json json, int index, char c, char next) {
            if (c == '"') return json.end(index, Token.STRING);
            if (c == '\\') return STRING_ESCAPED;
            if (c <= 31) return super.consume(json, index, c, next);
            return this;
        }
    };

    private static final State STRING_ESCAPED = new State() {
        @Override State consume(Json json, int index, char c, char next) {
            for (char e : ESCAPED_CHARS) if (c == e) return STRING;
            if (c == 'u') return UNICODE_1;
            return super.consume(json, index, c, next);
        }
    };

    private static final State UNICODE_1 = new State() {
        @Override State consume(Json json, int index, char c, char next) {
            for (char h : HEX_CHARS) if (c == h) return UNICODE_2;
            return super.consume(json, index, c, next);
        }
    };

    private static final State UNICODE_2 = new State() {
        @Override State consume(Json json, int index, char c, char next) {
            for (char h : HEX_CHARS) if (c == h) return UNICODE_3;
            return super.consume(json, index, c, next);
        }
    };

    private static final State UNICODE_3 = new State() {
        @Override State consume(Json json, int index, char c, char next) {
            for (char h : HEX_CHARS) if (c == h) return UNICODE_4;
            return super.consume(json, index, c, next);
        }
    };

    private static final State UNICODE_4 = new State() {
        @Override State consume(Json json, int index, char c, char next) {
            for (char h : HEX_CHARS) if (c == h) return STRING;
            return super.consume(json, index, c, next);
        }
    };

    private class Element {
        final Token token;
        final int from;
        int to;
        int contained;

        Element(Token token, int from) {
            this.token = token;
            this.from = from;
        }

        public String getRaw() {
            return raw.substring(from, to + 1);
        }

        public String getRawString() {
            return raw.substring(from + 1, to);
        }

        @Override public String toString() {
            return "Element{" + token + " [" + from + "-" + to + "] " + contained + " " + getRaw() + "\n";
        }
    }

    private class Frame {
        final Token token;
        final int element;

        Frame(Token token, int element) {
            this.token = token;
            this.element = element;
        }
    }

    public static JsonValue parse(String raw) {
        if (raw == null) throw new ParseException("cannot parse null");
        if (raw.isEmpty()) throw new ParseException("cannot parse empty string");
        Json json = new Json(raw);
        return json.parse();
    }

    private String raw;
    private List<Element> elements = new ArrayList<>();
    private Stack<Frame> stack = new Stack<>();

    private Json(String raw) {
        this.raw = raw;
    }

    public JsonValue parse() {
        State state = VALUE;
        int last = raw.length() - 1;
        for (int i = 0; i < last; i++) {
            state = state.consume(this, i, raw.charAt(i), raw.charAt(i + 1));
        }
        state = state.consume(this, last, raw.charAt(last), ' ');
        if (state != END) throw new ParseException("unbalanced json");
        return create(0);
    }

    JsonValue create(int element) {
        Token token = elements.get(element).token;
        if (token == Token.ARRAY) {
            return new JsonArray(element);
        } else if (token == Token.OBJECT) {
            return new JsonObject(element);
        } else {
            return new JsonValue(element);
        }
    }

    State begin(int index, Token token) {
        System.out.println("BEGIN " + token);
        if (token != Token.OBJECT_VALUE) {
            elements.add(new Element(token, index));
        }
        stack.add(new Frame(token, elements.size() - 1));
        if (token == Token.NULL) return NULL_1;
        if (token == Token.TRUE) return TRUE_1;
        if (token == Token.FALSE) return FALSE_1;
        if (token == Token.ARRAY) return ARRAY_START;
        if (token == Token.OBJECT) return OBJECT_START;
        if (token == Token.OBJECT_VALUE) return VALUE;
        if (token == Token.STRING) return STRING;
        if (token == Token.NUMBER) return NUMBER_START;
        throw new ParseException("illegal state: " + token);
    }

    State end(int index, Token token) {
        System.out.println("END " + token);
        Frame last = stack.pop();
        if (last.token != token) throw new ParseException(token);
        Element element = elements.get(last.element);
        element.to = index;
        element.contained = elements.size() - last.element - 1;
        if (stack.empty()) return END;
        if (Token.ARRAY == stack.peek().token) return ARRAY_NEXT;
        if (Token.OBJECT == stack.peek().token) return OBJECT_COLON;
        if (Token.OBJECT_VALUE == stack.peek().token) {
            stack.pop();
            return OBJECT_NEXT;
        }
        throw new ParseException("illegal state: " + token);
    }

    @Override
    public String toString() {
        return String.join("\n", elements.stream().map(e -> e.toString()).collect(Collectors.toList()));
    }

    public class JsonValue {

        protected final int element;

        JsonValue(int element) {
            this.element = element;
        }

        public boolean isNull() {
            return element().token == Token.NULL;
        }

        public boolean isBoolean() {
            return element().token == Token.TRUE || element().token == Token.FALSE;
        }

        public boolean isNumber() {
            return element().token == Token.NUMBER;
        }

        public boolean isString() {
            return element().token == Token.STRING;
        }

        public boolean isArray() {
            return element().token == Token.ARRAY;
        }

        public boolean isObject() {
            return element().token == Token.OBJECT;
        }

        public boolean asBoolean() {
            if (!isBoolean()) throw new IllegalStateException("not a boolean");
            return Boolean.valueOf(element().getRaw());
        }

        public double asNumber() {
            if (!isNumber()) throw new IllegalStateException("not a number");
            return Double.valueOf(element().getRaw());
        }

        public String asString() {
            if (!isString()) throw new IllegalStateException("not a string");
            return element().getRawString(); // todo: convert escaped chars
        }

        public JsonArray asArray() {
            if (!isArray()) throw new IllegalStateException("not an array");
            return (JsonArray)this;
        }

        public JsonObject asObject() {
            if (!isObject()) throw new IllegalStateException("not an object");
            return (JsonObject)this;
        }

        protected Element element() {
            return elements.get(element);
        }
    }

    public class JsonArray extends JsonValue {

        private List<JsonValue> values;

        JsonArray(int element) {
            super(element);
        }

        int size() {
            return element().contained;
        }

        List<JsonValue> getValues() {
            if (values == null) values = Collections.unmodifiableList(createValues());
            return values;
        }

        private List<JsonValue> createValues() {
            List<JsonValue> result = new ArrayList(size());
            for (int i = 0; i < size(); i++) {
                result.add(create(element + i + 1));
            }
            return result;
        }
    }

    public class JsonObject extends JsonValue {

        private Map<String, JsonValue> values;

        JsonObject(int element) {
            super(element);
        }

        int size() {
            return element().contained / 2;
        }

        Map<String, JsonValue> getValues() {
            if (values == null) values = Collections.unmodifiableMap(createValues());
            return values;
        }

        private Map<String, JsonValue> createValues() {
            Map<String, JsonValue> result = new HashMap<>(size());
            for (int i = 0; i < size(); i++) {
                String key = elements.get(element + 2 * i + 1).getRawString();
                result.put(key, create(element + 2 * i + 2));
            }
            return result;
        }
    }


    public static void main(String[] args) throws IOException {
//        String input = "   [null, [ \"hello world\", [], true ], null]  ";
//        String input = "{ \"foo\": [true, false] }";
//        String input = new String(Files.readAllBytes(Paths.get("test/sample.json")));
        String input = "0.33e+4";
        JsonValue value = Json.parse(input);
        System.out.println(value.asNumber());
    }

}

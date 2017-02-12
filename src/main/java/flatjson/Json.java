package flatjson;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

public class Json {

    enum Token {
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

    private class State {
        State consume(int index, char c, char next) {
            throw new ParseException(c);
        }
    }

    private class SkipWhitespace extends State {
        @Override
        State consume(int index, char c, char next) {
            for (char w : WHITESPACE_CHARS) if (c == w) return this;
            return super.consume(index, c, next);
        }
    }

    private class Value extends SkipWhitespace {
        @Override
        State consume(int index, char c, char next) {
            if (c == 'n') return beginElement(index, Token.NULL);
            if (c == 't') return beginElement(index, Token.TRUE);
            if (c == 'f') return beginElement(index, Token.FALSE);
            if (c == '"') return beginElement(index, Token.STRING);
            if (c == '[') return beginElement(index, Token.ARRAY);
            if (c == '{') return beginElement(index, Token.OBJECT);
            if (c == '0') {
                if (next >= '0' && next <= '9') throw new ParseException(c);
                if (next == '.') return beginElement(index, Token.NUMBER);
                if (next == 'e' || next == 'E') {
                    beginElement(index, Token.NUMBER);
                    return EXPONENT;
                }
                beginElement(index, Token.NUMBER);
                return endElement(index, Token.NUMBER);
            }
            if (c >= '1' && c <= '9') {
                if (next >= '0' && next <= '9') return beginElement(index, Token.NUMBER);
                if (next == '.') return beginElement(index, Token.NUMBER);
                if (next == 'e' || next == 'E') {
                    beginElement(index, Token.NUMBER);
                    return EXPONENT;
                }
                beginElement(index, Token.NUMBER);
                return endElement(index, Token.NUMBER);
            }
            if (c == '-') {
                beginElement(index, Token.NUMBER);
                return NUMBER_NEGATIVE;
            }
            return super.consume(index, c, next);
        }
    }

    private final State VALUE = new Value();

    private final State END = new SkipWhitespace();

    private final State NULL_1 = new State() {
        @Override State consume(int index, char c, char next) {
            if (c == 'u') return NULL_2;
            return super.consume(index, c, next);
        }
    };

    private final State NULL_2 = new State() {
        @Override State consume(int index, char c, char next) {
            if (c == 'l') return NULL_3;
            return super.consume(index, c, next);
        }
    };

    private final State NULL_3 = new State() {
        @Override State consume(int index, char c, char next) {
            if (c == 'l') return endElement(index, Token.NULL);
            return super.consume(index, c, next);
        }
    };

    private final State TRUE_1 = new State() {
        @Override State consume(int index, char c, char next) {
            if (c == 'r') return TRUE_2;
            return super.consume(index, c, next);
        }
    };

    private final State TRUE_2 = new State() {
        @Override State consume(int index, char c, char next) {
            if (c == 'u') return TRUE_3;
            return super.consume(index, c, next);
        }
    };

    private final State TRUE_3 = new State() {
        @Override State consume(int index, char c, char next) {
            if (c == 'e') return endElement(index, Token.TRUE);
            return super.consume(index, c, next);
        }
    };

    private final State FALSE_1 = new State() {
        @Override State consume(int index, char c, char next) {
            if (c == 'a') return FALSE_2;
            return super.consume(index, c, next);
        }
    };

    private final State FALSE_2 = new State() {
        @Override State consume(int index, char c, char next) {
            if (c == 'l') return FALSE_3;
            return super.consume(index, c, next);
        }
    };

    private final State FALSE_3 = new State() {
        @Override State consume(int index, char c, char next) {
            if (c == 's') return FALSE_4;
            return super.consume(index, c, next);
        }
    };

    private final State FALSE_4 = new State() {
        @Override State consume(int index, char c, char next) {
            if (c == 'e') return endElement(index, Token.FALSE);
            return super.consume(index, c, next);
        }
    };

    private final State NUMBER_NEGATIVE = new State() {
        @Override State consume(int index, char c, char next) {
            if (c == '0') {
                if (next >= '0' && next <= '9') return super.consume(index, c, next);
                if (next == '.') return NUMBER_DOT;
                if (next == 'e' || next == 'E') return EXPONENT;
                return endElement(index, Token.NUMBER);
            }
            if (c >= '1' && c <= '9') {
                if (next >= '0' && next <= '9') return NUMBER_START;
                if (next == '.') return NUMBER_DOT;
                if (next == 'e' || next == 'E') return EXPONENT;
                return endElement(index, Token.NUMBER);

            }
            return super.consume(index, c, next);
        }
    };

    private final State NUMBER_START = new State() {
        @Override State consume(int index, char c, char next) {
            if (c == '.') return NUMBER_FRACTION;
            if (c >= '0' && c <= '9') {
                if (next >= '0' && next <= '9') return NUMBER_START;
                if (next == '.') return NUMBER_DOT;
                if (next == 'e' || next == 'E') return EXPONENT;
                return endElement(index, Token.NUMBER);
            }
            return super.consume(index, c, next);
        }
    };

    private final State NUMBER_DOT = new State() {
        @Override State consume(int index, char c, char next) {
            return NUMBER_FRACTION;
        }
    };

    private final State NUMBER_FRACTION = new State() {
        @Override State consume(int index, char c, char next) {
            if (c >= '0' && c <= '9') {
                if (next >= '0' && next <= '9') return NUMBER_FRACTION;
                if (next == 'e' || next == 'E') return EXPONENT;
                return endElement(index, Token.NUMBER);
            }
            return super.consume(index, c, next);
        }
    };

    private final State EXPONENT = new State() {
        @Override State consume(int index, char c, char next) {
            if (next == '+' || next == '-') return EXPONENT_SIGN;
            return EXPONENT_DIGITS;
        }
    };

    private final State EXPONENT_SIGN = new State() {
        @Override State consume(int index, char c, char next) {
            return EXPONENT_DIGITS;
        }
    };

    private final State EXPONENT_DIGITS = new State() {
        @Override State consume(int index, char c, char next) {
            if (c >= '0' && c <= '9') {
                if (next >= '0' && next <= '9') return EXPONENT_DIGITS;
                return endElement(index, Token.NUMBER);
            }
            return super.consume(index, c, next);
        }
    };

    private final State ARRAY_START = new Value() {
        @Override State consume(int index, char c, char next) {
            if (c == ']') return endElement(index, Token.ARRAY);
            return super.consume(index, c, next);
        }
    };

    private final State ARRAY_NEXT = new SkipWhitespace() {
        @Override State consume(int index, char c, char next) {
            if (c == ']') return endElement(index, Token.ARRAY);
            if (c == ',') return VALUE;
            return super.consume(index, c, next);
        }
    };

    private final State OBJECT_START = new SkipWhitespace() {
        @Override State consume(int index, char c, char next) {
            if (c == '}') return endElement(index, Token.OBJECT);
            if (c == '"') return beginElement(index, Token.STRING);
            return super.consume(index, c, next);
        }
    };

    private final State OBJECT_KEY = new SkipWhitespace() {
        @Override State consume(int index, char c, char next) {
            if (c == '"') return beginElement(index, Token.STRING);
            return super.consume(index, c, next);
        }
    };

    private final State OBJECT_COLON = new SkipWhitespace() {
        @Override State consume(int index, char c, char next) {
            if (c == ':') return beginElement(index, Token.OBJECT_VALUE);
            return super.consume(index, c, next);
        }
    };

    private final State OBJECT_NEXT = new SkipWhitespace() {
        @Override State consume(int index, char c, char next) {
            if (c == '}') return endElement(index, Token.OBJECT);
            if (c == ',') return OBJECT_KEY;
            return super.consume(index, c, next);
        }
    };

    private final State STRING = new State() {
        @Override State consume(int index, char c, char next) {
            if (c == '"') return endElement(index, Token.STRING);
            if (c == '\\') return STRING_ESCAPED;
            if (c <= 31) return super.consume(index, c, next);
            return this;
        }
    };

    private final State STRING_ESCAPED = new State() {
        @Override State consume(int index, char c, char next) {
            for (char e : ESCAPED_CHARS) if (c == e) return STRING;
            if (c == 'u') return UNICODE_1;
            return super.consume(index, c, next);
        }
    };

    private final State UNICODE_1 = new State() {
        @Override State consume(int index, char c, char next) {
            for (char h : HEX_CHARS) if (c == h) return UNICODE_2;
            return super.consume(index, c, next);
        }
    };

    private final State UNICODE_2 = new State() {
        @Override State consume(int index, char c, char next) {
            for (char h : HEX_CHARS) if (c == h) return UNICODE_3;
            return super.consume(index, c, next);
        }
    };

    private final State UNICODE_3 = new State() {
        @Override State consume(int index, char c, char next) {
            for (char h : HEX_CHARS) if (c == h) return UNICODE_4;
            return super.consume(index, c, next);
        }
    };

    private final State UNICODE_4 = new State() {
        @Override State consume(int index, char c, char next) {
            for (char h : HEX_CHARS) if (c == h) return STRING;
            return super.consume(index, c, next);
        }
    };

    class Element {
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

        @Override
        public String toString() {
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

    private final String raw;
    private final List<Element> elements;
    private final Stack<Frame> stack;

    private Json(String raw) {
        this.raw = raw;
        this.elements = new ArrayList<>();
        this.stack = new Stack<>();
    }

    Element getElement(int index) {
        return elements.get(index);
    }

    JsonValue createValue(int element) {
        Token token = elements.get(element).token;
        if (token == Token.ARRAY) {
            return new JsonArray(this, element);
        } else if (token == Token.OBJECT) {
            return new JsonObject(this, element);
        } else {
            return new JsonValue(this, element);
        }
    }

    private JsonValue parse() {
        State state = VALUE;
        int last = raw.length() - 1;
        for (int i = 0; i < last; i++) {
            state = state.consume(i, raw.charAt(i), raw.charAt(i + 1));
        }
        state = state.consume(last, raw.charAt(last), ' ');
        if (state != END) throw new ParseException("unbalanced json");
        return createValue(0);
    }

    private State beginElement(int index, Token token) {
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

    private State endElement(int index, Token token) {
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

    @Override public String toString() {
        return String.join("\n", elements.stream().map(Element::toString).collect(Collectors.toList()));
    }

    public static JsonValue parse(String raw) {
        if (raw == null) throw new ParseException("cannot parse null");
        if (raw.isEmpty()) throw new ParseException("cannot parse empty string");
        return new Json(raw).parse();
    }

}

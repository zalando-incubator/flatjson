package flatjson;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

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
        STRING,
        ARRAY,
        OBJECT,
        OBJECT_VALUE
    }

    private static final char[] HEX_CHARS = "0123456789abcdefABCDEF".toCharArray();
    private static final char[] ESCAPED_CHARS = "\"\\/bfnrt".toCharArray();
    private static final char[] WHITESPACE_CHARS = " \t\r\n".toCharArray();

    private static class State {
        State consume(Json json, int index, char c) {
            throw new ParseException(c);
        }
    }

    private static class SkipWhitespace extends State {
        @Override State consume(Json json, int index, char c) {
            for (char w : WHITESPACE_CHARS) if (c == w) return this;
            return super.consume(json, index, c);
        }
    }

    private static class Value extends SkipWhitespace {
        @Override State consume(Json json, int index, char c) {
            if (c == 'n') {
                json.begin(index, Token.NULL);
                return NULL_1;
            } else if (c == 't') {
                json.begin(index, Token.TRUE);
                return TRUE_1;
            } else if (c == 'f') {
                json.begin(index, Token.FALSE);
                return FALSE_1;
            } else if (c == '[') {
                json.begin(index, Token.ARRAY);
                return ARRAY_START;
            } else if (c == '{') {
                json.begin(index, Token.OBJECT);
                return OBJECT_START;
            } else if (c == '"') {
                json.begin(index, Token.STRING);
                return STRING;
            } else return super.consume(json, index, c);
        }
    }

    private static final State VALUE = new Value();

    private static final State END = new SkipWhitespace();

    private static final State NULL_1 = new State() {
        @Override State consume(Json json, int index, char c) {
            if (c == 'u') return NULL_2;
            else return super.consume(json, index, c);
        }
    };

    private static final State NULL_2 = new State() {
        @Override State consume(Json json, int index, char c) {
            if (c == 'l') return NULL_3;
            else return super.consume(json, index, c);
        }
    };

    private static final State NULL_3 = new State() {
        @Override State consume(Json json, int index, char c) {
            if (c == 'l') return json.end(index, Token.NULL);
            else return super.consume(json, index, c);
        }
    };

    private static final State TRUE_1 = new State() {
        @Override State consume(Json json, int index, char c) {
            if (c == 'r') return TRUE_2;
            else return super.consume(json, index, c);
        }
    };

    private static final State TRUE_2 = new State() {
        @Override State consume(Json json, int index, char c) {
            if (c == 'u') return TRUE_3;
            else return super.consume(json, index, c);
        }
    };

    private static final State TRUE_3 = new State() {
        @Override State consume(Json json, int index, char c) {
            if (c == 'e') return json.end(index, Token.TRUE);
            else return super.consume(json, index, c);
        }
    };

    private static final State FALSE_1 = new State() {
        @Override State consume(Json json, int index, char c) {
            if (c == 'a') return FALSE_2;
            else return super.consume(json, index, c);
        }
    };

    private static final State FALSE_2 = new State() {
        @Override State consume(Json json, int index, char c) {
            if (c == 'l') return FALSE_3;
            else return super.consume(json, index, c);
        }
    };

    private static final State FALSE_3 = new State() {
        @Override State consume(Json json, int index, char c) {
            if (c == 's') return FALSE_4;
            else return super.consume(json, index, c);
        }
    };

    private static final State FALSE_4 = new State() {
        @Override State consume(Json json, int index, char c) {
            if (c == 'e') return json.end(index, Token.FALSE);
            else return super.consume(json, index, c);
        }
    };

    private static final State ARRAY_START = new Value() {
        @Override State consume(Json json, int index, char c) {
            if (c == ']') return json.end(index, Token.ARRAY);
            else return super.consume(json, index, c);
        }
    };

    private static final State ARRAY_NEXT = new SkipWhitespace() {
        @Override State consume(Json json, int index, char c) {
            if (c == ']') return json.end(index, Token.ARRAY);
            else if (c == ',') return VALUE;
            else return super.consume(json, index, c);
        }
    };

    private static final State OBJECT_START = new SkipWhitespace() {
        @Override State consume(Json json, int index, char c) {
            if (c == '}') return json.end(index, Token.OBJECT);
            else if (c == '"') {
                json.begin(index, Token.STRING);
                return STRING;
            } else return super.consume(json, index, c);
        }
    };

    private static final State OBJECT_KEY = new SkipWhitespace() {
        @Override State consume(Json json, int index, char c) {
            if (c == '"') {
                json.begin(index, Token.STRING);
                return STRING;
            } else return super.consume(json, index, c);
        }
    };

    private static final State OBJECT_COLON = new SkipWhitespace() {
        @Override State consume(Json json, int index, char c) {
            if (c == ':') {
                json.begin(index, Token.OBJECT_VALUE);
                return VALUE;
            } else return super.consume(json, index, c);
        }
    };

    private static final State OBJECT_NEXT = new SkipWhitespace() {
        @Override State consume(Json json, int index, char c) {
            if (c == '}') return json.end(index, Token.OBJECT);
            else if (c == ',') return OBJECT_KEY;
            return super.consume(json, index, c);
        }
    };

    private static final State STRING = new State() {
        @Override State consume(Json json, int index, char c) {
            if (c == '"') return json.end(index, Token.STRING);
            else if (c == '\\') return STRING_ESCAPED;
            else if (c <= 31) return super.consume(json, index, c);
            else return this;
        }
    };

    private static final State STRING_ESCAPED = new State() {
        @Override State consume(Json json, int index, char c) {
            for (char e : ESCAPED_CHARS) if (c == e) return STRING;
            if (c == 'u') return UNICODE_1;
            else return super.consume(json, index, c);
        }
    };

    private static final State UNICODE_1 = new State() {
        @Override State consume(Json json, int index, char c) {
            for (char h : HEX_CHARS) if (c == h) return UNICODE_2;
            return super.consume(json, index, c);
        }
    };

    private static final State UNICODE_2 = new State() {
        @Override State consume(Json json, int index, char c) {
            for (char h : HEX_CHARS) if (c == h) return UNICODE_3;
            return super.consume(json, index, c);
        }
    };

    private static final State UNICODE_3 = new State() {
        @Override State consume(Json json, int index, char c) {
            for (char h : HEX_CHARS) if (c == h) return UNICODE_4;
            return super.consume(json, index, c);
        }
    };

    private static final State UNICODE_4 = new State() {
        @Override State consume(Json json, int index, char c) {
            for (char h : HEX_CHARS) if (c == h) return STRING;
            return super.consume(json, index, c);
        }
    };

    public static Json parse(String input) {
        Json json = new Json(input);
        State state = VALUE;
        for (int index = 0; index < input.length(); index++) {
            state = state.consume(json, index, input.charAt(index));
        }
        if (state != END) throw new ParseException("unbalanced json");
        return json;
    }

    private String _raw;
    private List<Integer> _indexes = new ArrayList<>();
    private List<Token> _tokens = new ArrayList<>();
    private Stack<Token> _stack = new Stack();

    private Json(String raw) {
        _raw = raw;
    }

    void begin(int index, Token token) {
        System.out.println("BEGIN " + token);
        _stack.add(token);
        if (token != Token.OBJECT_VALUE) {
            _indexes.add(index);
            _tokens.add(token);
        }
    }

    State end(int index, Token token) {
        System.out.println("END " + token);
        Token last = _stack.pop();
        if (last != token) throw new ParseException(token);
        _indexes.add(index);
        _tokens.add(token);
        if (_stack.empty()) {
            return END;
        } else if (Token.OBJECT_VALUE == _stack.peek()) {
            _stack.pop();
            return OBJECT_NEXT;
        } else if (Token.ARRAY == _stack.peek()) {
            return ARRAY_NEXT;
        } else if (Token.OBJECT == _stack.peek()) {
            return OBJECT_COLON;
        } else {
            throw new ParseException("illegal state: " + token);
        }
    }

    public List<Integer> getIndexes() {
        return _indexes;
    }

    public List<Token> getTokens() {
        return _tokens;
    }

    @Override
    public String toString() {
        return Arrays.toString(_indexes.toArray()) + Arrays.toString(_tokens.toArray());
    }

    public static void main(String[] args) {
//        String input = "   [null, [ \"hello world\" ], null]  ";
        String input = "{ \"foo\": null }";
        Json json = parse(input);
        System.out.println(json);
    }

}

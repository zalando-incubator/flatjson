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
        ARRAY
    }

    private static class State {
        State consume(Json json, int index, char c) {
            throw new ParseException(c);
        }
    }

    private static class SkipWhitespace extends State {
        @Override State consume(Json json, int index, char c) {
            if (c == ' ') return this;
            else return super.consume(json, index, c);
        }
    }

    private static class Value extends SkipWhitespace {
        @Override State consume(Json json, int index, char c) {
            if (c == 'n') {
                json.begin(index, Token.NULL);
                return N;
            } else if (c == 't') {
                json.begin(index, Token.TRUE);
                return T;
            } else if (c == '[') {
                json.begin(index, Token.ARRAY);
                return ARRAY;
            } else return super.consume(json, index, c);
        }
    }

    private static final State END = new SkipWhitespace() {};

    private static final State NUL = new State() {
        @Override State consume(Json json, int index, char c) {
            if (c == 'l') {
                return json.end(index, Token.NULL);
            } else return super.consume(json, index, c);
        }
    };

    private static final State NU = new State() {
        @Override State consume(Json json, int index, char c) {
            if (c == 'l') return NUL;
            else return super.consume(json, index, c);
        }
    };

    private static final State N = new State() {
        @Override State consume(Json json, int index, char c) {
            if (c == 'u') return NU;
            else return super.consume(json, index, c);
        }
    };

    private static final State TRU = new State() {
        @Override State consume(Json json, int index, char c) {
            if (c == 'e') {
                return json.end(index, Token.TRUE);
            } else return super.consume(json, index, c);
        }
    };

    private static final State TR = new State() {
        @Override State consume(Json json, int index, char c) {
            if (c == 'u') return TRU;
            else return super.consume(json, index, c);
        }
    };

    private static final State T = new State() {
        @Override State consume(Json json, int index, char c) {
            if (c == 'r') return TR;
            else return super.consume(json, index, c);
        }
    };

    private static final State VALUE = new Value();

    private static final State ARRAY = new Value() {
        @Override State consume(Json json, int index, char c) {
            if (c == ']') {
                return json.end(index, Token.ARRAY);
            } else return super.consume(json, index, c);
        }
    };

    private static final State ARRAY_NEXT = new Value() {
        @Override State consume(Json json, int index, char c) {
            if (c == ']') {
                return json.end(index, Token.ARRAY);
            } else if (c == ',') {
                return VALUE;
            } else return super.consume(json, index, c);
        }
    };

    public static Json parse(String input) {
        Json json = new Json(input);
        State state = VALUE;
        for (int index = 0; index < input.length(); index++) {
            state = state.consume(json, index, input.charAt(index));
        }
        return json;
    }

    public static void main(String[] args) {
        String input = "   [ null ]  ";
        Json json = parse(input);
        System.out.println(json);
    }

    private String _raw;
    private List<Integer> _indexes = new ArrayList<>();
    private List<Token> _tokens = new ArrayList<>();
    private Stack<Token> _stack = new Stack();

    private Json(String raw) {
        _raw = raw;
    }

    void begin(int index, Token token) {
        _stack.add(token);
        _indexes.add(index);
        _tokens.add(token);
    }

    State end(int index, Token token) {
        Token last = _stack.pop();
        if (last != token) throw new ParseException(token);
        _indexes.add(index);
        _tokens.add(token);
        if (_stack.empty()) {
            return END;
        } else if (Token.ARRAY == _stack.peek()) {
            return ARRAY_NEXT;
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
}

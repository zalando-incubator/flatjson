package flatjson;

public class Parser {

    public static class ParseException extends RuntimeException {
        public ParseException(char c) {
            super("invalid char: '" + c + "'");
        }
    }

    public enum Token {
        NULL_BEGIN,
        NULL_END,
        TRUE_BEGIN,
        TRUE_END
    }

    public static class State {
        State consume(Json json, char c) {
            throw new ParseException(c);
        }
    }

    public static class SkipWhitespace extends State {
        @Override State consume(Json json, char c) {
            if (c == ' ') return this;
            else return super.consume(json, c);
        }
    }

    private static final State END = new SkipWhitespace() {
    };

    private static final State NUL = new State() {
        @Override State consume(Json json, char c) {
            if (c == 'l') {
                json.addToken(Token.NULL_END);
                return END;
            }
            else return super.consume(json, c);
        }
    };

    private static final State NU = new State() {
        @Override State consume(Json json, char c) {
            if (c == 'l') return NUL;
            else return super.consume(json, c);
        }
    };

    private static final State N = new State() {
        @Override State consume(Json json, char c) {
            if (c == 'u') return NU;
            else return super.consume(json, c);
        }
    };

    private static final State TRU = new State() {
        @Override State consume(Json json, char c) {
            if (c == 'e') {
                json.addToken(Token.TRUE_END);
                return END;
            }
            else return super.consume(json, c);
        }
    };

    private static final State TR = new State() {
        @Override State consume(Json json, char c) {
            if (c == 'u') return TRU;
            else return super.consume(json, c);
        }
    };

    private static final State T = new State() {
        @Override State consume(Json json, char c) {
            if (c == 'r') return TR;
            else return super.consume(json, c);
        }
    };
    private static final State VALUE = new SkipWhitespace() {
        State consume(Json json, char c) {
            if (c == 'n') {
                json.addToken(Token.NULL_BEGIN);
                return N;
            } else if (c == 't') {
                json.addToken(Token.TRUE_BEGIN);
                return T;
            } else return super.consume(json, c);
        }
    };

    public static Json parse(String input) {
        Json json = new Json(input);
        State state = VALUE;
        for (int i = 0; i < input.length(); i++) {
            state = state.consume(json, input.charAt(i));
        }
        return json;
    }

    public static void main(String[] args) {
        Json json = parse("null");
        System.out.println(json);
    }

}

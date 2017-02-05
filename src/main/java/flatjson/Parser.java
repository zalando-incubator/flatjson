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
        State consume(Json json, int index, char c) {
            throw new ParseException(c);
        }
    }

    public static class SkipWhitespace extends State {
        @Override State consume(Json json, int index, char c) {
            if (c == ' ') return this;
            else return super.consume(json, index, c);
        }
    }

    private static final State END = new SkipWhitespace() {};

    private static final State NUL = new State() {
        @Override State consume(Json json, int index, char c) {
            if (c == 'l') {
                json.addToken(index, Token.NULL_END);
                return END;
            }
            else return super.consume(json, index, c);
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
                json.addToken(index, Token.TRUE_END);
                return END;
            }
            else return super.consume(json, index, c);
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
    private static final State VALUE = new SkipWhitespace() {
        State consume(Json json, int index, char c) {
            if (c == 'n') {
                json.addToken(index, Token.NULL_BEGIN);
                return N;
            } else if (c == 't') {
                json.addToken(index, Token.TRUE_BEGIN);
                return T;
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
        String input = "      null";
        Json json = parse(input);
        System.out.println(json);
    }

}

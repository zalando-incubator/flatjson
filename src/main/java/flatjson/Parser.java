package flatjson;

public class Parser {

    public static class ParseException extends RuntimeException {
        public ParseException(char c) {
            super("invalid char: '" + c + "'");
        }
    }

    public enum Token {
        NULL_BEGIN,
        NULL_END
    }

    public static class Index {
        void addToken(Token token) {
            System.out.println(token);
        }
    }

    public static class State {
        State consume(Index index, char c) {
            throw new ParseException(c);
        }
    }

    public static class SkipWhitespace extends State {
        @Override State consume(Index index, char c) {
            if (c == ' ') return this;
            else return super.consume(index, c);
        }
    }

    private static final State NULL = new SkipWhitespace() {
    };

    private static final State NUL = new State() {
        @Override State consume(Index index, char c) {
            if (c == 'l') {
                index.addToken(Token.NULL_END);
                return NULL;
            }
            else return super.consume(index, c);
        }
    };

    private static final State NU = new State() {
        @Override State consume(Index index, char c) {
            if (c == 'l') return NUL;
            else return super.consume(index, c);
        }
    };

    private static final State N = new State() {
        @Override State consume(Index index, char c) {
            if (c == 'u') return NU;
            else return super.consume(index, c);
        }
    };

    private static final State VALUE = new SkipWhitespace() {
        State consume(Index index, char c) {
            if (c == 'n') {
                index.addToken(Token.NULL_BEGIN);
                return N;
            }
            else return super.consume(index, c);
        }
    };

    public static void main(String[] args) {
        Index index = new Index();
        State state = VALUE;
        String json = "  null  ";
        for (int i = 0; i < json.length(); i++) {
            state = state.consume(index, json.charAt(i));
        }
        System.out.println(state == NULL);
    }

}

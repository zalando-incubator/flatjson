package flatjson;

public class ParseException extends RuntimeException {

    public ParseException(String message) {
        super(message);
    }

    public ParseException(Json.Token token) {
        super("illegal token: " + token);
    }

    public ParseException(char c) {
        super("illegal char: '" + c + "'");
    }

}

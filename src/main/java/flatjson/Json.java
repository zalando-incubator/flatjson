package flatjson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Json {

    private String raw;
    private List<Parser.Token> tokens = new ArrayList<>();

    public Json(String raw) {
        this.raw = raw;
    }

    void addToken(Parser.Token token) {
        tokens.add(token);
    }

    public List<Parser.Token> getTokens() {
        return tokens;
    }

    @Override
    public String toString() {
        return Arrays.toString(tokens.toArray());
    }
}

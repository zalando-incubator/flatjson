package flatjson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Json {

    private String raw;
    private List<Integer> _indexes = new ArrayList<>();
    private List<Parser.Token> _tokens = new ArrayList<>();

    public Json(String raw) {
        this.raw = raw;
    }

    void addToken(int index, Parser.Token token) {
        _indexes.add(index);
        _tokens.add(token);
    }

    public List<Integer> getIndexes() {
        return _indexes;
    }

    public List<Parser.Token> getTokens() {
        return _tokens;
    }

    @Override
    public String toString() {
        return Arrays.toString(_indexes.toArray()) + Arrays.toString(_tokens.toArray());
    }
}

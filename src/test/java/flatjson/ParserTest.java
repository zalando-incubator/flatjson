package flatjson;

import flatjson.Parser.Token;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ParserTest {

    @Test public void parseNull() {
        assertEquals(
                asList(Token.NULL_BEGIN, Token.NULL_END),
                Parser.parse("null").getTokens()
        );
    }

    @Test public void parseNullWithWhitespace() {
        assertEquals(
                asList(Token.NULL_BEGIN, Token.NULL_END),
                Parser.parse("   null ").getTokens()
        );
    }

    @Test public void parseTrue() {
        assertEquals(
                asList(Token.TRUE_BEGIN, Token.TRUE_END),
                Parser.parse("true").getTokens()
        );
    }

    private List asList(Object... objects) {
        return Arrays.asList(objects);
    }


}

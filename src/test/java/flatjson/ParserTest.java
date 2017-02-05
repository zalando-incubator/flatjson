package flatjson;

import flatjson.Parser.Token;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class ParserTest {

    @Test public void parseNull() {
        assertEquals(
                Arrays.asList(new Token[] {
                        Token.NULL_BEGIN,
                        Token.NULL_END
                }),
                Parser.parse("null").getTokens()
        );
    }

    @Test public void parseNullWithWhitespace() {
        assertEquals(
                Arrays.asList(new Token[] {
                        Token.NULL_BEGIN,
                        Token.NULL_END
                }),
                Parser.parse("   null ").getTokens()
        );
    }

    @Test public void parseTrue() {
        assertEquals(
                Arrays.asList(new Token[] {
                        Token.TRUE_BEGIN,
                        Token.TRUE_END
                }),
                Parser.parse("true").getTokens()
        );
    }


}

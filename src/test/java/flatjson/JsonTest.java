package flatjson;

import flatjson.Json.Token;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class JsonTest {

    @Test public void parseNull() {
        assertEquals(
                asList(Token.NULL, Token.NULL),
                Json.parse("null").getTokens()
        );
    }

    @Test public void parseNullWithWhitespace() {
        assertEquals(
                asList(Token.NULL, Token.NULL),
                Json.parse("   null ").getTokens()
        );
    }

    @Test public void parseTrue() {
        assertEquals(
                asList(Token.TRUE, Token.TRUE),
                Json.parse("true").getTokens()
        );
    }

    @Test public void parseFalse() {
        try {
            Json.parse("false");
            fail("should raise ParseException");
        } catch (Json.ParseException expected) {}
    }

    @Test public void parseArray() {
        assertEquals(
                asList(Token.ARRAY, Token.ARRAY),
                Json.parse("[]").getTokens()
        );
    }

    private List asList(Object... objects) {
        return Arrays.asList(objects);
    }


}

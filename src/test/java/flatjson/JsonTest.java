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

    @Test public void parseNil() {
        try {
            Json.parse("nil");
            fail("should raise ParseException");
        } catch (Json.ParseException expected) {}
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
        assertEquals(
                asList(Token.FALSE, Token.FALSE),
                Json.parse("false").getTokens()
        );
    }

    @Test public void parseEmptyArray() {
        assertEquals(
                asList(Token.ARRAY, Token.ARRAY),
                Json.parse("[ ]").getTokens()
        );
    }

    @Test public void parseBrokenArray() {
        try {
            Json.parse("[ ,]");
            fail("should raise ParseException");
        } catch (Json.ParseException expected) {}
    }

    @Test public void parseOpenArray() {
        try {
            Json.parse("[ null,");
            fail("should raise ParseException");
        } catch (Json.ParseException expected) {}
    }

    @Test public void parseNestedArray() {
        assertEquals(
                asList(Token.ARRAY, Token.ARRAY, Token.ARRAY, Token.ARRAY),
                Json.parse("[ [ ]]").getTokens()
        );
    }

    @Test public void parseBooleanArray() {
        assertEquals(
                asList(Token.ARRAY, Token.TRUE, Token.TRUE, Token.FALSE, Token.FALSE, Token.ARRAY),
                Json.parse("[ true,false ]").getTokens()
        );
    }

    private List asList(Object... objects) {
        return Arrays.asList(objects);
    }


}

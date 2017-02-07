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
                asList(Token.NULL),
                Json.parse("null").getTokens()
        );
    }

    @Test public void parseBrokenNull() {
        try {
            Json.parse("nul");
            fail("should raise ParseException");
        } catch (Json.ParseException expected) {}
    }

    @Test public void parseNullWithWhitespace() {
        assertEquals(
                asList(Token.NULL),
                Json.parse("   \r\n null \t").getTokens()
        );
    }

    @Test public void parseTrue() {
        assertEquals(
                asList(Token.TRUE),
                Json.parse("true").getTokens()
        );
    }

    @Test public void parseFalse() {
        assertEquals(
                asList(Token.FALSE),
                Json.parse("false").getTokens()
        );
    }

    @Test public void parseEmptyArray() {
        assertEquals(
                asList(Token.ARRAY),
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
                asList(Token.ARRAY, Token.ARRAY),
                Json.parse("[ [ ]]").getTokens()
        );
    }

    @Test public void parseBooleanArray() {
        assertEquals(
                asList(Token.ARRAY, Token.TRUE, Token.FALSE),
                Json.parse("[ true,false ]").getTokens()
        );
    }

    @Test public void parseString() {
        assertEquals(
                asList(Token.STRING),
                Json.parse("\"hello\"").getTokens()
        );
    }

    @Test public void parseStringWithEscapedQuote() {
        assertEquals(
                asList(Token.STRING),
                Json.parse("\"hello \\\"darling\\\"\"").getTokens()
        );
    }

    @Test public void parseStringWithEscapedBackslash() {
        assertEquals(
                asList(Token.STRING),
                Json.parse("\"hello \\\\ world\"").getTokens()
        );
    }

    @Test public void parseStringWithEscapedSlash() {
        assertEquals(
                asList(Token.STRING),
                Json.parse("\"hello \\/ world\"").getTokens()
        );
    }

    @Test public void parseStringWithEscapedBackspace() {
        assertEquals(
                asList(Token.STRING),
                Json.parse("\"hello \\b world\"").getTokens()
        );
    }

    @Test public void parseStringWithEscapedFormfeed() {
        assertEquals(
                asList(Token.STRING),
                Json.parse("\"hello \\f world\"").getTokens()
        );
    }

    @Test public void parseStringWithEscapedNewline() {
        assertEquals(
                asList(Token.STRING),
                Json.parse("\"hello \\n world\"").getTokens()
        );
    }

    @Test public void parseStringWithUnescapedNewline() {
        try {
            Json.parse("\"hello \n world\"");
            fail("should raise ParseException");
        } catch (Json.ParseException expected) {}
    }

    @Test public void parseStringWithEscapedCarriageReturn() {
        assertEquals(
                asList(Token.STRING),
                Json.parse("\"hello \\r world\"").getTokens()
        );
    }

    @Test public void parseStringWithEscapedTab() {
        assertEquals(
                asList(Token.STRING),
                Json.parse("\"hello \\t world\"").getTokens()
        );
    }

    @Test public void parseStringWithEscapedUnicode() {
        assertEquals(
                asList(Token.STRING),
                Json.parse("\"hello \\u2ebf world\"").getTokens()
        );
    }

    @Test public void parseStringWithBrokenUnicode() {
        try {
            Json.parse("\"hello \\u123 world\"");
            fail("should raise ParseException");
        } catch (Json.ParseException expected) {}
    }

    @Test public void parseStringWithControlChar() {
        try {
            Json.parse("\"hello \u0000 world\"");
            fail("should raise ParseException");
        } catch (Json.ParseException expected) {}
    }

    @Test public void parseEmptyObject() {
        assertEquals(
                asList(Token.OBJECT),
                Json.parse("{}").getTokens()
        );
    }

    @Test public void parseObject() {
        assertEquals(
                asList(Token.OBJECT, Token.STRING, Token.TRUE),
                Json.parse("{\"foo\": true }").getTokens()
        );
    }

    private List asList(Object... objects) {
        return Arrays.asList(objects);
    }


}

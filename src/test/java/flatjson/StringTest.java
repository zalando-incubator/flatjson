package flatjson;

import org.junit.Test;

import static org.junit.Assert.*;

public class StringTest {

    @Test public void parseString() {
        Json json = Json.parse("\"hello\"");
        assertTrue(json.isString());
        assertEquals("hello", json.asString());
    }

    @Test public void parseEmptyString() {
        Json json = Json.parse("  \"\"  ");
        assertTrue(json.isString());
        assertEquals("", json.asString());
    }

    @Test(expected = ParseException.class)
    public void parseOpenString() {
        Json.parse("\"hello");
    }

    @Test public void parseStringWithEscapedQuote() {
        Json json = Json.parse("\"hello \\\"quoted\\\" world\"");
        assertEquals("hello \"quoted\" world", json.asString());
    }

    @Test public void parseStringWithEscapedBackslash() {
        Json json = Json.parse("\"hello \\\\ world\"");
        assertEquals("hello \\ world", json.asString());
    }

    @Test public void parseStringWithEscapedSlash() {
        Json json = Json.parse("\"hello \\/ world\"");
        assertEquals("hello / world", json.asString());
    }

    @Test public void parseStringWithEscapedBackspace() {
        Json json = Json.parse("\"hello \\b world\"");
        assertEquals("hello \b world", json.asString());
    }

    @Test public void parseStringWithEscapedFormfeed() {
        Json json = Json.parse("\"hello \\f world\"");
        assertEquals("hello \f world", json.asString());
    }

    @Test public void parseStringWithEscapedNewline() {
        Json json = Json.parse("\"hello \\n world\"");
        assertEquals("hello \n world", json.asString());
    }

    @Test(expected = ParseException.class)
    public void parseStringWithUnescapedNewline() {
        Json.parse("\"hello \n world\"");
    }

    @Test public void parseStringWithEscapedCarriageReturn() {
        Json json = Json.parse("\"hello \\r world\"");
        assertEquals("hello \r world", json.asString());
    }

    @Test public void parseStringWithEscapedTab() {
        Json json = Json.parse("\"hello \\t world\"");
        assertEquals("hello \t world", json.asString());
    }

    @Test public void parseStringWithEscapedUnicode() {
        Json json = Json.parse("\"hello \\u2ebf world\"");
        assertEquals("hello \u2ebf world", json.asString());
    }

    @Test(expected = ParseException.class)
    public void parseStringWithBrokenUnicode() {
        Json.parse("\"hello \\u123 world\"");
    }

    @Test(expected = ParseException.class)
    public void parseStringWithNonHexUnicode() {
        Json.parse("\"hello \\uzzzz world\"");
    }

    @Test(expected = ParseException.class)
    public void parseStringWithControlChar() {
        Json.parse("\"hello \u0000 world\"");
    }

}

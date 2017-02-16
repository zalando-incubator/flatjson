package flatjson;

import org.junit.Test;

import static org.junit.Assert.*;

public class StringTest {

    @Test public void parseEmptyString() {
        JsonValue value = Json.parse("  \"\"  ");
        assertTrue(value.isString());
        assertEquals("", value.asString());
    }

    @Test public void parseString() {
        JsonValue value = Json.parse("\"hello\"");
        assertTrue(value.isString());
        assertEquals("hello", value.asString());
    }

    @Test public void parseStringWithEscapedQuote() {
        JsonValue value = Json.parse("\"hello \\\"quoted\\\" world\"");
        assertEquals("hello \"quoted\" world", value.asString());
    }

    @Test public void parseStringWithEscapedBackslash() {
        JsonValue value = Json.parse("\"hello \\\\ world\"");
        assertEquals("hello \\ world", value.asString());
    }

    @Test public void parseStringWithEscapedSlash() {
        JsonValue value = Json.parse("\"hello \\/ world\"");
        assertEquals("hello / world", value.asString());
    }

    @Test public void parseStringWithEscapedBackspace() {
        JsonValue value = Json.parse("\"hello \\b world\"");
        assertEquals("hello \b world", value.asString());
    }

    @Test public void parseStringWithEscapedFormfeed() {
        JsonValue value = Json.parse("\"hello \\f world\"");
        assertEquals("hello \f world", value.asString());
    }

    @Test public void parseStringWithEscapedNewline() {
        JsonValue value = Json.parse("\"hello \\n world\"");
        assertEquals("hello \n world", value.asString());
    }

    @Test public void parseStringWithUnescapedNewline() {
        try {
            Json.parse("\"hello \n world\"");
            fail("should raise ParseException");
        } catch (ParseException expected) {}
    }

    @Test public void parseStringWithEscapedCarriageReturn() {
        JsonValue value = Json.parse("\"hello \\r world\"");
        assertEquals("hello \r world", value.asString());
    }

    @Test public void parseStringWithEscapedTab() {
        JsonValue value = Json.parse("\"hello \\t world\"");
        assertEquals("hello \t world", value.asString());
    }

    @Test public void parseStringWithEscapedUnicode() {
        JsonValue value = Json.parse("\"hello \\u2ebf world\"");
        assertEquals("hello \u2ebf world", value.asString());
    }

    @Test public void parseStringWithBrokenUnicode() {
        try {
            Json.parse("\"hello \\u123 world\"");
            fail("should raise ParseException");
        } catch (ParseException expected) {}
    }

    @Test public void parseStringWithControlChar() {
        try {
            Json.parse("\"hello \u0000 world\"");
            fail("should raise ParseException");
        } catch (ParseException expected) {}
    }

}

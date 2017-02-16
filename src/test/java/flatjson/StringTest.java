package flatjson;

import org.junit.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
        // todo: unescape quotes
        assertEquals("hello \\\"quoted\\\" world", value.asString());
    }

    @Test public void parseStringWithEscapedBackslash() {
        JsonValue value = Json.parse("\"hello \\\\ world\"");
        // todo: unescape backslash
        assertEquals("hello \\\\ world", value.asString());
    }

    @Test public void parseStringWithEscapedSlash() {
        JsonValue value = Json.parse("\"hello \\/ world\"");
        // todo: unescape slash
        assertEquals("hello \\/ world", value.asString());
    }

    @Test public void parseStringWithEscapedBackspace() {
        JsonValue value = Json.parse("\"hello \\b world\"");
        // todo: unescape backspace
        assertEquals("hello \\b world", value.asString());
    }

    @Test public void parseStringWithEscapedFormfeed() {
        JsonValue value = Json.parse("\"hello \\f world\"");
        // todo: unescape formfeed
        assertEquals("hello \\f world", value.asString());
    }

    @Test public void parseStringWithEscapedNewline() {
        JsonValue value = Json.parse("\"hello \\n world\"");
        // todo: unescape newline
        assertEquals("hello \\n world", value.asString());
    }

    @Test public void parseStringWithUnescapedNewline() {
        try {
            Json.parse("\"hello \n world\"");
            fail("should raise ParseException");
        } catch (ParseException expected) {}
    }

    @Test public void parseStringWithEscapedCarriageReturn() {
        JsonValue value = Json.parse("\"hello \\r world\"");
        // todo: unescape carriage return
        assertEquals("hello \\r world", value.asString());
    }

    @Test public void parseStringWithEscapedTab() {
        JsonValue value = Json.parse("\"hello \\t world\"");
        // todo: unescape tab
        assertEquals("hello \\t world", value.asString());
    }

    @Test public void parseStringWithEscapedUnicode() {
        JsonValue value = Json.parse("\"hello \\u2ebf world\"");
        // todo: unescape unicode
        assertEquals("hello \\u2ebf world", value.asString());
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

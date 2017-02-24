package flatjson;

import org.junit.Test;

import static org.junit.Assert.*;


public class EscapeTest {

    @Test public void escapeEmptyString() {
        assertEquals("", Json.escape(""));
    }

    @Test public void escapeText() {
        String text = "th3 quick brown fox jumps ov3r th3 l4zy dog";
        assertEquals(text, Json.escape(text));
    }

    @Test public void escapeQuote() {
        assertEquals("brown \\\" fox", Json.escape("brown \" fox"));
    }

    @Test public void escapeBackslash() {
        assertEquals("brown \\\\ fox", Json.escape("brown \\ fox"));
    }

    @Test public void escapeSlash() {
        assertEquals("brown / fox", Json.escape("brown / fox"));
    }

    @Test public void escapeControlChars() {
        assertEquals("\\b\\f\\n\\r\\t", Json.escape("\b\f\n\r\t"));
    }

    @Test public void escapeUnicode() {
        assertEquals("\\u2ebf", Json.escape("\u2ebf"));
    }

}

package flatjson;

import org.junit.Test;

import static org.junit.Assert.*;


public class UnescapeTest {

    @Test public void unescapeEmptyString() {
        assertEquals("", Json.unescape(""));
    }

    @Test public void unescapeText() {
        String text = "th3 quick brown fox jumps ov3r th3 l4zy dog";
        assertEquals(text, Json.unescape(text));
    }

    @Test public void unescapeQuote() {
        assertEquals("brown \" fox", Json.unescape("brown \\\" fox"));
    }

    @Test public void unescapeBackslash() {
        assertEquals("brown \\ fox", Json.unescape("brown \\\\ fox"));
    }

    @Test public void unescapeSlash() {
        assertEquals("brown / fox", Json.unescape("brown \\/ fox"));
    }

    @Test public void unescapeControlChars() {
        assertEquals("\b\f\n\r\t", Json.unescape("\\b\\f\\n\\r\\t"));
    }

    @Test public void unescapeUnicode() {
        assertEquals("\u2ebf", Json.unescape("\\u2ebf"));
    }

}

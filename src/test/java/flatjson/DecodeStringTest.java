package flatjson;

import org.junit.Test;

import static org.junit.Assert.*;


public class DecodeStringTest {

    @Test public void decodeEmptyString() {
        assertEquals("", JsonValue.decodeString(""));
    }

    @Test public void decodeText() {
        String text = "th3 quick brown fox jumps ov3r th3 l4zy dog";
        assertEquals(text, JsonValue.decodeString(text));
    }

    @Test public void decodeQuote() {
        assertEquals("brown \" fox", JsonValue.decodeString("brown \\\" fox"));
    }

    @Test public void decodeBackslash() {
        assertEquals("brown \\ fox", JsonValue.decodeString("brown \\\\ fox"));
    }

    @Test public void decodeSlash() {
        assertEquals("brown / fox", JsonValue.decodeString("brown \\/ fox"));
    }

    @Test public void decodeControlChars() {
        assertEquals("\b\f\n\r\t", JsonValue.decodeString("\\b\\f\\n\\r\\t"));
    }

    @Test public void decodeUnicode() {
        assertEquals("\u2ebf", JsonValue.decodeString("\\u2ebf"));
    }

}

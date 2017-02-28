package org.zalando.flatjson;

import org.junit.Test;

import static org.junit.Assert.*;


public class EscapeTest {

    @Test public void escapeEmptyString() {
        assertEquals("", StringCodec.escape(""));
    }

    @Test public void escapeText() {
        String text = "th3 quick brown fox jumps ov3r th3 l4zy dog";
        assertEquals(text, StringCodec.escape(text));
    }

    @Test public void escapeQuote() {
        assertEquals("brown \\\" fox", StringCodec.escape("brown \" fox"));
    }

    @Test public void escapeBackslash() {
        assertEquals("brown \\\\ fox", StringCodec.escape("brown \\ fox"));
    }

    @Test public void escapeSlash() {
        assertEquals("brown / fox", StringCodec.escape("brown / fox"));
    }

    @Test public void escapeControlChars() {
        assertEquals("\\b\\f\\n\\r\\t", StringCodec.escape("\b\f\n\r\t"));
    }

    @Test public void escapeUnicode() {
        assertEquals("\\u2ebf", StringCodec.escape("\u2ebf"));
    }

}

package org.zalando.flatjson;

import org.junit.Test;

import static org.junit.Assert.*;


public class UnescapeTest {

    @Test public void unescapeEmptyString() {
        assertEquals("", StringCodec.unescape(""));
    }

    @Test public void unescapeText() {
        String text = "th3 quick brown fox jumps ov3r th3 l4zy dog";
        assertEquals(text, StringCodec.unescape(text));
    }

    @Test public void unescapeQuote() {
        assertEquals("brown \" fox", StringCodec.unescape("brown \\\" fox"));
    }

    @Test public void unescapeBackslash() {
        assertEquals("brown \\ fox", StringCodec.unescape("brown \\\\ fox"));
    }

    @Test public void unescapeSlash() {
        assertEquals("brown / fox", StringCodec.unescape("brown \\/ fox"));
    }

    @Test public void unescapeControlChars() {
        assertEquals("\b\f\n\r\t", StringCodec.unescape("\\b\\f\\n\\r\\t"));
    }

    @Test public void unescapeUnicode() {
        assertEquals("\u2ebf", StringCodec.unescape("\\u2ebf"));
    }

}

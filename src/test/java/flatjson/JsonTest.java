package flatjson;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class JsonTest {

    @Test public void parseNull() {
        assertTrue(Json.parse("null").isNull());
    }

    @Test public void parseNullWithWhitespace() {
        assertTrue(Json.parse("   \r\n null \t").isNull());
    }

    @Test public void parseBrokenNull() {
        try {
            Json.parse("nul");
            fail("should raise ParseException");
        } catch (Json.ParseException expected) {}
    }

    @Test public void parseTrue() {
        Json.JsonValue value = Json.parse("true");
        assertTrue(value.isBoolean());
        assertTrue(value.asBoolean());
    }

    @Test public void parseFalse() {
        Json.JsonValue value = Json.parse("false");
        assertTrue(value.isBoolean());
        assertFalse(value.asBoolean());
    }

    @Test public void parseEmptyArray() {
        Json.JsonValue value = Json.parse("[ ]");
        assertTrue(value.isArray());
        assertEquals(0, value.asArray().size());
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
        Json.JsonValue value = Json.parse("[ []]");
        assertTrue(value.isArray());
        Json.JsonArray array = value.asArray();
        assertEquals(1, array.size());
        Json.JsonValue first = array.getValues().get(0);
        assertTrue(first.isArray());
        assertEquals(0, first.asArray().size());
    }

    @Test public void parseBooleanArray() {
        Json.JsonValue value = Json.parse("[ true,false ]");
        assertTrue(value.isArray());
        Json.JsonArray array = value.asArray();
        assertEquals(2, array.size());
        List<Json.JsonValue> values = array.getValues();
        assertTrue(values.get(0).asBoolean());
        assertFalse(values.get(1).asBoolean());
    }

    @Test public void parseEmptyString() {
        Json.JsonValue value = Json.parse("  \"\"  ");
        assertTrue(value.isString());
        assertEquals("", value.asString());
    }

    @Test public void parseString() {
        Json.JsonValue value = Json.parse("\"hello\"");
        assertTrue(value.isString());
        assertEquals("hello", value.asString());
    }

    @Test public void parseStringWithEscapedQuote() {
        Json.JsonValue value = Json.parse("\"hello \\\"quoted\\\" world\"");
        // todo: unescape quotes
        assertEquals("hello \\\"quoted\\\" world", value.asString());
    }

    @Test public void parseStringWithEscapedBackslash() {
        Json.JsonValue value = Json.parse("\"hello \\\\ world\"");
        // todo: unescape backslash
        assertEquals("hello \\\\ world", value.asString());
    }

    @Test public void parseStringWithEscapedSlash() {
        Json.JsonValue value = Json.parse("\"hello \\/ world\"");
        // todo: unescape slash
        assertEquals("hello \\/ world", value.asString());
    }

    @Test public void parseStringWithEscapedBackspace() {
        Json.JsonValue value = Json.parse("\"hello \\b world\"");
        // todo: unescape backspace
        assertEquals("hello \\b world", value.asString());
    }

    @Test public void parseStringWithEscapedFormfeed() {
        Json.JsonValue value = Json.parse("\"hello \\f world\"");
        // todo: unescape formfeed
        assertEquals("hello \\f world", value.asString());
    }

    @Test public void parseStringWithEscapedNewline() {
        Json.JsonValue value = Json.parse("\"hello \\n world\"");
        // todo: unescape newline
        assertEquals("hello \\n world", value.asString());
    }

    @Test public void parseStringWithUnescapedNewline() {
        try {
            Json.parse("\"hello \n world\"");
            fail("should raise ParseException");
        } catch (Json.ParseException expected) {}
    }

    @Test public void parseStringWithEscapedCarriageReturn() {
        Json.JsonValue value = Json.parse("\"hello \\r world\"");
        // todo: unescape carriage return
        assertEquals("hello \\r world", value.asString());
    }

    @Test public void parseStringWithEscapedTab() {
        Json.JsonValue value = Json.parse("\"hello \\t world\"");
        // todo: unescape tab
        assertEquals("hello \\t world", value.asString());
    }

    @Test public void parseStringWithEscapedUnicode() {
        Json.JsonValue value = Json.parse("\"hello \\u2ebf world\"");
        // todo: unescape unicode
        assertEquals("hello \\u2ebf world", value.asString());
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

//    @Test public void parseEmptyObject() {
//        assertEquals(
//                asList(Token.OBJECT),
//                Json.parse("{}").getTokens()
//        );
//    }
//
//    @Test public void parseObject() {
//        assertEquals(
//                asList(Token.OBJECT, Token.STRING, Token.TRUE),
//                Json.parse("{\"foo\": true }").getTokens()
//        );
//    }
//
//    @Test public void parseZero() {
//        assertEquals(
//                asList(Token.NUMBER),
//                Json.parse("0").getTokens()
//        );
//    }
//
//    @Test public void parseZeroWithExponent() {
//        assertEquals(
//                asList(Token.NUMBER),
//                Json.parse("0e-23").getTokens()
//        );
//    }
//
//    @Test public void parseMinus() {
//        try {
//            Json.parse("-");
//            fail("should raise ParseException");
//        } catch (Json.ParseException expected) {}
//    }
//
//    @Test public void parseNegativeZero() {
//        assertEquals(
//                asList(Token.NUMBER),
//                Json.parse("-0").getTokens()
//        );
//    }
//
//    @Test public void parseNegativeZeroWithExponent() {
//        assertEquals(
//                asList(Token.NUMBER),
//                Json.parse("-0e-2").getTokens()
//        );
//    }
//
//    @Test public void parseSingleDigit() {
//        assertEquals(
//                asList(Token.NUMBER),
//                Json.parse("3").getTokens()
//        );
//    }
//
//    @Test public void parseSingleDigitWithExponent() {
//        assertEquals(
//                asList(Token.NUMBER),
//                Json.parse("3e+7").getTokens()
//        );
//    }
//
//    @Test public void parseNumberWithLeadingZero() {
//        try {
//            Json.parse("023");
//            fail("should raise ParseException");
//        } catch (Json.ParseException expected) {}
//    }
//
//    @Test public void parseInteger() {
//        assertEquals(
//                asList(Token.NUMBER),
//                Json.parse("23").getTokens()
//        );
//    }
//
//    @Test public void parseVeryLongInteger() {
//        assertEquals(
//                asList(Token.NUMBER),
//                Json.parse("100000000000000023").getTokens()
//        );
//    }
//
//    @Test public void parseNegativeInteger() {
//        assertEquals(
//                asList(Token.NUMBER),
//                Json.parse("-23").getTokens()
//        );
//    }
//
//    @Test public void parseNegativeIntegerWithExponent() {
//        assertEquals(
//                asList(Token.NUMBER),
//                Json.parse("-2e-2").getTokens()
//        );
//    }
//
//    @Test public void parseVeryLongNegativeInteger() {
//        assertEquals(
//                asList(Token.NUMBER),
//                Json.parse("-234567898765432").getTokens()
//        );
//    }
//
//    @Test public void parseNumberWithExponent() {
//        assertEquals(
//                asList(Token.NUMBER),
//                Json.parse("33e12").getTokens()
//        );
//    }
//
//    @Test public void parseNumberWithExponentUppercase() {
//        assertEquals(
//                asList(Token.NUMBER),
//                Json.parse("33E12").getTokens()
//        );
//    }
//
//    @Test public void parseNumberWithExponentPlus() {
//        assertEquals(
//                asList(Token.NUMBER),
//                Json.parse("33E+12").getTokens()
//        );
//    }
//
//    @Test public void parseNumberWithExponentMinus() {
//        assertEquals(
//                asList(Token.NUMBER),
//                Json.parse("33E-12").getTokens()
//        );
//    }
//
//    @Test public void parseNumberWithEmptyExponent() {
//        try {
//            Json.parse("33E+");
//            fail("should raise ParseException");
//        } catch (Json.ParseException expected) {}
//    }
//
//    @Test public void parseNumberWithBrokenExponent() {
//        try {
//            Json.parse("33E++2");
//            fail("should raise ParseException");
//        } catch (Json.ParseException expected) {}
//    }
//
//    @Test public void parseFloat() {
//        assertEquals(
//                asList(Token.NUMBER),
//                Json.parse("3.141").getTokens()
//        );
//    }
//
//    @Test public void parseNegativeFloat() {
//        assertEquals(
//                asList(Token.NUMBER),
//                Json.parse("-3.141").getTokens()
//        );
//    }
//
//    @Test public void parseFloatWithExponent() {
//        assertEquals(
//                asList(Token.NUMBER),
//                Json.parse("-3.141e+4").getTokens()
//        );
//    }
//
//    @Test public void parseFloatWithLeadingZero() {
//        assertEquals(
//                asList(Token.NUMBER),
//                Json.parse("0.33333333").getTokens()
//        );
//    }
//
//    @Test public void parseFloatWithLeadingZeroAndExponent() {
//        assertEquals(
//                asList(Token.NUMBER),
//                Json.parse("0.333E4").getTokens()
//        );
//    }
//
//    @Test public void parseFloatWithComma() {
//        try {
//            Json.parse("3,141");
//            fail("should raise ParseException");
//        } catch (Json.ParseException expected) {}
//    }
//
//    @Test public void parseFloatStartingWithDot() {
//        try {
//            Json.parse(".141");
//            fail("should raise ParseException");
//        } catch (Json.ParseException expected) {}
//    }
//
//    @Test public void parseFloatWithDoubleDot() {
//        try {
//            Json.parse("111.222.333");
//            fail("should raise ParseException");
//        } catch (Json.ParseException expected) {}
//    }
//
//    @Test public void parseNumberArray() {
//        assertEquals(
//                asList(Token.ARRAY, Token.NUMBER, Token.NUMBER),
//                Json.parse("[23, 42]").getTokens()
//        );
//    }
//
//    @Test public void parseMixedNumberArray() {
//        assertEquals(
//                asList(Token.ARRAY, Token.NUMBER, Token.NUMBER, Token.NUMBER, Token.NUMBER),
//                Json.parse("[0, -23, 2e+5, -0.333]").getTokens()
//        );
//    }

    private List asList(Object... objects) {
        return Arrays.asList(objects);
    }


}

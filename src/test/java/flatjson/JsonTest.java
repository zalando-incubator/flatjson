package flatjson;

import org.junit.Test;

import java.util.List;
import java.util.Map;

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

    @Test public void parseNumberArray() {
        Json.JsonValue value = Json.parse("[23,42e8,3.141]");
        assertTrue(value.isArray());
        Json.JsonArray array = value.asArray();
        assertEquals(3, array.size());
        List<Json.JsonValue> values = array.getValues();
        assertEquals(23, values.get(0).asLong());
        assertEquals(42e8, values.get(1).asDouble(), 0);
        assertEquals(3.141, values.get(2).asDouble(), 0);
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

    @Test public void parseEmptyObject() {
        Json.JsonValue value = Json.parse("{}");
        assertTrue(value.isObject());
        assertEquals(0, value.asObject().size());
    }

    @Test public void parseObject() {
        Json.JsonValue value = Json.parse("{\"foo\": true }");
        assertTrue(value.isObject());
        Json.JsonObject object = value.asObject();
        assertEquals(1, object.size());
        Map<String, Json.JsonValue> values = object.getValues();
        assertTrue(values.containsKey("foo"));
        assertTrue(values.get("foo").asBoolean());
    }

    @Test public void parseZero() {
        Json.JsonValue value = Json.parse("0");
        assertTrue(value.isNumber());
        assertEquals(0, value.asLong());
    }

    @Test public void parseZeroWithExponent() {
        Json.JsonValue value = Json.parse("0e-23");
        assertTrue(value.isNumber());
        assertEquals(0, value.asDouble(), 0);
    }

    @Test public void parseMinus() {
        try {
            Json.parse("-");
            fail("should raise ParseException");
        } catch (Json.ParseException expected) {}
    }

    @Test public void parseNegativeZero() {
        Json.JsonValue value = Json.parse("-0");
        assertTrue(value.isNumber());
        assertEquals(0, value.asLong());
    }

    @Test public void parseNegativeZeroWithExponent() {
        Json.JsonValue value = Json.parse("-0e-2");
        assertTrue(value.isNumber());
        assertEquals(0, value.asDouble(), 0);
    }

    @Test public void parseSingleDigit() {
        Json.JsonValue value = Json.parse("3");
        assertTrue(value.isNumber());
        assertEquals(3, value.asLong());
    }

    @Test public void parseSingleDigitWithExponent() {
        Json.JsonValue value = Json.parse("3e+7");
        assertTrue(value.isNumber());
        assertEquals(3e+7, value.asDouble(), 0);
    }

    @Test public void parseNumberWithLeadingZero() {
        try {
            Json.parse("023");
            fail("should raise ParseException");
        } catch (Json.ParseException expected) {}
    }

    @Test public void parseNumber() {
        Json.JsonValue value = Json.parse("123");
        assertTrue(value.isNumber());
        assertEquals(123, value.asLong());
    }

    @Test public void parseLongNumber() {
        Json.JsonValue value = Json.parse("100000000000000023");
        assertTrue(value.isNumber());
        assertEquals(100000000000000023L, value.asLong());
    }

    @Test public void parseNegativeNumber() {
        Json.JsonValue value = Json.parse("-23");
        assertTrue(value.isNumber());
        assertEquals(-23, value.asLong());
    }

    @Test public void parseNegativeNumberWithExponent() {
        Json.JsonValue value = Json.parse("-2e-2");
        assertTrue(value.isNumber());
        assertEquals(-2e-2, value.asDouble(), 0);
    }

    @Test public void parseNegativeLongNumber() {
        Json.JsonValue value = Json.parse("-234567898765432");
        assertTrue(value.isNumber());
        assertEquals(-234567898765432L, value.asLong());
    }

    @Test public void parseNumberWithExponent() {
        Json.JsonValue value = Json.parse("33e12");
        assertTrue(value.isNumber());
        assertEquals(33e12, value.asDouble(), 0);
    }

    @Test public void parseNumberWithExponentUppercase() {
        Json.JsonValue value = Json.parse("33E12");
        assertTrue(value.isNumber());
        assertEquals(33e12, value.asDouble(), 0);
    }

    @Test public void parseNumberWithExponentPlus() {
        Json.JsonValue value = Json.parse("33E+12");
        assertTrue(value.isNumber());
        assertEquals(33e12, value.asDouble(), 0);
    }

    @Test public void parseNumberWithExponentMinus() {
        Json.JsonValue value = Json.parse("33E-12");
        assertTrue(value.isNumber());
        assertEquals(33e-12, value.asDouble(), 0);
    }

    @Test public void parseNumberWithEmptyExponent() {
        try {
            Json.parse("33E+");
            fail("should raise ParseException");
        } catch (Json.ParseException expected) {}
    }

    @Test public void parseNumberWithBrokenExponent() {
        try {
            Json.parse("33E++2");
            fail("should raise ParseException");
        } catch (Json.ParseException expected) {}
    }

    @Test public void parseFloat() {
        Json.JsonValue value = Json.parse("3.141");
        assertTrue(value.isNumber());
        assertEquals(3.141, value.asDouble(), 0);
    }

    @Test public void parseNegativeFloat() {
        Json.JsonValue value = Json.parse("-3.141");
        assertTrue(value.isNumber());
        assertEquals(-3.141, value.asDouble(), 0);
    }

    @Test public void parseFloatWithExponent() {
        Json.JsonValue value = Json.parse("-3.141e+4");
        assertTrue(value.isNumber());
        assertEquals(-3.141e4, value.asDouble(), 0);
    }

    @Test public void parseFloatWithLeadingZero() {
        Json.JsonValue value = Json.parse("0.33333333");
        assertTrue(value.isNumber());
        assertEquals(0.33333333, value.asDouble(), 0);
    }

    @Test public void parseFloatWithLeadingZeroAndExponent() {
        Json.JsonValue value = Json.parse("0.333e4");
        assertTrue(value.isNumber());
        assertEquals(0.333e4, value.asDouble(), 0);
    }

    @Test public void parseFloatWithComma() {
        try {
            Json.parse("3,141");
            fail("should raise ParseException");
        } catch (Json.ParseException expected) {}
    }

    @Test public void parseFloatStartingWithDot() {
        try {
            Json.parse(".141");
            fail("should raise ParseException");
        } catch (Json.ParseException expected) {}
    }

    @Test public void parseFloatWithDoubleDot() {
        try {
            Json.parse("111.222.333");
            fail("should raise ParseException");
        } catch (Json.ParseException expected) {}
    }

}

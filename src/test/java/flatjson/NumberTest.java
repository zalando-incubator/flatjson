package flatjson;

import org.junit.Test;

import static org.junit.Assert.*;

public class NumberTest {

    @Test public void parseZero() {
        JsonValue value = Json.parse("0");
        assertTrue(value.isNumber());
        assertEquals(0, value.asLong());
    }

    @Test public void parseZeroWithExponent() {
        JsonValue value = Json.parse("0e-23");
        assertTrue(value.isNumber());
        assertEquals(0, value.asDouble(), 0);
    }

    @Test(expected = ParseException.class)
    public void parseMinus() {
        Json.parse("-");
    }

    @Test public void parseNegativeZero() {
        JsonValue value = Json.parse("-0");
        assertTrue(value.isNumber());
        assertEquals(0, value.asLong());
    }

    @Test public void parseNegativeZeroWithExponent() {
        JsonValue value = Json.parse("-0e-2");
        assertTrue(value.isNumber());
        assertEquals(0, value.asDouble(), 0);
    }

    @Test public void parseSingleDigit() {
        JsonValue value = Json.parse("3");
        assertTrue(value.isNumber());
        assertEquals(3, value.asLong());
    }

    @Test public void parseSingleDigitWithExponent() {
        JsonValue value = Json.parse("3e+7");
        assertTrue(value.isNumber());
        assertEquals(3e+7, value.asDouble(), 0);
    }

    @Test(expected = ParseException.class)
    public void parseNumberWithLeadingZero() {
        Json.parse("023");
    }

    @Test public void parseNumber() {
        JsonValue value = Json.parse("123");
        assertTrue(value.isNumber());
        assertEquals(123, value.asLong());
    }

    @Test public void parseLongNumber() {
        JsonValue value = Json.parse("100000000000000023");
        assertTrue(value.isNumber());
        assertEquals(100000000000000023L, value.asLong());
    }

    @Test public void parseNegativeNumber() {
        JsonValue value = Json.parse("-23");
        assertTrue(value.isNumber());
        assertEquals(-23, value.asLong());
    }

    @Test public void parseNegativeNumberWithExponent() {
        JsonValue value = Json.parse("-2e-2");
        assertTrue(value.isNumber());
        assertEquals(-2e-2, value.asDouble(), 0);
    }

    @Test public void parseNegativeLongNumber() {
        JsonValue value = Json.parse("-234567898765432");
        assertTrue(value.isNumber());
        assertEquals(-234567898765432L, value.asLong());
    }

    @Test public void parseNumberWithExponent() {
        JsonValue value = Json.parse("33e12");
        assertTrue(value.isNumber());
        assertEquals(33e12, value.asDouble(), 0);
    }

    @Test public void parseNumberWithExponentUppercase() {
        JsonValue value = Json.parse("33E12");
        assertTrue(value.isNumber());
        assertEquals(33e12, value.asDouble(), 0);
    }

    @Test public void parseNumberWithExponentPlus() {
        JsonValue value = Json.parse("33E+12");
        assertTrue(value.isNumber());
        assertEquals(33e12, value.asDouble(), 0);
    }

    @Test public void parseNumberWithExponentMinus() {
        JsonValue value = Json.parse("33E-12");
        assertTrue(value.isNumber());
        assertEquals(33e-12, value.asDouble(), 0);
    }

    @Test(expected = ParseException.class)
    public void parseNumberWithEmptyExponent() {
        Json.parse("33E");
    }

    @Test(expected = ParseException.class)
    public void parseNumberWithEmptyExponentPlus() {
        Json.parse("33E+");
    }

    @Test(expected = ParseException.class)
    public void parseNumberWithBrokenExponent() {
        Json.parse("33E++2");
    }

    @Test(expected = ParseException.class)
    public void parseNumberWithMultipleExponents() {
        Json.parse("33E2E4");
    }

    @Test public void parseFloat() {
        JsonValue value = Json.parse("3.141");
        assertTrue(value.isNumber());
        assertEquals(3.141, value.asDouble(), 0);
    }

    @Test public void parseNegativeFloat() {
        JsonValue value = Json.parse("-3.141");
        assertTrue(value.isNumber());
        assertEquals(-3.141, value.asDouble(), 0);
    }

    @Test public void parseFloatWithExponent() {
        JsonValue value = Json.parse("-3.141e+4");
        assertTrue(value.isNumber());
        assertEquals(-3.141e4, value.asDouble(), 0);
    }

    @Test public void parseFloatWithLeadingZero() {
        JsonValue value = Json.parse("0.33333333");
        assertTrue(value.isNumber());
        assertEquals(0.33333333, value.asDouble(), 0);
    }

    @Test public void parseFloatWithLeadingZeroAndExponent() {
        JsonValue value = Json.parse("0.333e4");
        assertTrue(value.isNumber());
        assertEquals(0.333e4, value.asDouble(), 0);
    }

    @Test(expected = ParseException.class)
    public void parseFloatWithComma() {
        Json.parse("3,141");
    }

    @Test(expected = ParseException.class)
    public void parseFloatStartingWithDot() {
        Json.parse(".141");
    }

    @Test(expected = ParseException.class)
    public void parseNegativeFloatStartingWithDot() {
        Json.parse("-.141");
    }

    @Test(expected = ParseException.class)
    public void parseFloatWithDoubleDot() {
        Json.parse("111..333");
    }

}

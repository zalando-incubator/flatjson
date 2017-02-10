package flatjson;

import flatjson.Json.Token;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class JsonTest {

    @Test public void parseNull() {
        assertEquals(
                asList(Token.NULL),
                Json.parse("null").getTokens()
        );
    }

    @Test public void parseBrokenNull() {
        try {
            Json.parse("nul");
            fail("should raise ParseException");
        } catch (Json.ParseException expected) {}
    }

    @Test public void parseNullWithWhitespace() {
        assertEquals(
                asList(Token.NULL),
                Json.parse("   \r\n null \t").getTokens()
        );
    }

    @Test public void parseTrue() {
        assertEquals(
                asList(Token.TRUE),
                Json.parse("true").getTokens()
        );
    }

    @Test public void parseFalse() {
        assertEquals(
                asList(Token.FALSE),
                Json.parse("false").getTokens()
        );
    }

    @Test public void parseEmptyArray() {
        assertEquals(
                asList(Token.ARRAY),
                Json.parse("[ ]").getTokens()
        );
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
        assertEquals(
                asList(Token.ARRAY, Token.ARRAY),
                Json.parse("[ [ ]]").getTokens()
        );
    }

    @Test public void parseBooleanArray() {
        assertEquals(
                asList(Token.ARRAY, Token.TRUE, Token.FALSE),
                Json.parse("[ true,false ]").getTokens()
        );
    }

    @Test public void parseString() {
        assertEquals(
                asList(Token.STRING),
                Json.parse("\"hello\"").getTokens()
        );
    }

    @Test public void parseStringWithEscapedQuote() {
        assertEquals(
                asList(Token.STRING),
                Json.parse("\"hello \\\"darling\\\"\"").getTokens()
        );
    }

    @Test public void parseStringWithEscapedBackslash() {
        assertEquals(
                asList(Token.STRING),
                Json.parse("\"hello \\\\ world\"").getTokens()
        );
    }

    @Test public void parseStringWithEscapedSlash() {
        assertEquals(
                asList(Token.STRING),
                Json.parse("\"hello \\/ world\"").getTokens()
        );
    }

    @Test public void parseStringWithEscapedBackspace() {
        assertEquals(
                asList(Token.STRING),
                Json.parse("\"hello \\b world\"").getTokens()
        );
    }

    @Test public void parseStringWithEscapedFormfeed() {
        assertEquals(
                asList(Token.STRING),
                Json.parse("\"hello \\f world\"").getTokens()
        );
    }

    @Test public void parseStringWithEscapedNewline() {
        assertEquals(
                asList(Token.STRING),
                Json.parse("\"hello \\n world\"").getTokens()
        );
    }

    @Test public void parseStringWithUnescapedNewline() {
        try {
            Json.parse("\"hello \n world\"");
            fail("should raise ParseException");
        } catch (Json.ParseException expected) {}
    }

    @Test public void parseStringWithEscapedCarriageReturn() {
        assertEquals(
                asList(Token.STRING),
                Json.parse("\"hello \\r world\"").getTokens()
        );
    }

    @Test public void parseStringWithEscapedTab() {
        assertEquals(
                asList(Token.STRING),
                Json.parse("\"hello \\t world\"").getTokens()
        );
    }

    @Test public void parseStringWithEscapedUnicode() {
        assertEquals(
                asList(Token.STRING),
                Json.parse("\"hello \\u2ebf world\"").getTokens()
        );
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
        assertEquals(
                asList(Token.OBJECT),
                Json.parse("{}").getTokens()
        );
    }

    @Test public void parseObject() {
        assertEquals(
                asList(Token.OBJECT, Token.STRING, Token.TRUE),
                Json.parse("{\"foo\": true }").getTokens()
        );
    }

    @Test public void parseZero() {
        assertEquals(
                asList(Token.NUMBER),
                Json.parse("0").getTokens()
        );
    }

    @Test public void parseZeroWithExponent() {
        assertEquals(
                asList(Token.NUMBER),
                Json.parse("0e-23").getTokens()
        );
    }

    @Test public void parseMinus() {
        try {
            Json.parse("-");
            fail("should raise ParseException");
        } catch (Json.ParseException expected) {}
    }

    @Test public void parseNegativeZero() {
        assertEquals(
                asList(Token.NUMBER),
                Json.parse("-0").getTokens()
        );
    }

    @Test public void parseSingleDigit() {
        assertEquals(
                asList(Token.NUMBER),
                Json.parse("3").getTokens()
        );
    }

    @Test public void parseSingleDigitWithExponent() {
        assertEquals(
                asList(Token.NUMBER),
                Json.parse("3e+7").getTokens()
        );
    }

    @Test public void parseNumberWithLeadingZero() {
        try {
            Json.parse("023");
            fail("should raise ParseException");
        } catch (Json.ParseException expected) {}
    }

    @Test public void parseInteger() {
        assertEquals(
                asList(Token.NUMBER),
                Json.parse("23").getTokens()
        );
    }

    @Test public void parseVeryLongInteger() {
        assertEquals(
                asList(Token.NUMBER),
                Json.parse("100000000000000023").getTokens()
        );
    }

    @Test public void parseNegativeInteger() {
        assertEquals(
                asList(Token.NUMBER),
                Json.parse("-23").getTokens()
        );
    }

    @Test public void parseVeryLongNegativeInteger() {
        assertEquals(
                asList(Token.NUMBER),
                Json.parse("-234567898765432").getTokens()
        );
    }

    @Test public void parseNumberWithExponent() {
        assertEquals(
                asList(Token.NUMBER),
                Json.parse("33e12").getTokens()
        );
    }

    @Test public void parseNumberWithExponentUppercase() {
        assertEquals(
                asList(Token.NUMBER),
                Json.parse("33E12").getTokens()
        );
    }

    @Test public void parseNumberWithExponentPlus() {
        assertEquals(
                asList(Token.NUMBER),
                Json.parse("33E+12").getTokens()
        );
    }

    @Test public void parseNumberWithExponentMinus() {
        assertEquals(
                asList(Token.NUMBER),
                Json.parse("33E-12").getTokens()
        );
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
        assertEquals(
                asList(Token.NUMBER),
                Json.parse("3.141").getTokens()
        );
    }

    @Test public void parseNegativeFloat() {
        assertEquals(
                asList(Token.NUMBER),
                Json.parse("-3.141").getTokens()
        );
    }

    @Test public void parseFloatWithExponent() {
        assertEquals(
                asList(Token.NUMBER),
                Json.parse("-3.141e+4").getTokens()
        );
    }

    @Test public void parseFloatWithLeadingZero() {
        assertEquals(
                asList(Token.NUMBER),
                Json.parse("0.33333333").getTokens()
        );
    }

    @Test public void parseFloatWithLeadingZeroAndExponent() {
        assertEquals(
                asList(Token.NUMBER),
                Json.parse("0.333E4").getTokens()
        );
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

    @Test public void parseNumberArray() {
        assertEquals(
                asList(Token.ARRAY, Token.NUMBER, Token.NUMBER),
                Json.parse("[23, 42]").getTokens()
        );
    }

    @Test public void parseMixedNumberArray() {
        assertEquals(
                asList(Token.ARRAY, Token.NUMBER, Token.NUMBER, Token.NUMBER, Token.NUMBER),
                Json.parse("[0, -23, 2e+5, -0.333]").getTokens()
        );
    }

    private List asList(Object... objects) {
        return Arrays.asList(objects);
    }


}

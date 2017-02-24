package flatjson;

import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.*;

public class JsonTest {

    @Test(expected = ParseException.class)
    public void parseNull() {
        Json.parse(null);
    }

    @Test(expected = ParseException.class)
    public void parseEmpty() {
        Json.parse("");
    }

    @Test(expected = ParseException.class)
    public void parseWhitespace() {
        Json.parse("  \r\n  \t ");
    }

    @Test public void parseJsonNull() {
        assertTrue(Json.parse("null").isNull());
    }

    @Test public void parseNullWithWhitespace() {
        assertTrue(Json.parse("   \r\n null \t").isNull());
    }

    @Test(expected = ParseException.class)
    public void parseBrokenNull() {
        Json.parse("nul");
    }

    @Test public void parseTrue() {
        Json json = Json.parse("true");
        assertTrue(json.isBoolean());
        assertTrue(json.asBoolean());
    }

    @Test public void parseFalse() {
        Json json = Json.parse("false");
        assertTrue(json.isBoolean());
        assertFalse(json.asBoolean());
    }

    @Test public void parseEmptyArray() {
        Json json = Json.parse("[ ]");
        assertTrue(json.isArray());
        assertEquals(0, json.asArray().size());
    }

    @Test(expected = ParseException.class)
    public void parseArrayWithLeadingComma() {
        Json.parse("[ , true]");
    }

    @Test(expected = ParseException.class)
    public void parseArrayWithTrailingComma() {
        Json.parse("[ true,]");
    }

    @Test(expected = ParseException.class)
    public void parseOpenArray() {
        Json.parse("[ null,");
    }

    @Test public void parseBooleanArray() {
        Json json = Json.parse("[ true,false ]");
        assertTrue(json.isArray());
        List<Json> array = json.asArray();
        assertEquals(2, array.size());
        assertTrue(array.get(0).asBoolean());
        assertFalse(array.get(1).asBoolean());
    }

    @Test public void parseNumberArray() {
        Json json = Json.parse("[23,42e8,3.141]");
        assertTrue(json.isArray());
        List<Json> array = json.asArray();
        assertEquals(3, array.size());
        assertEquals(23, array.get(0).asLong());
        assertEquals(42e8, array.get(1).asDouble(), 0);
        assertEquals(3.141, array.get(2).asDouble(), 0);
    }

    @Test public void parseNestedArray() {
        Json json = Json.parse("[[[],[]]]");
        assertTrue(json.isArray());
        List<Json> array = json.asArray();
        assertEquals(1, array.size());
        Json nested = array.get(0);
        assertTrue(nested.isArray());
        List<Json> nestedArray = nested.asArray();
        assertEquals(2, nestedArray.size());
        assertTrue(nestedArray.get(0).isArray());
        assertTrue(nestedArray.get(1).isArray());
    }

    @Test public void parseEmptyObject() {
        Json json = Json.parse("{}");
        assertTrue(json.isObject());
        Map<String, Json> object = json.asObject();
        assertEquals(0, object.size());
    }

    @Test public void parseObject() {
        Json json = Json.parse("{\"foo\": true ,\n   \"bar\": false   }");
        assertTrue(json.isObject());
        Map<String, Json> object = json.asObject();
        assertEquals(2, object.size());
        assertTrue(object.containsKey("foo"));
        assertTrue(object.get("foo").asBoolean());
        assertTrue(object.containsKey("bar"));
        assertFalse(object.get("bar").asBoolean());
    }

    @Test public void parseObjectWithEscapedKey() {
        Json json = Json.parse("{\"\\noo\\b\": true }");
        assertTrue(json.isObject());
        Map<String, Json> object = json.asObject();
        assertEquals(1, object.size());
        assertTrue(object.containsKey("\noo\b"));
    }

    @Test public void parseNestedObject() {
        Json json = Json.parse("{\"nested\": {\"foo\": 23 }, \"bar\": false , \"baz\": -1 }");
        assertTrue(json.isObject());
        Map<String, Json> object = json.asObject();
        assertEquals(3, object.size());
        Json nested = object.get("nested");
        assertTrue(nested.isObject());
        Map<String, Json> nestedObject = nested.asObject();
        assertEquals(23, nestedObject.get("foo").asLong());
    }

    @Test public void parseArrayOfObjects() {
        Json json = Json.parse("[{\"foo\": 23, \"bar\": 44}, {\"foo\": 11, \"bar\": 64}]");
        assertTrue(json.isArray());
        List<Json> array = json.asArray();
        assertEquals(2, array.size());
        for (Json value : array) {
            assertTrue(value.isObject());
            Set<String> keys = value.asObject().keySet();
            assertTrue(keys.contains("foo"));
            assertTrue(keys.contains("bar"));
        }
    }

}

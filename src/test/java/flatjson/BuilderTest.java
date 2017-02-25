package flatjson;

import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BuilderTest {

    @Test public void createJsonNull() {
        Json json = Json.nil();
        assertTrue(json.isNull());
        assertEquals("null", json.toString());
    }

    @Test public void createJsonString() {
        Json json = Json.string("hello");
        assertTrue(json.isString());
        assertEquals("hello", json.asString());
        assertEquals("\"hello\"", json.toString());
    }

    @Test public void createJsonStringWithNewline() {
        Json json = Json.string("hello \n world");
        assertTrue(json.isString());
        assertEquals("hello \n world", json.asString());
        assertEquals("\"hello \\n world\"", json.toString());
    }

    @Test public void createJsonNullString() {
        Json json = Json.string(null);
        assertTrue(json.isNull());
        assertFalse(json.isString());
        assertEquals("null", json.toString());
    }

    @Test public void createJsonTrue() {
        Json json = Json.bool(true);
        assertTrue(json.isBoolean());
        assertTrue(json.asBoolean());
        assertEquals("true", json.toString());
    }

    @Test public void createJsonFalse() {
        Json json = Json.bool(false);
        assertTrue(json.isBoolean());
        assertFalse(json.asBoolean());
        assertEquals("false", json.toString());
    }

    @Test public void createJsonLong() {
        Json json = Json.number(10);
        assertTrue(json.isNumber());
        assertEquals(10, json.asLong());
        assertEquals("10", json.toString());
    }

    @Test public void createJsonDouble() {
        Json json = Json.number(3.141);
        assertTrue(json.isNumber());
        assertEquals(3.141, json.asDouble(), 0.001);
        assertEquals("3.141", json.toString());
    }

    @Test public void createJsonObject() {
        Map<String, Json> object = Json.object();
        assertEquals("{}", object.toString());
    }

    @Test public void createJsonArray() {
        List<Json> array = Json.array();
        assertEquals("[]", array.toString());
    }

}

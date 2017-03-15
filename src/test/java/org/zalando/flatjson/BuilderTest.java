package org.zalando.flatjson;

import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BuilderTest {

    @Test public void createJsonNull() {
        Json json = Json.value((String)null);
        assertTrue(json.isNull());
        assertEquals("null", json.toString());
    }

    @Test public void createJsonTrue() {
        Json json = Json.value(true);
        assertTrue(json.isBoolean());
        assertTrue(json.asBoolean());
        assertEquals("true", json.toString());
    }

    @Test public void createJsonFalse() {
        Json json = Json.value(false);
        assertTrue(json.isBoolean());
        assertFalse(json.asBoolean());
        assertEquals("false", json.toString());
    }

    @Test public void createJsonInt() {
        Json json = Json.value(10);
        assertTrue(json.isNumber());
        assertEquals(10, json.asInt());
        assertEquals("10", json.toString());
    }

    @Test public void createJsonLong() {
        Json json = Json.value(10L);
        assertTrue(json.isNumber());
        assertEquals(10L, json.asLong());
        assertEquals("10", json.toString());
    }

    @Test public void createJsonFloat() {
        Json json = Json.value(3.141);
        assertTrue(json.isNumber());
        assertEquals(3.141, json.asFloat(), 0.001);
        assertEquals("3.141", json.toString());
    }

    @Test public void createJsonDouble() {
        Json json = Json.value(3.141D);
        assertTrue(json.isNumber());
        assertEquals(3.141D, json.asDouble(), 0.001);
        assertEquals("3.141", json.toString());
    }

    @Test public void createJsonString() {
        Json json = Json.value("hello");
        assertTrue(json.isString());
        assertEquals("hello", json.asString());
        assertEquals("\"hello\"", json.toString());
    }

    @Test public void createJsonStringWithNewline() {
        Json json = Json.value("hello \n world");
        assertTrue(json.isString());
        assertEquals("hello \n world", json.asString());
        assertEquals("\"hello \\n world\"", json.toString());
    }

    @Test public void createJsonObject() {
        Json json = Json.object();
        Map<String, Json> object = json.asObject();
        object.put("hello", Json.value("world"));
        assertEquals("{\"hello\":\"world\"}", object.toString());
    }

    @Test public void createJsonArray() {
        Json json = Json.array();
        List<Json> array = json.asArray();
        array.add(Json.value("hello"));
        array.add(Json.value(42));
        assertEquals("[\"hello\",42]", json.toString());
    }

    @Test public void createNestedStructure() {
        Json json = Json.array();
        List<Json> array = json.asArray();
        array.add(Json.value(true));
        array.add(Json.object());
        assertEquals("[true,{}]", json.toString());

        Map<String, Json> object = array.get(1).asObject();
        object.put("color", Json.value("blue"));
        object.put("size", Json.value(39));
        assertEquals("[true,{\"color\":\"blue\",\"size\":39}]", json.toString());
    }

    @Test public void mergeTrees() {
        Json json = Json.parse("[1,2,3]");
        Json merged = Json.array(json, json);
        assertEquals("[[1,2,3],[1,2,3]]", merged.toString());
    }

}

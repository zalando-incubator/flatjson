package flatjson;

import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BuilderTest {

    @Test public void createJsonObject() {
        Map<String, Json> object = Json.object();
        assertEquals("{}", object.toString());
    }

    @Test public void createJsonArray() {
        List<Json> array = Json.array();
        assertEquals("[]", array.toString());
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

}

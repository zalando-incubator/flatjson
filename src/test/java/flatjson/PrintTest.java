package flatjson;

import org.junit.Test;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PrintTest {

    @Test public void printObjectWithEscapedKey() {
        Json json = Json.parse("{\"\\noo\\b\": true }");
        assertTrue(json.isObject());
        Map<String, Json> object = json.asObject();
        assertEquals("{\"\\noo\\b\":true}", object.toString());
    }

    @Test public void printReversedArray() {
        Json json = Json.parse("[1,2,3]");
        List<Json> array = json.asArray();
        Collections.reverse(array);
        assertEquals("[3,2,1]", json.toString());
    }

    @Test public void printModifiedObject() {
        Json json = Json.parse("{\"foo\":23}");
        Map<String, Json> object = json.asObject();
        object.put("bar", Json.value(42));
        assertEquals("{\"foo\":23,\"bar\":42}", json.toString());
    }

}

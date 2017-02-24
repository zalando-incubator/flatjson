package flatjson;

import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class JsonValueTest {

    @Test public void printObjectWithEscapedKey() {
        JsonValue value = Json.parse("{\"\\noo\\b\": true }");
        assertTrue(value.isObject());
        Map<String, JsonValue> object = value.asObject();
        assertEquals("{\"\\noo\\b\":true}", object.toString());
    }
}

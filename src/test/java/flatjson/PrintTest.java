package flatjson;

import org.junit.Test;

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
}

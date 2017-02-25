package flatjson;

import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class BuilderTest {

    @Test public void createJsonObject() {
        Map<String, Json> object = Json.object();
        assertEquals("{}", object.toString());
    }

    @Test public void createJsonArray() {
        List<Json> array = Json.array();
        assertEquals("[]", array.toString());
    }


}

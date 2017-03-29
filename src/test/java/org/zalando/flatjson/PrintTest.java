package org.zalando.flatjson;

import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertEquals;

public class PrintTest {

    @Test public void printObjectWithEscapedKey() {
        Json json = Json.object();
        json.asObject().put("\noo\b", Json.value(true));
        assertEquals("{\"\\noo\\b\":true}", json.toString());
    }

    @Test public void printReversedArray() {
        Json json = Json.parse("[1,2,3]");
        Collections.reverse(json.asArray());
        assertEquals("[3,2,1]", json.toString());
    }

    @Test public void printModifiedObject() {
        Json json = Json.parse("{\"foo\":23}");
        json.asObject().put("bar", Json.value(42));
        assertEquals("{\"foo\":23,\"bar\":42}", json.toString());
    }

}

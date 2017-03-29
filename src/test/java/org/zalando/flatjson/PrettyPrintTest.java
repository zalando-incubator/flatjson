package org.zalando.flatjson;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PrettyPrintTest {

    @Test public void prettyPrintNull() {
        Json json = Json.parse("null");
        assertEquals("null", json.prettyPrint());
    }

    @Test public void prettyPrintLiteralNull() {
        Json json = Json.value(null);
        assertEquals("null", json.prettyPrint());
    }

    @Test public void prettyPrintTrue() {
        Json json = Json.parse("true");
        assertEquals("true", json.prettyPrint());
    }

    @Test public void prettyPrintLiteralTrue() {
        Json json = Json.value(true);
        assertEquals("true", json.prettyPrint());
    }

    @Test public void prettyPrintNumber() {
        Json json = Json.parse("-23");
        assertEquals("-23", json.prettyPrint());
    }

    @Test public void prettyPrintLiteralNumber() {
        Json json = Json.value(-23);
        assertEquals("-23", json.prettyPrint());
    }

    @Test public void prettyPrintString() {
        Json json = Json.parse("\"hello\"");
        assertEquals("\"hello\"", json.prettyPrint());
    }

    @Test public void prettyPrintLiteralString() {
        Json json = Json.value("hello");
        assertEquals("\"hello\"", json.prettyPrint());
    }

    @Test public void prettyPrintEmptyArray() {
        Json json = Json.parse("[]");
        assertEquals("[]", json.prettyPrint());
    }

    @Test public void prettyPrintLiteralEmptyArray() {
        Json json = Json.array();
        assertEquals("[]", json.prettyPrint());
    }

    @Test public void prettyPrintArray() {
        Json json = Json.parse("[11,17,23]");
        assertEquals("[\n\t11,\n\t17,\n\t23\n]", json.prettyPrint("\t"));
    }

    @Test public void prettyPrintLiteralArray() {
        Json json = Json.array(Json.value(11), Json.value(17), Json.value(23));
        assertEquals("[\n\t11,\n\t17,\n\t23\n]", json.prettyPrint("\t"));
    }

    @Test public void prettyPrintNestedArray() {
        Json json = Json.parse("[1,[],3]");
        assertEquals("[\n\t1,\n\t[],\n\t3\n]", json.prettyPrint("\t"));
    }

    @Test public void prettyPrintLiteralNestedArray() {
        Json json = Json.array(Json.value(1), Json.array(), Json.value(3));
        assertEquals("[\n\t1,\n\t[],\n\t3\n]", json.prettyPrint("\t"));
    }

    @Test public void prettyPrintEmptyObject() {
        Json json = Json.parse("{}");
        assertEquals("{}", json.prettyPrint());
    }

    @Test public void prettyPrintLiteralEmptyObject() {
        Json json = Json.object();
        assertEquals("{}", json.prettyPrint());
    }

    @Test public void prettyPrintObject() {
        Json json = Json.parse("{\"foo\":23}");
        assertEquals("{\n\t\"foo\": 23\n}", json.prettyPrint("\t"));
    }

    @Test public void prettyPrintLiteralObject() {
        Json json = Json.object();
        json.asObject().put("foo", Json.value(23));
        assertEquals("{\n\t\"foo\": 23\n}", json.prettyPrint("\t"));
    }

    @Test public void prettyPrintNestedObject() {
        Json json = Json.parse("{\"foo\":{}}");
        assertEquals("{\n\t\"foo\": {}\n}", json.prettyPrint("\t"));
    }

    @Test public void prettyPrintLiteralNestedObject() {
        Json json = Json.object();
        json.asObject().put("foo", Json.object());
        assertEquals("{\n\t\"foo\": {}\n}", json.prettyPrint("\t"));
    }

    @Test public void prettyPrintCompact() {
        Json json = Json.object();
        json.asObject().put("foo", Json.array(Json.TRUE, Json.FALSE));
        assertEquals("{\"foo\":[true,false]}", json.prettyPrint(null));
    }

}

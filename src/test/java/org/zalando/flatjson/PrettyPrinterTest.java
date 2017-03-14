package org.zalando.flatjson;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PrettyPrinterTest {

    @Test public void prettyPrintNull() {
        PrettyPrinter pp = new PrettyPrinter("#");
        Json json = Json.parse("null");
        json.convert(pp);
        assertEquals("null", pp.toString());
    }

    @Test public void prettyPrintLiteralNull() {
        PrettyPrinter pp = new PrettyPrinter("#");
        Json json = Json.value(null);
        json.convert(pp);
        assertEquals("null", pp.toString());
    }

    @Test public void prettyPrintTrue() {
        PrettyPrinter pp = new PrettyPrinter("#");
        Json json = Json.parse("true");
        json.convert(pp);
        assertEquals("true", pp.toString());
    }

    @Test public void prettyPrintLiteralTrue() {
        PrettyPrinter pp = new PrettyPrinter("#");
        Json json = Json.value(true);
        json.convert(pp);
        assertEquals("true", pp.toString());
    }

    @Test public void prettyPrintNumber() {
        PrettyPrinter pp = new PrettyPrinter("#");
        Json json = Json.parse("-23");
        json.convert(pp);
        assertEquals("-23", pp.toString());
    }

    @Test public void prettyPrintLiteralNumber() {
        PrettyPrinter pp = new PrettyPrinter("#");
        Json json = Json.value(-23);
        json.convert(pp);
        assertEquals("-23", pp.toString());
    }

    @Test public void prettyPrintString() {
        PrettyPrinter pp = new PrettyPrinter("#");
        Json json = Json.parse("\"hello\"");
        json.convert(pp);
        assertEquals("\"hello\"", pp.toString());
    }

    @Test public void prettyPrintLiteralString() {
        PrettyPrinter pp = new PrettyPrinter("#");
        Json json = Json.value("hello");
        json.convert(pp);
        assertEquals("\"hello\"", pp.toString());
    }

    @Test public void prettyPrintEmptyArray() {
        PrettyPrinter pp = new PrettyPrinter("#");
        Json json = Json.parse("[]");
        json.convert(pp);
        assertEquals("[]", pp.toString());
    }

    @Test public void prettyPrintLiteralEmptyArray() {
        PrettyPrinter pp = new PrettyPrinter("#");
        Json json = Json.array();
        json.convert(pp);
        assertEquals("[]", pp.toString());
    }

    @Test public void prettyPrintArray() {
        PrettyPrinter pp = new PrettyPrinter("#");
        Json json = Json.parse("[11,17,23]");
        json.convert(pp);
        assertEquals("[\n#11,\n#17,\n#23\n]", pp.toString());
    }

    @Test public void prettyPrintLiteralArray() {
        PrettyPrinter pp = new PrettyPrinter("#");
        Json json = Json.array(Json.value(11), Json.value(17), Json.value(23));
        json.convert(pp);
        assertEquals("[\n#11,\n#17,\n#23\n]", pp.toString());
    }

    @Test public void prettyPrintNestedArray() {
        PrettyPrinter pp = new PrettyPrinter("#");
        Json json = Json.parse("[1,[],3]");
        json.convert(pp);
        assertEquals("[\n#1,\n#[],\n#3\n]", pp.toString());
    }

    @Test public void prettyPrintLiteralNestedArray() {
        PrettyPrinter pp = new PrettyPrinter("#");
        Json json = Json.array(Json.value(1), Json.array(), Json.value(3));
        json.convert(pp);
        assertEquals("[\n#1,\n#[],\n#3\n]", pp.toString());
    }

    @Test public void prettyPrintEmptyObject() {
        PrettyPrinter pp = new PrettyPrinter("#");
        Json json = Json.parse("{}");
        json.convert(pp);
        assertEquals("{}", pp.toString());
    }

    @Test public void prettyPrintLiteralEmptyObject() {
        PrettyPrinter pp = new PrettyPrinter("#");
        Json json = Json.object();
        json.convert(pp);
        assertEquals("{}", pp.toString());
    }

    @Test public void prettyPrintObject() {
        PrettyPrinter pp = new PrettyPrinter("#");
        Json json = Json.parse("{\"foo\":23}");
        json.convert(pp);
        assertEquals("{\n#\"foo\": 23\n}", pp.toString());
    }

    @Test public void prettyPrintLiteralObject() {
        PrettyPrinter pp = new PrettyPrinter("#");
        Json json = Json.object();
        json.asObject().put("foo", Json.value(23));
        json.convert(pp);
        assertEquals("{\n#\"foo\": 23\n}", pp.toString());
    }

    @Test public void prettyPrintNestedObject() {
        PrettyPrinter pp = new PrettyPrinter("#");
        Json json = Json.parse("{\"foo\":{}}");
        json.convert(pp);
        assertEquals("{\n#\"foo\": {}\n}", pp.toString());
    }

    @Test public void prettyPrintLiteralNestedObject() {
        PrettyPrinter pp = new PrettyPrinter("#");
        Json json = Json.object();
        json.asObject().put("foo", Json.object());
        json.convert(pp);
        assertEquals("{\n#\"foo\": {}\n}", pp.toString());
    }

}

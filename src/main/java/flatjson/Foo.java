package flatjson;

public class Foo {

    public static class Value {
        boolean isNull() { return false; }
        boolean isBoolean() { return false; }
        boolean isArray() { return false; }
        boolean isObject() { return false; }
    }

    public static class Array extends Value {
        boolean isArray() { return true; }
    }

    public static class Object extends Value {
        boolean isObject() { return true; }
    }


}

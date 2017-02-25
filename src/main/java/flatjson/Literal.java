package flatjson;

public class Literal extends Json {

    static class Null extends Json {

        @Override public boolean isNull() {
            return true;
        }

        @Override public String toString() {
            return "null";
        }
    }

    static class Boolean extends Json {

        private final boolean value;

        Boolean(boolean value) {
            this.value = value;
        }

        @Override public boolean isBoolean() {
            return true;
        }

        @Override public boolean asBoolean() {
            return value;
        }

        @Override public String toString() {
            return java.lang.Boolean.toString(value);
        }
    }

    static class Number extends Literal {

        private final String value;

        Number(long value) {
            this.value = Long.toString(value);
        }

        Number(double value) {
            this.value = Double.toString(value);
        }

        @Override public boolean isNumber() {
            return true;
        }

        @Override public long asLong() {
            return Long.valueOf(value);
        }

        @Override public double asDouble() {
            return Double.valueOf(value);
        }

        @Override public String toString() {
            return value;
        }
    }

    static class Strng extends Json {

        private final String string;

        Strng(String string) {
            this.string = string;
        }

        @Override public boolean isString() {
            return true;
        }

        @Override public String asString() {
            return string;
        }

        @Override public String toString() {
            return "\"" + StringCodec.escape(string) + "\"";
        }
    }

}

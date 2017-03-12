package org.zalando.flatjson;

import java.util.Stack;

public class PrettyPrinter implements Converter {

    private enum Type { ARRAY, OBJECT }

    private class Mode {
        private Type type;
        private int count;

        Mode(Type type) {
            this.type = type;
            this.count = 0;
        }
    }

    private final StringBuilder builder;
    private final Stack<Mode> mode;

    public PrettyPrinter() {
        builder = new StringBuilder();
        mode = new Stack();
    }

    @Override public void handleNull() {
        String value = "null";
        append(value);
    }

    @Override public void handleBoolean(boolean value) {
        append(Boolean.toString(value));
    }

    @Override public void handleNumber(String value) {
        append(value);
    }

    @Override public void handleString(String value) {
        append(String.format("\"%s\"", StringCodec.escape(value)));
    }

    @Override public void beginArray() {
        append("[");
        mode.push(new Mode(Type.ARRAY));
    }

    @Override public void endArray() {
        if (current().type != Type.ARRAY) throw new IllegalStateException("not inside array");
        mode.pop();
        append("]");
    }

    @Override public void beginObject() {
        append("{");
        mode.push(new Mode(Type.OBJECT));
    }

    @Override public void endObject() {
        if (current().type != Type.OBJECT) throw new IllegalStateException("not inside object");
        if (current().count % 2 == 1) throw new IllegalStateException("unbalanced object");
        mode.pop();
        append("}");
    }

    @Override public String toString() {
        if (!mode.empty()) throw new IllegalStateException("unbalanced json");
        return builder.toString();
    }

    private void append(String value) {
        if (current().type == Type.ARRAY) {
            if (current().count > 0) builder.append(",\n");
            for (int i = 0; i < mode.size(); i++) builder.append("\t");
        } else if (current().type == Type.OBJECT) {
            if (current().count % 2 == 0) {
                if (current().count > 0) builder.append(",\n");
                for (int i = 0; i < mode.size(); i++) builder.append("\t");
            } else {
                builder.append(": ");
            }
        }
        builder.append(value);
    }

    private Mode current() {
        return mode.peek();
    }

}

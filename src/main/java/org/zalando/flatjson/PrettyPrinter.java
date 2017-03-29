package org.zalando.flatjson;

import java.util.Stack;


class PrettyPrinter implements Visitor {

    private enum Type { TOP, ARRAY, OBJECT }

    private class Context {
        private Type type;
        private int count = 0;

        Context(Type type) {
            this.type = type;
        }
    }

    private final StringBuilder builder;
    private final Stack<Context> context;
    private final String indent;

    PrettyPrinter(String indent) {
        builder = new StringBuilder();
        context = new Stack<>();
        context.push(new Context(Type.TOP));
        this.indent = indent;
    }

    @Override public void visitNull() {
        append("null");
    }

    @Override public void visitBoolean(boolean value) {
        append(Boolean.toString(value));
    }

    @Override public void visitNumber(String value) {
        append(value);
    }

    @Override public void visitString(String value) {
        append(String.format("\"%s\"", StringCodec.escape(value)));
    }

    @Override public void beginArray() {
        append("[");
        context.push(new Context(Type.ARRAY));
    }

    @Override public void endArray() {
        if (current().type != Type.ARRAY) throw new IllegalStateException("not inside array");
        if (current().count > 0) {
            if (indent != null) {
                builder.append("\n");
                for (int i = 2; i < context.size(); i++) builder.append(indent);
            }
        }
        builder.append("]");
        context.pop();
    }

    @Override public void beginObject() {
        append("{");
        context.push(new Context(Type.OBJECT));
    }

    @Override public void endObject() {
        if (current().type != Type.OBJECT) throw new IllegalStateException("not inside object");
        if (current().count % 2 == 1) throw new IllegalStateException("unbalanced object");
        if (current().count > 0) {
            if (indent != null) {
                builder.append("\n");
                for (int i = 2; i < context.size(); i++) builder.append(indent);
            }
        }
        builder.append("}");
        context.pop();
    }

    @Override public String toString() {
        if (context.size() > 1) throw new IllegalStateException("unbalanced json");
        return builder.toString();
    }

    private void append(String value) {
        if (current().type == Type.TOP) {
            if (current().count > 0) throw new IllegalStateException("multiple toplevel values");
        } else if (current().type == Type.ARRAY) {
            if (current().count > 0) builder.append(",");
            if (indent != null) {
                builder.append("\n");
                for (int i = 1; i < context.size(); i++) builder.append(indent);
            }
        } else if (current().type == Type.OBJECT) {
            if (current().count % 2 == 0) {
                if (current().count > 0) builder.append(",");
                if (indent != null) {
                    builder.append("\n");
                    for (int i = 1; i < context.size(); i++) builder.append(indent);
                }
            } else {
                builder.append(":");
                if (indent != null) builder.append(" ");
            }
        }
        builder.append(value);
        current().count++;
    }

    private Context current() {
        return context.peek();
    }

}

package org.zalando.flatjson;

public interface Visitor {

    void handleNull();

    void handleBoolean(boolean value);

    void handleNumber(String value);

    void handleString(String value);

    void beginArray();

    void endArray();

    void beginObject();

    void endObject();
}

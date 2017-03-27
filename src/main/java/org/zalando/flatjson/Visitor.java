package org.zalando.flatjson;

public interface Visitor {

    public void handleNull();

    public void handleBoolean(boolean value);

    public void handleNumber(String value);

    public void handleString(String value);

    public void beginArray();

    public void endArray();

    public void beginObject();

    public void endObject();
}

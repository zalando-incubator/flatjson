package org.zalando.flatjson;

import java.util.ArrayList;
import java.util.List;

class JsonList extends ArrayList<Json> {

    public JsonList() {
        super();
    }

    public JsonList(List<Json> values) {
        super(values);
    }

    @Override public String toString() {
        StringBuilder result = new StringBuilder("[");
        for (int i = 0; i < size(); i++) {
            if (i > 0) result.append(",");
            result.append(get(i).toString());
        }
        result.append("]");
        return result.toString();
    }

    @Override public JsonList clone() {
        JsonList list = new JsonList();
        for (Json value: this) {
            if (value.isObject() || value.isArray()) {
                list.add(value.clone());
            } else {
                list.add(value);
            }
        }
        return list;
    }
}

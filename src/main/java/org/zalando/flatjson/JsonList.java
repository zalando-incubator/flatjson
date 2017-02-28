package org.zalando.flatjson;

import java.util.ArrayList;

class JsonList<E> extends ArrayList<E> {

    @Override public String toString() {
        StringBuilder result = new StringBuilder("[");
        for (int i = 0; i < size(); i++) {
            if (i > 0) result.append(",");
            result.append(get(i).toString());
        }
        result.append("]");
        return result.toString();
    }

}

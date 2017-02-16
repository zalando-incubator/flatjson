package flatjson;

public class JsonString extends JsonValue {

    private String decoded;

    JsonString(Json json, int element) {
        super(json, element);
    }

    @Override public boolean isString() {
        return true;
    }

    @Override public synchronized String asString() {
        if (decoded == null) {
            decoded = decodeString(json.getRawString(element));
        }
        return decoded;
    }

}

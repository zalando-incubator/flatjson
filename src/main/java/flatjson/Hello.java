package flatjson;

import java.util.List;
import java.util.Map;

public class Hello {

    public static void main(String[] args) {
        JsonValue json = Json.parse("{\"foo\":23,\"bar\":42}");
        Map<String,JsonValue> map = json.asObject();
        System.out.println(map);

        System.out.println(Integer.toUnsignedString((int)'\ucafe', 16));
    }

}

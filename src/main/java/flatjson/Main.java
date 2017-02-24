package flatjson;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {
        String input = new String(Files.readAllBytes(Paths.get("test/colors.json")));
        Json json = Json.parse(input);
        List<Json> data = json.asObject().get("data").asArray();
        for (Json value : data) {
            System.out.println(value);
        }

    }

}

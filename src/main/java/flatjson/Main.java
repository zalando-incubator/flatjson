package flatjson;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;


public class Main {

    public static void main(String[] args) throws IOException {
//        String input = "   [null, [ \"hello world\", [], true ], null]  ";
//        String input = "{ \"foo\": [true, false] }";
        String input = new String(Files.readAllBytes(Paths.get("test/sample.json")));
//        String input = "0.33e+4";
        JsonValue value = Json.parse(input);
//        System.out.println(value.asDouble());
    }

}

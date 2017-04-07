package org.zalando.flatjson;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) throws IOException {
        String input = new String(Files.readAllBytes(Paths.get("test/colors.json")));
        Json json = Json.parse(input);
        System.out.println(json.prettyPrint());
    }

}

package org.zalando.flatjson;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        Json json = Json.parse("[1,2,3]");
        PrettyPrinter pretty = new PrettyPrinter("\t");
        json.convert(pretty);
        System.out.println(pretty.toString());
    }

}

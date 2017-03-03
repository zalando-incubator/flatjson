package bench;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

@State(Scope.Benchmark)
public class Parse {

    private String sample;
    private com.fasterxml.jackson.databind.ObjectReader jackson;
    private com.google.gson.Gson gson;
    private org.boon.json.ObjectMapper boon;

    @Setup public void setup() throws IOException {
        sample = new String(Files.readAllBytes(Paths.get("test/colors.json")));
        jackson = new com.fasterxml.jackson.databind.ObjectMapper().reader(Map.class);
        gson = new com.google.gson.Gson();
        boon = org.boon.json.JsonFactory.create();
    }

    @Benchmark public org.zalando.flatjson.Json flatjson() {
        return org.zalando.flatjson.Json.parse(sample);
    }

    @Benchmark public com.eclipsesource.json.JsonValue minimaljson() {
        return com.eclipsesource.json.Json.parse(sample);
    }

    @Benchmark public Map jackson() throws IOException {
        return jackson.readValue(sample);
    }

    @Benchmark public Map gson() {
        return gson.fromJson(sample, Map.class);
    }

    @Benchmark public Map boon() {
        return boon.readValue(sample, Map.class);
    }
}

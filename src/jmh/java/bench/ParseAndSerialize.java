package bench;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonValue;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.zalando.flatjson.Json;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@State(Scope.Benchmark)
public class ParseAndSerialize {

    private String sample;
    private com.fasterxml.jackson.databind.ObjectMapper jackson_mapper;
    private com.fasterxml.jackson.databind.ObjectReader jackson_reader;
    private com.google.gson.Gson gson;
    private org.boon.json.ObjectMapper boon;

    @Setup public void setup() throws IOException {
        sample = new String(Files.readAllBytes(Paths.get("test/colors.json")));
        jackson_mapper = new com.fasterxml.jackson.databind.ObjectMapper();
        jackson_reader = jackson_mapper.reader(Map.class);
        gson = new com.google.gson.Gson();
        boon = org.boon.json.JsonFactory.create();
    }

    @Benchmark public String flatjson() {
        Json event = Json.parse(sample);
        List<Json> data = event.asObject().get("data").asArray();
        Collections.reverse(data);
        return data.toString();
    }

    @Benchmark public String minimaljson() {
        JsonValue event = com.eclipsesource.json.Json.parse(sample);
        List<JsonValue> data = event.asObject().get("data").asArray().values();
        data = new ArrayList(data); // original list is not reversible
        Collections.reverse(data);
        JsonArray result = (JsonArray) com.eclipsesource.json.Json.array();
        for (JsonValue value : data) result.add(value);
        return result.toString();
    }

    @Benchmark public String jackson() throws IOException {
        Map event = jackson_reader.readValue(sample);
        List data = (List) event.get("data");
        Collections.reverse(data);
        return jackson_mapper.writeValueAsString(data);
    }

    @Benchmark public String gson() {
        Map event = gson.fromJson(sample, Map.class);
        List data = (List) event.get("data");
        Collections.reverse(data);
        String result = gson.toJson(data);
        return result;
    }

    @Benchmark public String boon() {
        Map event = boon.readValue(sample, Map.class);
        List data = (List) event.get("data");
        data = new ArrayList(data); // original list is not reversible
        Collections.reverse(data);
        String result = boon.writeValueAsString(data);
        return result;
    }


}

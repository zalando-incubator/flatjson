package bench;

import com.eclipsesource.json.JsonValue;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.zalando.flatjson.Json;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

@State(Scope.Benchmark)
public class ParseSample {

    private String sample;

    @Setup public void loadSample() throws IOException {
        sample = new String(Files.readAllBytes(Paths.get("test/sample.json")));
    }

    @Benchmark public Json flatjson() {
        return Json.parse(sample);
    }

    @Benchmark public JsonValue minimaljson() {
        return com.eclipsesource.json.Json.parse(sample);
    }

    @Benchmark public Map jackson() throws IOException {
        ObjectReader reader = new ObjectMapper().reader(Map.class);
        return reader.readValue(sample);
    }
}

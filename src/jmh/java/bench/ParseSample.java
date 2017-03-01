package bench;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.zalando.flatjson.Json;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@State(Scope.Benchmark)
public class ParseSample {

    private String sample;

    @Setup
    public void loadSample() throws IOException {
        sample = new String(Files.readAllBytes(Paths.get("test/sample.json")));
    }

    @Benchmark
    public Json parse() {
        return Json.parse(sample);
    }
}

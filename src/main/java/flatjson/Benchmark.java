package flatjson;

import com.google.gson.Gson;
import org.boon.json.JsonFactory;
import org.boon.json.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;


public abstract class Benchmark {

    public static class FlatJsonBenchmark extends Benchmark {
        @Override protected String process(String input) {
            return Json.parse(input)
                    .asObject().get("TVUN8QNVHW")
                    .asObject().get("D0jukTjAmn")
                    .asArray().get(5)
                    .toString();
        }
    }

    public static class SimpleJsonBenchmark extends Benchmark {
        @Override protected String process(String input) {
            return com.eclipsesource.json.Json.parse(input)
                    .asObject().get("TVUN8QNVHW")
                    .asObject().get("D0jukTjAmn")
                    .asArray().get(5)
                    .toString();
        }
    }

    public static class BoonBenchmark extends Benchmark {
        private static final ObjectMapper mapper = JsonFactory.create();

        @Override protected String process(String input) {
            Map one = mapper.readValue(input, Map.class);
            Map two = (Map) one.get("TVUN8QNVHW");
            List three = (List) two.get("D0jukTjAmn");
            return mapper.writeValueAsString(three.get(5));
        }
    }

    public static class GsonBenchmark extends Benchmark {
        private static final Gson gson = new Gson();

        @Override protected String process(String input) {
            Map one = gson.fromJson(input, Map.class);
            Map two = (Map) one.get("TVUN8QNVHW");
            List three = (List) two.get("D0jukTjAmn");
            return gson.toJson(three.get(5));
        }
    }

    // inspired by jmh
    private static class Blackhole {

        private int tlr = (int) System.nanoTime();
        private int tlrMask = 1;

        public final void consume(Object obj) {
            int tlr = (this.tlr = (this.tlr * 1664525 + 1013904223));
            if ((tlr & tlrMask) == 0) {
                this.tlr = obj.hashCode() ^ (int) System.nanoTime();
                this.tlrMask = (this.tlrMask << 1) + 1;
            }
        }
    }

    private Blackhole blackhole = new Blackhole();

    public void execute(int warmupRuns, int runs) throws IOException {
        System.out.println("-----------------------------------------------------------");
        System.out.println(getClass().getName());
        String input = new String(Files.readAllBytes(Paths.get("test/sample.json")));
        warmup(input, warmupRuns);
        double time = benchmark(input, runs);
        System.out.println();
        System.out.println(String.format("time: %3.3f ms", time));
        collectGarbage();
    }

    protected abstract String process(String input);

    private void warmup(String input, int runs) {
        System.out.print("warmup ");
        for (int i = 1; i <= runs; i++) {
            blackhole.consume(process(input));
            showProgress(i);
        }
        System.out.println(process(input));
        collectGarbage();
    }

    private double benchmark(String input, int runs) {
        System.out.print("benchmark ");
        long start = System.nanoTime();
        for (int i = 1; i <= runs; i++) {
            blackhole.consume(process(input));
            showProgress(i);
        }
        return (System.nanoTime() - start) / 1_000_000.0 / runs;
    }

    private void showProgress(int i) {
        if (i % 500 == 0) System.out.print('.');
    }

    private void collectGarbage() {
        System.gc();
        try { Thread.sleep(3000); } catch (InterruptedException ignored) {}
    }

    public static void main(String[] args) throws IOException {
        int warmupRuns = 20_000;
        int runs = 100_000;
        for (int i = 0; i < 3; i++) {
            new GsonBenchmark().execute(warmupRuns, runs);
//            new BoonBenchmark().execute(warmupRuns, runs);
            new FlatJsonBenchmark().execute(warmupRuns, runs);
//            new SimpleJsonBenchmark().execute(warmupRuns, runs);
        }
    }

}

package flatjson;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;


public abstract class Benchmark {

    public static class FlatJsonBenchmark extends Benchmark {
        @Override protected String process(String input) {
            return Json.parse(new String(input))
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

    public void execute(int warmupRuns, int runs) throws IOException {
        System.out.println("-----------------------------------------------------------");
        System.out.println(getClass().getName());
        String input = new String(Files.readAllBytes(Paths.get("test/sample.json")));
        warmup(input, warmupRuns);
        double duration = benchmark(input, runs);
        System.out.println();
        System.out.println(String.format("duration: %3.3f ms", duration));
    }

    protected abstract String process(String input);

    private void warmup(String input, int runs) {
        System.out.print("warmup ");
        for (int i = 1; i <= runs; i++) {
            process(input);
            showProgress(i);
        }
        System.out.println(process(input));
        collectGarbage();
    }

    private double benchmark(String input, int runs) {
        System.out.print("benchmark ");
        long start = System.nanoTime();
        for (int i = 1; i <= runs; i++) {
            process(input);
            showProgress(i);
        }
        return (System.nanoTime() - start) / 1_000_000.0 / runs;
    }

    private void showProgress(int i) {
        if (i % 500 == 0) System.out.print('.');
    }

    private void collectGarbage() {
        System.gc();
        try { Thread.sleep(5000); } catch (InterruptedException ignored) {}
    }

    public static void main(String[] args) throws IOException {
        int warmupRuns = 20_000;
        int runs = 100_000;
        new FlatJsonBenchmark().execute(warmupRuns, runs);
        new SimpleJsonBenchmark().execute(warmupRuns, runs);
    }

}

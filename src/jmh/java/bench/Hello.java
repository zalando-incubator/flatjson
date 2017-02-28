package bench;

import org.openjdk.jmh.annotations.Benchmark;

public class Hello {
    @Benchmark
    public void testSomething()
    {
        System.out.println("hello");
    }
}

package app.benchmark;

import java.util.ArrayList;
import java.util.List;

public class BenchmarkRunner {

    public static void main(String[] args) {
        ZuggeneratorBenchmark bench = new ZuggeneratorBenchmark();
        List<BenchmarkResult> results = new ArrayList<>();

        results.add(bench.run(TestPositions.startPosition(), true, "Start (White)"));
        results.add(bench.run(TestPositions.midGame(), true, "Midgame (White)"));
        results.add(bench.run(TestPositions.endGame(), true, "Endgame (White)"));

        print(results);
    }

    private static void print(List<BenchmarkResult> results) {
        System.out.println("=== ZUGGENERATOR BENCHMARK ===");

        for (BenchmarkResult r : results) {
            System.out.printf(
                    "%s -> %.2f ms | %.2f pos/s | Ø moves: %.2f | total moves: %d%n",
                    r.positionName,
                    r.timeMs,
                    r.positionsPerSecond,
                    r.avgMovesPerPosition,
                    r.totalMovesGenerated
            );
        }
    }
}
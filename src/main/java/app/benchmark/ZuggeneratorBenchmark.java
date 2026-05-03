package app.benchmark;

import app.Zuggenerator;

public class ZuggeneratorBenchmark {

    private final Zuggenerator generator = new Zuggenerator();

    public BenchmarkResult run(char[][] board, boolean whiteToMove, String name) {

        // Warmup
        for (int i = 0; i < 2000; i++) {
            generator.getAllLegalMoves(board, whiteToMove);
        }

        int iterations = 10000;

        long start = System.nanoTime();

        int totalMoves = 0;

        for (int i = 0; i < iterations; i++) {
            totalMoves += generator.getAllLegalMoves(board, whiteToMove).size();
        }

        long end = System.nanoTime();

        return new BenchmarkResult(
                name,
                (end - start) / 1e6,
                iterations,
                totalMoves
        );
    }
}
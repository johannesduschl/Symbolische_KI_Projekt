package app.benchmark;

import app.board.Zuggenerator;

public class ZuggeneratorBenchmark {

    private final Zuggenerator generator = new Zuggenerator();

    public BenchmarkResult run(char[][] board, boolean whiteToMove, String name) {

        for (int i = 0; i < 2000; i++) {
            generator.getAllLegalMoves(board, whiteToMove);
        }

        int iterations = 10000;
        int totalMoves = 0;

        long start = System.nanoTime();

        for (int i = 0; i < iterations; i++) {
            totalMoves += generator.getAllLegalMoves(board, whiteToMove).size();
        }

        long end = System.nanoTime();
        double timeMs = (end - start) / 1e6;

        return new BenchmarkResult(
                name,
                timeMs,
                iterations,
                totalMoves
        );
    }
}
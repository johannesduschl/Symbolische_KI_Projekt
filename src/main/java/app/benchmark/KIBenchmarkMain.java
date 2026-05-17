package app.benchmark;

import app.KI.AlphaBetaKI;
import app.board.Board;

import java.util.ArrayList;
import java.util.List;

public class KIBenchmarkMain {

    public static void main(String[] args) {

        AlphaBetaKI ki = new AlphaBetaKI();
        KIBenchmarkRunner runner = new KIBenchmarkRunner(ki);

        List<KIBenchmarkResult> results = new ArrayList<>();

        Board start = new Board(TestPositions.startPosition());
        Board mid = new Board(TestPositions.midGame());
        Board end = new Board(TestPositions.endGame());

        // =========================================================
        // EXPERIMENT 1: 1s SEARCH (Alpha-Beta vs Minimax)
        // =========================================================
        results.add(runner.run(start, true, 1000L, 10, true,  "START - 1s"));
        results.add(runner.run(start, true, 1000L, 10, false, "START - 1s"));

        results.add(runner.run(mid, true, 1000L, 10, true,  "MID - 1s"));
        results.add(runner.run(mid, true, 1000L, 10, false, "MID - 1s"));

        results.add(runner.run(end, true, 1000L, 10, true,  "END - 1s"));
        results.add(runner.run(end, true, 1000L, 10, false, "END - 1s"));

        // =========================================================
        // EXPERIMENT 2: DEPTH LIMIT 4
        // (NO TIME LIMIT -> Long.MAX_VALUE)
        // =========================================================
        long NO_LIMIT = Long.MAX_VALUE;

        results.add(runner.run(start, true, NO_LIMIT, 4, true,  "START - depth 4"));
        results.add(runner.run(start, true, NO_LIMIT, 4, false, "START - depth 4"));

        results.add(runner.run(mid, true, NO_LIMIT, 4, true,  "MID - depth 4"));
        results.add(runner.run(mid, true, NO_LIMIT, 4, false, "MID - depth 4"));

        results.add(runner.run(end, true, NO_LIMIT, 4, true,  "END - depth 4"));
        results.add(runner.run(end, true, NO_LIMIT, 4, false, "END - depth 4"));

        // =========================================================
        // EXPERIMENT 3: EXHAUST SEARCH (MAX DEPTH / 2 MIN LIMIT)
        // =========================================================
        long TWO_MIN = 120_000L;

        results.add(runner.run(start, true, TWO_MIN, 50, true,  "START - max search"));
        results.add(runner.run(start, true, TWO_MIN, 50, false, "START - max search"));

        results.add(runner.run(mid, true, TWO_MIN, 50, true,  "MID - max search"));
        results.add(runner.run(mid, true, TWO_MIN, 50, false, "MID - max search"));

        results.add(runner.run(end, true, TWO_MIN, 50, true,  "END - max search"));
        results.add(runner.run(end, true, TWO_MIN, 50, false, "END - max search"));

        // =========================================================
        // OUTPUT
        // =========================================================
        KIBenchmarkRunner.print(results);
    }
}
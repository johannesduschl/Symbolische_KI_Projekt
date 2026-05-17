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

        // =========================
        // TIME TEST (1s)
        // =========================

        results.add(runner.run(
                new Board(TestPositions.startPosition()),
                true,
                1000L,
                10,
                "START - 1s"
        ));

        results.add(runner.run(
                new Board(TestPositions.midGame()),
                true,
                1000L,
                10,
                "MID - 1s"
        ));

        results.add(runner.run(
                new Board(TestPositions.endGame()),
                true,
                1000L,
                10,
                "END - 1s"
        ));

        // =========================
        // DEPTH TEST (4)
        // =========================

        long NO_LIMIT = Long.MAX_VALUE;

        results.add(runner.run(
                new Board(TestPositions.startPosition()),
                true,
                NO_LIMIT,
                4,
                "START - depth 4"
        ));

        results.add(runner.run(
                new Board(TestPositions.midGame()),
                true,
                NO_LIMIT,
                4,
                "MID - depth 4"
        ));

        results.add(runner.run(
                new Board(TestPositions.endGame()),
                true,
                NO_LIMIT,
                4,
                "END - depth 4"
        ));

        // =========================
        // OUTPUT
        // =========================

        KIBenchmarkRunner.print(results);
    }
}
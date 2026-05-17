package app.benchmark;

import app.KI.AlphaBetaKI;
import app.board.Board;

import java.util.ArrayList;
import java.util.List;

public class KIBenchmarkMain {

    public static void main(String[] args) {

        //TODO: Implement BF
        AlphaBetaKI ki = new AlphaBetaKI();
        KIBenchmarkRunner runner = new KIBenchmarkRunner(ki);

        List<KIBenchmarkResult> results = new ArrayList<>();

        // =========================
        // 1) TIME LIMIT TEST (1s)
        // =========================
        results.add(runner.run(
                new Board(TestPositions.startPosition()),
                true,
                1000,
                10,
                "START - 1s"
        ));

        results.add(runner.run(
                new Board(TestPositions.midGame()),
                true,
                1000,
                10,
                "MID - 1s"
        ));

        results.add(runner.run(
                new Board(TestPositions.endGame()),
                true,
                1000,
                10,
                "END - 1s"
        ));

        // =========================
        // 2) DEPTH LIMIT TEST (4)
        // =========================
        results.add(runner.run(
                new Board(TestPositions.startPosition()),
                true,
                Long.MAX_VALUE,   // kein Zeitlimit effektiv
                4,
                "START - depth 4"
        ));

        results.add(runner.run(
                new Board(TestPositions.midGame()),
                true,
                Long.MAX_VALUE,
                4,
                "MID - depth 4"
        ));

        results.add(runner.run(
                new Board(TestPositions.endGame()),
                true,
                Long.MAX_VALUE,
                4,
                "END - depth 4"
        ));

        // =========================
        // OUTPUT
        // =========================
        KIBenchmarkRunner.print(results);
    }
}
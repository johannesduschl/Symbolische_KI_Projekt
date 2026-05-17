package app.benchmark.alphabetaki;

import app.KI.AlphaBetaKI;
import app.board.Board;
import app.board.Zug;

public class KIBenchmarkRunner {

    private final AlphaBetaKI ki = new AlphaBetaKI();

    public void run() {

        System.out.println("\n=== BENCHMARK START ===\n");

        runPosition("START", TestPositions.startPosition());
        runPosition("MID", TestPositions.midGame());
        runPosition("END", TestPositions.endGame());
    }

    private void runPosition(String name, char[][] board) {

        System.out.println("Position: " + name);

        // -------------------------
        // M: 10k evaluation
        // -------------------------
        long t1 = System.nanoTime();

        for (int i = 0; i < 10000; i++) {
            ki.bf.evaluate(new Board(board), true);
        }

        long evalMs = (System.nanoTime() - t1) / 1_000_000;

        System.out.println("10k eval: " + evalMs + " ms");

        // -------------------------
        // N-Q: 1s search
        // -------------------------
        ki.resetStatsForBenchmark();
        ki.configBenchmark(1000, 100, true);

        long t2 = System.nanoTime();
        Zug best1s = ki.findBestMove(new Board(board), true);
        long searchMs = (System.nanoTime() - t2) / 1_000_000;

        System.out.println("1s best: " + best1s);
        System.out.println("depth: " + ki.lastCompletedDepth);
        System.out.println("nodes: " + ki.nodesSearched);
        System.out.println("time: " + searchMs + " ms");

        // -------------------------
        // R-U: depth 4
        // -------------------------
        ki.resetStatsForBenchmark();
        ki.maxDepth = 4;

        Zug bestD4 = ki.findBestMove(new Board(board), true);

        System.out.println("depth4 best: " + bestD4);

        // -------------------------
        // V-Z: stress
        // -------------------------
        ki.resetStatsForBenchmark();
        ki.maxDepth = 100;
        ki.configBenchmark(120000, 100, true);

        Zug bestStress = ki.findBestMove(new Board(board), true);

        System.out.println("stress best: " + bestStress);
        System.out.println("nodes: " + ki.nodesSearched);
        System.out.println("depth: " + ki.lastCompletedDepth);

        System.out.println("---------------------\n");
    }
}
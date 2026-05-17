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

    private void runPosition(String name, char[][] boardArray) {

        Board board = new Board(boardArray);

        System.out.println("=================================");
        System.out.println("Position: " + name);
        System.out.println("=================================");

        // -------------------------
        // M: 10k evaluation
        // -------------------------
        long evalStart = System.nanoTime();

        for (int i = 0; i < 10000; i++) {
            ki.bf.evaluate(board);
        }

        long evalTimeMs = (System.nanoTime() - evalStart) / 1_000_000;

        System.out.println("10k eval: " + evalTimeMs + " ms");

        // -------------------------
        // N-Q: AlphaBeta 1s
        // -------------------------
        ki.resetStatsForBenchmark();

        ki.configBenchmark(1000, 100, true);

        long abStart = System.nanoTime();

        Zug abBestMove = ki.findBestMove(new Board(boardArray), true);

        long abTimeMs = (System.nanoTime() - abStart) / 1_000_000;

        double abNodesPerSecond =
                ki.nodesSearched / (abTimeMs / 1000.0);

        System.out.println("\nALPHABETA 1s");
        System.out.println("best move: " + abBestMove);
        System.out.println("depth: " + ki.lastCompletedDepth);
        System.out.println("nodes: " + ki.nodesSearched);
        System.out.println("nodes/sec: " + (long) abNodesPerSecond);
        System.out.println("time: " + abTimeMs + " ms");

        // -------------------------
        // N-Q: Minimax 1s
        // -------------------------
        ki.resetStatsForBenchmark();

        ki.configBenchmark(1000, 100, false);

        long mmStart = System.nanoTime();

        Zug mmBestMove = ki.findBestMove(new Board(boardArray), true);

        long mmTimeMs = (System.nanoTime() - mmStart) / 1_000_000;

        double mmNodesPerSecond =
                ki.nodesSearched / (mmTimeMs / 1000.0);

        System.out.println("\nMINIMAX 1s");
        System.out.println("best move: " + mmBestMove);
        System.out.println("depth: " + ki.lastCompletedDepth);
        System.out.println("nodes: " + ki.nodesSearched);
        System.out.println("nodes/sec: " + (long) mmNodesPerSecond);
        System.out.println("time: " + mmTimeMs + " ms");

        // -------------------------
        // R-U: depth 4
        // -------------------------
        ki.resetStatsForBenchmark();

        ki.configBenchmark(120000, 4, true);

        long d4Start = System.nanoTime();

        Zug depth4Move = ki.findBestMove(new Board(boardArray), true);

        long d4TimeMs = (System.nanoTime() - d4Start) / 1_000_000;

        double d4NodesPerSecond =
                ki.nodesSearched / (d4TimeMs / 1000.0);

        System.out.println("\nDEPTH 4");
        System.out.println("best move: " + depth4Move);
        System.out.println("depth: " + ki.lastCompletedDepth);
        System.out.println("nodes: " + ki.nodesSearched);
        System.out.println("nodes/sec: " + (long) d4NodesPerSecond);
        System.out.println("time: " + d4TimeMs + " ms");

        // -------------------------
        // V-Z: stress test
        // -------------------------
        ki.resetStatsForBenchmark();

        ki.configBenchmark(120000, 100, true);

        long stressStart = System.nanoTime();

        Zug stressMove = ki.findBestMove(new Board(boardArray), true);

        long stressTimeMs = (System.nanoTime() - stressStart) / 1_000_000;

        double stressNodesPerSecond =
                ki.nodesSearched / (stressTimeMs / 1000.0);

        System.out.println("\nSTRESS");
        System.out.println("best move: " + stressMove);
        System.out.println("depth: " + ki.lastCompletedDepth);
        System.out.println("nodes: " + ki.nodesSearched);
        System.out.println("nodes/sec: " + (long) stressNodesPerSecond);
        System.out.println("time: " + stressTimeMs + " ms");

        System.out.println("\n");
    }
}
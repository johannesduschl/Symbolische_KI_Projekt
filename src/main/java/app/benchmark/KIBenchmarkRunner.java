package app.benchmark;

import app.KI.AlphaBetaKI;
import app.board.Board;

import java.util.List;

public class KIBenchmarkRunner {

    private final AlphaBetaKI ki;

    public KIBenchmarkRunner(AlphaBetaKI ki) {
        this.ki = ki;
    }

    // =========================
    // GENERIC RUN (TIME + ID)
    // =========================
    public KIBenchmarkResult run(Board board,
                                 boolean whiteToMove,
                                 long timeLimitMs,
                                 int maxDepth,
                                 String name) {

        ki.resetStats();

        long start = System.nanoTime();

        ki.iterativeDeepening(board, whiteToMove, timeLimitMs, maxDepth);

        long end = System.nanoTime();

        double timeMs = (end - start) / 1e6;

        return new KIBenchmarkResult(
                name,
                timeMs,
                ki.getLastDepthReached(),
                ki.getNodesSearched(),
                ki.getBestMove()
        );
    }

    // =========================
    // RUN WITH EXPLICIT MODE
    // (CUTOFF vs MINIMAX)
    // =========================
    public KIBenchmarkResult run(Board board,
                                 boolean whiteToMove,
                                 long timeLimitMs,
                                 int maxDepth,
                                 boolean useCutoffs,
                                 String name) {

        ki.resetStats();
        ki.setUseCutoffs(useCutoffs);

        long start = System.nanoTime();

        ki.iterativeDeepening(board, whiteToMove, timeLimitMs, maxDepth);

        long end = System.nanoTime();

        double timeMs = (end - start) / 1e6;

        return new KIBenchmarkResult(
                name + (useCutoffs ? " (AB)" : " (MM)"),
                timeMs,
                ki.getLastDepthReached(),
                ki.getNodesSearched(),
                ki.getBestMove()
        );
    }

    // =========================
    // PRINT RESULTS
    // =========================
    public static void print(List<KIBenchmarkResult> results) {

        System.out.println("\n=== KI BENCHMARK ===\n");

        for (KIBenchmarkResult r : results) {

            System.out.println(r.positionName);
            System.out.printf("time: %.2f ms%n", r.timeMs);
            System.out.printf("depth: %d%n", r.depthReached);
            System.out.printf("nodes: %d%n", r.nodesSearched);
            System.out.printf("nodes/sec: %.0f%n", r.nodesPerSecond);
            System.out.printf("best: %s%n", r.bestMove);
            System.out.println("---------------------------");
        }
    }
}
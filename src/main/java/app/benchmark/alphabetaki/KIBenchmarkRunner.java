package app.benchmark.alphabetaki;

import app.KI.AlphaBetaKI;
import app.board.Board;
import app.board.Zug;

import java.util.List;
import java.util.Locale;

public class KIBenchmarkRunner {

    private static final boolean WHITE_TO_MOVE = true;

    private static final int EVAL_WARMUP = 2_000;
    private static final int EVAL_ITERATIONS = 10_000;

    private static final long ONE_SECOND_MS = 1_000;
    private static final long TWO_MINUTES_MS = 120_000;

    private static final int FIXED_DEPTH = 4;

    private record SearchConfig(
            String name,
            boolean useAlphaBeta,
            boolean useTranspositionTable,
            boolean useMoveOrdering,
            boolean useNullMovePruning
    ) {}

    private record SearchResult(
            String positionName,
            String configName,
            int depth,
            long timeMs,
            long nodes,
            long nodesPerSecond,
            String bestMove
    ) {}

    public void run() {
        System.out.println("\n=== BENCHMARK START ===\n");

        runEvaluationBenchmark();

        runSearchBenchmarkSet(
                "Iterative Suche mit Zeitlimit von 1 Sekunde",
                ONE_SECOND_MS,
                100,
                benchmarkConfigs()
        );

        runSearchBenchmarkSet(
                "Benchmark bei festem Tiefenlimit von 4",
                TWO_MINUTES_MS,
                FIXED_DEPTH,
                benchmarkConfigs()
        );

        runSearchBenchmarkSet(
                "Benchmark bei maximaler Suchzeit von 2 Minuten",
                TWO_MINUTES_MS,
                100,
                benchmarkConfigs()
        );
    }

    private void runEvaluationBenchmark() {
        System.out.println("=================================");
        System.out.println("BEWERTUNGSFUNKTION");
        System.out.println("=================================");

        runEvaluationOnPosition("START", TestPositions.startPosition());
        runEvaluationOnPosition("MITTELSPIEL", TestPositions.midGame());
        runEvaluationOnPosition("ENDSPIEL", TestPositions.endGame());

        System.out.println();
    }

    private void runEvaluationOnPosition(String positionName, char[][] boardArray) {
        AlphaBetaKI ki = new AlphaBetaKI();
        Board board = new Board(boardArray);

        for (int i = 0; i < EVAL_WARMUP; i++) {
            ki.bf.evaluate(board);
        }

        long checksum = 0;
        long start = System.nanoTime();

        for (int i = 0; i < EVAL_ITERATIONS; i++) {
            checksum += ki.bf.evaluate(board);
        }

        long timeMs = (System.nanoTime() - start) / 1_000_000L;
        if (timeMs == 0) timeMs = 1;

        double evalsPerSecond = EVAL_ITERATIONS / (timeMs / 1000.0);

        System.out.printf(
                Locale.GERMAN,
                "%-12s | %8d ms | %,.0f Bewertungen/s | checksum=%d%n",
                positionName,
                timeMs,
                evalsPerSecond,
                checksum
        );
    }

    private void runSearchBenchmarkSet(String title,
                                       long timeLimitMs,
                                       int maxDepth,
                                       List<SearchConfig> configs) {

        System.out.println("=================================");
        System.out.println(title);
        System.out.println("=================================");
        System.out.printf(
                Locale.GERMAN,
                "%-12s | %-34s | %4s | %12s | %14s | %14s | %s%n",
                "Position",
                "Konfiguration",
                "Tiefe",
                "Zeit (ms)",
                "Knoten",
                "Knoten/s",
                "Bester Zug"
        );

        printSearchLineForPosition("START", TestPositions.startPosition(), timeLimitMs, maxDepth, configs);
        printSearchLineForPosition("MITTELSPIEL", TestPositions.midGame(), timeLimitMs, maxDepth, configs);
        printSearchLineForPosition("ENDSPIEL", TestPositions.endGame(), timeLimitMs, maxDepth, configs);

        System.out.println();
    }

    private void printSearchLineForPosition(String positionName,
                                            char[][] boardArray,
                                            long timeLimitMs,
                                            int maxDepth,
                                            List<SearchConfig> configs) {

        for (SearchConfig config : configs) {
            SearchResult result = runSearch(positionName, boardArray, timeLimitMs, maxDepth, config);

            System.out.printf(
                    Locale.GERMAN,
                    "%-12s | %-34s | %4d | %12d | %14d | %14d | %s%n",
                    result.positionName(),
                    result.configName(),
                    result.depth(),
                    result.timeMs(),
                    result.nodes(),
                    result.nodesPerSecond(),
                    result.bestMove()
            );
        }
    }

    private SearchResult runSearch(String positionName,
                                   char[][] boardArray,
                                   long timeLimitMs,
                                   int maxDepth,
                                   SearchConfig config) {

        AlphaBetaKI ki = new AlphaBetaKI();

        // WICHTIG:
        // Diese Schalter müssen in AlphaBetaKI vorhanden sein.
        ki.useAlphaBeta = config.useAlphaBeta();
        ki.useTranspositionTable = config.useTranspositionTable();
        ki.useMoveOrdering = config.useMoveOrdering();
        ki.useNullMovePruning = config.useNullMovePruning();

        ki.configBenchmark(timeLimitMs, maxDepth, config.useAlphaBeta());
        ki.resetStatsForBenchmark();

        // Optionaler Warmup-Lauf, damit JIT-Effekte die erste Messung nicht dominieren.
        ki.findBestMove(new Board(boardArray), WHITE_TO_MOVE);
        ki.resetStatsForBenchmark();

        long start = System.nanoTime();
        Zug bestMove = ki.findBestMove(new Board(boardArray), WHITE_TO_MOVE);
        long timeMs = (System.nanoTime() - start) / 1_000_000L;

        if (timeMs == 0) timeMs = 1;

        long nodesPerSecond = (long) (ki.nodesSearched / (timeMs / 1000.0));

        return new SearchResult(
                positionName,
                config.name(),
                ki.lastCompletedDepth,
                timeMs,
                ki.nodesSearched,
                nodesPerSecond,
                bestMove == null ? "-" : bestMove.toString()
        );
    }

    private List<SearchConfig> benchmarkConfigs() {
        return List.of(
                new SearchConfig(
                        "Baseline / Minimax",
                        false,
                        false,
                        false,
                        false
                ),
                new SearchConfig(
                        "Alpha-Beta",
                        true,
                        false,
                        false,
                        false
                ),
                new SearchConfig(
                        "Alpha-Beta + TT",
                        true,
                        true,
                        false,
                        false
                ),
                new SearchConfig(
                        "Alpha-Beta + Sortierung",
                        true,
                        false,
                        true,
                        false
                ),
                new SearchConfig(
                        "Alpha-Beta + Nullzug",
                        true,
                        false,
                        false,
                        true
                ),
                new SearchConfig(
                        "Alle Techniken",
                        true,
                        true,
                        true,
                        true
                )
        );
    }
}
package app.KI;

import app.board.Board;
import app.board.Zug;
import app.board.Zuggenerator;

import java.util.List;

public class AlphaBetaKI {

    public AlphaBetaKI(Bewertungsfunktion bf){
        this.zugsortierer = new Zugsortierer(maxDepth);
        this.bf = bf;
    }
    public AlphaBetaKI() {
        this.zugsortierer = new Zugsortierer(maxDepth);
        this.bf = new BewertungsfunktionImpl();
    }

    // Bewertungsfunktion
    public Bewertungsfunktion bf;

    // Suchparameter
    public int maxDepth = 4;

    // Schalter für die Benchmark-Konfiguration
    public boolean useAlphaBeta = true;
    public boolean useTranspositionTable = true;
    public boolean useMoveOrdering = true;
    public boolean useNullMovePruning = true;

    private final Zuggenerator zuggenerator = new Zuggenerator();
    private Zugsortierer zugsortierer;
    private final transpositionTable tt = new transpositionTable(22); // 22~4 Mio. Slots

    /**
     * Counts the moves over the period of a game, to adjust the time limit
     * 0-10 Züge = opening, max 5s pro Zug
     * 11-50 Züge = midgame, max 8s pro Zug
     * 51+ Züge = endgame, max 4s pro Zug
     */
    public int moveCounter = 0;

    private long startTime;
    private long timeLimit;

    // Benchmark
    public long nodesSearched = 0;
    public int lastCompletedDepth = 0;
    public Zug bestMove;
    public boolean benchmarkMode = false;
    private long benchmarkTimeLimitMs;


    public Zug findBestMove(Board board, boolean isWhiteToMove) {

        List<Zug> allMoves = zuggenerator.getAllLegalMoves(board.getBoard(), isWhiteToMove);
        if (allMoves.isEmpty()) return null;

        bestMove = allMoves.getFirst();
        startTime = System.nanoTime();

        if (benchmarkMode) {
            timeLimit = benchmarkTimeLimitMs * 1_000_000;
        } else {
            timeLimit = getTimeForMove() * 1_000_000;
        }

        for (int depth = 1; depth <= maxDepth; depth++) {

            if (useMoveOrdering) {
                allMoves = zugsortierer.getSortedList(allMoves, board, depth);
            }

            if (timeUp()) break;

            Zug currentBestMove = null;
            int currentBestScore = isWhiteToMove ? Integer.MIN_VALUE : Integer.MAX_VALUE;

            for (Zug move : allMoves) {

                board.move(move);

                int score;

                if (isWhiteToMove) {
                    score = alphaBetaMin(board, Integer.MIN_VALUE, Integer.MAX_VALUE, depth - 1, 1);

                    if (score > currentBestScore) {
                        currentBestScore = score;
                        currentBestMove = move;
                    }
                } else {
                    score = alphaBetaMax(board, Integer.MIN_VALUE, Integer.MAX_VALUE, depth - 1, 1);

                    if (score < currentBestScore) {
                        currentBestScore = score;
                        currentBestMove = move;
                    }
                }

                board.undoMove();
            }

            if ((benchmarkMode || !timeUp()) && currentBestMove != null) {
                bestMove = currentBestMove;
                lastCompletedDepth = depth;
            } else {
                break;
            }
        }

        moveCounter++;
        return bestMove;
    }

    public int alphaBetaMax(Board board, int alpha, int beta, int depth, int depthAsc) {
        return alphaBetaMax(board, alpha, beta, depth, depthAsc, true);
    }

    private int alphaBetaMax(Board board, int alpha, int beta, int depth, int depthAsc, boolean allowNullMovePruning) {

        nodesSearched++;

        long hash = 0L;
        TTEntry entry = null;

        if (useTranspositionTable) {
            hash = board.getZobristHash();
            entry = tt.lookup(hash);

            if (entry != null && entry.depth >= depth) {
                if (entry.flag == TTEntry.EXACT) return entry.score;
                if (entry.flag == TTEntry.LOWER) alpha = Math.max(alpha, entry.score);
                if (entry.flag == TTEntry.UPPER) beta = Math.min(beta, entry.score);
                if (alpha >= beta) return entry.score;
            }
        }

        if (depth == 0 || board.isGameOver()) {
            int score = bf.evaluate(board);
            if (useTranspositionTable) {
                tt.store(hash, score, depth, TTEntry.EXACT, null);
            }
            return score;
        }

        if (!benchmarkMode && timeUp()) return bf.evaluate(board);

        // ✔ single null-move entry point
        if (useNullMovePruning && allowNullMovePruning) {
            Integer nullScore = tryNullMoveMax(board, alpha, beta, depth, depthAsc, hash);
            if (nullScore != null) return nullScore;
        }

        boolean isWhiteToMove = !board.blackMovesNext();

        List<Zug> allMoves = zuggenerator.getAllLegalMoves(
                board.getBoard(),
                isWhiteToMove
        );

        if (useMoveOrdering) {
            allMoves = zugsortierer.getSortedList(allMoves, board, depthAsc);
        }

        int bestScore = Integer.MIN_VALUE;
        Zug bestMoveLocal = null;
        int origAlpha = alpha;

        for (Zug move : allMoves) {

            board.move(move);

            int score = alphaBetaMin(
                    board,
                    alpha,
                    beta,
                    depth - 1,
                    depthAsc + 1,
                    false
            );

            board.undoMove();

            if (score > bestScore) {
                bestScore = score;
                bestMoveLocal = move;
            }

            if (useAlphaBeta && score >= beta) {
                if (useMoveOrdering) {
                    zugsortierer.storeKillerMove(move, depthAsc);
                    zugsortierer.addHistory(move, depthAsc);
                }

                if (useTranspositionTable) {
                    tt.store(hash, bestScore, depth, TTEntry.LOWER, move);
                }
                return bestScore;
            }

            if (score > alpha) alpha = score;
        }

        byte flag;
        if (bestScore <= origAlpha) flag = TTEntry.UPPER;
        else if (bestScore >= beta) flag = TTEntry.LOWER;
        else flag = TTEntry.EXACT;

        if (useTranspositionTable) {
            tt.store(hash, bestScore, depth, flag, bestMoveLocal);
        }

        return bestScore;
    }

    public int alphaBetaMin(Board board, int alpha, int beta, int depth, int depthAsc) {
        return alphaBetaMin(board, alpha, beta, depth, depthAsc, true);
    }

    private int alphaBetaMin(Board board, int alpha, int beta, int depth, int depthAsc, boolean allowNullMovePruning) {

        nodesSearched++;

        long hash = 0L;
        TTEntry entry = null;

        if (useTranspositionTable) {
            hash = board.getZobristHash();
            entry = tt.lookup(hash);

            if (entry != null && entry.depth >= depth) {
                if (entry.flag == TTEntry.EXACT) return entry.score;
                if (entry.flag == TTEntry.LOWER) alpha = Math.max(alpha, entry.score);
                if (entry.flag == TTEntry.UPPER) beta = Math.min(beta, entry.score);
                if (alpha >= beta) return entry.score;
            }
        }
        if (depth == 0 || board.isGameOver()) {
            int score = bf.evaluate(board);
            if (useTranspositionTable) {
                tt.store(hash, score, depth, TTEntry.EXACT, null);
            }
            return score;
        }

        if (!benchmarkMode && timeUp()) return bf.evaluate(board);

        if (useNullMovePruning && allowNullMovePruning) {
            Integer nullScore = tryNullMoveMin(board, alpha, beta, depth, depthAsc, hash);
            if (nullScore != null) return nullScore;
        }

        boolean isWhiteToMove = !board.blackMovesNext();

        List<Zug> allMoves = zuggenerator.getAllLegalMoves(
                board.getBoard(),
                isWhiteToMove
        );

        if (useMoveOrdering) {
            allMoves = zugsortierer.getSortedList(allMoves, board, depthAsc);
        }

        int bestScore = Integer.MAX_VALUE;
        Zug bestMoveLocal = null;
        int origBeta = beta;

        for (Zug move : allMoves) {

            board.move(move);

            int score = alphaBetaMax(
                    board,
                    alpha,
                    beta,
                    depth - 1,
                    depthAsc + 1,
                    false
            );

            board.undoMove();

            if (score < bestScore) {
                bestScore = score;
                bestMoveLocal = move;
            }

            if (useAlphaBeta && score <= alpha) {
                if (useMoveOrdering) {
                    zugsortierer.storeKillerMove(move, depthAsc);
                    zugsortierer.addHistory(move, depthAsc);
                }

                if (useTranspositionTable) {
                    tt.store(hash, bestScore, depth, TTEntry.UPPER, move);
                }
                return bestScore;
            }

            if (score < beta) beta = score;
        }

        byte flag;
        if (bestScore <= alpha) flag = TTEntry.UPPER;
        else if (bestScore >= origBeta) flag = TTEntry.LOWER;
        else flag = TTEntry.EXACT;

        if (useTranspositionTable) {
            tt.store(hash, bestScore, depth, flag, bestMoveLocal);
        }

        return bestScore;
    }

    private Integer tryNullMoveMax(Board board, int alpha, int beta, int depth, int depthAsc, long hash) {

        int pieces = bf.pieceCount(board);
        boolean kingOnThrone = isKingOnThrone(board);
        boolean kingHasDirectEdgeSight = bf.kingHasDirectEdgeSight(board);

        if (!allowNullMove(depth, pieces, kingOnThrone, kingHasDirectEdgeSight)) return null;

        int reduction = nullMoveReduction(pieces, kingOnThrone, kingHasDirectEdgeSight);
        int reducedDepth = depth - 1 - reduction;

        if (reducedDepth < 0) return null;

        // ===== MAKE NULL MOVE =====
        board.makeNullMove();

        int score = alphaBetaMin(board, alpha, beta, reducedDepth, depthAsc + 1, true);

        board.undoNullMove();
        // ==========================

        if (score >= beta) {

            if (shouldVerifyNullMove(depth, pieces, kingOnThrone, kingHasDirectEdgeSight)) {

                int verifyScore;

                // Re-use original board state (already restored)

                verifyScore = alphaBetaMax(board, beta - 1, beta, depth - 1, depthAsc, false);

                if (verifyScore >= beta) {
                    if (useTranspositionTable) {
                        tt.store(hash, verifyScore, depth, TTEntry.LOWER, null);
                    }
                    return verifyScore;
                }
                return null;
            }

            if (useTranspositionTable) {
                tt.store(hash, score, depth, TTEntry.LOWER, null);
            }

            return score;
        }

        return null;
    }

    private Integer tryNullMoveMin(Board board, int alpha, int beta, int depth, int depthAsc, long hash) {

        int pieces = bf.pieceCount(board);
        boolean kingOnThrone = isKingOnThrone(board);
        boolean kingHasDirectEdgeSight = bf.kingHasDirectEdgeSight(board);

        if (!allowNullMove(depth, pieces, kingOnThrone, kingHasDirectEdgeSight)) return null;

        int reduction = nullMoveReduction(pieces, kingOnThrone, kingHasDirectEdgeSight);
        int reducedDepth = depth - 1 - reduction;

        if (reducedDepth < 0) return null;

        // ===== MAKE NULL MOVE =====
        board.makeNullMove();

        int score = alphaBetaMax(board, alpha, beta, reducedDepth, depthAsc + 1, true);

        board.undoNullMove();
        // ==========================

        if (score <= alpha) {

            if (shouldVerifyNullMove(depth, pieces, kingOnThrone, kingHasDirectEdgeSight)) {

                int verifyScore = alphaBetaMin(board, alpha, alpha + 1, depth - 1, depthAsc, false);

                if (verifyScore <= alpha) {
                    if (useTranspositionTable) {
                        tt.store(hash, verifyScore, depth, TTEntry.UPPER, null);
                    }
                    return verifyScore;
                }

                return null;
            }

            if (useTranspositionTable) {
                tt.store(hash, score, depth, TTEntry.UPPER, null);
            }

            return score;
        }

        return null;
    }

    /**
     * Null-Move nur dann zulassen, wenn die Stellung dafür stabil genug ist.
     * - direkte Rand-Sicht des Königs => deutlich vorsichtiger
     * - sobald der König den Thron verlassen hat => klar taktischer, aber nicht komplett aus
     * - 10-12 Figuren => komplett aus
     * - je taktischer die Stellung, desto tiefer muss der Knoten sein
     */
    private boolean allowNullMove(int depth, int pieces, boolean kingOnThrone, boolean kingHasDirectEdgeSight) {
        if (pieces <= 12) return false;

        int minDepth = nullMoveMinDepth(pieces, kingOnThrone, kingHasDirectEdgeSight);
        return depth >= minDepth;
    }

    private int nullMoveMinDepth(int pieces, boolean kingOnThrone, boolean kingHasDirectEdgeSight) {
        int minDepth = (pieces > 16) ? 4 : 5;

        if (!kingOnThrone) {
            minDepth += 2;
        }

        if (kingHasDirectEdgeSight) {
            minDepth += 1;
        }

        return minDepth;
    }

    private int nullMoveReduction(int pieces, boolean kingOnThrone, boolean kingHasDirectEdgeSight) {
        int reduction = (pieces > 16) ? 2 : 1;

        if (!kingOnThrone) {
            reduction -= 1;
        }

        if (kingHasDirectEdgeSight) {
            reduction -= 1;
        }

        return Math.max(1, reduction);
    }

    private boolean shouldVerifyNullMove(int depth, int pieces, boolean kingOnThrone, boolean kingHasDirectEdgeSight) {
        if (depth < 5) return false;
        return !kingOnThrone || kingHasDirectEdgeSight || pieces <= 16;
    }

    private boolean isKingOnThrone(Board board) {
        char[][] b = board.getBoard();
        return b != null && b.length > 4 && b[4].length > 4 && b[4][4] == 'k';
    }

    private boolean timeUp() {
        return System.nanoTime() - startTime > timeLimit;
    }

    private long getTimeForMove() {
        if (moveCounter <= 10) {
            return 3000; //war 5
        } else if (moveCounter <= 50) {
            return 5000; // war 8
        }
        return 3000; // war 4
    }

    public void configBenchmark(long timeLimitMs, int maxDepth, boolean useAlphaBeta) {
        benchmarkMode = true;
        this.benchmarkTimeLimitMs = timeLimitMs;
        this.maxDepth = maxDepth;
        this.useAlphaBeta = useAlphaBeta;
    }

    public void resetStatsForBenchmark() {
        nodesSearched = 0;
        lastCompletedDepth = 0;
        bestMove = null;

        // Cache und Heuristik-Infos leeren, damit die Benchmark pro Konfiguration sauber ist
        tt.clear();
        zugsortierer = new Zugsortierer(maxDepth);
    }
}
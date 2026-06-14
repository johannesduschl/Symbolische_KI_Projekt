package app.KI;

import app.board.Board;
import app.board.Zug;
import app.board.Zuggenerator;

import java.util.List;

public class AlphaBetaKI {

    // Wichtig: konkreter Typ, damit die Helfer aus der Bewertungsfunktion direkt nutzbar sind
    public BewertungsfunktionImpl bf = new BewertungsfunktionImpl();
    public int maxDepth = 8;

    Zuggenerator zuggenerator = new Zuggenerator();
    Zugsortierer zugsortierer = new Zugsortierer(maxDepth);

    transpositionTable tt = new transpositionTable(22); // ~4M Slots

    /**
     * Counts the moves over the period of a game, to adjust the time limit
     * 0-10 Züge = opening, max 5s pro Zug
     * 11-50 Züge = midgame, max 8s pro Zug
     * 51+ Züge = endgame, max 4s pro Zug
     */
    private int moveCounter = 0;

    private long startTime;
    private long timeLimit;

    // Benchmark:
    public long nodesSearched = 0;
    public int lastCompletedDepth = 0;
    public Zug bestMove;
    public boolean benchmarkMode = false;
    private long benchmarkTimeLimitMs;

    public boolean useAlphaBeta = true;

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

            allMoves = zugsortierer.getSortedList(allMoves, board, depth);

            if (timeUp()) break;

            Zug currentBestMove = null;
            int currentBestScore = isWhiteToMove ? Integer.MIN_VALUE : Integer.MAX_VALUE;

            for (Zug move : allMoves) {

                if (timeUp()) break;

                Board child = board.copy();
                child.move(move);

                int score;

                if (isWhiteToMove) {
                    score = alphaBetaMin(child, Integer.MIN_VALUE, Integer.MAX_VALUE, depth - 1, 1);

                    if (score > currentBestScore) {
                        currentBestScore = score;
                        currentBestMove = move;
                    }
                } else {
                    score = alphaBetaMax(child, Integer.MIN_VALUE, Integer.MAX_VALUE, depth - 1, 1);

                    if (score < currentBestScore) {
                        currentBestScore = score;
                        currentBestMove = move;
                    }
                }
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

        long hash = board.getZobristHash();
        TTEntry entry = tt.lookup(hash);

        if (entry != null && entry.depth >= depth) {
            if (entry.flag == TTEntry.EXACT) return entry.score;
            if (entry.flag == TTEntry.LOWER) alpha = Math.max(alpha, entry.score);
            if (entry.flag == TTEntry.UPPER) beta = Math.min(beta, entry.score);
            if (alpha >= beta) return entry.score;
        }

        if (depth == 0 || board.isGameOver()) {
            int score = bf.evaluate(board);
            tt.store(hash, score, depth, TTEntry.EXACT, null);
            return score;
        }

        if (!benchmarkMode && timeUp()) return bf.evaluate(board);

        // Null-Move-Pruning
        if (allowNullMovePruning) {
            Integer nullMoveScore = tryNullMoveMax(board, alpha, beta, depth, depthAsc, hash);
            if (nullMoveScore != null) {
                return nullMoveScore;
            }
        }

        List<Zug> allMoves = zuggenerator.getAllLegalMoves(board.getBoard(), false);
        allMoves = zugsortierer.getSortedList(allMoves, board, depthAsc);

        int origAlpha = alpha;
        int bestScore = Integer.MIN_VALUE;
        Zug bestMoveLocal = null;

        for (Zug move : allMoves) {

            Board child = board.copy();
            child.move(move);

            int score = alphaBetaMin(child, alpha, beta, depth - 1, depthAsc + 1, allowNullMovePruning);

            if (score > bestScore) {
                bestScore = score;
                bestMoveLocal = move;
            }

            if (useAlphaBeta && score >= beta) {
                zugsortierer.storeKillerMove(move, depthAsc);
                zugsortierer.addHistory(move, depthAsc);
                tt.store(hash, bestScore, depth, TTEntry.LOWER, move);
                return bestScore;
            }

            if (score > alpha) alpha = score;
        }

        byte flag;
        if (bestScore <= origAlpha) flag = TTEntry.UPPER;
        else if (bestScore >= beta) flag = TTEntry.LOWER;
        else flag = TTEntry.EXACT;

        tt.store(hash, bestScore, depth, flag, bestMoveLocal);
        return bestScore;
    }

    public int alphaBetaMin(Board board, int alpha, int beta, int depth, int depthAsc) {
        return alphaBetaMin(board, alpha, beta, depth, depthAsc, true);
    }

    private int alphaBetaMin(Board board, int alpha, int beta, int depth, int depthAsc, boolean allowNullMovePruning) {

        nodesSearched++;

        long hash = board.getZobristHash();
        TTEntry entry = tt.lookup(hash);

        if (entry != null && entry.depth >= depth) {
            if (entry.flag == TTEntry.EXACT) return entry.score;
            if (entry.flag == TTEntry.LOWER) alpha = Math.max(alpha, entry.score);
            if (entry.flag == TTEntry.UPPER) beta = Math.min(beta, entry.score);
            if (alpha >= beta) return entry.score;
        }

        if (depth == 0 || board.isGameOver()) {
            int score = bf.evaluate(board);
            tt.store(hash, score, depth, TTEntry.EXACT, null);
            return score;
        }

        if (!benchmarkMode && timeUp()) return bf.evaluate(board);

        // Null-Move-Pruning
        if (allowNullMovePruning) {
            Integer nullMoveScore = tryNullMoveMin(board, alpha, beta, depth, depthAsc, hash);
            if (nullMoveScore != null) {
                return nullMoveScore;
            }
        }

        List<Zug> allMoves = zuggenerator.getAllLegalMoves(board.getBoard(), true);
        allMoves = zugsortierer.getSortedList(allMoves, board, depthAsc);

        int origAlpha = alpha;
        int origBeta = beta;
        int bestScore = Integer.MAX_VALUE;
        Zug bestMoveLocal = null;

        for (Zug move : allMoves) {

            Board child = board.copy();
            child.move(move);

            int score = alphaBetaMax(child, alpha, beta, depth - 1, depthAsc + 1, allowNullMovePruning);

            if (score < bestScore) {
                bestScore = score;
                bestMoveLocal = move;
            }

            if (useAlphaBeta && score <= alpha) {
                zugsortierer.storeKillerMove(move, depthAsc);
                zugsortierer.addHistory(move, depthAsc);
                tt.store(hash, bestScore, depth, TTEntry.UPPER, move);
                return bestScore;
            }

            if (score < beta) beta = score;
        }

        byte flag;
        if (bestScore <= origAlpha) flag = TTEntry.UPPER;
        else if (bestScore >= origBeta) flag = TTEntry.LOWER;
        else flag = TTEntry.EXACT;

        tt.store(hash, bestScore, depth, flag, bestMoveLocal);
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

        Board nullBoard = board.copy();
        nullBoard.setBlackToMove(!board.isBlackToMove());
        nullBoard.setBewegt(' ');
        nullBoard.initHash();

        int score = alphaBetaMin(nullBoard, alpha, beta, reducedDepth, depthAsc + 1, true);

        if (score >= beta) {
            // Verifikation: in taktischen / riskanten Stellungen nicht blind cutten
            if (shouldVerifyNullMove(depth, pieces, kingOnThrone, kingHasDirectEdgeSight)) {
                int verifyScore = alphaBetaMax(board, beta - 1, beta, depth - 1, depthAsc, false);
                if (verifyScore >= beta) {
                    tt.store(hash, verifyScore, depth, TTEntry.LOWER, null);
                    return verifyScore;
                }
                return null;
            }

            tt.store(hash, score, depth, TTEntry.LOWER, null);
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

        Board nullBoard = board.copy();
        nullBoard.setBlackToMove(!board.isBlackToMove());
        nullBoard.setBewegt(' ');
        nullBoard.initHash();

        int score = alphaBetaMax(nullBoard, alpha, beta, reducedDepth, depthAsc + 1, true);

        if (score <= alpha) {
            // Verifikation: in taktischen / riskanten Stellungen nicht blind cutten
            if (shouldVerifyNullMove(depth, pieces, kingOnThrone, kingHasDirectEdgeSight)) {
                int verifyScore = alphaBetaMin(board, alpha, alpha + 1, depth - 1, depthAsc, false);
                if (verifyScore <= alpha) {
                    tt.store(hash, verifyScore, depth, TTEntry.UPPER, null);
                    return verifyScore;
                }
                return null;
            }

            tt.store(hash, score, depth, TTEntry.UPPER, null);
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
        // Grundsätzlich eher konservativ; NMP soll grobe taktische Fehler vermeiden
        int minDepth = (pieces > 16) ? 5 : 6;

        // Wenn der König den Thron verlassen hat, ist die Stellung wesentlich taktischer
        if (!kingOnThrone) {
            minDepth += 2;
        }

        // Wenn der König eine direkte Linie zum Rand hat, noch vorsichtiger
        if (kingHasDirectEdgeSight) {
            minDepth += 2;
        }

        return minDepth;
    }

    private int nullMoveReduction(int pieces, boolean kingOnThrone, boolean kingHasDirectEdgeSight) {
        // Konservativer als zuvor: lieber etwas weniger reduzieren, dafür robuster
        int reduction = (pieces > 16) ? 3 : 2;

        // König vom Thron => weniger Reduktion, also strengere NMP-Nutzung
        if (!kingOnThrone) {
            reduction -= 1;
        }

        // Direkter Randblick des Königs => ebenfalls weniger aggressiv
        if (kingHasDirectEdgeSight) {
            reduction -= 1;
        }

        return Math.max(1, reduction);
    }

    private boolean shouldVerifyNullMove(int depth, int pieces, boolean kingOnThrone, boolean kingHasDirectEdgeSight) {
        // Validation lohnt sich vor allem in riskanten / taktischen Stellungen
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
            return 5000;
        } else if (moveCounter <= 50) {
            return 8000;
        }
        return 4000;
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
    }
}
package app.KI;

import app.board.Board;
import app.board.Zug;
import app.board.Zuggenerator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AlphaBetaKI {

    public Bewertungsfunktion bf = new BewertungsfunktionImpl();
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

    //Benchmark:
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

            //Zugsortierung
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

        nodesSearched++;

        //TT lookup
        long hash = board.getZobristHash();
        TTEntry entry = tt.lookup(hash);

        if (entry != null && entry.depth >= depth) {
            if (entry.flag == TTEntry.EXACT) return entry.score;
            if (entry.flag == TTEntry.LOWER) alpha = Math.max(alpha, entry.score);
            if (entry.flag == TTEntry.UPPER) beta = Math.min(beta, entry.score);
            //möglicher Cutoff
            if (alpha >= beta) return entry.score;
        }

        if (depth == 0 || board.isGameOver()) {
            int score = bf.evaluate(board);
            tt.store(hash, score, depth, TTEntry.EXACT, null);
            return score;
        }

        if (!benchmarkMode && timeUp()) return bf.evaluate(board);

        List<Zug> allMoves = zuggenerator.getAllLegalMoves(board.getBoard(), false);
        allMoves = zugsortierer.getSortedList(allMoves, board, depthAsc);

        int origAlpha = alpha;
        int bestScore = Integer.MIN_VALUE;
        Zug bestMoveLocal = null;

        for (Zug move : allMoves) {

            Board child = board.copy();
            child.move(move);

            int score = alphaBetaMin(child, alpha, beta, depth - 1, depthAsc+1);

            if (score > bestScore) {
                bestScore = score;
                bestMoveLocal = move;
            }

            if (useAlphaBeta && score >= beta){
                zugsortierer.storeKillerMove(move, depthAsc);
                zugsortierer.addHistory(move, depthAsc);
                tt.store(hash, bestScore, depth, TTEntry.LOWER, move);
                return bestScore;
            }

            if (score > alpha) alpha = score;
        }

        byte flag;
        if(bestScore <= origAlpha) flag = TTEntry.UPPER;
        else if (bestScore >= beta) flag = TTEntry.LOWER;
        else flag = TTEntry.EXACT;

        tt.store(hash, bestScore, depth, flag, bestMoveLocal);

        return bestScore;
    }


    public int alphaBetaMin(Board board, int alpha, int beta, int depth, int depthAsc) {

        nodesSearched++;

        //TT Lookup
        long hash = board.getZobristHash();
        TTEntry entry = tt.lookup(hash);

        if (entry != null && entry.depth >= depth) {
            if (entry.flag == TTEntry.EXACT) return entry.score;
            if (entry.flag == TTEntry.LOWER) alpha = Math.max(alpha, entry.score);
            if (entry.flag == TTEntry.UPPER) beta  = Math.min(beta,  entry.score);
            if (alpha >= beta) return entry.score;
        }

        if (depth == 0 || board.isGameOver()) {
            int score = bf.evaluate(board);
            tt.store(hash, score, depth, TTEntry.EXACT, null);
            return score;
        }

        if (!benchmarkMode && timeUp()) return bf.evaluate(board);

        List<Zug> allMoves = zuggenerator.getAllLegalMoves(board.getBoard(), true);
        allMoves = zugsortierer.getSortedList(allMoves, board, depthAsc);

        int origBeta = beta;
        int bestScore = Integer.MAX_VALUE;
        Zug bestMoveLocal = null;

        for (Zug move : allMoves) {

            Board child = board.copy();
            child.move(move);

            int score = alphaBetaMax(child, alpha, beta, depth - 1, depthAsc+1);

            if (score < bestScore) {
                bestScore = score;
                bestMoveLocal = move;
            }

            if (useAlphaBeta && score <= alpha) {
                zugsortierer.storeKillerMove(move, depthAsc);
                zugsortierer.addHistory(move, depthAsc);
                tt.store(hash, bestScore, depth, TTEntry.UPPER,move);
                return bestScore;
            }

            if (score < beta) beta = score;
        }

        byte flag;
        if (bestScore >= origBeta) flag = TTEntry.LOWER;
        else if (bestScore <= alpha) flag = TTEntry.UPPER;
        else flag = TTEntry.EXACT;

        tt.store(hash, bestScore, depth, flag,bestMoveLocal);


        return bestScore;
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
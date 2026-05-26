package app.KI;

import app.board.Board;
import app.board.Zug;
import app.board.Zuggenerator;

import java.util.List;

public class AlphaBetaKI {

    public Bewertungsfunktion bf = new BewertungsfunktionImpl();

    Zuggenerator zuggenerator = new Zuggenerator();

    /**
     * Counts the moves over the period of a game, to adjust the time limit
     * 0-10 Züge = opening, max 5s pro Zug
     * 11-50 Züge = midgame, max 8s pro Zug
     * 51+ Züge = endgame, max 4s pro Zug
     */
    private int moveCounter = 0;


    private long startTime;
    private long timeLimit;

    public int maxDepth = 8;

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

            if (timeUp()) break;

            Zug currentBestMove = null;

            int currentBestScore = isWhiteToMove ? Integer.MIN_VALUE : Integer.MAX_VALUE;

            for (Zug move : allMoves) {

                if (timeUp()) break;

                Board child = board.copy();
                child.move(move);

                int score;

                if (isWhiteToMove) {

                    score = alphaBetaMin(child, Integer.MIN_VALUE, Integer.MAX_VALUE, depth - 1);

                    if (score > currentBestScore) {
                        currentBestScore = score;
                        currentBestMove = move;
                    }

                } else {

                    score = alphaBetaMax(child, Integer.MIN_VALUE, Integer.MAX_VALUE, depth - 1);

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


    public int alphaBetaMax(Board board, int alpha, int beta, int depth) {

        nodesSearched++;

        if (depth == 0 || board.isGameOver()) {
            return bf.evaluate(board);
        }

        if (!benchmarkMode && timeUp()) return bf.evaluate(board);

        List<Zug> allMoves = zuggenerator.getAllLegalMoves(board.getBoard(), false);

        for (Zug move : allMoves) {

            Board child = board.copy();
            child.move(move);

            int score = alphaBetaMin(child, alpha, beta, depth - 1);

            if (useAlphaBeta && score >= beta) return beta;

            if (score > alpha) alpha = score;
        }

        return alpha;
    }


    public int alphaBetaMin(Board board, int alpha, int beta, int depth) {

        nodesSearched++;

        if (depth == 0 || board.isGameOver()) {
            return bf.evaluate(board);
        }

        if (!benchmarkMode && timeUp()) return bf.evaluate(board);

        List<Zug> allMoves = zuggenerator.getAllLegalMoves(board.getBoard(), true);

        for (Zug move : allMoves) {

            Board child = board.copy();
            child.move(move);

            int score = alphaBetaMax(child, alpha, beta, depth - 1);

            if (useAlphaBeta && score <= alpha) return alpha;

            if (score < beta) beta = score;
        }

        return beta;
    }


    private boolean timeUp() {
        return System.nanoTime() - startTime > timeLimit;
    }


    private long getTimeForMove() {
        if (moveCounter <= 10) {
            return 8000;
        } else if (moveCounter <= 50) {
            return 8000;
        }
        return 8000;
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
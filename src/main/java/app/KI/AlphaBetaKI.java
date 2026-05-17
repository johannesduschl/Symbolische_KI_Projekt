package app.KI;

import app.board.Board;
import app.board.Zug;
import app.board.Zuggenerator;
import app.Bewertungsfunktion;
import lombok.Setter;

import java.util.List;

public class AlphaBetaKI {

    private final Zuggenerator zuggenerator = new Zuggenerator();
    private final TimeManager timeManager = new TimeManager();

    private long nodesSearched = 0;
    private int lastDepthReached = 0;
    private Zug lastBestMove = null;

    private boolean stopped = false;

    @Setter
    private boolean useCutoffs = true;

    public long getNodesSearched() {
        return nodesSearched;
    }

    public int getLastDepthReached() {
        return lastDepthReached;
    }

    public Zug getBestMove() {
        return lastBestMove;
    }

    public void resetStats() {
        nodesSearched = 0;
        lastDepthReached = 0;
        lastBestMove = null;
        stopped = false;
    }

    // =========================
    // MAX
    // =========================
    public int alphaBetaMax(Board board, int alpha, int beta, int depth) {

        if (timeManager.isTimeUp()) {
            stopped = true;
            return Bewertungsfunktion.evaluate(board.getBoard());
        }

        nodesSearched++;

        if (depth == 0 || board.isGameOver()) {
            return Bewertungsfunktion.evaluate(board.getBoard());
        }

        List<Zug> moves = zuggenerator.getAllLegalMoves(board.getBoard(), false);

        for (Zug move : moves) {

            Board child = board.copy();
            child.move(move);

            int score = alphaBetaMin(child, alpha, beta, depth - 1);

            if (stopped) return alpha;

            // =========================
            // CUTOFF LOGIC (optional)
            // =========================
            if (useCutoffs && score >= beta) {
                return beta;
            }

            if (score > alpha) {
                alpha = score;
            }
        }

        return alpha;
    }

    // =========================
    // MIN
    // =========================
    public int alphaBetaMin(Board board, int alpha, int beta, int depth) {

        if (timeManager.isTimeUp()) {
            stopped = true;
            return Bewertungsfunktion.evaluate(board.getBoard());
        }

        nodesSearched++;

        if (depth == 0 || board.isGameOver()) {
            return Bewertungsfunktion.evaluate(board.getBoard());
        }

        List<Zug> moves = zuggenerator.getAllLegalMoves(board.getBoard(), true);

        for (Zug move : moves) {

            Board child = board.copy();
            child.move(move);

            int score = alphaBetaMax(child, alpha, beta, depth - 1);

            if (stopped) return alpha;

            // =========================
            // CUTOFF LOGIC (optional)
            // =========================
            if (useCutoffs && score <= alpha) {
                return alpha;
            }

            if (score < beta) {
                beta = score;
            }
        }

        return beta;
    }

    // =========================
    // ITERATIVE DEEPENING
    // =========================
    public Zug iterativeDeepening(Board root, boolean isWhiteToMove, long timeLimitMs, int maxDepth) {

        timeManager.start(timeLimitMs);
        resetStats();

        for (int depth = 1; depth <= maxDepth; depth++) {

            if (timeManager.isTimeUp()) break;

            stopped = false;

            Zug currentBest = searchRoot(root, depth, isWhiteToMove);

            if (!stopped && currentBest != null) {
                lastBestMove = currentBest;
                lastDepthReached = depth;
            }
        }

        return lastBestMove;
    }

    // =========================
    // ROOT
    // =========================
    private Zug searchRoot(Board board, int depth, boolean isWhiteToMove) {

        List<Zug> moves = zuggenerator.getAllLegalMoves(board.getBoard(), isWhiteToMove);

        Zug bestMove = null;
        int bestScore = Integer.MIN_VALUE;

        for (Zug move : moves) {

            Board child = board.copy();
            child.move(move);

            int score = alphaBetaMin(
                    child,
                    Integer.MIN_VALUE + 1,
                    Integer.MAX_VALUE,
                    depth - 1
            );

            if (stopped) break;

            if (score > bestScore) {
                bestScore = score;
                bestMove = move;
            }
        }

        return bestMove;
    }
}
package app.KI;

import app.board.Board;
import app.board.Zug;
import app.board.Zuggenerator;

import java.util.List;

public class AlphaBetaKI {

    Bewertungsfunktion bf;
    Zuggenerator zuggenerator = new Zuggenerator();
    TimeManager timeManager = new TimeManager();
    private long nodesSearched = 0;
    private int lastDepthReached = 0;
    private Zug lastBestMove = null;

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
    }

    public int alphaBetaMax(Board board, int alpha, int beta, int depth){

        nodesSearched++;

        if (timeManager.isTimeUp()) {
            return bf.evaluate(board, false);
        }

        if (depth == 0 || board.isGameOver()){
            return bf.evaluate(board, false);
        }

        List<Zug> allMoves = zuggenerator.getAllLegalMoves(board.getBoard(), false);

        for (Zug move : allMoves){

            Board child = board.copy();
            child.move(move);

            int score = alphaBetaMin(child, alpha, beta, depth-1);
            if (score >= beta) return beta;
            if (score > alpha) alpha = score;
        }

        return alpha;
    }

    public int alphaBetaMin(Board board, int alpha, int beta, int depth) {

        nodesSearched++;

        if (timeManager.isTimeUp()) {
            return bf.evaluate(board, true);
        }

        if (depth == 0 || board.isGameOver()) {
            return bf.evaluate(board, true);
        }

        List<Zug> allMoves = zuggenerator.getAllLegalMoves(board.getBoard(), true);

        for (Zug move : allMoves) {

            Board child = board.copy();
            child.move(move);

            int score = alphaBetaMax(child, alpha, beta, depth - 1);

            if (score <= alpha) return alpha;

            if (score < beta) beta = score;
        }

        return beta;
    }

    public Zug iterativeDeepening(Board root, boolean isWhiteToMove, long timeLimitMs, int maxDepth) {

        timeManager.start(timeLimitMs);
        Zug bestMove = null;

        for (int depth = 1; depth <= maxDepth; depth++) {

            if (timeManager.isTimeUp()) break;

            Zug currentBest = searchRoot(root, depth, isWhiteToMove);

            if (!timeManager.isTimeUp() && currentBest != null) {
                bestMove = currentBest;
                lastDepthReached = depth;
                lastBestMove = currentBest;
            }
        }

        return bestMove;
    }

    private Zug searchRoot(Board board, int depth, boolean isWhiteToMove) {

        List<Zug> moves = zuggenerator.getAllLegalMoves(board.getBoard(), isWhiteToMove);

        Zug bestMove = null;
        int bestScore = Integer.MIN_VALUE;

        for (Zug move : moves) {

            Board child = board.copy();
            child.move(move);

            int score = alphaBetaMin(child, Integer.MIN_VALUE + 1, Integer.MAX_VALUE, depth - 1);

            if (timeManager.isTimeUp()) break;

            if (score > bestScore) {
                bestScore = score;
                bestMove = move;
            }
        }

        return bestMove;
    }
}

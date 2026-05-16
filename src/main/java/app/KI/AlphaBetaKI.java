package app.KI;

import app.board.Board;
import app.board.Zug;
import app.board.Zuggenerator;

import java.util.List;

public class AlphaBetaKI {

    Bewertungsfunktion bf = new Bewertungsfunktion() {
        @Override
        public int evaluate(Board board, boolean isWhiteToMove) {
            return (int)(1000 * Math.random());
        }
    };

    Zuggenerator zuggenerator = new Zuggenerator();

    /**
     * 0-10 Züge = opening, max 5s pro Zug
     * 11-50 Züge = midgame, max 8s pro Zug
     * 51+ Züge = endgame, max 4s pro Zug
     */
    private int moveCounter = 0;

    private long startTime;
    private long timeLimit;

    public Zug findBestMove(Board board, boolean isWhiteToMove) {

        List<Zug> allMoves = zuggenerator.getAllLegalMoves(board.getBoard(), isWhiteToMove);
        if (allMoves.isEmpty()) return null;

        startTime = System.nanoTime();
        timeLimit = getTimeForMove() * 1_000_000;

        Zug bestMove = allMoves.get(0);

        int maxDepth = 5;

        for (int depth = 1; depth <= maxDepth; depth++) {

            if (timeUp()) break;

            Zug currentBestMove = null;
            int currentBestScore = Integer.MIN_VALUE;

            for (Zug move : allMoves) {

                if (timeUp()) break;

                Board child = board.copy();
                child.move(move);

                int score = alphaBetaMin(child, Integer.MIN_VALUE, Integer.MAX_VALUE, depth - 1);

                if (score > currentBestScore) {
                    currentBestScore = score;
                    currentBestMove = move;
                }
            }

            if (!timeUp() && currentBestMove != null) {
                bestMove = currentBestMove;
            } else {
                break;
            }
        }

        moveCounter++;
        return bestMove;
    }

    public int alphaBetaMax(Board board, int alpha, int beta, int depth) {

        if (depth == 0 || board.isGameOver()) {
            return bf.evaluate(board, false);
        }

        if (timeUp()) return bf.evaluate(board, false);

        List<Zug> allMoves = zuggenerator.getAllLegalMoves(board.getBoard(), false);

        for (Zug move : allMoves) {

            Board child = board.copy();
            child.move(move);

            int score = alphaBetaMin(child, alpha, beta, depth - 1);

            if (score >= beta) return beta;

            if (score > alpha) alpha = score;
        }

        return alpha;
    }

    public int alphaBetaMin(Board board, int alpha, int beta, int depth) {

        if (depth == 0 || board.isGameOver()) {
            return bf.evaluate(board, true);
        }

        if (timeUp()) return bf.evaluate(board, true);

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
}
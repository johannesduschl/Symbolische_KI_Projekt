package app.KI;

import app.board.Board;
import app.board.Zug;
import app.board.Zuggenerator;

import java.util.List;

public class AlphaBetaKI {

    Bewertungsfunktion bf = new Bewertungsfunktion() {
        @Override
        public int evaluate(Board board, boolean isWhiteToMove) {
            return (int)(1000*Math.random());
        }
    };
    Zuggenerator zuggenerator = new Zuggenerator();


    public Zug findBestMove(Board board, int depth, boolean isWhiteToMove) {

        List<Zug> allMoves = zuggenerator.getAllLegalMoves(board.getBoard(), isWhiteToMove);

        if (allMoves.isEmpty()) {
            return null;
        }

        Zug bestMove = null;

        int bestScore = isWhiteToMove
                ? Integer.MIN_VALUE
                : Integer.MAX_VALUE;

        for (Zug move : allMoves) {

            Board child = board.copy();
            child.move(move);

            int score;

            if (isWhiteToMove) {
                score = alphaBetaMin(child, Integer.MIN_VALUE, Integer.MAX_VALUE, depth - 1);

                if (score > bestScore) {
                    bestScore = score;
                    bestMove = move;
                }

            } else {

                score = alphaBetaMax(child, Integer.MIN_VALUE, Integer.MAX_VALUE, depth - 1);

                if (score < bestScore) {
                    bestScore = score;
                    bestMove = move;
                }
            }
        }

        return bestMove;
    }


    public int alphaBetaMax(Board board, int alpha, int beta, int depth) {

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
}

package app;

import app.KI.AlphaBetaKI;
import app.KI.BewertungsfunktionImpl;
import app.board.Board;
import app.board.Zug;
import app.board.Zuggenerator;

import java.util.List;

public class Spiel {
    public BewertungsfunktionImpl bf = new BewertungsfunktionImpl();
    public Board board = new Board();
    Zuggenerator zuggenerator = new Zuggenerator();
    AlphaBetaKI ki = new AlphaBetaKI();

    public void startGame() {

        boolean isWhiteToMove = !board.blackMovesNext();
        boolean isGameOver = false;

        while (!isGameOver) {

            List<Zug> possibleMoves = zuggenerator.getAllLegalMoves(this.board.getBoard(), isWhiteToMove);
            if (possibleMoves.isEmpty()) {
                System.out.println("No legal moves available!");
                break;
            }

            Zug chosenMove = ki.findBestMove(this.board, isWhiteToMove);
            System.out.println("Move for " + (isWhiteToMove ? "White" : "Black") + ": " + chosenMove);
            isGameOver = board.move(chosenMove);
            System.out.println("blackMovesNext? " + board.blackMovesNext());
            System.out.println("toX: " + board.getLastMove().getToX());
            System.out.println("toY: " + board.getLastMove().getToY());
            System.out.println("fromX: " + board.getLastMove().getFromX());
            System.out.println("fromY: " + board.getLastMove().getFromY());
            System.out.println(board.getLastMove().toString());

            if (isGameOver) {
                this.board.printBoard();
                String winner = isWhiteToMove ? "White" : "Black";
                System.out.printf("Game is over. %s has won.", winner);
                System.out.println("\nMoves played: " + ki.moveCounter);
                System.out.println("Last move: " + board.getLastMove().toString());
                System.out.println("Reached depth: " + ki.lastCompletedDepth +"\n");

                System.out.println("Schwarz hat gerade gezogen? " + !board.blackMovesNext());
                bf.evaluate(this.board);
                bf.debugEvaluation(this.board);
                break;
            }

            this.board.printBoard();
            System.out.println("Reached depth: " + ki.lastCompletedDepth +"\n");
            System.out.println("Schwarz hat gerade gezogen? " + !board.blackMovesNext());
            bf.evaluate(this.board);
            bf.debugEvaluation(this.board);

            isWhiteToMove = !board.blackMovesNext();
        }
    }
}

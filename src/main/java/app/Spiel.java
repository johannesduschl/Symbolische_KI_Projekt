package app;

import app.KI.AlphaBetaKI;
import app.board.Board;
import app.board.Zug;
import app.board.Zuggenerator;

import java.util.List;

public class Spiel {

    public Board board = new Board();
    Zuggenerator zuggenerator = new Zuggenerator();
    AlphaBetaKI ki = new AlphaBetaKI();

    public void startGame() {

        boolean isWhiteToMove = false;
        boolean isGameOver = false;

        while (!isGameOver) {

            List<Zug> possibleMoves = zuggenerator.getAllLegalMoves(this.board.getBoard(), isWhiteToMove);
            if (possibleMoves.isEmpty()) {
                System.out.println("No legal moves available!");
                break;
            }

            Zug chosenMove = ki.findBestMove(this.board, 5, isWhiteToMove);
            System.out.println("Move for dummy KI: " + chosenMove);
            isGameOver = board.move(chosenMove);

            if (isGameOver) {
                this.board.printBoard();
                String winner = isWhiteToMove ? "White" : "Black";
                System.out.printf("Game is over. %s has won.", winner);
                break;
            }

            this.board.printBoard();

            isWhiteToMove = !isWhiteToMove;
        }
    }
}

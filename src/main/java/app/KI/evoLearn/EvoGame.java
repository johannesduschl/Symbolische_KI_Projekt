package app.KI.evoLearn;

import app.KI.AlphaBetaKI;
import app.board.Board;
import app.board.Zug;
import app.board.Zuggenerator;

import java.util.List;

public class EvoGame {
    public final Board board = new Board();
    Zuggenerator zuggenerator = new Zuggenerator();

    public final EvoBF bfWeiß;
    public final EvoBF bfSchwarz;

    final AlphaBetaKI kiWeiß;
    final AlphaBetaKI kiSchwarz;

    public EvoGame(EvoBF bfSchwarz, EvoBF bfWeiß) {
        if (bfSchwarz == null || bfWeiß == null) {
            throw new IllegalArgumentException("Evaluation functions must not be null.");
        }

        this.bfWeiß = bfWeiß;
        this.bfSchwarz = bfSchwarz;

        this.kiWeiß = new AlphaBetaKI(this.bfWeiß);
        this.kiSchwarz = new AlphaBetaKI(this.bfSchwarz);
    }

    public void startGame() {
        boolean isWhiteToMove = false;
        boolean isGameOver = false;
        int x = 0;

        while (!isGameOver) {
            System.out.println("nächste Runde: "+x);
            x++;
            List<Zug> possibleMoves = zuggenerator.getAllLegalMoves(this.board.getBoard(), isWhiteToMove);
            if (possibleMoves.isEmpty()) {
                System.out.println("No legal moves available!");
                break;
            }
            Zug chosenMove;
            if(isWhiteToMove) {chosenMove = kiWeiß.findBestMove(this.board, isWhiteToMove);}
            else{ chosenMove = kiSchwarz.findBestMove(this.board, isWhiteToMove);}

            System.out.println("Move for " + (isWhiteToMove ? "White" : "Black") + ": " + chosenMove);
            isGameOver = board.move(chosenMove);
            /**
            System.out.println("blackMovesNext? " + board.blackMovesNext());
            System.out.println("toX: " + board.getLastMove().getToX());
            System.out.println("toY: " + board.getLastMove().getToY());
            System.out.println("fromX: " + board.getLastMove().getFromX());
            System.out.println("fromY: " + board.getLastMove().getFromY());
             */
            //System.out.println(board.getLastMove().toString());

            if (isGameOver) {
                this.board.printBoard();
                String winner = isWhiteToMove ? "White" : "Black";
                if(winner.equals("Black")) {
                    bfSchwarz.getEvoKi().winrate +=1;
                }
                else {
                    bfWeiß.getEvoKi().winrate +=1;
                }
                System.out.printf("Game is over. %s has won.", winner);
                System.out.println("\nMoves played: weiß:" + kiWeiß.moveCounter +", schwarz:" + kiSchwarz.moveCounter + "");
                System.out.println("Last move: " + board.getLastMove().toString());
                System.out.println("Reached depth: weiß:" + kiWeiß.lastCompletedDepth +" schwarz"+ kiSchwarz.lastCompletedDepth+"\n");

                System.out.println("Schwarz hat gerade gezogen? " + !board.blackMovesNext());
                if(board.blackMovesNext()) {
                    bfSchwarz.evaluate(this.board);
                    bfSchwarz.debugEvaluation(this.board);
                    break;
                }
                else {
                    bfWeiß.evaluate(this.board);
                    bfWeiß.debugEvaluation(this.board);
                }
            }

            this.board.printBoard();
            /**
            System.out.println("Reached depth: weiß:" + kiWeiß.lastCompletedDepth +" schwarz"+ kiSchwarz.lastCompletedDepth+"\n");
            System.out.println("Schwarz hat gerade gezogen? " + !board.blackMovesNext());
            if(board.blackMovesNext()) {
                bfSchwarz.evaluate(this.board);
                bfSchwarz.debugEvaluation(this.board);
            }
            else {
                bfWeiß.evaluate(this.board);
                bfWeiß.debugEvaluation(this.board);
            }
             */
            isWhiteToMove = !board.blackMovesNext();
        }
    }

}

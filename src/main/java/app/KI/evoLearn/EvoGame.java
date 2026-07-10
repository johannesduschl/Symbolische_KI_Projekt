package app.KI.evoLearn;

import app.KI.AlphaBetaKI;
import app.board.Board;
import app.board.Zug;
import app.board.Zuggenerator;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class EvoGame extends Thread{
    public Board board = new Board();
    Zuggenerator zuggenerator = new Zuggenerator();


    public final EvoKi Ki1;
    public final EvoKi Ki2;

    public AlphaBetaKI Ki;
    @Getter
    public int fitness = 0;

    public EvoGame(EvoKi KI1, EvoKi KI2) {
        if (KI1 == null || KI2 == null) {
            throw new IllegalArgumentException("Evaluation functions must not be null.");
        }
        this.Ki1 = KI1;
        this.Ki2 = KI2;


        this.Ki = new AlphaBetaKI(new EvoBF(createGenom(Ki1, Ki2)));


    }
    @Override
    public void run() {
        this.startGame();
    }

    public void startGame() {

            boolean isWhiteToMove = false;
            boolean isGameOver = false;
            int x = 0;
            int finalScore = 0;

            while (!isGameOver) {
                System.out.println("nächste Runde: " + x);
                x++;
                List<Zug> possibleMoves = zuggenerator.getAllLegalMoves(this.board.getBoard(), isWhiteToMove);
                if (possibleMoves.isEmpty()) {
                    System.out.println("No legal moves available!");
                    break;
                }
                Zug chosenMove;

                chosenMove = this.Ki.findBestMove(this.board, isWhiteToMove);

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

                    System.out.printf("Game is over. %s has won.", winner);
                    System.out.println("\nMoves played:" + this.Ki.moveCounter );
                    System.out.println("Last move: " + board.getLastMove().toString());
                    System.out.println("Reached depth: " + this.Ki.lastCompletedDepth);

                    System.out.println("Schwarz hat gerade gezogen? " + !board.blackMovesNext());
                    this.Ki.bf.evaluate(this.board);
                    this.Ki.bf.debugEvaluation(this.board);
                    finalScore += this.Ki.bf.getScore();

                    if (winner.equals("White")) {
                        Ki1.winrate.addAndGet(1);
                    } else {
                        Ki2.winrate.addAndGet(1);
                    }
                    Ki1.fitness.addAndGet(finalScore);
                    Ki2.fitness.addAndGet(-finalScore);
                    break;
                }

                this.board.printBoard();
                /**
                System.out.println("Reached depth: " + ki.lastCompletedDepth + "\n");
                System.out.println("Schwarz hat gerade gezogen? " + !board.blackMovesNext());
                    ki.bf.evaluate(this.board);
                    ki.bf.debugEvaluation(this.board);
                **/
                isWhiteToMove = !board.blackMovesNext();
        }
    }

    public static Genom createGenom(EvoKi White, EvoKi Black) {
        return new Genom(
                White.genom.getKING_PST_R0C1(),
                White.genom.getKING_PST_R0C2(),
                White.genom.getKING_PST_R0C3(),
                White.genom.getKING_PST_R0C4(),
                White.genom.getKING_PST_R1C1(),
                White.genom.getKING_PST_R1C2(),
                White.genom.getKING_PST_R1C3(),
                White.genom.getKING_PST_R1C4(),
                White.genom.getKING_PST_R2C2(),
                White.genom.getKING_PST_R2C3(),
                White.genom.getKING_PST_R2C4(),
                White.genom.getKING_PST_R3C3(),
                White.genom.getKING_PST_R3C4(),
                White.genom.getKING_PST_R4C4(),
                White.genom.getWHITE_PST_R0C1(),
                White.genom.getWHITE_PST_R0C2(),
                White.genom.getWHITE_PST_R0C3(),
                White.genom.getWHITE_PST_R0C4(),
                White.genom.getWHITE_PST_R1C1(),
                White.genom.getWHITE_PST_R1C2(),
                White.genom.getWHITE_PST_R1C3(),
                White.genom.getWHITE_PST_R1C4(),
                White.genom.getWHITE_PST_R2C2(),
                White.genom.getWHITE_PST_R2C3(),
                White.genom.getWHITE_PST_R2C4(),
                White.genom.getWHITE_PST_R3C3(),
                White.genom.getWHITE_PST_R3C4(),
                White.genom.getWHITE_PST_R4C4(),
                Black.genom.getBLACK_PST_R0C1(),
                Black.genom.getBLACK_PST_R0C2(),
                Black.genom.getBLACK_PST_R0C3(),
                Black.genom.getBLACK_PST_R0C4(),
                Black.genom.getBLACK_PST_R1C1(),
                Black.genom.getBLACK_PST_R1C2(),
                Black.genom.getBLACK_PST_R1C3(),
                Black.genom.getBLACK_PST_R1C4(),
                Black.genom.getBLACK_PST_R2C2(),
                Black.genom.getBLACK_PST_R2C3(),
                Black.genom.getBLACK_PST_R2C4(),
                Black.genom.getBLACK_PST_R3C3(),
                Black.genom.getBLACK_PST_R3C4(),
                Black.genom.getBLACK_PST_R4C4(),
                White.genom.getW_WHITE_GOAL(),
                Black.genom.getW_BLACK_GOAL(),
                White.genom.getW_KING_PROGRESS(),
                White.genom.getW_CORNER(),
                White.genom.getW_KING_MOBILITY(),
                White.genom.getW_WHITE_MATERIAL(),
                White.genom.getW_WHITE_PST(),
                White.genom.getW_WHITE_PST_THREAT(),
                White.genom.getW_KING_EDGE_ACCESS(),
                White.genom.getW_KING_EDGE_SECURE(),
                White.genom.getW_WINNING_THREAT(),
                White.genom.getW_EDGES_SECURE_SCORE(),
                White.genom.getW_EDGES_ACCESS_BLOCKED(),
                White.genom.getW_CHECKMATE_SCORE(),
                White.genom.getW_CHECKMATE_THREAT(),
                Black.genom.getW_BLACK_MATERIAL(),
                Black.genom.getW_BLACK_PST(),
                Black.genom.getW_BLACK_PST_THREAT()
        );
    }

}

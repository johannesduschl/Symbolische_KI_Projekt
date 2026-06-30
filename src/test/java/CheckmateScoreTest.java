import app.KI.BewertungsfunktionImpl;
import app.board.Board;
import app.board.Zug;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

public class CheckmateScoreTest {

    @Test
    void testCheckmateOnThrone() {
        char[][] board = throneMateBoard;

        Zug lastMove = new Zug('e',9, 'e', 6, 's', 0, 4, 3, 4);
        Zug differentMove = new Zug('e',9, 'e', 7, 's', 0, 4, 2, 4);

        Board boardObj = new Board();
        boardObj.setBoard(board);
        boardObj.setBlackMovesNext(false);
        boardObj.setLastMove(lastMove);
        BewertungsfunktionImpl eval = new BewertungsfunktionImpl();
        eval.evaluate(boardObj);

        int score = invokeCheckmateScore(boardObj);
        assertEquals(1000, score);
    }

    @Test
    void testCheckmateOnEdge() {
        char[][] board = matedOnEdge;

        Zug lastMove = new Zug('c',9, 'c', 1, 's', 0, 2, 8, 2);
        Zug wrongMove = new Zug('c',9, 'c', 2, 's', 0, 2, 7, 2);

        Board boardObj = new Board();
        boardObj.setBoard(board);
        boardObj.setBlackMovesNext(false);
        boardObj.setLastMove(lastMove);
        BewertungsfunktionImpl eval = new BewertungsfunktionImpl();
        eval.evaluate(boardObj);

        int score = invokeCheckmateScore(boardObj);
        assertEquals(1000, score);

        boardObj.setLastMove(wrongMove);
        eval.evaluate(boardObj);
        System.out.println(boardObj.getLastMove().getToX() + " " + boardObj.getLastMove().getToY());
        score = invokeCheckmateScore(boardObj);
        assertEquals(0, score);
    }



    // =========================
    // REFLECTION
    // =========================

    private int invokeCheckmateScore(Board board) {
        try {
            Method m = BewertungsfunktionImpl.class.getDeclaredMethod(
                    "checkmateScore",
                    Board.class
            );

            m.setAccessible(true);

            BewertungsfunktionImpl eval = new BewertungsfunktionImpl();

            return (int) m.invoke(eval, board);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    // =========================
    // BOARDS
    // =========================

    private final char[][] emptyBoard = new char[][]{
            { '-', '-', '-', '-', '-', '-', '-', '-', '-' },
            { '-', '-', '-', '-', '-', '-', '-', '-', '-' },
            { '-', '-', '-', '-', '-', '-', '-', '-', '-' },
            { '-', '-', '-', '-', '-', '-', '-', '-', '-' },
            { '-', '-', '-', '-', 'k', '-', '-', '-', '-' },
            { '-', '-', '-', '-', '-', '-', '-', '-', '-' },
            { '-', '-', '-', '-', '-', '-', '-', '-', '-' },
            { '-', '-', '-', '-', '-', '-', '-', '-', '-' },
            { '-', '-', '-', '-', '-', '-', '-', '-', '-' }
    };

    // 4 Seiten vollständig mit 's' blockiert → throne mate condition
    private final char[][] throneMateBoard = new char[][]{
            { '-', '-', '-', '-', '-', '-', '-', '-', '-' },
            { '-', '-', '-', '-', '-', '-', '-', '-', '-' },
            { '-', '-', '-', '-', '-', '-', '-', '-', '-' },
            { '-', '-', '-', '-', 's', '-', '-', '-', '-' },
            { '-', '-', '-', 's', 'k', 's', '-', '-', '-' },
            { '-', '-', '-', '-', 's', '-', '-', '-', '-' },
            { '-', '-', '-', '-', '-', '-', '-', '-', '-' },
            { '-', '-', '-', '-', '-', '-', '-', '-', '-' },
            { '-', '-', '-', '-', '-', '-', '-', '-', '-' }
    };

    // vertikale Klammerung aber kein Matt wegen Thron
    private final char[][] verticalNoMateBoard = new char[][]{
            { '-', '-', '-', '-', '-', '-', '-', '-', '-' },
            { '-', '-', '-', '-', '-', '-', '-', '-', '-' },
            { '-', '-', '-', '-', '-', '-', '-', '-', '-' },
            { '-', '-', '-', '-', 's', '-', '-', '-', '-' },
            { '-', '-', '-', '-', 'k', '-', '-', '-', '-' },
            { '-', '-', '-', '-', 's', '-', '-', '-', '-' },
            { '-', '-', '-', '-', '-', '-', '-', '-', '-' },
            { '-', '-', '-', '-', '-', '-', '-', '-', '-' },
            { '-', '-', '-', '-', '-', '-', '-', '-', '-' }
    };

    // vertikale Klammerung aber Matt
    private final char[][] verticalMateBoard = new char[][]{
            { '-', '-', '-', '-', '-', '-', '-', '-', '-' },
            { '-', '-', '-', '-', '-', '-', '-', '-', '-' },
            { '-', '-', '-', '-', 's', '-', '-', '-', '-' },
            { '-', '-', '-', '-', 'k', '-', '-', '-', '-' },
            { '-', '-', '-', '-', 's', '-', '-', '-', '-' },
            { '-', '-', '-', '-', '-', '-', '-', '-', '-' },
            { '-', '-', '-', '-', '-', '-', '-', '-', '-' },
            { '-', '-', '-', '-', '-', '-', '-', '-', '-' },
            { '-', '-', '-', '-', '-', '-', '-', '-', '-' }
    };

    // horizontale Klammerung aber kein Matt wegen Thron
    private final char[][] horizontalNoMateBoard = new char[][]{
            { '-', '-', '-', '-', '-', '-', '-', '-', '-' },
            { '-', '-', '-', '-', '-', '-', '-', '-', '-' },
            { '-', '-', '-', '-', '-', '-', '-', '-', '-' },
            { '-', '-', '-', '-', '-', '-', '-', '-', '-' },
            { '-', '-', '-', 's', 'k', 's', '-', '-', '-' },
            { '-', '-', '-', '-', '-', '-', '-', '-', '-' },
            { '-', '-', '-', '-', '-', '-', '-', '-', '-' },
            { '-', '-', '-', '-', '-', '-', '-', '-', '-' },
            { '-', '-', '-', '-', '-', '-', '-', '-', '-' }
    };

    // horizontale Klammerung aber Matt
    private final char[][] horizontalMateBoard = new char[][]{
            { '-', '-', '-', '-', '-', '-', '-', '-', '-' },
            { '-', '-', '-', '-', '-', '-', '-', '-', '-' },
            { '-', '-', '-', '-', '-', '-', '-', '-', '-' },
            { '-', '-', '-', 's', 'k', 's', '-', '-', '-' },
            { '-', '-', '-', '-', '-', '-', '-', '-', '-' },
            { '-', '-', '-', '-', '-', '-', '-', '-', '-' },
            { '-', '-', '-', '-', '-', '-', '-', '-', '-' },
            { '-', '-', '-', '-', '-', '-', '-', '-', '-' },
            { '-', '-', '-', '-', '-', '-', '-', '-', '-' }
    };

    // Gegner da, aber keine vollständige Einkesselung
    private final char[][] matedWithThrone = new char[][]{
            { '-', '-', '-', '-', '-', '-', '-', '-', '-' },
            { '-', '-', '-', '-', '-', '-', '-', '-', '-' },
            { '-', '-', '-', '-', 's', '-', '-', '-', '-' },
            { '-', '-', '-', '-', 'k', '-', '-', '-', '-' },
            { '-', '-', '-', '-', '-', '-', '-', '-', '-' },
            { '-', '-', '-', '-', '-', '-', '-', '-', '-' },
            { '-', '-', '-', '-', '-', '-', '-', '-', '-' },
            { '-', '-', '-', '-', '-', '-', '-', '-', '-' },
            { '-', '-', '-', '-', '-', '-', '-', '-', '-' }
    };

    private final char[][] matedHorizontallyWithTopLeftCorner = new char[][]{
            { '-', 'k', 's', '-', '-', '-', '-', '-', '-' },
            { '-', '-', '-', '-', '-', '-', '-', '-', '-' },
            { '-', '-', '-', '-', '-', '-', '-', '-', '-' },
            { '-', '-', '-', '-', '-', '-', '-', '-', '-' },
            { '-', '-', '-', '-', '-', '-', '-', '-', '-' },
            { '-', '-', '-', '-', '-', '-', '-', '-', '-' },
            { '-', '-', '-', '-', '-', '-', '-', '-', '-' },
            { '-', '-', '-', '-', '-', '-', '-', '-', '-' },
            { '-', '-', '-', '-', '-', '-', '-', '-', '-' }
    };

    private final char[][] matedVerticallyWithTopLeftCorner = new char[][]{
            { '-', '-', '-', '-', '-', '-', '-', '-', '-' },
            { 'k', '-', '-', '-', '-', '-', '-', '-', '-' },
            { 's', '-', '-', '-', '-', '-', '-', '-', '-' },
            { '-', '-', '-', '-', '-', '-', '-', '-', '-' },
            { '-', '-', '-', '-', '-', '-', '-', '-', '-' },
            { '-', '-', '-', '-', '-', '-', '-', '-', '-' },
            { '-', '-', '-', '-', '-', '-', '-', '-', '-' },
            { '-', '-', '-', '-', '-', '-', '-', '-', '-' },
            { '-', '-', '-', '-', '-', '-', '-', '-', '-' }
    };

    private final char[][] noMateOnEdge = new char[][]{
            { '-', '-', '-', '-', '-', '-', '-', '-', '-' },
            { '-', '-', '-', '-', '-', '-', '-', '-', '-' },
            { '-', '-', '-', '-', '-', '-', '-', '-', '-' },
            { '-', '-', '-', '-', '-', '-', '-', '-', '-' },
            { '-', '-', '-', '-', '-', '-', '-', '-', '-' },
            { '-', '-', '-', '-', '-', '-', '-', '-', '-' },
            { '-', '-', '-', '-', '-', '-', '-', '-', '-' },
            { '-', '-', '-', '-', 's', '-', '-', '-', '-' },
            { '-', '-', '-', 's', 'k', '-', '-', '-', '-' }
    };

    private final char[][] matedOnEdge = new char[][]{
        { 'x', '-', '-', '-', '-', '-', '-', '-', 'x' },
        { '-', '-', '-', '-', '-', '-', '-', '-', '-' },
        { '-', '-', '-', '-', '-', '-', '-', '-', '-' },
        { '-', '-', '-', '-', '-', '-', '-', '-', '-' },
        { '-', '-', '-', '-', 's', '-', '-', '-', '-' },
        { '-', '-', '-', '-', '-', '-', '-', '-', '-' },
        { '-', '-', '-', '-', '-', '-', '-', '-', '-' },
        { '-', '-', '-', '-', '-', '-', '-', '-', '-' },
        { 'x', 'k', 's', '-', '-', '-', '-', '-', 'x' }
    };
}
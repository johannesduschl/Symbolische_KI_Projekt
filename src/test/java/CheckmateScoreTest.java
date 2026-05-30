import app.KI.BewertungsfunktionImpl;
import app.board.Board;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

public class CheckmateScoreTest {

    @Test
    void testNoCheckmateEmptyBoard() {
        char[][] board = emptyBoard;

        BewertungsfunktionImpl.setOnThrone(true);
        BewertungsfunktionImpl eval = new BewertungsfunktionImpl();
        eval.evaluate(new Board(board));

        int score = invokeCheckmateScore(board);

        System.out.println("SCORE: " + score);
        assertEquals(0, score);
    }

    @Test
    void testCheckmateOnThrone() {
        char[][] board = throneMateBoard;

        BewertungsfunktionImpl.setOnThrone(true);
        BewertungsfunktionImpl eval = new BewertungsfunktionImpl();
        eval.evaluate(new Board(board));

        int score = invokeCheckmateScore(board);

        System.out.println("SCORE (throneMateBoard): " + score);
        assertEquals(100, score);
    }

    @Test
    void testNoCheckmateOnThroneVertical() {
        char[][] board = verticalNoMateBoard;

        BewertungsfunktionImpl.setOnThrone(true);
        BewertungsfunktionImpl eval = new BewertungsfunktionImpl();
        eval.evaluate(new Board(board));

        int score = invokeCheckmateScore(board);

        System.out.println("SCORE (verticalNoMateBoard): " + score);
        assertEquals(0, score);
    }

    @Test
    void testCheckmateOffThroneVertical() {
        char[][] board = verticalMateBoard;

        BewertungsfunktionImpl.setOnThrone(true);
        BewertungsfunktionImpl eval = new BewertungsfunktionImpl();
        eval.evaluate(new Board(board));

        int score = invokeCheckmateScore(board);

        System.out.println("SCORE (verticalMateBoard): " + score);
        assertEquals(100, score);
    }

    @Test
    void testNoCheckmateOnThroneHorizontal() {
        char[][] board = horizontalNoMateBoard;

        BewertungsfunktionImpl.setOnThrone(true);
        BewertungsfunktionImpl eval = new BewertungsfunktionImpl();
        eval.evaluate(new Board(board));

        int score = invokeCheckmateScore(board);

        System.out.println("SCORE (horizontalNoMateBoard): " + score);
        assertEquals(0, score);
    }

    @Test
    void testCheckmateOffThroneHorizontal() {
        char[][] board = horizontalMateBoard;

        BewertungsfunktionImpl.setOnThrone(true);
        BewertungsfunktionImpl eval = new BewertungsfunktionImpl();
        eval.evaluate(new Board(board));

        int score = invokeCheckmateScore(board);

        System.out.println("SCORE (horizontalMateBoard): " + score);
        assertEquals(100, score);
    }

    @Test
    void testMatedWithThrone() {
        char[][] board = matedWithThrone;

        BewertungsfunktionImpl.setOnThrone(true);
        BewertungsfunktionImpl eval = new BewertungsfunktionImpl();
        eval.evaluate(new Board(board));

        int score = invokeCheckmateScore(board);

        System.out.println("SCORE (matedWithThrone): " + score);
        assertEquals(100, score);
    }

    @Test
    void testMatedWithTopLeftCorner() {
        char[][] board_1 = matedHorizontallyWithTopLeftCorner;
        char[][] board_2 = matedVerticallyWithTopLeftCorner;

        BewertungsfunktionImpl eval = new BewertungsfunktionImpl();

        BewertungsfunktionImpl.setOnThrone(true);
        eval.evaluate(new Board(board_1));
        int score_1 = invokeCheckmateScore(board_1);

        BewertungsfunktionImpl.setOnThrone(true);
        eval.evaluate(new Board(board_2));
        int score_2 = invokeCheckmateScore(board_2);

        System.out.println("SCORE (matedHorizontallyWithTopLeftCorner): " + score_1);
        System.out.println("SCORE (matedVerticallyWithTopLeftCorner): " + score_2);
        assertEquals(100, score_1);
        assertEquals(100, score_2);
    }

    @Test
    void testNoMateOnEdge() {
        char[][] board = noMateOnEdge;

        BewertungsfunktionImpl.setOnThrone(true);
        BewertungsfunktionImpl eval = new BewertungsfunktionImpl();
        eval.evaluate(new Board(board));

        int score = invokeCheckmateScore(board);

        System.out.println("SCORE (noMateOnEdge): " + score);
        assertEquals(0, score);
    }


    // =========================
    // REFLECTION
    // =========================

    private int invokeCheckmateScore(char[][] board) {
        try {
            Method m = BewertungsfunktionImpl.class.getDeclaredMethod(
                    "checkmateScore",
                    char[][].class
            );

            m.setAccessible(true);

            BewertungsfunktionImpl eval = new BewertungsfunktionImpl();

            return (int) m.invoke(eval, new Object[]{board});

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
}
import app.KI.BewertungsfunktionImpl;
import app.board.Board;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

public class CornerAccessTest {

    // =========================
    // TESTS
    // =========================

    @Test
    void testKingNotOnEdge_returnsZero() {
        char[][] board = emptyBoard;

        BewertungsfunktionImpl.setOnThrone(true);
        BewertungsfunktionImpl eval = new BewertungsfunktionImpl();
        eval.evaluate(new Board(board));

        int score = invokeCornerAccess(board);

        System.out.println("SCORE (center): " + score);
        assertEquals(0, score);
    }

    @Test
    void testTopEdge_freePath() {
        char[][] board = topEdgeFreeBoard;

        BewertungsfunktionImpl.setOnThrone(true);
        BewertungsfunktionImpl eval = new BewertungsfunktionImpl();
        eval.evaluate(new Board(board));

        int score = invokeCornerAccess(board);

        System.out.println("SCORE (topEdgeFreeBoard): " + score);

        // beide Richtungen frei → 10 + 10
        assertEquals(20, score);
    }

    @Test
    void testTopEdge_partiallyBlocked() {
        char[][] board = topEdgeBlockedBoard;

        BewertungsfunktionImpl.setOnThrone(true);
        BewertungsfunktionImpl eval = new BewertungsfunktionImpl();
        eval.evaluate(new Board(board));

        int score = invokeCornerAccess(board);

        System.out.println("SCORE (topEdgeBlockedBoard): " + score);

        // eine Seite frei (10), andere bedroht/blocked (0 oder 5 je nach Setup)
        assertEquals(20, score);
    }

    @Test
    void testLeftEdge_freePath() {
        char[][] board = leftEdgeFreeBoard;

        BewertungsfunktionImpl.setOnThrone(true);
        BewertungsfunktionImpl eval = new BewertungsfunktionImpl();
        eval.evaluate(new Board(board));

        int score = invokeCornerAccess(board);

        System.out.println("SCORE (leftEdgeFreeBoard): " + score);

        assertEquals(20, score);
    }

    @Test
    void testBottomEdge_noAccess() {
        char[][] board = bottomEdgeBlockedBoard;

        BewertungsfunktionImpl.setOnThrone(true);
        BewertungsfunktionImpl eval = new BewertungsfunktionImpl();
        eval.evaluate(new Board(board));

        int score = invokeCornerAccess(board);

        System.out.println("SCORE (bottomEdgeBlockedBoard): " + score);

        assertEquals(0, score);
    }

    // =========================
    // REFLECTION
    // =========================

    private int invokeCornerAccess(char[][] board) {
        try {
            BewertungsfunktionImpl eval = new BewertungsfunktionImpl();

            Method m = BewertungsfunktionImpl.class.getDeclaredMethod(
                    "cornerAccess",
                    char[][].class
            );

            m.setAccessible(true);

            return (int) m.invoke(
                    eval,
                    (Object) board   // <<< DAS ist der entscheidende Fix
            );

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // =========================
    // BOARDS
    // =========================

    private final char[][] emptyBoard = new char[][]{
            { 'x', '-', '-', '-', '-', '-', '-', '-', 'x' },
            { '-', '-', '-', '-', '-', '-', '-', '-', '-' },
            { '-', '-', '-', '-', '-', '-', '-', '-', '-' },
            { '-', '-', '-', '-', '-', '-', '-', '-', '-' },
            { '-', '-', '-', '-', 'k', '-', '-', '-', '-' },
            { '-', '-', '-', '-', '-', '-', '-', '-', '-' },
            { '-', '-', '-', '-', '-', '-', '-', '-', '-' },
            { '-', '-', '-', '-', '-', '-', '-', '-', '-' },
            { 'x', '-', '-', '-', '-', '-', '-', '-', 'x' }
    };

    private final char[][] topEdgeFreeBoard = new char[][]{
            { 'x','-','-','k','-','-','w','-','x' },
            { '-','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','-','-','s','-','-' },
            { '-','-','-','-','-','-','-','-','-' },
            { 'x','-','-','-','-','-','-','-','x' }
    };

    private final char[][] topEdgeBlockedBoard = new char[][]{
            { 'x','-','-','k','-','-','-','-','x' },
            { '-','-','-','s','-','-','-','-','-' }, // blockiert eine Richtung
            { '-','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','-' },
            { 'x','-','-','-','-','-','-','-','x' }
    };

    private final char[][] leftEdgeFreeBoard = new char[][]{
            { 'x','-','-','-','-','s','-','-','x' },
            { 'k','-','-','-','-','s','-','-','-' },
            { '-','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','-' },
            { 'x','-','-','-','-','-','-','-','x' }
    };

    private final char[][] bottomEdgeBlockedBoard = new char[][]{
            { 'x','-','-','-','-','-','-','-','x' },
            { '-','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','s','-','-','-','-' },
            { 'x','s','-','k','-','-','-','-','x' }
    };
}
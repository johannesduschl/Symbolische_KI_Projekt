import app.KI.BewertungsfunktionImpl;
import app.board.Board;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

public class EdgeAccessBlockedByTest {

    @Test
    void testNoThreatReturnsZero() {
        char[][] board = emptyBoard;

        BewertungsfunktionImpl.setOnThrone(true);
        BewertungsfunktionImpl eval = new BewertungsfunktionImpl();
        eval.evaluate(new Board(board));

        int score = 0;

        score += invokeEdgeAccess(board, 's', 4, 4, -1, 0);
        score += invokeEdgeAccess(board, 's', 4, 4, 1, 0);
        score += invokeEdgeAccess(board, 's', 4, 4, 0, -1);
        score += invokeEdgeAccess(board, 's', 4, 4, 0, 1);

        System.out.println("SCORE (emptyBoard): " + score);
        assertEquals(0, score);
    }

    @Test
    void testNoThreatOnStartBoard() {
        char[][] board = startBoard;

        BewertungsfunktionImpl.setOnThrone(true);
        BewertungsfunktionImpl eval = new BewertungsfunktionImpl();
        eval.evaluate(new Board(board));

        int score = 0;
        score += invokeEdgeAccess(board, 's', 4, 4, -1, 0);
        score += invokeEdgeAccess(board, 's', 4, 4, 1, 0);
        score += invokeEdgeAccess(board, 's', 4, 4, 0, -1);
        score += invokeEdgeAccess(board, 's', 4, 4, 0, 1);


        System.out.println("SCORE (startBoard): " + score);
        assertEquals(0, score);
    }

    @Test
    void testTopBottomEdgeUnsecure() {
        char[][] board = topBottomEdgeUnsecureBoard;

        BewertungsfunktionImpl.setOnThrone(true);
        BewertungsfunktionImpl eval = new BewertungsfunktionImpl();
        eval.evaluate(new Board(board));

        int score_top = invokeEdgeAccess(board, 's', 4, 4, -1, 0);
        int score_bottom = invokeEdgeAccess(board, 's', 4, 4, 1, 0);
        int score_left = invokeEdgeAccess(board, 's', 4, 4, 0, -1);
        int score_right = invokeEdgeAccess(board, 's', 4, 4, 0, 1);

        System.out.println("SCORE TOP  (topBottomEdgeUnsecureBoard): " + score_top);
        System.out.println("SCORE BOTTOM  (topBottomEdgeUnsecureBoard): " + score_bottom);
        System.out.println("SCORE LEFT  (topBottomEdgeUnsecureBoard): " + score_left);
        System.out.println("SCORE RIGHT  (topBottomEdgeUnsecureBoard): " + score_right);

        assertEquals(2, score_top);
        assertEquals(2, score_bottom);
        assertEquals(0, score_left);
        assertEquals(0, score_right);
    }

    @Test
    void testLeftRightEdgeUnsecure() {
        char[][] board = leftRightEdgeUnsecureBoard;

        BewertungsfunktionImpl.setOnThrone(true);
        BewertungsfunktionImpl eval = new BewertungsfunktionImpl();
        eval.evaluate(new Board(board));

        int score_top = invokeEdgeAccess(board, 's', 4, 4, -1, 0);
        int score_bottom = invokeEdgeAccess(board, 's', 4, 4, 1, 0);
        int score_left = invokeEdgeAccess(board, 's', 4, 4, 0, -1);
        int score_right = invokeEdgeAccess(board, 's', 4, 4, 0, 1);

        System.out.println("SCORE TOP  (leftRightEdgeUnsecureBoard): " + score_top);
        System.out.println("SCORE BOTTOM  (leftRightEdgeUnsecureBoard): " + score_bottom);
        System.out.println("SCORE LEFT  (leftRightEdgeUnsecureBoard): " + score_left);
        System.out.println("SCORE RIGHT  (leftRightEdgeUnsecureBoard): " + score_right);

        assertEquals(0, score_top);
        assertEquals(0, score_bottom);
        assertEquals(2, score_left);
        assertEquals(2, score_right);
    }

    @Test
    void testEdgeCaseTop() {
        char[][] board = edgeCaseTopBoard;

        BewertungsfunktionImpl.setOnThrone(true);
        BewertungsfunktionImpl eval = new BewertungsfunktionImpl();
        eval.evaluate(new Board(board));

        int score_top = invokeEdgeAccess(board, 's', 0, 1, -1, 0);
        int score_bottom = invokeEdgeAccess(board, 's', 0, 1, 1, 0);
        int score_left = invokeEdgeAccess(board, 's', 0, 1, 0, -1);
        int score_right = invokeEdgeAccess(board, 's', 0, 1, 0, 1);

        System.out.println("SCORE TOP  (edgeCaseTopBoard): " + score_top);
        System.out.println("SCORE BOTTOM  (edgeCaseTopBoard): " + score_bottom);
        System.out.println("SCORE LEFT  (edgeCaseTopBoard): " + score_left);
        System.out.println("SCORE RIGHT  (edgeCaseTopBoard): " + score_right);

        assertEquals(0, score_top);
        assertEquals(2, score_bottom);
        assertEquals(0, score_left);
        assertEquals(2, score_right);
    }


    // =========================
    // REFLECTION
    // =========================

    private int invokeEdgeAccess(char[][] board, char piece, int x, int y, int dx, int dy) {
        try {
            Method m = BewertungsfunktionImpl.class.getDeclaredMethod(
                    "edgeAccessBlockedBy",
                    char[][].class,
                    char.class,
                    int.class,
                    int.class,
                    int.class,
                    int.class
            );

            m.setAccessible(true);

            BewertungsfunktionImpl eval = new BewertungsfunktionImpl();

            return (int) m.invoke(eval, board, piece, x, y, dx, dy);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    // =========================
    // BOARDS
    // =========================

    private final char[][] startBoard = new char[][]{
            { 'x','-','-','s','s','s','-','-','x' },
            { '-','-','-','-','s','-','-','-','-' },
            { '-','-','-','-','w','-','-','-','-' },
            { 's','-','-','-','w','-','-','-','s' },
            { 's','s','w','w','k','w','w','s','s' },
            { 's','-','-','-','w','-','-','-','s' },
            { '-','-','-','-','w','-','-','-','-' },
            { '-','-','-','-','s','-','-','-','-' },
            { 'x','-','-','s','s','s','-','-','x' }
    };

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

    private final char[][] topBottomEdgeUnsecureBoard = new char[][]{
            { 'x','-','-','-','-','-','-','-','x' },
            { '-','-','-','-','s','-','-','-','-' },
            { '-','-','-','-','w','-','-','-','-' },
            { 's','-','-','-','w','-','-','-','s' },
            { 's','s','w','w','k','w','w','s','s' },
            { 's','-','-','-','-','-','-','w','s' },
            { '-','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','s','-','-','-','-' },
            { 'x','-','s','-','-','-','-','-','x' }
    };

    private final char[][] leftRightEdgeUnsecureBoard = new char[][]{
            { 'x','-','-','s','s','s','-','-','x' },
            { '-','-','-','-','s','-','-','-','-' },
            { '-','-','-','-','w','-','-','-','w' },
            { '-','-','-','-','w','-','-','-','-' },
            { 's','-','w','w','k','-','-','s','-' },
            { '-','-','-','-','w','-','-','-','-' },
            { '-','-','-','-','w','-','-','-','-' },
            { '-','-','-','-','s','w','-','-','-' },
            { 'x','-','-','s','s','s','-','-','x' }
    };

    private final char[][] edgeCaseTopBoard = new char[][]{
            { 'x', 'k', '-', '-', '-', 's', '-', '-', 'x' },
            { '-', '-', '-', '-', '-', '-', '-', '-', '-' },
            { '-', '-', '-', '-', '-', '-', '-', '-', '-' },
            { '-', '-', '-', '-', '-', '-', '-', '-', '-' },
            { '-', 's', '-', '-', '-', '-', '-', '-', '-' },
            { '-', '-', '-', '-', '-', '-', '-', '-', '-' },
            { '-', '-', '-', '-', '-', '-', '-', '-', '-' },
            { '-', '-', '-', '-', '-', '-', '-', '-', '-' },
            { 'x', '-', '-', '-', '-', '-', '-', '-', 'x' }
    };

}
import app.KI.BewertungsfunktionImpl;
import app.board.Board;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

public class EdgeSecureScoreTest {

    @Test
    void testEdgeSecureScoreNoBlack() {
        char[][] board = emptyBoard;
        BewertungsfunktionImpl.setOnThrone(true);
        BewertungsfunktionImpl eval = new BewertungsfunktionImpl();
        eval.evaluate(new Board(board));

        int up = invokeEdgeSecureScore(board, 4, 4, -1, 0);
        int down = invokeEdgeSecureScore(board, 4, 4, 1, 0);
        int left = invokeEdgeSecureScore(board, 4, 4, 0, -1);
        int right = invokeEdgeSecureScore(board, 4, 4, 0, 1);

        System.out.println("UP: " + up);
        System.out.println("DOWN: " + down);
        System.out.println("LEFT: " + left);
        System.out.println("RIGHT: " + right);

        assertEquals(0, up);
        assertEquals(0, down);
        assertEquals(0, left);
        assertEquals(0, right);
    }

    @Test
    void testEdgeSecureScoreWithEnemyOnEdge() {
        char[][] board = enemyEdgeBoard;
        BewertungsfunktionImpl.setOnThrone(true);
        BewertungsfunktionImpl eval = new BewertungsfunktionImpl();
        eval.evaluate(new Board(board));

        int up = invokeEdgeSecureScore(board, 4, 4, -1, 0);
        int down = invokeEdgeSecureScore(board, 4, 4, 1, 0);
        int left = invokeEdgeSecureScore(board, 4, 4, 0, -1);
        int right = invokeEdgeSecureScore(board, 4, 4, 0, 1);

        System.out.println("UP: " + up);
        System.out.println("DOWN: " + down);
        System.out.println("LEFT: " + left);
        System.out.println("RIGHT: " + right);

        assertEquals(0, up);
        assertEquals(0, down);
        assertEquals(0, left);
        assertEquals(0, right);
    }

    @Test
    void testEdgeSecureScoreLeftUnsecureRightSecure() {
        char[][] board = leftRightBoard;
        BewertungsfunktionImpl.setOnThrone(true);
        BewertungsfunktionImpl eval = new BewertungsfunktionImpl();
        eval.evaluate(new Board(board));
        System.out.println("KingMoves should be 16 but has value: " + BewertungsfunktionImpl.getKingMoves());

        int left = invokeEdgeSecureScore(board, 4, 4, 0, -1);
        int right = invokeEdgeSecureScore(board, 4, 4, 0, 1);

        System.out.println("LEFT: " + left);
        System.out.println("RIGHT: " + right);

        assertEquals(0, left);
        assertEquals(16, right);
    }

    @Test
    void testEdgeSecureScoreComplex() {
        char[][] board = complexBoard;
        BewertungsfunktionImpl.setOnThrone(true);
        BewertungsfunktionImpl eval = new BewertungsfunktionImpl();
        eval.evaluate(new Board(board));
        System.out.println("KingMoves should be 8 but has value: " + BewertungsfunktionImpl.getKingMoves());


        int up = invokeEdgeSecureScore(board, 4, 4, -1, 0);
        int down = invokeEdgeSecureScore(board, 4, 4, 1, 0);
        int left = invokeEdgeSecureScore(board, 4, 4, 0, -1);
        int right = invokeEdgeSecureScore(board, 4, 4, 0, 1);

        System.out.println("LEFT: " + left);
        System.out.println("RIGHT: " + right);

        assertEquals(0, up);
        assertEquals(0, down);
        assertEquals(16, left);
        assertEquals(8, right);
    }

    // =========================
    // REFLECTION
    // =========================
    private int invokeEdgeSecureScore(char[][] board,
                                      int x, int y,
                                      int dx, int dy) {
        try {
            BewertungsfunktionImpl eval = new BewertungsfunktionImpl();

            Method m = BewertungsfunktionImpl.class.getDeclaredMethod(
                    "edgeSecureScore",
                    char[][].class,
                    int.class, int.class,
                    int.class, int.class
            );

            m.setAccessible(true);

            return (int) m.invoke(eval,
                    board,
                    x, y,
                    dx, dy
            );

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // =========================
    // BOARDS
    // =========================

    private final char[][] emptyBoard = new char[][]{
            { '-','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','k','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','-' }
    };

    private final char[][] enemyEdgeBoard = new char[][]{
            { 'x','-','-','-','s','-','-','-','x' },
            { '-','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','k','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','-' },
            { 'x','-','-','-','s','-','-','-','x' }
    };

    private final char[][] leftRightBoard = new char[][]{
            { 'x','-','-','-','-','-','-','-','x' },
            { '-','-','-','-','-','-','-','-','-' },
            { 's','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','k','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','w' },
            { '-','-','-','-','-','-','-','-','-' },
            { 'x','-','-','-','-','-','-','-','x' }
    };

    private final char[][] complexBoard = new char[][]{
            { 'x','-','-','-','-','-','-','-','x' },
            { '-','-','-','-','-','-','-','-','-' },
            { 's','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','s','-','-','-','-' },
            { '-','-','-','-','k','-','-','-','-' },
            { '-','-','-','-','s','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','w' },
            { '-','-','-','-','-','-','-','-','-' },
            { 'x','-','-','-','-','-','-','-','x' }
    };
}
import app.KI.BewertungsfunktionImpl;
import app.board.Board;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

public class CountMovesTest {

    @Test
    void testKingMovesInAllDirections() {
        char[][] board = debugBoard;
        BewertungsfunktionImpl.setOnThrone(true);
        BewertungsfunktionImpl eval = new BewertungsfunktionImpl();
        eval.evaluate(new Board(board));

        int up = invokeCountMoves(board, 4, 4, -1, 0);
        int down = invokeCountMoves(board, 4, 4, 1, 0);
        int left = invokeCountMoves(board, 4, 4, 0, -1);
        int right = invokeCountMoves(board, 4, 4, 0, 1);

        System.out.println("UP: " + up);
        System.out.println("DOWN: " + down);
        System.out.println("LEFT: " + left);
        System.out.println("RIGHT: " + right);

        assertEquals(1, up);
        assertEquals(2, down);
        assertEquals(1, left);
        assertEquals(3, right);
    }

    @Test
    void testBlockedByPiece() {
        char[][] board = startBoard;
        BewertungsfunktionImpl.setOnThrone(true);
        BewertungsfunktionImpl eval = new BewertungsfunktionImpl();
        eval.evaluate(new Board(board));

        int up = invokeCountMoves(board, 4, 4, -1, 0);
        int down = invokeCountMoves(board, 4, 4, 1, 0);
        int left = invokeCountMoves(board, 4, 4, 0, -1);
        int right = invokeCountMoves(board, 4, 4, 0, 1);

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
    void testOnThroneBlocksMovement() {
        char[][] board = debugBoard;
        BewertungsfunktionImpl.setOnThrone(true);
        BewertungsfunktionImpl eval = new BewertungsfunktionImpl();
        eval.evaluate(new Board(board));

        int right = invokeCountMoves(board, 4, 2, 0, 1);

        System.out.println("RIGHT: " + right);

        assertEquals(1, right);
    }

    @Test
    void testCornerBlocksMovement() {
        char[][] board = debugBoard;
        BewertungsfunktionImpl.setOnThrone(true);
        BewertungsfunktionImpl eval = new BewertungsfunktionImpl();
        eval.evaluate(new Board(board));

        int up = invokeCountMoves(board, 3, 0, -1, 0);

        System.out.println("UP: " + up);

        assertEquals(2, up);
    }

    @Test
    void testEdgeBlocksMovement() {
        char[][] board = debugBoard;
        BewertungsfunktionImpl.setOnThrone(true);
        BewertungsfunktionImpl eval = new BewertungsfunktionImpl();
        eval.evaluate(new Board(board));

        int up = invokeCountMoves(board, 4, 1, -1, 0);

        System.out.println("UP: " + up);

        assertEquals(4, up);
    }

    @Test
    void testCornersCountForKing(){
        char [][] board = oneKing;
        BewertungsfunktionImpl.setOnThrone(true);
        BewertungsfunktionImpl eval = new BewertungsfunktionImpl();
        eval.evaluate(new Board(board));

        int up = invokeCountMoves(board, 4, 0, -1, 0);
        int down = invokeCountMoves(board, 4, 0, 1, 0);
        int left = invokeCountMoves(board, 4, 0, 0, -1);
        int right = invokeCountMoves(board, 4, 0, 0, 1);

        assertEquals(4, up);
        assertEquals(4, down);
        assertEquals(0, left);
        assertEquals(7, right);

    }

    @Test
    void testMovesEdgeCase(){
        char [][] board = edgeCase;
        BewertungsfunktionImpl.setOnThrone(true);
        BewertungsfunktionImpl eval = new BewertungsfunktionImpl();
        eval.evaluate(new Board(board));

        int up = invokeCountMoves(board, 4, 0, -1, 0);
        int down = invokeCountMoves(board, 4, 0, 1, 0);
        int left = invokeCountMoves(board, 4, 0, 0, -1);
        int right = invokeCountMoves(board, 4, 0, 0, 1);

        assertEquals(3, up);
        assertEquals(3, down);
        assertEquals(0, left);
        assertEquals(3, right);
    }

    // =========================
    // REFLECTION HELPER
    // =========================
    private int invokeCountMoves(char[][] board, int x, int y, int dx, int dy) {
        try {
            BewertungsfunktionImpl eval = new BewertungsfunktionImpl();

            Method m = BewertungsfunktionImpl.class.getDeclaredMethod(
                    "countMoves",
                    char[][].class,
                    int.class, int.class,
                    int.class, int.class
            );

            m.setAccessible(true);

            return (int) m.invoke(eval, board, x, y, dx, dy);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // =========================
    // TEST BOARDS
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

    private final char[][] debugBoard = new char[][]{
            { 'x','-','-','s','s','s','-','-','x' },
            { '-','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','w','-','-','-','-' },
            { 's','-','-','-','-','-','-','-','s' },
            { 's','s','w','-','k','-','-','-','s' },
            { 's','-','-','-','-','-','-','-','s' },
            { '-','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','s','-','-','-','-' },
            { 'x','-','-','s','s','s','-','-','x' }
    };

    private final char[][] oneKing = new char[][]{
            { 'x','-','-','-','-','-','-','-','x' },
            { '-','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','-' },
            { 'k','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','-' },
            { 'x','-','-','-','-','-','-','-','x' }
    };

    private final char[][] edgeCase = new char[][]{
            { 'x','-','-','-','-','-','-','-','x' },
            { '-','-','-','-','-','-','-','-','-' },
            { '-','-','k','-','-','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','-' },
            { 's','-','-','-','-','s','-','-','-' },
            { '-','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','-' },
            { 'x','-','-','-','-','-','-','-','x' }
    };
}
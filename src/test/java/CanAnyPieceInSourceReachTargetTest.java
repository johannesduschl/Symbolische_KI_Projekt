import app.KI.BewertungsfunktionImpl;
import app.board.Board;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class CanAnyPieceInSourceReachTargetTest {

    @Test
    void testCanAnyWhitePieceReachTopEdge() {
        char[][] board = startBoard;
        BewertungsfunktionImpl.setOnThrone(true);
        BewertungsfunktionImpl eval = new BewertungsfunktionImpl();
        eval.evaluate(new Board(board));
        System.out.println("WHITE SQUARES: " + Arrays.toString(eval.getWhiteSquares()));

        boolean result_up = invokeCanAnyPieceInSourceReachTarget(board, 'w', -1, 0, 0, 8, 0, 8, 0, 0, 0, 8);
        boolean result_down = invokeCanAnyPieceInSourceReachTarget(board, 'w', 1, 0, 0, 8, 0, 8, 0, 0, 0, 8);
        boolean result_left = invokeCanAnyPieceInSourceReachTarget(board, 'w', 0, -1, 0, 8, 0, 8, 0, 0, 0, 8);
        boolean result_right = invokeCanAnyPieceInSourceReachTarget(board, 'w', 0, 1, 0, 8, 0, 8, 0, 0, 0, 8);

        System.out.println("UP: " + result_up);
        System.out.println("DOWN: " + result_down);
        System.out.println("LEFT: " + result_left);
        System.out.println("RIGHT: " + result_right);

        assertTrue(result_up);
        assertFalse(result_down);
        assertFalse(result_left);
        assertFalse(result_right);

        assertTrue(eval.getOnThrone());
    }

    @Test
    void testCanBlackPieceReachTopEdge() {
        char[][] board = oneBlack;
        BewertungsfunktionImpl eval = new BewertungsfunktionImpl();
        BewertungsfunktionImpl.setOnThrone(true);
        eval.evaluate(new Board(board));

        boolean result_up = invokeCanAnyPieceInSourceReachTarget(board, 's', -1, 0, 0, 8, 0, 8, 0, 0, 0, 8);
        boolean result_down = invokeCanAnyPieceInSourceReachTarget(board, 's', 1, 0, 0, 8, 0, 8, 0, 0, 0, 8);
        boolean result_left = invokeCanAnyPieceInSourceReachTarget(board, 's', 0, -1, 0, 8, 0, 8, 0, 0, 0, 8);
        boolean result_right = invokeCanAnyPieceInSourceReachTarget(board, 's', 0, 1, 0, 8, 0, 8, 0, 0, 0, 8);

        System.out.println("UP: " + result_up);
        System.out.println("DOWN: " + result_down);
        System.out.println("LEFT: " + result_left);
        System.out.println("RIGHT: " + result_right);

        assertFalse(result_up);
        assertFalse(result_down);
        assertFalse(result_left);
        assertFalse(result_right);

        assertFalse(eval.getOnThrone());

    }

    @Test
    void testCanKingReachTopEdge() {
        char[][] board = oneKing;
        BewertungsfunktionImpl eval = new BewertungsfunktionImpl();
        BewertungsfunktionImpl.setOnThrone(true);
        eval.evaluate(new Board(board));

        boolean result_up = invokeCanAnyPieceInSourceReachTarget(board, 'k', -1, 0, 0, 8, 0, 8, 0, 0, 0, 8);
        boolean result_down = invokeCanAnyPieceInSourceReachTarget(board, 'k', 1, 0, 0, 8, 0, 8, 0, 0, 0, 8);
        boolean result_left = invokeCanAnyPieceInSourceReachTarget(board, 'k', 0, -1, 0, 8, 0, 8, 0, 0, 0, 8);
        boolean result_right = invokeCanAnyPieceInSourceReachTarget(board, 'k', 0, 1, 0, 8, 0, 8, 0, 0, 0, 8);

        System.out.println("UP: " + result_up);
        System.out.println("DOWN: " + result_down);
        System.out.println("LEFT: " + result_left);
        System.out.println("RIGHT: " + result_right);

        assertTrue(result_up);
        assertFalse(result_down);
        assertFalse(result_left);
        assertFalse(result_right);

        assertFalse(eval.getOnThrone());

    }

    @Test
    void testCanBlackPieceReachRightEdge() {
        char[][] board = oneBlack;
        BewertungsfunktionImpl.setOnThrone(true);
        BewertungsfunktionImpl eval = new BewertungsfunktionImpl();
        eval.evaluate(new Board(board));

        boolean result_up = invokeCanAnyPieceInSourceReachTarget(board, 's', -1, 0, 0, 8, 0, 8, 0, 8, 8, 8);
        boolean result_down = invokeCanAnyPieceInSourceReachTarget(board, 's', 1, 0, 0, 8, 0, 8, 0, 8, 8, 8);
        boolean result_left = invokeCanAnyPieceInSourceReachTarget(board, 's', 0, -1, 0, 8, 0, 8, 0, 8, 8, 8);
        boolean result_right = invokeCanAnyPieceInSourceReachTarget(board, 's', 0, 1, 0, 8, 0, 8, 0, 8, 8, 8);

        System.out.println("UP: " + result_up);
        System.out.println("DOWN: " + result_down);
        System.out.println("LEFT: " + result_left);
        System.out.println("RIGHT: " + result_right);

        assertFalse(result_up);
        assertFalse(result_down);
        assertFalse(result_left);
        assertTrue(result_right);

        assertFalse(eval.getOnThrone());
    }

    @Test
    void testCanBlackPieceReachRightEdgeWithKingOnThrone() {
        char[][] board = oneBlackAndThrone;
        BewertungsfunktionImpl.setOnThrone(true);
        BewertungsfunktionImpl eval = new BewertungsfunktionImpl();
        eval.evaluate(new Board(board));
        System.out.println("Is King on Throne?: " + eval.getOnThrone());

        boolean result_up = invokeCanAnyPieceInSourceReachTarget(board, 's', -1, 0, 0, 8, 0, 8, 0, 8, 8, 8);
        boolean result_down = invokeCanAnyPieceInSourceReachTarget(board, 's', 1, 0, 0, 8, 0, 8, 0, 8, 8, 8);
        boolean result_left = invokeCanAnyPieceInSourceReachTarget(board, 's', 0, -1, 0, 8, 0, 8, 0, 8, 8, 8);
        boolean result_right = invokeCanAnyPieceInSourceReachTarget(board, 's', 0, 1, 0, 8, 0, 8, 0, 8, 8, 8);

        System.out.println("UP: " + result_up);
        System.out.println("DOWN: " + result_down);
        System.out.println("LEFT: " + result_left);
        System.out.println("RIGHT: " + result_right);

        assertFalse(result_up);
        assertFalse(result_down);
        assertFalse(result_left);
        assertFalse(result_right);

        assertTrue(eval.getOnThrone());
    }

    @Test
    void testCanKingReachRightEdge() {
        char[][] board = oneKing;
        BewertungsfunktionImpl.setOnThrone(true);
        BewertungsfunktionImpl eval = new BewertungsfunktionImpl();
        eval.evaluate(new Board(board));

        boolean result_up = invokeCanAnyPieceInSourceReachTarget(board, 'k', -1, 0, 0, 8, 0, 8, 0, 8, 8, 8);
        boolean result_down = invokeCanAnyPieceInSourceReachTarget(board, 'k', 1, 0, 0, 8, 0, 8, 0, 8, 8, 8);
        boolean result_left = invokeCanAnyPieceInSourceReachTarget(board, 'k', 0, -1, 0, 8, 0, 8, 0, 8, 8, 8);
        boolean result_right = invokeCanAnyPieceInSourceReachTarget(board, 'k', 0, 1, 0, 8, 0, 8, 0, 8, 8, 8);

        System.out.println("UP: " + result_up);
        System.out.println("DOWN: " + result_down);
        System.out.println("LEFT: " + result_left);
        System.out.println("RIGHT: " + result_right);

        assertFalse(result_up);
        assertFalse(result_down);
        assertFalse(result_left);
        assertTrue(result_right);

        assertFalse(eval.getOnThrone());
    }


    @Test
    void testCanAnyBlackPieceReachTopEdge() {
        char[][] board = startBoard;
        BewertungsfunktionImpl.setOnThrone(true);
        BewertungsfunktionImpl eval = new BewertungsfunktionImpl();
        eval.evaluate(new Board(board));
        System.out.println("BLACK SQUARES: " + Arrays.toString(eval.getBlackSquares()));

        boolean result_up = invokeCanAnyPieceInSourceReachTarget(board, 's', -1, 0, 0, 8, 0, 8, 0, 0, 0, 8);
        boolean result_down = invokeCanAnyPieceInSourceReachTarget(board, 's', 1, 0, 0, 8, 0, 8, 0, 0, 0, 8);
        boolean result_left = invokeCanAnyPieceInSourceReachTarget(board, 's', 0, -1, 0, 8, 0, 8, 0, 0, 0, 8);
        boolean result_right = invokeCanAnyPieceInSourceReachTarget(board, 's', 0, 1, 0, 8, 0, 8, 0, 0, 0, 8);

        System.out.println("UP: " + result_up);
        System.out.println("DOWN: " + result_down);
        System.out.println("LEFT: " + result_left);
        System.out.println("RIGHT: " + result_right);

        assertTrue(result_up);
        assertFalse(result_down);
        assertTrue(result_left);
        assertTrue(result_right);

        assertTrue(eval.getOnThrone());
    }

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

    private final char[][] oneBlack = new char[][]{
            { 'x','-','-','-','-','-','-','-','x' },
            { '-','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','-' },
            { 's','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','k','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','-' },
            { 'x','-','-','-','-','-','-','-','x' }
    };

    private final char[][] oneBlackAndThrone = new char[][]{
            { 'x','-','-','-','-','-','-','-','x' },
            { '-','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','-' },
            { 's','-','-','-','k','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','-' },
            { 'x','-','-','-','-','-','-','-','x' }
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


    private boolean invokeCanAnyPieceInSourceReachTarget(
            char[][] board,
            char pieceType,
            int dx, int dy,
            int sourceMinX, int sourceMaxX,
            int sourceMinY, int sourceMaxY,
            int targetMinX, int targetMaxX,
            int targetMinY, int targetMaxY
    ) {
        try {
            BewertungsfunktionImpl eval = new BewertungsfunktionImpl();

            Method m = BewertungsfunktionImpl.class.getDeclaredMethod(
                    "canAnyPieceInSourceReachTarget",
                    char[][].class,
                    char.class,
                    int.class, int.class,
                    int.class, int.class, int.class, int.class,
                    int.class, int.class, int.class, int.class
            );

            m.setAccessible(true);

            return (boolean) m.invoke(
                    eval,
                    board,
                    pieceType,
                    dx, dy,
                    sourceMinX, sourceMaxX,
                    sourceMinY, sourceMaxY,
                    targetMinX, targetMaxX,
                    targetMinY, targetMaxY
            );

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

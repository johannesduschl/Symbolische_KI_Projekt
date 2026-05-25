import app.KI.BewertungsfunktionImpl;
import app.board.Board;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class CanPieceReachTargetTest {

    @Test
    void testCanKingReachEdge() {
        BewertungsfunktionImpl eval = new BewertungsfunktionImpl();

        char[][] board = startBoard;

        boolean result_up = invokeCanPieceReachTarget(board, 'k', 4, 4, -1, 0, 0, 0, 4, 4);
        boolean result_down = invokeCanPieceReachTarget(board, 'k', 4, 4, 1, 0, 8, 8, 4, 4);
        boolean result_left = invokeCanPieceReachTarget(board, 'k', 4, 4, 0, -1, 4, 4, 0, 0);
        boolean result_right = invokeCanPieceReachTarget(board,'k', 4, 4, 0, 1, 4, 4, 8, 8);

        // Erwartung: false
        System.out.println("UP: " + result_up);
        System.out.println("DOWN: " + result_down);
        System.out.println("LEFT: " + result_left);
        System.out.println("RIGHT: " + result_right);

        assertFalse(result_up);
        assertFalse(result_down);
        assertFalse(result_left);
        assertFalse(result_right);

    }

    @Test
    void testCanKingReachAnyNextFile() {
        BewertungsfunktionImpl eval = new BewertungsfunktionImpl();

        char[][] board = debugBoard;

        boolean result_up = invokeCanPieceReachTarget(board, 'k', 4, 4, -1, 0, 0, 3, 4, 4);
        boolean result_down = invokeCanPieceReachTarget(board, 'k', 4, 4, 1, 0, 5, 8, 4, 4);
        boolean result_left = invokeCanPieceReachTarget(board, 'k', 4, 4, 0, -1, 4, 4, 0, 3);
        boolean result_right = invokeCanPieceReachTarget(board, 'k', 4, 4, 0, 1, 4, 4, 5, 8);

        // Erwartung: true
        System.out.println("UP: " + result_up);
        System.out.println("DOWN: " + result_down);
        System.out.println("LEFT: " + result_left);
        System.out.println("RIGHT: " + result_right);

        assertTrue(result_up);
        assertTrue(result_down);
        assertTrue(result_left);
        assertTrue(result_right);

    }

    @Test
    void testCanKingReachLeftEdge() {
        BewertungsfunktionImpl eval = new BewertungsfunktionImpl();

        char[][] board = left_edge;

        boolean result_up = invokeCanPieceReachTarget(board, 'k',4, 4, -1, 0, 0, 8, 0, 0);
        boolean result_down = invokeCanPieceReachTarget(board, 'k',4, 4, 1, 0, 0, 8, 0, 0);
        boolean result_left = invokeCanPieceReachTarget(board, 'k',4, 4, 0, -1, 0, 8, 0, 0);
        boolean result_right = invokeCanPieceReachTarget(board, 'k',4, 4, 0, 1, 0, 8, 0, 0);

        // Erwartung: true
        System.out.println("UP: " + result_up);
        System.out.println("DOWN: " + result_down);
        System.out.println("LEFT: " + result_left);
        System.out.println("RIGHT: " + result_right);

        assertFalse(result_up);
        assertFalse(result_down);
        assertTrue(result_left);
        assertFalse(result_right);

    }

    @Test
    void testCanAnyWhitePieceReachTopEdgeByLeft() {
        char[][] board = startBoard;
        BewertungsfunktionImpl eval = new BewertungsfunktionImpl();
        eval.evaluate(new Board(board));
        System.out.println("WHITE SQUARES: " + Arrays.toString(eval.getWhiteSquares()));
        int[] WhiteSquares = eval.getWhiteSquares();
        for (int i = 0; i < WhiteSquares.length; i += 2){
            int x = WhiteSquares[i];
            int y = WhiteSquares[i+1];
            boolean move_left = invokeCanPieceReachTarget(board, 'w', x, y, 0, -1, 0, 0, 0, 8);
            assertFalse(move_left);
        }
    }

    private boolean invokeCanPieceReachTarget(char[][] board, char PieceType,
                                              int x, int y, int dx, int dy,
                                              int targetMinX, int targetMaxX,
                                              int targetMinY, int targetMaxY) {
        try {
            BewertungsfunktionImpl eval = new BewertungsfunktionImpl();

            Method m = BewertungsfunktionImpl.class.getDeclaredMethod(
                    "canPieceReachTarget",
                    char[][].class, char.class,
                    int.class, int.class, int.class, int.class,
                    int.class, int.class, int.class, int.class
            );

            m.setAccessible(true);

            return (boolean) m.invoke(eval,
                    board, PieceType,
                    x, y, dx, dy,
                    targetMinX, targetMaxX,
                    targetMinY, targetMaxY
            );

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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
    private final char[][] left_edge = new char[][]{
            { 'x','-','-','s','s','s','-','-','x' },
            { '-','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','w','-','-','-','-' },
            { 's','-','-','-','-','-','-','-','s' },
            { '-','-','-','-','k','-','-','-','s' },
            { 's','-','-','-','-','-','-','-','s' },
            { '-','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','s','-','-','-','-' },
            { 'x','-','-','s','s','s','-','-','x' }
    };
}
import app.KI.BewertungsfunktionImpl;
import app.board.Board;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class InitBlackThreatPSTTest {

    @Test
    void testInitBlackThreatPST_startPosition() {
        char[][] board = startBoard;

        BewertungsfunktionImpl.setOnThrone(true);
        BewertungsfunktionImpl eval = new BewertungsfunktionImpl();

        eval.evaluate(new Board(board));
        resetBlackPST(eval);

        invokeInitBlackThreatPST(eval, board);

        int[][] pst = getInt2DArray(eval, "BLACK_PST_THREAT");

        System.out.println("\n================= BLACK_PST_THREAT =================");
        for (int i = 0; i < pst.length; i++) {
            System.out.println("row " + i + ": " + Arrays.toString(pst[i]));
        }
        System.out.println("====================================================\n");

        assertArrayEquals(emptyPST, pst);
    }

    @Test
    void testInitBlackThreatPST_complexPosition() {
        char[][] board = complexBoard;

        BewertungsfunktionImpl.setOnThrone(true);
        BewertungsfunktionImpl eval = new BewertungsfunktionImpl();

        eval.evaluate(new Board(board));
        resetBlackPST(eval);

        invokeInitBlackThreatPST(eval, board);

        int[][] pst = getInt2DArray(eval, "BLACK_PST_THREAT");

        System.out.println("\n================= BLACK_PST_THREAT =================");
        for (int i = 0; i < pst.length; i++) {
            System.out.println("row " + i + ": " + Arrays.toString(pst[i]));
        }
        System.out.println("====================================================\n");

        assertArrayEquals(complexPST, pst);
    }

    @Test
    void testInitBlackThreatPST_complexPosition_2() {
        char[][] board = complexBoard_2;

        BewertungsfunktionImpl.setOnThrone(true);
        BewertungsfunktionImpl eval = new BewertungsfunktionImpl();

        eval.evaluate(new Board(board));
        resetBlackPST(eval);

        invokeInitBlackThreatPST(eval, board);

        int[][] pst = getInt2DArray(eval, "BLACK_PST_THREAT");

        System.out.println("\n================= BLACK_PST_THREAT =================");
        for (int i = 0; i < pst.length; i++) {
            System.out.println("row " + i + ": " + Arrays.toString(pst[i]));
        }
        System.out.println("====================================================\n");

        assertArrayEquals(complexPST_2, pst);
    }

    // =========================
    // REFLECTION
    // =========================

    private void invokeInitBlackThreatPST(BewertungsfunktionImpl eval, char[][] board) {
        try {
            Method m = BewertungsfunktionImpl.class.getDeclaredMethod(
                    "initThreatPST",
                    char[][].class,
                    int[][].class,
                    int[].class,
                    char.class
            );
            m.setAccessible(true);

            int[][] pst = getInt2DArray(eval, "BLACK_PST_THREAT");
            int[] squares = getIntArray(eval, "whiteSquares");

            m.invoke(eval, board, pst, squares, 's');

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void resetBlackPST(BewertungsfunktionImpl eval) {
        try {
            Field f = BewertungsfunktionImpl.class.getDeclaredField("BLACK_PST_THREAT");
            f.setAccessible(true);

            int[][] fresh = new int[9][9];
            f.set(null, fresh);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private int[][] getInt2DArray(Object obj, String fieldName) {
        try {
            Field f = obj.getClass().getDeclaredField(fieldName);
            f.setAccessible(true);
            return (int[][]) f.get(null); // static
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private int[] getIntArray(Object obj, String fieldName) {
        try {
            Field f = obj.getClass().getDeclaredField(fieldName);
            f.setAccessible(true);
            return (int[]) f.get(obj); // instance field
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // =========================
    // BOARDS
    // =========================

    private final char[][] startBoard = {
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

    private final char[][] complexBoard = {
            { 'x','-','-','s','-','s','-','-','x' },
            { '-','-','s','w','-','-','-','-','-' },
            { '-','-','-','-','k','-','-','-','-' },
            { 's','-','-','-','-','-','-','-','s' },
            { 's','s','w','-','-','-','-','-','s' },
            { 's','-','-','-','-','-','-','-','s' },
            { '-','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','-' },
            { 'x','-','-','w','-','-','-','-','x' }
    };

    private final char[][] complexBoard_2 = {
            { 'x','-','-','s','-','s','-','-','x' },
            { '-','-','s','-','-','-','-','w','-' },
            { 'w','-','-','w','-','-','s','-','-' },
            { 's','-','w','-','s','-','-','-','-' },
            { 'w','s','-','-','k','-','-','-','s' },
            { '-','-','s','-','-','w','w','-','-' },
            { 's','-','-','-','s','-','s','-','-' },
            { '-','-','s','-','-','-','-','-','-' },
            { 'x','-','-','-','-','w','s','-','x' }
    };

    // =========================
    // EXPECTED PST
    // =========================

    int[][] complexPST_2 = {
            { 0,0,0,1,0,2,0,0,0 },
            { 0,0,3,0,0,0,0,0,0 },
            { 0,0,0,0,0,0,4,0,0 },
            { 5,0,0,0,2,0,0,0,0 },
            { 0,1,0,0,0,0,0,0,0 },
            { 0,0,4,0,0,0,0,0,0 },
            { 2,0,0,0,0,0,2,0,0 },
            { 0,0,0,0,0,0,0,0,0 },
            { 0,0,0,0,0,0,1,0,0 }
    };

    int[][] complexPST = {
            { 0,0,0,2,0,0,0,0,0 },
            { 0,0,2,0,0,0,0,0,0 },
            { 0,0,0,0,0,0,0,0,0 },
            { 0,0,0,0,0,0,0,0,0 },
            { 0,2,0,0,0,0,0,0,2 },
            { 0,0,0,0,0,0,0,0,0 },
            { 0,0,0,0,0,0,0,0,0 },
            { 0,0,0,0,0,0,0,0,0 },
            { 0,0,0,0,0,0,0,0,0 }
    };

    int[][] emptyPST = {
            { 0,0,0,2,0,2,0,0,0 },
            { 0,0,0,0,8,0,0,0,0 },
            { 0,0,0,0,0,0,0,0,0 },
            { 2,0,0,0,0,0,0,0,2 },
            { 0,8,0,0,0,0,0,8,0 },
            { 2,0,0,0,0,0,0,0,2 },
            { 0,0,0,0,0,0,0,0,0 },
            { 0,0,0,0,8,0,0,0,0 },
            { 0,0,0,2,0,2,0,0,0 }
    };
}
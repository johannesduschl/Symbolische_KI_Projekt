import app.KI.BewertungsfunktionImpl;
import app.board.Board;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class InitArraysTest {

    @Test
    void testInitArrays_blackAndWhiteIntersectionCorrect() {

        char[][] board = startBoard;

        BewertungsfunktionImpl.setOnThrone(true);
        BewertungsfunktionImpl eval = new BewertungsfunktionImpl();

        eval.evaluate(new Board(board));

        invokeInitArrays(eval);

        boolean[] blackOnRow = getBooleanArray(eval, "isBlackOnRow");
        boolean[] whiteOnRow = getBooleanArray(eval, "isWhiteOnRow");

        boolean[] blackOnCol = getBooleanArray(eval, "isBlackOnColumn");
        boolean[] whiteOnCol = getBooleanArray(eval, "isWhiteOnColumn");

        int[] bwRows = getIntArray(eval, "blackAndWhiteRows");
        int[] bwCols = getIntArray(eval, "blackAndWhiteColumns");

        // =========================
        // DEBUG OUTPUT
        // =========================
        System.out.println("\n================= DEBUG =================");

        System.out.println("BlackOnRow      : " + Arrays.toString(blackOnRow));
        System.out.println("WhiteOnRow      : " + Arrays.toString(whiteOnRow));
        System.out.println("BlackOnColumn   : " + Arrays.toString(blackOnCol));
        System.out.println("WhiteOnColumn   : " + Arrays.toString(whiteOnCol));

        System.out.println("BlackWhiteRows  : " + Arrays.toString(bwRows));
        System.out.println("BlackWhiteCols  : " + Arrays.toString(bwCols));

        System.out.println("========================================\n");

        // =========================
        // ROW CHECK
        // =========================
        for (int i = 0; i < blackOnRow.length; i++) {

            boolean expected = blackOnRow[i] && whiteOnRow[i];
            boolean actual = contains(bwRows, i);

            System.out.println("ROW " + i +
                    " expected=" + expected +
                    " actual=" + actual);

            assertEquals(expected, actual,
                    "Row mismatch at index " + i);
        }

        // =========================
        // COLUMN CHECK
        // =========================
        for (int i = 0; i < blackOnCol.length; i++) {

            boolean expected = blackOnCol[i] && whiteOnCol[i];
            boolean actual = contains(bwCols, i);

            System.out.println("COL " + i +
                    " expected=" + expected +
                    " actual=" + actual);

            assertEquals(expected, actual,
                    "Column mismatch at index " + i);
        }
    }

    // =========================
    // REFLECTION
    // =========================

    private void invokeInitArrays(BewertungsfunktionImpl eval) {
        try {
            Method m = BewertungsfunktionImpl.class
                    .getDeclaredMethod("initArrays");

            m.setAccessible(true);
            m.invoke(eval);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private boolean[] getBooleanArray(Object obj, String field) {
        try {
            Field f = obj.getClass().getDeclaredField(field);
            f.setAccessible(true);
            return (boolean[]) f.get(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private int[] getIntArray(Object obj, String field) {
        try {
            Field f = obj.getClass().getDeclaredField(field);
            f.setAccessible(true);
            return (int[]) f.get(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private boolean contains(int[] arr, int value) {
        for (int j : arr) {
            if (j == value) return true;
        }
        return false;
    }

    // =========================
    // BOARD
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
}
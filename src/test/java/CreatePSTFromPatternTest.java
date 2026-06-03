import app.KI.BewertungsfunktionImpl;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class CreatePSTFromPatternTest {

    @Test
    void testCreatePSTFromPatternThrone() {
        int[][] blackPST = invokeCreatePSTFromPattern(BLACK_PATTERN, 4, 4);
        int[][] whitePST = invokeCreatePSTFromPattern(WHITE_PATTERN, 4, 4);

        printPST(blackPST);
        printPST(whitePST);

        assertArrayEquals(BLACK_PST, blackPST);
        assertArrayEquals(WHITE_PST, whitePST);
    }

    @Test
    void testCreatePSTFromPatternNotThrone() {
        int[][] blackPST = invokeCreatePSTFromPattern(BLACK_PATTERN, 0, 0);
        int[][] whitePST = invokeCreatePSTFromPattern(WHITE_PATTERN, 0, 0);

        printPST(blackPST);
        printPST(whitePST);

        int[][] exp_white = {
                {  0, 4, 3, 2, 1, 1, 1, 1,  1 },
                {  4, 4, 3, 2, 1, 1, 1, 1,  1 },
                {  3, 3, 2, 1, 0, 0, 0, 0,  0 },
                {  2, 2, 1, 1, 0, 0, 0, 0,  0 },
                {  1, 1, 0, 0, 0 ,0, 0, 0,  0 },
                {  1, 1, 0, 0, 0 ,0, 0, 0,  0 },
                {  1, 1, 0, 0, 0 ,0, 0, 0,  0 },
                {  1, 1, 0, 0, 0 ,0, 0, 0,  0 },
                {  1, 1, 0, 0, 0 ,0, 0, 0,  0 }
        };

        int[][] exp_black = {
                {  0, 5, 2, 2, 2, 2, 2, 2,  2 },
                {  5, 4, 3, 2, 2, 2, 2, 2,  2 },
                {  2, 3, 0, 0, 0, 0, 0, 0,  0 },
                {  2, 2, 0, 0, 0, 0, 0, 0,  0 },
                {  2, 2, 0, 0, 0, 0, 0, 0,  0 },
                {  2, 2, 0, 0, 0, 0, 0, 0,  0 },
                {  2, 2, 0, 0, 0, 0, 0, 0,  0 },
                {  2, 2, 0, 0, 0, 0, 0, 0,  0 },
                {  2, 2, 0, 0, 0, 0, 0, 0,  0 }
        };

        assertArrayEquals(exp_black, blackPST);
        assertArrayEquals(exp_white, whitePST);
    }

    // =========================
    // REFLECTION
    // =========================

    private int[][] invokeCreatePSTFromPattern(int[][] pattern, int x, int y) {
        try {
            Method m = BewertungsfunktionImpl.class.getDeclaredMethod(
                    "createPSTFromPattern",
                    int[][].class,
                    int.class,
                    int.class
            );

            m.setAccessible(true);

            BewertungsfunktionImpl eval = new BewertungsfunktionImpl();

            return (int[][]) m.invoke(eval, (Object) pattern, x, y);

        } catch (Exception e) {
            throw new RuntimeException("Fehler beim Aufruf von createPSTFromPattern per Reflection", e);
        }
    }


    // =========================
    // HILFSFUNKTION ZUM AUSGEBEN
    // =========================

    public static void printPST(int[][] pst) {
        System.out.println("PST:");

        for (int y = 0; y < pst.length; y++) {
            for (int x = 0; x < pst[0].length; x++) {
                System.out.printf("%3d ", pst[y][x]);
            }
            System.out.println();
        }
    }


    // =========================
    // TESTDATEN
    // =========================

    private static final int[][] BLACK_PATTERN = {
            {4, 5, 4},
            {0, 3, 2, 3, 0},
            {0, 0, 2, 2, 2, 0, 0},
            {0, 0, 0, 2, 2, 2, 0, 0, 0},
            {0, 0, 0, 0, 2, 2, 2, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 2, 2, 2, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 2, 2, 2, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 2, 2, 2, 0, 0, 0, 0, 0, 0, 0}
    };

    private static final int[][] WHITE_PATTERN = {
            {4, 4, 4},
            {2, 3, 3, 3, 2},
            {1, 1, 2, 2, 2, 1, 1},
            {0, 0, 0, 1, 1, 1, 0, 0, 0},
            {0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0}
    };

    private static final int[][] BLACK_PST = {
            {  0, 0, 0, 2, 2, 2, 0, 0,  0 },
            {  0, 0, 0, 2, 2, 2, 0, 0,  0 },
            {  0, 0, 0, 3, 2, 3, 0, 0,  0 },
            {  2, 2, 3, 4, 5, 4, 3, 2,  2 },
            {  2, 2, 2, 5, 0 ,5, 2, 2,  2 },
            {  2, 2, 3, 4, 5, 4, 3, 2,  2 },
            {  0, 0, 0, 3, 2, 3, 0, 0,  0 },
            {  0, 0, 0, 2, 2, 2, 0, 0,  0 },
            {  0, 0, 0, 2, 2, 2, 0, 0,  0 }
    };

    private static final int[][] WHITE_PST = {
            { 0, 0, 0, 1, 1, 1, 0, 0, 0 },
            { 0, 1, 1, 2, 2, 2, 1, 1, 0 },
            { 0, 1, 2, 3, 3, 3, 2, 1, 0 },
            { 1, 2, 3, 4, 4, 4, 3, 2, 1 },
            { 1, 2, 3, 4, 0, 4, 3, 2, 1 },
            { 1, 2, 3, 4, 4, 4, 3, 2, 1 },
            { 0, 1, 2, 3, 3, 3, 2, 1, 0 },
            { 0, 1, 1, 2, 2, 2, 1, 1, 0 },
            { 0, 0, 0, 1, 1, 1, 0, 0, 0 }
    };
}
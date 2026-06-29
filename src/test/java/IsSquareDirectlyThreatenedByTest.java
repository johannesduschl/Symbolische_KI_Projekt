import app.KI.BewertungsfunktionImpl;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

public class IsSquareDirectlyThreatenedByTest {

    // =========================
    // TESTS
    // =========================

    @Test
    void testThreatened_simpleHorizontal() {
        char[][] board = emptyBoard();
        board[4][2] = 's'; // attacker
        board[4][4] = 'w'; // target

        boolean result = invoke(board, 's', 4, 4, 0, -1);

        assertTrue(result);
    }

    @Test
    void testNotThreatened_blockedByPiece() {
        char[][] board = emptyBoard();
        board[4][2] = 's';
        board[4][3] = 'w'; // blocker
        board[4][4] = 'w';

        boolean result = invoke(board, 's', 4, 4, 0, -1);

        assertFalse(result);
    }

    @Test
    void testThreatened_vertical() {
        char[][] board = emptyBoard();
        board[1][4] = 's';
        board[4][4] = 'w';

        boolean result = invoke(board, 's', 4, 4, -1, 0);

        assertTrue(result);
    }

    @Test
    void testNotThreatened_emptyLine() {
        char[][] board = emptyBoard();
        board[4][4] = 'w';

        boolean result = invoke(board, 's', 4, 4, 0, -1);

        assertFalse(result);
    }

    @Test
    void testThroneDoesNotBlock_whenOnThroneTrue() {
        char[][] board = emptyBoard();
        board[4][4] = 'w';
        board[4][2] = 's';
        board[4][3] = 'x'; // throne tile

        BewertungsfunktionImpl.setOnThrone(true);

        boolean result = invoke(board, 's', 4, 4, 0, -1);

        assertTrue(result);
    }

    @Test
    void testBlockedByNonSpecialPiece_notOnThrone() {
        char[][] board = emptyBoard();
        board[4][2] = 's';
        board[4][3] = 'k'; // blocker (not '-', 'x', attacker)
        board[4][4] = 'w';

        BewertungsfunktionImpl.setOnThrone(false);

        boolean result = invoke(board, 's', 4, 4, 0, -1);

        assertFalse(result);
    }

    // =========================
    // REFLECTION CALL
    // =========================

    private boolean invoke(char[][] board, char attacker, int x, int y, int dx, int dy) {
        try {
            BewertungsfunktionImpl eval = new BewertungsfunktionImpl();

            Method m = BewertungsfunktionImpl.class.getDeclaredMethod(
                    "isSquareDirectlyThreatenedBy",
                    char[][].class,
                    char.class,
                    int.class,
                    int.class,
                    int.class,
                    int.class
            );

            m.setAccessible(true);

            return (boolean) m.invoke(eval, board, attacker, x, y, dx, dy);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // =========================
    // HELPERS
    // =========================

    private char[][] emptyBoard() {
        char[][] board = new char[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                board[i][j] = '-';
            }
        }
        return board;
    }
}
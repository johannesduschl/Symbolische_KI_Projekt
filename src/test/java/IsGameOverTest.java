import app.board.Board;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class IsGameOverTest {

    @Test
    void testWhiteWinsWhenKingOnCorner() {
        Board board = new Board();

        char[][] b = emptyBoard();

        b[0][0] = 'k';

        board.setBoard(b);

        assertTrue(board.isGameOver());
    }

    @Test
    void testBlackWinsWhenKingCapturedNormal() {
        Board board = new Board();

        char[][] b = emptyBoard();

        b[4][4] = 'k';
        b[4][3] = 's';
        b[4][5] = 's';
        b[3][4] = 's';
        b[5][4] = 's';

        board.setBoard(b);

        assertTrue(board.isGameOver());
    }

    @Test
    void testKingOnThroneCapturedByFourSides() {
        Board board = new Board();

        char[][] b = emptyBoard();

        b[4][4] = 'k';

        b[3][4] = 's';
        b[5][4] = 's';
        b[4][3] = 's';
        b[4][5] = 's';

        board.setBoard(b);

        assertTrue(board.isGameOver());
    }

    @Test
    void testKingNextToThroneCapturedByThreeSidesAndThrone() {
        Board board = new Board();

        char[][] b = emptyBoard();

        b[3][4] = 'k';

        b[2][4] = 's';
        b[3][3] = 's';
        b[3][5] = 's';

        board.setBoard(b);

        assertTrue(board.isGameOver());
    }

    @Test
    void testGameNotOverWhenKingFree() {
        Board board = new Board();

        char[][] b = emptyBoard();

        b[4][4] = 'k';
        b[4][6] = 's';

        board.setBoard(b);

        assertFalse(board.isGameOver());
    }

    @Test
    void testGameOverWhenKingMissing() {
        Board board = new Board();

        char[][] b = emptyBoard();

        board.setBoard(b);

        assertTrue(board.isGameOver());
    }

    private char[][] emptyBoard() {
        char[][] b = new char[9][9];

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                b[i][j] = '-';
            }
        }

        b[0][0] = 'x';
        b[0][8] = 'x';
        b[8][0] = 'x';
        b[8][8] = 'x';

        return b;
    }
}
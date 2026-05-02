import app.Spiel;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SpielGameOverTest {

    @Test
    void testWhiteWinsWhenKingOnCorner() {
        Spiel spiel = new Spiel();

        char[][] b = emptyBoard();

        // König auf Ecke
        b[0][0] = 'k';

        spiel.board.board = b;

        assertTrue(spiel.isGameOver());
    }

    @Test
    void testBlackWinsWhenKingCapturedNormal() {
        Spiel spiel = new Spiel();

        char[][] b = emptyBoard();

        // König eingekesselt
        b[4][4] = 'k';
        b[4][3] = 's';
        b[4][5] = 's';
        b[3][4] = 's';
        b[5][4] = 's';

        spiel.board.board = b;

        assertTrue(spiel.isGameOver());
    }

    @Test
    void testKingOnThroneCapturedByFourSides() {
        Spiel spiel = new Spiel();

        char[][] b = emptyBoard();

        // König auf Thron
        b[4][4] = 'k';

        // 4 schwarze Seiten
        b[3][4] = 's';
        b[5][4] = 's';
        b[4][3] = 's';
        b[4][5] = 's';

        spiel.board.board = b;

        assertTrue(spiel.isGameOver());
    }

    @Test
    void testKingNextToThroneCapturedByThreeSides() {
        Spiel spiel = new Spiel();

        char[][] b = emptyBoard();

        // König neben Thron
        b[3][4] = 'k';

        // 3 Seiten blockiert
        b[2][4] = 's';
        b[3][3] = 's';
        b[3][5] = 's';

        // Thron blockiert indirekt durch Regel
        b[4][4] = 's';

        spiel.board.board = b;

        assertTrue(spiel.isGameOver());
    }

    @Test
    void testGameNotOverWhenKingFree() {
        Spiel spiel = new Spiel();

        char[][] b = emptyBoard();

        b[4][4] = 'k';
        b[4][6] = 's';

        spiel.board.board = b;

        assertFalse(spiel.isGameOver());
    }

    private char[][] emptyBoard() {
        char[][] b = new char[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                b[i][j] = '-';
            }
        }
        return b;
    }
}
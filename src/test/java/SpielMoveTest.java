import app.Spiel;
import app.board.Zug;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SpielMoveTest {

    @Test
    void testSimpleHorizontalCapture() {
        Spiel spiel = new Spiel();

        char[][] b = emptyBoard();
        b[4][3] = 's';
        b[4][4] = 'w';
        b[4][6] = 's';

        spiel.board.setBoard(b);

        spiel.board.move(new Zug('g',5,'f',5,'s'));

        assertEquals('-', b[4][4]);
    }

    @Test
    void testSimpleVerticalCapture() {
        Spiel spiel = new Spiel();

        char[][] b = emptyBoard();
        b[3][4] = 's';
        b[4][4] = 'w';
        b[6][4] = 's';

        spiel.board.setBoard(b);

        spiel.move(new Zug('e',3,'e',4,'s'));

        assertEquals('-', b[4][4]);
    }

    @Test
    void testCaptureAgainstCorner() {
        Spiel spiel = new Spiel();

        char[][] b = emptyBoard();
        b[0][0] = 'x';
        b[0][1] = 'w';
        b[0][3] = 's';

        spiel.board.setBoard(b);

        spiel.move(new Zug('d',9,'c',9,'s'));

        assertEquals('-', b[0][1]);
    }

    @Test
    void testCaptureAgainstThrone() {
        Spiel spiel = new Spiel();

        char[][] b = emptyBoard();
        b[4][4] = '-';
        b[4][3] = 'w';
        b[4][1] = 's';

        spiel.board.setBoard(b);

        spiel.move(new Zug('b',5,'c',5,'s'));

        assertEquals('-', b[4][3]);
    }

    @Test
    void testDoubleCapture() {
        Spiel spiel = new Spiel();

        char[][] b = emptyBoard();

        b[4][2] = 's';
        b[4][3] = 'w';
        b[4][4] = '-';
        b[4][5] = 'w';
        b[4][6] = 's';
        b[2][4] = 's';

        spiel.board.setBoard(b);

        spiel.move(new Zug('e',7,'e',5,'s'));

        assertEquals('-', b[4][3]);
        assertEquals('-', b[4][5]);
    }

    @Test
    void testNoCaptureIfNotEnclosed() {
        Spiel spiel = new Spiel();

        char[][] b = emptyBoard();
        b[4][4] = 'w';
        b[4][6] = 's';

        spiel.board.setBoard(b);

        spiel.move(new Zug('g',5,'f',5,'s'));

        assertEquals('w', b[4][4]);
    }

    @Test
    void testKingHelpsWhiteCaptureBlack() {
        Spiel spiel = new Spiel();

        char[][] b = emptyBoard();
        b[4][3] = 'k';
        b[4][4] = 's';
        b[4][7] = 'w';

        spiel.board.setBoard(b);

        spiel.move(new Zug('h',5,'f',5,'w'));

        assertEquals('-', b[4][4]);
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
import app.KI.BewertungsfunktionImpl;
import app.board.Board;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ThreatensCheckmateTest {

    private final Class<?> clazz = BewertungsfunktionImpl.class;

    // =========================
    // THRONE CASES
    // =========================

    @Test
    void throne_threeSidesBlocked_andLeftThreat() throws Exception {
        Object sut = clazz.getDeclaredConstructor().newInstance();

        char[][] board_left = emptyBoard();
        Board board = createBoard(board_left);

        setField(sut, "kingSquare", new int[]{4, 4});
        setField(sut, "onThrone", true);

        //check left square
        board_left[3][4] = 's';
        board_left[5][4] = 's';
        board_left[4][5] = 's';

        board_left[4][0] = 's';
        //not blocked
        board.setBoard(board_left);
        int result = invoke(sut, board);
        assertEquals(-1000, result);
        //blocked
        board_left[4][2] = 'w';
        board.setBoard(board_left);
        result = invoke(sut, board);
        assertEquals(0, result);
        //reset
        board_left[4][0] = '-';
        board_left[4][2] = '-';

        //not blocked
        board_left[0][3] = 's';
        board.setBoard(board_left);
        result = invoke(sut, board);
        assertEquals(-1000, result);
        //blocked
        board_left[2][3] = 'w';
        board.setBoard(board_left);
        result = invoke(sut, board);
        assertEquals(0, result);
        //reset
        board_left[0][3] = '-';
        board_left[2][3] = '-';

        //not blocked
        board_left[8][3] = 's';
        board.setBoard(board_left);
        result = invoke(sut, board);
        assertEquals(-1000, result);
        //blocked
        board_left[6][3] = 'w';
        board.setBoard(board_left);
        result = invoke(sut, board);
        assertEquals(0, result);
    }

    @Test
    void throne_threeSidesBlocked_andRightThreat() throws Exception {
        Object sut = clazz.getDeclaredConstructor().newInstance();

        char[][] board_right = emptyBoard();
        Board board = createBoard(board_right);

        setField(sut, "kingSquare", new int[]{4, 4});
        setField(sut, "onThrone", true);

        //check right square
        board_right[3][4] = 's';
        board_right[5][4] = 's';
        board_right[4][3] = 's';

        //not blocked
        board_right[4][8] = 's';
        board.setBoard(board_right);
        int result = invoke(sut, board);
        assertEquals(-1000, result);
        //blocked
        board_right[4][6] = 'w';
        board.setBoard(board_right);
        result = invoke(sut, board);
        assertEquals(0, result);
        //reset
        board_right[4][8] = '-';
        board_right[4][6] = '-';

        //not blocked
        board_right[0][5] = 's';
        board.setBoard(board_right);
        result = invoke(sut, board);
        assertEquals(-1000, result);
        //blocked
        board_right[2][5] = 'w';
        board.setBoard(board_right);
        result = invoke(sut, board);
        assertEquals(0, result);
        //reset
        board_right[0][5] = '-';
        board_right[2][5] = '-';

        //not blocked
        board_right[8][5] = 's';
        board.setBoard(board_right);
        result = invoke(sut, board);
        assertEquals(-1000, result);
        //blocked
        board_right[6][5] = 'w';
        board.setBoard(board_right);
        result = invoke(sut, board);
        assertEquals(0, result);
    }

    @Test
    void throne_threeSidesBlocked_andTopThreat() throws Exception {
        Object sut = clazz.getDeclaredConstructor().newInstance();

        char[][] board_top = emptyBoard();
        Board board = createBoard(board_top);

        setField(sut, "kingSquare", new int[]{4, 4});
        setField(sut, "onThrone", true);

        //check top square
        board_top[4][3] = 's';
        board_top[4][5] = 's';
        board_top[5][4] = 's';

        //not blocked
        board_top[0][4] = 's';
        board.setBoard(board_top);
        int result = invoke(sut, board);
        assertEquals(-1000, result);
        //blocked
        board_top[2][4] = 'w';
        board.setBoard(board_top);
        result = invoke(sut, board);
        assertEquals(0, result);
        //reset
        board_top[0][4] = '-';
        board_top[2][4] = '-';

        //blocked
        board_top[3][0] = 's';
        board.setBoard(board_top);
        result = invoke(sut, board);
        assertEquals(-1000, result);
        //blocked
        board_top[3][2] = 'w';
        board.setBoard(board_top);
        result = invoke(sut, board);
        assertEquals(0, result);
        //reset
        board_top[3][0] = '-';
        board_top[3][2] = '-';

        //not blocked
        board_top[3][8] = 's';
        board.setBoard(board_top);
        result = invoke(sut, board);
        assertEquals(-1000, result);
        //blocked
        board_top[3][6] = 'w';
        board.setBoard(board_top);
        result = invoke(sut, board);
        assertEquals(0, result);

    }

    @Test
    void throne_threeSidesBlocked_andBottomThreat() throws Exception {
        Object sut = clazz.getDeclaredConstructor().newInstance();

        char[][] board_bottom = emptyBoard();
        Board board = createBoard(board_bottom);

        setField(sut, "kingSquare", new int[]{4, 4});
        setField(sut, "onThrone", true);

        //check top square
        board_bottom[4][3] = 's';
        board_bottom[4][5] = 's';
        board_bottom[3][4] = 's';

        //not blocked
        board_bottom[8][4] = 's';
        board.setBoard(board_bottom);
        int result = invoke(sut, board);
        assertEquals(-1000, result);
        //blocked
        board_bottom[6][4] = 'w';
        board.setBoard(board_bottom);
        result = invoke(sut, board);
        assertEquals(0, result);

        //reset
        board_bottom[8][4] = '-';
        board_bottom[6][4] = '-';

        //not blocked
        board_bottom[5][0] = 's';
        board.setBoard(board_bottom);
        result = invoke(sut, board);
        assertEquals(-1000, result);
        //blocked
        board_bottom[5][2] = 'w';
        board.setBoard(board_bottom);
        result = invoke(sut, board);
        assertEquals(0, result);
        //reset
        board_bottom[5][0] = '-';
        board_bottom[5][2] = '-';

        //not blocked
        board_bottom[5][8] = 's';
        board.setBoard(board_bottom);
        result = invoke(sut, board);
        assertEquals(-1000, result);
        //blocked
        board_bottom[5][6] = 'w';
        board.setBoard(board_bottom);
        result = invoke(sut, board);
        assertEquals(0, result);
    }

    @Test
    void throne_onlyTwoSides_returnsZero() throws Exception {
        Object sut = clazz.getDeclaredConstructor().newInstance();

        char[][] board = emptyBoard();
        Board boardObject = createBoard(board);


        setField(sut, "kingSquare", new int[]{4, 4});
        setField(sut, "onThrone", true);

        board[3][4] = 's';
        board[5][4] = 's';
        board[4][3] = 's';

        boardObject.setBoard(board);
        int result = invoke(sut, boardObject);

        assertEquals(0, result);
    }

    // =========================
    // NON-THRONE CASES
    // =========================

    @Test
    void notThrone_leftBlack_rightThreat_returnsMinus1000() throws Exception {
        Object sut = clazz.getDeclaredConstructor().newInstance();

        char[][] board = emptyBoard();
        Board boardObject = createBoard(board);

        setField(sut, "kingSquare", new int[]{4, 4});
        setField(sut, "onThrone", false);
        board[4][3] = 's'; // links blockiert
        board[4][8] = 's'; // rechts möglicher Angriff
        boardObject.setBoard(board);
        int result = invoke(sut, boardObject);
        assertEquals(-1000, result);
        board[4][6] = 'w';
        boardObject.setBoard(board);
        result = invoke(sut, boardObject);
        assertEquals(0, result);
        board[4][6] = '-';
        board[4][8] = '-';

        board[0][5] = 's';
        boardObject.setBoard(board);
        result = invoke(sut, boardObject);
        assertEquals(-1000, result);
        board[2][5] = 'w';
        boardObject.setBoard(board);
        result = invoke(sut, boardObject);
        assertEquals(0, result);
        board[0][5] = '-';
        board[2][5] = '-';

        board[8][5] = 's';
        boardObject.setBoard(board);
        result = invoke(sut, boardObject);
        assertEquals(-1000, result);
        board[6][5] = 'w';
        boardObject.setBoard(board);
        result = invoke(sut, boardObject);
        assertEquals(0, result);
    }

    @Test
    void notThrone_leftBlack_rightThreatened_returnsMinus1000() throws Exception {
        Object sut = clazz.getDeclaredConstructor().newInstance();

        char[][] board = emptyBoard();
        Board boardObject = createBoard(board);

        // König in der Mitte
        setField(sut, "kingSquare", new int[]{4, 4});
        setField(sut, "onThrone", false);

        // Linke Seite blockiert durch schwarz
        board[4][3] = 's';

        // Rechte Seite: Bedrohung erzeugen
        board[4][8] = 's'; // Angreifer in Linie rechts

        boardObject.setBoard(board);
        int result = invoke(sut, boardObject);
        assertEquals(-1000, result);

        // --- Negativfall: Bedrohung wird geblockt ---
        board[4][6] = 'w'; // blockiert die Linie
        boardObject.setBoard(board);
        result = invoke(sut, boardObject);
        assertEquals(0, result);

        // Cleanup für nächste Checks
        board[4][6] = '-';
        board[4][8] = '-';

        // --- Alternative Threat (oben rechts indirekt) ---
        board[2][5] = 's';
        boardObject.setBoard(board);
        result = invoke(sut, boardObject);
        assertEquals(-1000, result);

        board[3][5] = 'w'; // blockiert
        boardObject.setBoard(board);
        result = invoke(sut, boardObject);
        assertEquals(0, result);

        // Cleanup
        board[2][5] = '-';
        board[3][5] = '-';

        // --- Alternative Threat (unten rechts indirekt) ---
        board[6][5] = 's';
        boardObject.setBoard(board);
        result = invoke(sut, boardObject);
        assertEquals(-1000, result);

        board[5][5] = 'w'; // blockiert
        boardObject.setBoard(board);
        result = invoke(sut, boardObject);
        assertEquals(0, result);
    }

    @Test
    void notThrone_topBlack_bottomThreatened_returnsMinus1000() throws Exception {
        Object sut = clazz.getDeclaredConstructor().newInstance();

        char[][] board = emptyBoard();
        Board boardObject = createBoard(board);

        // König mittig
        setField(sut, "kingSquare", new int[]{4, 4});
        setField(sut, "onThrone", false);

        // Oben blockiert durch schwarz
        board[3][4] = 's';

        // Unten: direkter Angriff
        board[8][4] = 's';

        boardObject.setBoard(board);
        int result = invoke(sut, boardObject);
        assertEquals(-1000, result);

        // --- Negativfall: Block dazwischen ---
        board[6][4] = 'w';
        boardObject.setBoard(board);
        result = invoke(sut, boardObject);
        assertEquals(0, result);

        // Cleanup
        board[6][4] = '-';
        board[8][4] = '-';

        // --- Alternative Threat (unten links indirekt) ---
        board[5][2] = 's';
        boardObject.setBoard(board);
        result = invoke(sut, boardObject);
        assertEquals(-1000, result);

        board[5][3] = 'w'; // blockiert
        boardObject.setBoard(board);
        result = invoke(sut, boardObject);
        assertEquals(0, result);

        // Cleanup
        board[5][2] = '-';
        board[5][3] = '-';

        // --- Alternative Threat (unten rechts indirekt) ---
        board[5][6] = 's';
        boardObject.setBoard(board);
        result = invoke(sut, boardObject);
        assertEquals(-1000, result);

        board[5][5] = 'w'; // blockiert
        boardObject.setBoard(board);
        result = invoke(sut, boardObject);
        assertEquals(0, result);
    }

    @Test
    void notThrone_bottomBlack_topThreatened_returnsMinus1000() throws Exception {
        Object sut = clazz.getDeclaredConstructor().newInstance();

        char[][] board = emptyBoard();
        Board boardObject = createBoard(board);

        // König mittig
        setField(sut, "kingSquare", new int[]{4, 4});
        setField(sut, "onThrone", false);

        // Unten blockiert durch schwarz
        board[5][4] = 's';

        // Oben: direkter Angriff
        board[0][4] = 's';

        boardObject.setBoard(board);
        int result = invoke(sut, boardObject);
        assertEquals(-1000, result);

        // --- Negativfall: Block dazwischen ---
        board[2][4] = 'w';
        boardObject.setBoard(board);
        result = invoke(sut, boardObject);
        assertEquals(0, result);

        // Cleanup
        board[2][4] = '-';
        board[0][4] = '-';

        // --- Alternative Threat (oben links indirekt) ---
        board[3][0] = 's';
        boardObject.setBoard(board);
        result = invoke(sut, boardObject);
        assertEquals(-1000, result);

        board[3][4] = 'w'; // blockiert
        boardObject.setBoard(board);
        result = invoke(sut, boardObject);
        assertEquals(0, result);

        // Cleanup
        board[3][0] = '-';
        board[3][4] = '-';

        // --- Alternative Threat (oben rechts indirekt) ---
        board[3][8] = 's';
        boardObject.setBoard(board);
        result = invoke(sut, boardObject);
        assertEquals(-1000, result);

        board[3][6] = 'w'; // blockiert
        boardObject.setBoard(board);
        result = invoke(sut, boardObject);
        assertEquals(0, result);
    }

    @Test
    void notThrone_rightBlocked_leftThreatened() throws Exception {
        Object sut = clazz.getDeclaredConstructor().newInstance();

        char[][] board = emptyBoard();
        Board boardObject = createBoard(board);

        setField(sut, "kingSquare", new int[]{8, 7});
        setField(sut, "onThrone", false);

        board[8][2] = 's';
        boardObject.setBoard(board);
        int result = invoke(sut, boardObject);
        assertEquals(-1000, result);
        boardObject.setBlackToMove(true);
        result = invoke(sut, boardObject);
        assertEquals(-5, result);
        boardObject.setBlackToMove(false);

        board[8][4] = 'w';
        boardObject.setBoard(board);
        result = invoke(sut, boardObject);
        assertEquals(0, result);

        board[8][4] = '-';
        board[8][6] = 'w';
        boardObject.setBoard(board);
        result = invoke(sut, boardObject);
        assertEquals(0, result);
        boardObject.setBlackToMove(true);
        result = invoke(sut, boardObject);
        assertEquals(0, result);
        boardObject.setBlackToMove(false);


        board[8][2] = '-';
        board[8][6] = 's';
        boardObject.setBoard(board);
        result = invoke(sut, boardObject);
        assertEquals(-5, result);

        boardObject.setBlackToMove(true);
        result = invoke(sut, boardObject);
        assertEquals(-5, result);

    }

    // =========================
    // REFLECTION + BOARD HELPERS
    // =========================

    private int invoke(Object obj, Board board) throws Exception {
        Method m = clazz.getDeclaredMethod("threatensCheckmate", Board.class);
        m.setAccessible(true);
        return (int) m.invoke(obj, board);
    }

    private void setField(Object obj, String field, Object value) throws Exception {
        Field f = clazz.getDeclaredField(field);
        f.setAccessible(true);
        f.set(obj, value);
    }

    private Board createBoard(char[][] arr) throws Exception {
        Board board = new Board();

        Field f = Board.class.getDeclaredField("board");
        f.setAccessible(true);
        f.set(board, arr);

        Field move = Board.class.getDeclaredField("blackToMove");
        move.setAccessible(true);
        move.set(board, false);

        return board;
    }

    private char[][] emptyBoard() {
        char[][] b = new char[9][9];
        for (int i = 0; i < 9; i++) {
            Arrays.fill(b[i], '-');
        }
        return b;
    }
}
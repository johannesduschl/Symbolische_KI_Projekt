import app.KI.BewertungsfunktionImpl;
import app.board.Board;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

public class EdgesSecureScoreTest {

    @Test
    void testEdgesSecureScoreEmptyBoard() {
        char[][] board = emptyBoard;

        BewertungsfunktionImpl.setOnThrone(true);
        BewertungsfunktionImpl eval = new BewertungsfunktionImpl();
        eval.evaluate(new Board(board));

        int score = invokeEdgesSecureScore(board);

        System.out.println("SCORE EMPTY: " + score);

        assertEquals(0, score);
    }

    @Test
    void testEdgesSecureScoreEnemyOnEdges() {
        char[][] board = enemyEdgeBoard;
        BewertungsfunktionImpl.setOnThrone(true);
        BewertungsfunktionImpl eval = new BewertungsfunktionImpl();
        eval.evaluate(new Board(board));

        int score = invokeEdgesSecureScore(board);

        System.out.println("SCORE ENEMY EDGES: " + score);

        assertEquals(0, score);
    }

    @Test
    void testEdgesSecureScoreLeftUnsecureRightSecure() {
        char[][] board = leftRightBoard;
        BewertungsfunktionImpl.setOnThrone(true);
        BewertungsfunktionImpl eval = new BewertungsfunktionImpl();
        eval.evaluate(new Board(board));

        int score = invokeEdgesSecureScore(board);

        System.out.println("SCORE LEFT RIGHT BOARD: " + score);

        assertEquals(16, score);
    }

    @Test
    void testEdgesSecureScoreComplex() {
        char[][] board = complexBoard;
        BewertungsfunktionImpl.setOnThrone(true);
        BewertungsfunktionImpl eval = new BewertungsfunktionImpl();
        eval.evaluate(new Board(board));

        int score = invokeEdgesSecureScore(board);

        System.out.println("SCORE COMPLEX: " + score);

        assertEquals(15, BewertungsfunktionImpl.getKingMoves());
        assertEquals(75, score);
    }

    // =========================
    // REFLECTION FIXED
    // =========================
    private int invokeEdgesSecureScore(char[][] board) {
        try {
            Method m = BewertungsfunktionImpl.class.getDeclaredMethod(
                    "edgesSecureScore",
                    char[][].class
            );

            m.setAccessible(true);

            BewertungsfunktionImpl eval = new BewertungsfunktionImpl();

            return (int) m.invoke(eval, (Object) board);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // =========================
    // BOARDS
    // =========================

    private final char[][] emptyBoard = new char[][]{
            { 'x','-','-','-','-','-','-','-','x' },
            { '-','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','k','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','-' },
            { 'x','-','-','-','-','-','-','-','x' }
    };

    private final char[][] enemyEdgeBoard = new char[][]{
            { 'x','-','-','-','s','-','-','-','x' },
            { '-','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','k','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','-' },
            { 'x','-','-','-','s','-','-','-','x' }
    };

    private final char[][] leftRightBoard = new char[][]{
            { 'x','-','-','-','-','-','-','-','x' },
            { '-','-','-','-','-','-','-','-','-' },
            { 's','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','k','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','w' },
            { '-','-','-','-','-','-','-','-','-' },
            { 'x','-','-','-','-','-','-','-','x' }
    };

    private final char[][] complexBoard = new char[][]{
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
}
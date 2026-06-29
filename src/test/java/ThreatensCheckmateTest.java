import app.KI.BewertungsfunktionImpl;
import app.board.Board;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

public class ThreatensCheckmateTest {

    @Test
    void testCustomBoard() {
        Board boardObj = new Board();
        boardObj.setBoard(customBoard);
        boardObj.setBlackMovesNext(false);

        BewertungsfunktionImpl.setOnThrone(false);
        BewertungsfunktionImpl eval = new BewertungsfunktionImpl();
        eval.evaluate(boardObj);

        int score = invokeThreatensCheckmate(boardObj);

        System.out.println("SCORE (customBoard): " + score);
        assertEquals(5, score);
    }

    // =========================
    // REFLECTION
    // =========================

    private int invokeThreatensCheckmate(Board boardObj) {
        try {
            Method m = BewertungsfunktionImpl.class.getDeclaredMethod(
                    "threatensCheckmate",
                    Board.class
            );

            m.setAccessible(true);

            BewertungsfunktionImpl eval = new BewertungsfunktionImpl();
            return (int) m.invoke(eval, boardObj);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void setBlackMovesNext(Board board, boolean value) {
        try {
            java.lang.reflect.Field f = Board.class.getDeclaredField("blackMovesNext");
            f.setAccessible(true);
            f.set(board, value);
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

    // king in danger but not fully enclosed
    private final char[][] simpleThreatOnThroneBoard = new char[][]{
            { 'x','-','-','s','-','-','-','-','x' },
            { '-','-','-','s','-','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','k','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','-' },
            { 'x','-','-','-','-','-','-','-','x' }
    };

    // king surrounded enough to simulate checkmate condition
    private final char[][] checkmateThreatBoard = new char[][]{
            { 'x','-','-','-','-','-','-','-','x' },
            { '-','-','-','-','-','-','-','-','-' },
            { '-','-','s','s','s','-','-','-','-' },
            { '-','-','s','k','s','-','-','-','-' },
            { '-','-','s','s','s','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','-' },
            { 'x','-','-','-','-','-','-','-','x' }
    };

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

    private final char[][] customBoard = new char[][]{
            { 'x', '-', 'w', 's', 's', '-', '-', '-', 'x' },
            { '-', '-', '-', '-', '-', 's', '-', '-', '-' },
            { '-', '-', '-', '-', 'w', '-', '-', '-', '-' },
            { '-', '-', 'w', '-', 'k', 's', '-', 's', '-' },
            { '-', 's', '-', '-', '-', '-', '-', '-', 's' },
            { 's', '-', '-', '-', 'w', 's', '-', '-', 's' },
            { '-', '-', 'w', '-', '-', '-', '-', '-', '-' },
            { '-', '-', '-', 's', '-', '-', '-', '-', '-' },
            { 'x', '-', '-', '-', 's', '-', '-', '-', 'x' }
    };


}
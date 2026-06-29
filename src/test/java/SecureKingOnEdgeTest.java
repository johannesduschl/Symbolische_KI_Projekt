import app.KI.BewertungsfunktionImpl;
import app.board.Board;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SecureKingOnEdgeTest {

    // =========================
    // TOP EDGE
    // =========================

    @Test
    void top_noNeighbors() {
        runTest(kingTopEdgeNoNeighborsBoard, 0);
    }

    @Test
    void top_oneNeighbor() {
        runTest(kingTopEdgeOneNeighborBoard, 5);
    }

    @Test
    void top_bothNeighbors() {
        runTest(kingTopEdgeBothNeighborsBoard, 5);
    }

    @Test
    void top_wrongDirection() {
        runTest(kingTopEdgeWrongDirectionBoard, 0);
    }

    // =========================
    // BOTTOM EDGE
    // =========================

    @Test
    void bottom_noNeighbors() {
        runTest(kingBottomEdgeNoNeighborsBoard, 0);
    }

    @Test
    void bottom_oneNeighbor() {
        runTest(kingBottomEdgeOneNeighborBoard, 5);
    }

    @Test
    void bottom_bothNeighbors() {
        runTest(kingBottomEdgeBothNeighborsBoard, 5);
    }

    @Test
    void bottom_wrongDirection() {
        runTest(kingBottomEdgeWrongDirectionBoard, 0);
    }

    // =========================
    // LEFT EDGE
    // =========================

    @Test
    void left_noNeighbors() {
        runTest(kingLeftEdgeNoNeighborsBoard, 0);
    }

    @Test
    void left_oneNeighbor() {
        runTest(kingLeftEdgeOneNeighborBoard, 5);
    }

    @Test
    void left_bothNeighbors() {
        runTest(kingLeftEdgeBothNeighborsBoard, 5);
    }

    @Test
    void left_wrongDirection() {
        runTest(kingLeftEdgeWrongDirectionBoard, 0);
    }

    // =========================
    // RIGHT EDGE
    // =========================

    @Test
    void right_noNeighbors() {
        runTest(kingRightEdgeNoNeighborsBoard, 0);
    }

    @Test
    void right_oneNeighbor() {
        runTest(kingRightEdgeOneNeighborBoard, 5);
    }

    @Test
    void right_bothNeighbors() {
        runTest(kingRightEdgeBothNeighborsBoard, 5);
    }

    @Test
    void right_wrongDirection() {
        runTest(kingRightEdgeWrongDirectionBoard, 0);
    }

    // =========================
    // TEST HELPER
    // =========================

    private void runTest(char[][] board, int expected) {
        BewertungsfunktionImpl.setOnThrone(true);

        BewertungsfunktionImpl eval = new BewertungsfunktionImpl();
        eval.evaluate(new Board(board));

        int score = invoke(eval, board);

        System.out.println("SCORE: " + score);
        assertEquals(expected, score);
    }

    private int invoke(BewertungsfunktionImpl eval, char[][] board) {
        try {
            Method m = BewertungsfunktionImpl.class
                    .getDeclaredMethod("secureKingOnEdge", char[][].class);

            m.setAccessible(true);
            return (int) m.invoke(eval, (Object) board);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // =========================
    // BOARDS
    // =========================

    // --- TOP ---

    private final char[][] kingTopEdgeNoNeighborsBoard = {
            { 'x','-','-','-','k','-','-','-','x' },
            { '-','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','-' },
            { 'x','-','-','-','-','-','-','-','x' }
    };

    private final char[][] kingTopEdgeOneNeighborBoard = {
            { 'x','-','-','w','k','-','-','-','x' },
            { '-','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','-' },
            { 'x','-','-','-','-','-','-','-','x' }
    };

    private final char[][] kingTopEdgeBothNeighborsBoard = {
            { 'x','-','-','w','k','w','-','-','x' },
            { '-','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','-' },
            { 'x','-','-','-','-','-','-','-','x' }
    };

    private final char[][] kingTopEdgeWrongDirectionBoard = {
            { 'x','-','-','-','k','-','-','-','x' },
            { '-','-','-','-','w','-','-','-','-' }, // falsch: oben/unten zählt bei TOP nicht
            { '-','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','-' },
            { 'x','-','-','-','-','-','-','-','x' }
    };

    // --- BOTTOM ---

    private final char[][] kingBottomEdgeNoNeighborsBoard = {
            { 'x','-','-','-','-','-','-','-','x' },
            { '-','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','-' },
            { 'x','-','-','-','k','-','-','-','x' }
    };

    private final char[][] kingBottomEdgeOneNeighborBoard = {
            { 'x','-','-','-','-','-','-','-','x' },
            { '-','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','-' },
            { 'x','-','-','w','k','-','-','-','x' }
    };

    private final char[][] kingBottomEdgeBothNeighborsBoard = {
            { 'x','-','-','-','-','-','-','-','x' },
            { '-','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','-' },
            { 'x','-','-','w','k','w','-','-','x' }
    };

    private final char[][] kingBottomEdgeWrongDirectionBoard = {
            { 'x','-','-','-','-','-','-','-','x' },
            { '-','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','w','-','-','-','-' }, // falsch: oben/unten zählt bei BOTTOM nicht
            { 'x','-','-','-','k','-','-','-','x' }
    };

    // --- LEFT ---

    private final char[][] kingLeftEdgeNoNeighborsBoard = {
            { 'x','-','-','-','-','-','-','-','x' },
            { 'k','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','-' },
            { 'x','-','-','-','-','-','-','-','x' }
    };

    private final char[][] kingLeftEdgeOneNeighborBoard = {
            { 'x','-','-','-','-','-','-','-','x' },
            { 'k','-','-','-','-','-','-','-','-' },
            { 'w','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','-' }, // korrekt: oben/unten zählt bei LEFT
            { '-','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','-' },
            { 'x','-','-','-','-','-','-','-','x' }
    };

    private final char[][] kingLeftEdgeBothNeighborsBoard = {
            { 'x','-','-','-','-','-','-','-','x' },
            { '-','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','-' },
            { 'w','-','-','-','-','-','-','-','-' },
            { 'k','-','-','-','-','-','-','-','-' },
            { 'w','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','-' },
            { 'x','-','-','-','-','-','-','-','x' }
    };

    private final char[][] kingLeftEdgeWrongDirectionBoard = {
            { 'x','-','-','-','-','-','-','-','x' },
            { 'k','w','-','-','-','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','-' }, // falsch: links/rechts zählt bei LEFT nicht
            { '-','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','-' },
            { 'x','-','-','-','-','-','-','-','x' }
    };

    // --- RIGHT ---

    private final char[][] kingRightEdgeNoNeighborsBoard = {
            { 'x','-','-','-','-','-','-','-','x' },
            { '-','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','k' },
            { '-','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','-' },
            { 'x','-','-','-','-','-','-','-','x' }
    };

    private final char[][] kingRightEdgeOneNeighborBoard = {
            { 'x','-','-','-','-','-','-','-','x' },
            { '-','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','k' },
            { '-','-','-','-','-','-','-','-','w' }, // korrekt: oben/unten zählt bei RIGHT
            { '-','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','-' },
            { 'x','-','-','-','-','-','-','-','x' }
    };

    private final char[][] kingRightEdgeBothNeighborsBoard = {
            { 'x','-','-','-','-','-','-','-','x' },
            { '-','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','w' },
            { '-','-','-','-','-','-','-','-','k' },
            { '-','-','-','-','-','-','-','-','w' },
            { '-','-','-','-','-','-','-','-','-' },
            { 'x','-','-','-','-','-','-','-','x' }
    };

    private final char[][] kingRightEdgeWrongDirectionBoard = {
            { 'x','-','-','-','-','-','-','-','x' },
            { '-','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','-','-','-','w','k' }, // falsch: links/rechts zählt bei RIGHT nicht
            { '-','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','-' },
            { 'x','-','-','-','-','-','-','-','x' }
    };
}
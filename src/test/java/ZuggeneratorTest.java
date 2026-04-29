import app.Zug;
import app.Zuggenerator;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ZuggeneratorTest {

    @Test
    void testSinglePieceInCenter() {
        Zuggenerator gen = new Zuggenerator();

        char[][] board = {
                {'-','-','-','-','-','-','-','-','-'},
                {'-','-','-','-','-','-','-','-','-'},
                {'-','-','-','-','-','-','-','-','-'},
                {'-','-','-','-','-','-','-','-','-'},
                {'-','-','-','-','w','-','-','-','-'},
                {'-','-','-','-','-','-','-','-','-'},
                {'-','-','-','-','-','-','-','-','-'},
                {'-','-','-','-','-','-','-','-','-'},
                {'-','-','-','-','-','-','-','-','-'}
        };

        List<Zug> moves = gen.getAllLegalMoves(board);

        assertEquals(16, moves.size());
        assertTrue(containsMove(moves, 'e','5','e','6'));
        assertTrue(containsMove(moves, 'e','5','e','1'));
        assertTrue(containsMove(moves, 'e','5','i','5'));
        assertTrue(containsMove(moves, 'e','5','a','5'));
    }

    @Test
    void testBlockedPath() {
        Zuggenerator gen = new Zuggenerator();

        char[][] board = {
                {'-','-','-','-','-','-','-','-','-'},
                {'-','-','-','-','-','-','-','-','-'},
                {'-','-','-','-','s','-','-','-','-'},
                {'-','-','-','-','-','-','-','-','-'},
                {'-','-','-','-','w','-','-','-','-'},
                {'-','-','-','-','-','-','-','-','-'},
                {'-','-','-','-','-','-','-','-','-'},
                {'-','-','-','-','-','-','-','-','-'},
                {'-','-','-','-','-','-','-','-','-'}
        };

        List<Zug> moves = gen.getAllLegalMoves(board);

        assertFalse(containsMove(moves, 'e','5','e','7'));
        assertFalse(containsMove(moves, 'e','5','e','8'));
        assertTrue(containsMove(moves, 'e','5','e','6'));
    }

    @Test
    void testCannotEnterThrone() {
        Zuggenerator gen = new Zuggenerator();

        char[][] board = {
                {'-','-','-','-','-','-','-','-','-'},
                {'-','-','-','-','-','-','-','-','-'},
                {'-','-','-','-','-','-','-','-','-'},
                {'-','-','-','-','w','-','-','-','-'},
                {'-','-','-','-','-','-','-','-','-'},
                {'-','-','-','-','-','-','-','-','-'},
                {'-','-','-','-','-','-','-','-','-'},
                {'-','-','-','-','-','-','-','-','-'},
                {'-','-','-','-','-','-','-','-','-'}
        };

        List<Zug> moves = gen.getAllLegalMoves(board);

        assertFalse(containsMove(moves, 'e','4','e','5'));
    }

    @Test
    void testCanJumpOverThrone() {
        Zuggenerator gen = new Zuggenerator();

        char[][] board = {
                {'-','-','-','-','-','-','-','-','-'},
                {'-','-','-','-','-','-','-','-','-'},
                {'-','-','-','-','-','-','-','-','-'},
                {'-','-','-','-','w','-','-','-','-'},
                {'-','-','-','-','-','-','-','-','-'},
                {'-','-','-','-','-','-','-','-','-'},
                {'-','-','-','-','-','-','-','-','-'},
                {'-','-','-','-','-','-','-','-','-'},
                {'-','-','-','-','-','-','-','-','-'}
        };

        List<Zug> moves = gen.getAllLegalMoves(board);

        assertTrue(containsMove(moves, 'e','4','e','6'));
    }

    @Test
    void testKingCannotReturnToThrone() {
        Zuggenerator gen = new Zuggenerator();

        char[][] board = {
                {'-','-','-','-','-','-','-','-','-'},
                {'-','-','-','-','-','-','-','-','-'},
                {'-','-','-','-','-','-','-','-','-'},
                {'-','-','-','-','k','-','-','-','-'},
                {'-','-','-','-','-','-','-','-','-'},
                {'-','-','-','-','-','-','-','-','-'},
                {'-','-','-','-','-','-','-','-','-'},
                {'-','-','-','-','-','-','-','-','-'},
                {'-','-','-','-','-','-','-','-','-'}
        };

        List<Zug> moves = gen.getAllLegalMoves(board);

        assertFalse(containsMove(moves, 'e','4','e','5'));
    }

    @Test
    void testCannotEnterCorner() {
        Zuggenerator gen = new Zuggenerator();

        char[][] board = {
                {'x','-','-','-','-','-','-','-','x'},
                {'-','-','-','-','-','-','-','-','-'},
                {'-','-','-','-','-','-','-','-','-'},
                {'-','-','-','-','-','-','-','-','-'},
                {'-','-','-','-','w','-','-','-','-'},
                {'-','-','-','-','-','-','-','-','-'},
                {'-','-','-','-','-','-','-','-','-'},
                {'-','-','-','-','-','-','-','-','-'},
                {'x','-','-','-','-','-','-','-','x'}
        };

        List<Zug> moves = gen.getAllLegalMoves(board);

        assertFalse(containsMove(moves, 'e','5','a','1'));
        assertFalse(containsMove(moves, 'e','5','i','9'));
    }

    private boolean containsMove(List<Zug> moves, char fc, char fr, char tc, char tr){
        return moves.stream().anyMatch(z ->
                z.getFromColumn() == fc &&
                        z.getFromRow() == fr &&
                        z.getToColumn() == tc &&
                        z.getToRow() == tr
        );
    }
}
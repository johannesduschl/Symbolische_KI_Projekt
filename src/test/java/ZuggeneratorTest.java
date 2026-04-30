import app.Zug;
import app.Zuggenerator;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ZuggeneratorTest {

    private final Zuggenerator gen = new Zuggenerator();

    @Test
    void onlyCurrentPlayerMovesGenerated() {
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

        List<Zug> whiteMoves = gen.getAllLegalMoves(board, true);
        List<Zug> blackMoves = gen.getAllLegalMoves(board, false);

        assertTrue(whiteMoves.stream().allMatch(z -> z.getFromRow() == '5'));
        assertTrue(blackMoves.stream().allMatch(z -> z.getFromRow() == '7'));
    }

    @Test
    void pieceCannotJumpOverOtherPiece() {
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

        List<Zug> moves = gen.getAllLegalMoves(board, true);

        assertFalse(containsMove(moves, 'e','5','e','7'));
        assertFalse(containsMove(moves, 'e','5','e','8'));
        assertTrue(containsMove(moves, 'e','5','e','6'));
    }

    @Test
    void kingCanEnterCornerButStopsThere() {
        char[][] board = {
                {'x','-','-','-','-','-','-','-','x'},
                {'-','-','-','-','-','-','-','-','-'},
                {'-','-','-','-','-','-','-','-','-'},
                {'-','-','-','-','-','-','-','-','-'},
                {'-','-','-','-','k','-','-','-','-'},
                {'-','-','-','-','-','-','-','-','-'},
                {'-','-','-','-','-','-','-','-','-'},
                {'-','-','-','-','-','-','-','-','-'},
                {'x','-','-','-','-','-','-','-','x'}
        };

        List<Zug> moves = gen.getAllLegalMoves(board, true);

        assertTrue(containsMove(moves, 'e','5','a','5'));
        assertFalse(containsMove(moves, 'e','5','a','4'));
    }

    @Test
    void normalPieceCannotEnterCorner() {
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

        List<Zug> moves = gen.getAllLegalMoves(board, true);

        assertFalse(containsMove(moves, 'e','5','a','1'));
    }

    @Test
    void throneCannotBeEntered() {
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

        List<Zug> moves = gen.getAllLegalMoves(board, true);

        assertFalse(containsMove(moves, 'e','4','e','5'));
    }

    @Test
    void throneCanBeJumpedOver() {
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

        List<Zug> moves = gen.getAllLegalMoves(board, true);

        assertTrue(containsMove(moves, 'e','6','e','4'));
    }

    @Test
    void kingCannotReturnToThrone() {
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

        List<Zug> moves = gen.getAllLegalMoves(board, true);

        assertFalse(containsMove(moves, 'e','4','e','5'));
    }

    @Test
    void noMoveEndsOnOccupiedField() {
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

        List<Zug> moves = gen.getAllLegalMoves(board, true);

        for (Zug z : moves) {
            int x = 8 - (z.getToRow() - '1');
            int y = z.getToColumn() - 'a';
            assertEquals('-', board[x][y]);
        }
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
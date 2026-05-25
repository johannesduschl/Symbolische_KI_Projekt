import app.KI.BewertungsfunktionImpl;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class FindCharPositionTest {

    @Test
    void testFindKingPositionWhenKingMissing() {
        BewertungsfunktionImpl eval = new BewertungsfunktionImpl();

        char[][] board = emptyBoard();

        // kein 'k' gesetzt → König fehlt

        int[] result = invokeFindPiece(board, 'k');

        // Ausgabe zum Debuggen
        System.out.println("Result for missing king: " + result);

        // Erwartung: null
        assertNull(result, "Expected null when king is missing");
    }

    @Test
    void testFindKingPositionWhenKingPresent() {
        char[][] board = startBoard;
        int[] result = invokeFindPiece(board, 'k');
        System.out.println("Result for starting position: (" + result[0] + ", " + result[1] + ")" );
        assertArrayEquals(new int[]{2, 1}, result);
    }

    @Test
    void testFindWhiteSquares() {
        char[][] board = startBoard;
        int[] result = invokeFindPiece(board, 'w');
        System.out.print("Result for starting position: (");
        int i = 0;
        for (int j : result) {
            i++;
            if(i != result.length){
                System.out.print(j + ", ");
            }else{
                System.out.print(j + ")");
            }
        }
        assertArrayEquals(new int[]{2, 4, 3, 4, 4, 2, 4, 3, 4, 5, 4, 6, 5, 4, 6, 4}, result);
    }

    @Test
    void findBlackSquares(){
        char[][] board = startBoard;
        int[] result = invokeFindPiece(board, 's');
        System.out.print("Result for starting position: (");
        int i = 0;
        for (int j : result) {
            i++;
            if(i != result.length){
                System.out.print(j + ", ");
            }else{
                System.out.println(j + ")");
            }
        }
        assertArrayEquals(new int[]{0, 3, 0, 4, 0, 5, 1, 4, 3, 0, 3, 8, 4, 0, 4, 1, 4, 7, 4, 8, 5, 0, 5, 8, 7, 4, 8, 3, 8, 4, 8, 5}, result);
    }

    /**
     * Zugriff auf die private Methode über Reflection
     */
    private int[] invokeFindPiece(char[][] board, char target) {
        try {
            var method = BewertungsfunktionImpl.class
                    .getDeclaredMethod("findCharPosition", char[][].class, char.class);

            method.setAccessible(true);

            return (int[]) method.invoke(null, board, target);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private char[][] emptyBoard() {
        char[][] b = new char[9][9];

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                b[i][j] = '-';
            }
        }

        // Ecken blockiert wie im Spiel
        b[0][0] = 'x';
        b[0][8] = 'x';
        b[8][0] = 'x';
        b[8][8] = 'x';

        return b;
    }
    private final char[][] startBoard = new char[][]{
            { 'x','-','-','s','s','s','-','-','x' },
            { '-','-','-','-','s','-','-','-','-' },
            { '-','k','-','-','w','-','-','-','-' },
            { 's','-','-','-','w','-','-','-','s' },
            { 's','s','w','w','-','w','w','s','s' },
            { 's','-','-','-','w','-','-','-','s' },
            { '-','-','-','-','w','-','-','-','-' },
            { '-','-','-','-','s','-','-','-','-' },
            { 'x','-','-','s','s','s','-','-','x' }
    };
}
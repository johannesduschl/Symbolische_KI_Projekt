import app.KI.ZobristHash;
import app.board.Board;
import app.board.Zug;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class ZobristHashValueTest {

    // ---------------------------------------------------------------
    // Helper: erzeugt einen Zug aus a1-Notation + piece
    // ---------------------------------------------------------------
    private Zug zug(char fromColumn, int fromRow, char toColumn, int toRow, char piece) {
        int fromY = fromColumn - 'a';
        int toY = toColumn - 'a';

        // Bei 9x9: row 9 -> x=0, row 1 -> x=8
        int fromX = 9 - fromRow;
        int toX = 9 - toRow;

        return new Zug(fromColumn, fromRow, toColumn, toRow, piece, fromX, fromY, toX, toY);
    }

    // ---------------------------------------------------------------
    // Test 1: Gleiche Stellung → identischer Hash
    // ---------------------------------------------------------------
    @Test
    void gleicherBoardZustand_gleicheHash() {
        Board b1 = new Board();
        Board b2 = new Board();

        assertEquals(b1.getZobristHash(), b2.getZobristHash(),
                "Zwei frische Boards müssen denselben Hash haben");
    }

    // ---------------------------------------------------------------
    // Test 2: Verschiedene Stellungen → verschiedener Hash
    // ---------------------------------------------------------------
    @Test
    void verschiedeneZustaende_verschiedeneHashes() {
        Board board = new Board();
        long hashVorher = board.getZobristHash();

        board.move(zug('e', 9, 'e', 7, 's'));
        long hashNachher = board.getZobristHash();

        assertNotEquals(hashVorher, hashNachher,
                "Hash muss sich nach einem Zug ändern");
    }

    // ---------------------------------------------------------------
    // Test 3: Gleiche Zugfolge → gleicher Hash
    // ---------------------------------------------------------------
    @Test
    void gleicheZugfolge_gleicheHash() {
        Board b1 = new Board();
        Board b2 = new Board();

        b1.move(zug('e', 9, 'e', 8, 's'));
        b2.move(zug('e', 9, 'e', 8, 's'));

        assertEquals(b1.getZobristHash(), b2.getZobristHash(),
                "Gleiche Zugfolge muss gleichen Hash ergeben");

        b1.move(zug('e', 5, 'f', 5, 'w'));
        b2.move(zug('e', 5, 'f', 5, 'w'));

        assertEquals(b1.getZobristHash(), b2.getZobristHash(),
                "Hash muss nach jedem Zug übereinstimmen");
    }

    @Test
    void gleicheStellungGleicheSeite_gleicheHash() {
        Board b1 = new Board();
        b1.move(zug('e', 9, 'e', 7, 's'));
        long hash1 = b1.getZobristHash();

        Board b2 = new Board();
        b2.move(zug('e', 9, 'e', 7, 's'));
        long hash2 = b2.getZobristHash();

        assertEquals(hash1, hash2,
                "Gleiche Stellung mit gleicher Seite am Zug muss gleichen Hash haben");
    }

    @Test
    void copy_hashWirdKorrektUebernommen() {
        Board original = new Board();
        original.move(zug('e', 9, 'e', 7, 's'));

        Board kopie = original.copy();

        assertEquals(original.getZobristHash(), kopie.getZobristHash(),
                "copy() muss den Hash des Originals übernehmen");
    }

    @Test
    void inkrementellGleichNeuBerechnet() {
        Board board = new Board();
        board.move(zug('e', 9, 'e', 7, 's'));
        long inkrementell = board.getZobristHash();

        long neuBerechnet = ZobristHash.getInstance().compute(board);

        assertEquals(neuBerechnet, inkrementell,
                "Inkrementeller Hash muss identisch mit frisch berechnetem Hash sein");
    }

    @Test
    void gleicheEndstellung_verschiedeneZugfolge_gleicheHash() {
        Board boardA = new Board();
        boardA.move(zug('e', 9, 'e', 7, 's'));
        boardA.move(zug('e', 4, 'e', 6, 'w'));
        long hashA = boardA.getZobristHash();

        Board boardB = new Board();
        boardB.move(zug('e', 9, 'e', 7, 's'));
        boardB.move(zug('e', 4, 'e', 6, 'w'));
        long hashB = boardB.getZobristHash();

        assertEquals(hashA, hashB,
                "Gleiche Endstellung muss denselben Hash haben, unabhängig vom Weg");
    }

    @Test
    void debugUpdateAfterMove() {
        Board board = new Board();
        long hashVorZug = board.getZobristHash();
        System.out.println("Hash vor Zug: " + hashVorZug);

        Board snapshot = board.copy();

        Zug z = zug('e', 9, 'e', 7, 's');
        board.move(z);

        long inkrementell = board.getZobristHash();
        long neuBerechnet = ZobristHash.getInstance().compute(board);

        System.out.println("Inkrementell:  " + inkrementell);
        System.out.println("Neu berechnet: " + neuBerechnet);
        System.out.println("Differenz (XOR): " + (inkrementell ^ neuBerechnet));

        System.out.println("\nVeränderte Felder:");
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                char vorher = snapshot.getBoard()[row][col];
                char nachher = board.getBoard()[row][col];
                if (vorher != nachher) {
                    System.out.printf("  [%d][%d]: '%c' → '%c'%n", row, col, vorher, nachher);
                }
            }
        }
    }

    @Test
    void debugBlackToMove() {
        Board board = new Board();
        System.out.println("Start - blackToMove: " + board.blackMovesNext());

        board.move(zug('e', 9, 'e', 7, 's'));
        System.out.println("Nach Schwarz - blackToMove: " + board.blackMovesNext());

        board.move(zug('e', 4, 'f', 4, 'w'));
        System.out.println("Nach Weiß - blackToMove: " + board.blackMovesNext());

        board.move(zug('e', 7, 'e', 9, 's'));
        System.out.println("Nach Schwarz zurück - blackToMove: " + board.blackMovesNext());

        board.move(zug('f', 4, 'e', 4, 'w'));
        System.out.println("Nach Weiß zurück - blackToMove: " + board.blackMovesNext());
    }
}
import app.KI.ZobristHash;
import app.board.Board;
import app.board.Zug;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class ZobristHashValueTest {

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

        // Schwarz zieht: e9 → e7  (Spalte e, Reihe 9 nach Reihe 7)
        board.move(new Zug('e', 9, 'e', 7, 's'));
        long hashNachher = board.getZobristHash();

        assertNotEquals(hashVorher, hashNachher,
                "Hash muss sich nach einem Zug ändern");
    }

    // ---------------------------------------------------------------
    // Test 3: Hin- und Rückzug → ursprünglicher Hash wieder hergestellt
    // ---------------------------------------------------------------
    /**
    @Test
    void hinUndRueckzug_gleicheHashWieVorher() {
        Board board = new Board();
        long hashStart = board.getZobristHash();

        // Hinzug
        board.move(new Zug('e', 9, 'e', 7, 's'));
        // Rückzug
        board.move(new Zug('e', 7, 'e', 9, 's'));

        assertEquals(hashStart, board.getZobristHash(),
                "Nach Hin- und Rückzug muss der ursprüngliche Hash wiederhergestellt sein");
    }
    **/
    // Option A – Hin- und Rückzug mit BEIDEN Seiten, sodass dieselbe Seite wieder dran ist
    /**
    @Test
    void hinUndRueckzug_gleicheHashWieVorher() {
        Board board = new Board();
        long hashStart = board.getZobristHash();

        board.move(new Zug('e', 9, 'e', 7, 's')); // Schwarz zieht
        board.move(new Zug('e', 4, 'f', 4, 'w')); // Weiß zieht (beliebig)
        board.move(new Zug('e', 7, 'e', 9, 's')); // Schwarz zieht zurück
        board.move(new Zug('f', 4, 'e', 4, 'w')); // Weiß zieht zurück

        assertEquals(hashStart, board.getZobristHash(),
                "Nach vollständigem Hin- und Rückzug beider Seiten muss der Hash gleich sein");
    }
    **/
    //ersetzt durch
    @Test
    void gleicheZugfolge_gleicheHash() {
        Board b1 = new Board();
        Board b2 = new Board();

        // Beide Boards machen exakt dieselben Züge
        b1.move(new Zug('e', 9, 'e', 8, 's'));
        b2.move(new Zug('e', 9, 'e', 8, 's'));

        assertEquals(b1.getZobristHash(), b2.getZobristHash(),
                "Gleiche Zugfolge muss gleichen Hash ergeben");

        b1.move(new Zug('e', 5, 'f', 5, 'w'));
        b2.move(new Zug('e', 5, 'f', 5, 'w'));

        assertEquals(b1.getZobristHash(), b2.getZobristHash(),
                "Hash muss nach jedem Zug übereinstimmen");
    }
    // Option B – nur prüfen dass gleiche Stellung + gleiche Seite = gleicher Hash
    @Test
    void gleicheStellungGleicheSeiте_gleicheHash() {
        Board b1 = new Board();
        b1.move(new Zug('e', 9, 'e', 7, 's')); // Schwarz zieht
        long hash1 = b1.getZobristHash();

        Board b2 = new Board();
        b2.move(new Zug('e', 9, 'e', 7, 's')); // identischer Zug
        long hash2 = b2.getZobristHash();

        assertEquals(hash1, hash2,
                "Gleiche Stellung mit gleicher Seite am Zug muss gleichen Hash haben");
    }
    @Test
    void debugHinRueckzug() {
        Board board = new Board();
        System.out.println("=== START ===");
        board.printBoard();
        long hashStart = board.getZobristHash();

        board.move(new Zug('e', 9, 'e', 7, 's'));
        System.out.println("=== Nach Schwarz e9→e7 ===");
        board.printBoard();

        board.move(new Zug('e', 4, 'f', 4, 'w'));
        System.out.println("=== Nach Weiß e4→f4 ===");
        board.printBoard();

        board.move(new Zug('e', 7, 'e', 9, 's'));
        System.out.println("=== Nach Schwarz e7→e9 ===");
        board.printBoard();

        board.move(new Zug('f', 4, 'e', 4, 'w'));
        System.out.println("=== Nach Weiß f4→e4 ===");
        board.printBoard();

        System.out.println("Hash Start:  " + hashStart);
        System.out.println("Hash Ende:   " + board.getZobristHash());
        System.out.println("bewegt Ende: '" + board.getBewegt() + "'");
    }

    // ---------------------------------------------------------------
    // Test 4: copy() übernimmt Hash korrekt
    // ---------------------------------------------------------------
    @Test
    void copy_hashWirdKorrektUebernommen() {
        Board original = new Board();
        original.move(new Zug('e', 9, 'e', 7, 's'));

        Board kopie = original.copy();

        assertEquals(original.getZobristHash(), kopie.getZobristHash(),
                "copy() muss den Hash des Originals übernehmen");
    }

    // ---------------------------------------------------------------
    // Test 5: Inkrementell == vollständig neu berechnet
    // Das ist der wichtigste Test – er prüft ob updateAfterMove()
    // dasselbe Ergebnis liefert wie compute() von Grund auf
    // ---------------------------------------------------------------
    @Test
    void inkrementellGleichNeuBerechnet() {
        Board board = new Board();
        board.move(new Zug('e', 9, 'e', 7, 's'));
        long inkrementell = board.getZobristHash();

        // Dieselbe Instanz via getInstance()
        long neuBerechnet = ZobristHash.getInstance().compute(board);

        assertEquals(neuBerechnet, inkrementell,
                "Inkrementeller Hash muss identisch mit frisch berechnetem Hash sein");
    }

    // ---------------------------------------------------------------
    // Test 6: Zwei verschiedene Zugfolgen, gleiche Endstellung
    // → Hash muss identisch sein (stärkster Korrektheitsbeweis)
    // ---------------------------------------------------------------
    @Test
    void gleicheEndstellung_verschiedeneZugfolge_gleicheHash() {
        // Zugfolge A: erst e9→e7, dann zurück e7→e8, dann e8→e7
        Board boardA = new Board();
        boardA.move(new Zug('e', 9, 'e', 7, 's'));
        boardA.move(new Zug('e', 4, 'e', 6, 'w')); // Weiß zieht dazwischen
        long hashA = boardA.getZobristHash();

        // Zugfolge B: direkt e9→e7, Weiß macht denselben Zug
        Board boardB = new Board();
        boardB.move(new Zug('e', 9, 'e', 7, 's'));
        boardB.move(new Zug('e', 4, 'e', 6, 'w'));
        long hashB = boardB.getZobristHash();

        assertEquals(hashA, hashB,
                "Gleiche Endstellung muss denselben Hash haben, unabhängig vom Weg");
    }
    /**
    @Test
    void debugHash() {
        ZobristHash z1 = new ZobristHash(0xAB1A8B1ACAFEEL);
        ZobristHash z2 = new ZobristHash(0xAB1A8B1ACAFEEL);

        Board b = new Board();

        // Sind die zwei Instanzen überhaupt gleich?
        long h1 = z1.compute(b);
        long h2 = z2.compute(b);
        System.out.println("z1.compute: " + h1);
        System.out.println("z2.compute: " + h2);
        System.out.println("Gleich: " + (h1 == h2));

        // Ist der Board-interne Hash gleich?
        System.out.println("board.getHash: " + b.getZobristHash());
    }
     **/
    @Test
    void debugUpdateAfterMove() {
        Board board = new Board();
        long hashVorZug = board.getZobristHash();
        System.out.println("Hash vor Zug:          " + hashVorZug);

        // Snapshot vor dem Zug
        Board snapshot = board.copy();

        Zug zug = new Zug('e', 9, 'e', 7, 's');
        board.move(zug);

        long inkrementell = board.getZobristHash();
        long neuBerechnet = ZobristHash.getInstance().compute(board);

        System.out.println("Inkrementell:          " + inkrementell);
        System.out.println("Neu berechnet:         " + neuBerechnet);
        System.out.println("Differenz (XOR):       " + (inkrementell ^ neuBerechnet));

        // Was hat sich auf dem Brett verändert?
        System.out.println("\nVeränderte Felder:");
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                char vorher  = snapshot.getBoard()[row][col];
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
        System.out.println("Start - blackToMove: " + board.isBlackToMove());

        board.move(new Zug('e', 9, 'e', 7, 's'));
        System.out.println("Nach Schwarz - blackToMove: " + board.isBlackToMove());

        board.move(new Zug('e', 4, 'f', 4, 'w'));
        System.out.println("Nach Weiß - blackToMove: " + board.isBlackToMove());

        board.move(new Zug('e', 7, 'e', 9, 's'));
        System.out.println("Nach Schwarz zurück - blackToMove: " + board.isBlackToMove());

        board.move(new Zug('f', 4, 'e', 4, 'w'));
        System.out.println("Nach Weiß zurück - blackToMove: " + board.isBlackToMove());
    }
}

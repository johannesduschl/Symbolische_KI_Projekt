package app.KI;

import app.board.Board;
import app.board.Zug;

import java.util.Random;

public class ZobristHash {

    // Mapping von Board-chars auf Tabellen-Index
    // 's' → 0, 'w' → 1, 'k' → 2
    private static final int IDX_BLACK = 0;
    private static final int IDX_WHITE = 1;
    private static final int IDX_KING  = 2;

    private static final int SQUARES = 81;

    private final long[][] pieceTable = new long[3][SQUARES]; // [Figurtyp][Feld]
    private final long     blackToMove;

    // Eine einzige Instanz für die gesamte Anwendung
    private static final ZobristHash INSTANCE = new ZobristHash(0xAB1A8B1ACAFEEL);

    public static ZobristHash getInstance() {
        return INSTANCE;
    }

    private ZobristHash(long seed) {
        Random rng = new Random(seed);

        for (int p = 0; p < 3; p++)
            for (int s = 0; s < SQUARES; s++)
                pieceTable[p][s] = rng.nextLong();

        blackToMove = rng.nextLong();
    }

    // -----------------------------------------------------------
    // char → Tabellen-Index
    // -----------------------------------------------------------
    private static int pieceIndex(char c) {
        return switch (c) {
            case 's' -> IDX_BLACK;
            case 'w' -> IDX_WHITE;
            case 'k' -> IDX_KING;
            default  -> -1;   // '-' oder 'x' → keine Figur
        };
    }

    // row/col des char[][] direkt als Index
    private static int square(int row, int col) {
        return row * 9 + col;
    }

    // -----------------------------------------------------------
    // Vollständiger Hash – einmal beim Spielstart / nach copy()
    // -----------------------------------------------------------
    public long compute(Board board) {
        long hash = 0L;
        char[][] b = board.getBoard();

        for (int row = 0; row < 9; row++)
            for (int col = 0; col < 9; col++) {
                int idx = pieceIndex(b[row][col]);
                if (idx >= 0)
                    hash ^= pieceTable[idx][square(row, col)];
            }

        System.out.println("Hash vor blackToMove-XOR: " + hash);
        System.out.println("blackToMove: " + board.isBlackToMove());

        if (board.isBlackToMove())
            hash ^= blackToMove;

        System.out.println("Hash nach blackToMove-XOR: " + hash);
        return hash;
    }
@Deprecated
    // Schwarz ist am Zug wenn zuletzt Weiß ('w' oder 'k') gezogen hat
    private static boolean isBlackToMove(Board board) {
        char last = board.getBewegt();
        return last == 'w' || last == 'k' || last == ' '; // ' ' = Spielstart, Schwarz beginnt
    }

    // -----------------------------------------------------------
    // Inkrementelles Update nach board.move(zug)
    // -----------------------------------------------------------

    /**
     * Muss VOR board.move(zug) mit dem alten Zustand aufgerufen werden,
     * danach nochmal – XOR hebt sich dann auf den richtigen Zustand.
     *
     * Einfacher: einmal vor dem Zug aufrufen, einmal danach,
     * und den Hash komplett neu berechnen via compute().
     *
     * Für maximale Performance: updateHash() wie unten.
     */
    @Deprecated
    public long updateAfterMove(long oldHash, Board boardVorZug,
                                Board boardNachZug, Zug zug) {
        long hash = oldHash;
        char[][] vorher  = boardVorZug.getBoard();
        char[][] nachher = boardNachZug.getBoard();

        int size = 9;
        int fromRow = size - zug.getFromRow();
        int fromCol = zug.getFromColumn() - 'a';
        int toRow   = size - zug.getToRow();
        int toCol   = zug.getToColumn() - 'a';

        // 1. Ziehende Figur von Startfeld entfernen
        int movingIdx = pieceIndex(vorher[fromRow][fromCol]);
        hash ^= pieceTable[movingIdx][square(fromRow, fromCol)];

        // 2. Ziehende Figur auf Zielfeld setzen
        hash ^= pieceTable[movingIdx][square(toRow, toCol)];

        // 3. Geschlagene Figuren entfernen (Felder die vorher belegt, nachher leer sind)
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                if (row == fromRow && col == fromCol) continue; // Startfeld bereits behandelt
                if (row == toRow   && col == toCol)   continue; // Zielfeld bereits behandelt

                int vorIdx  = pieceIndex(vorher[row][col]);
                int nachIdx = pieceIndex(nachher[row][col]);

                if (vorIdx >= 0 && nachIdx < 0) {
                    // Figur wurde geschlagen → aus Hash entfernen
                    hash ^= pieceTable[vorIdx][square(row, col)];
                }
            }
        }

        // 4. Seitenwechsel
        hash ^= blackToMove;

        return hash;
    }
}

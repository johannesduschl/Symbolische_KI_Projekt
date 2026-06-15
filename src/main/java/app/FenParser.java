package app;

public class FenParser {

    /**
     * Parst eine Tablut-FEN und befüllt ein Board-Array.
     * Format: "3aaa3/4a4/4d4/a3d3a/aaddkddaa/a3d3a/4d4/4a4/3aaa3 a 0 0"
     * Reihenfolge: Zeile 0 = oben (Row 9 im Board)
     *
     * @return char[][] kompatibel mit Board (s=Angreifer, w=Verteidiger, k=König, x=Ecke, -=leer)
     */
    public static char[][] parseBoardFromFen(String fen) {
        char[][] board = new char[9][9];

        // Eckfelder vorbelegen
        board[0][0] = 'x'; board[0][8] = 'x';
        board[8][0] = 'x'; board[8][8] = 'x';

        String[] parts = fen.trim().split(" ");
        String boardPart = parts[0];
        String[] rows = boardPart.split("/");

        for (int row = 0; row < 9; row++) {
            int col = 0;
            for (char c : rows[row].toCharArray()) {
                if (Character.isDigit(c)) {
                    int empty = Character.getNumericValue(c);
                    for (int i = 0; i < empty; i++) {
                        // Eckfeld nicht überschreiben
                        if (!isCorner(row, col)) board[row][col] = '-';
                        col++;
                    }
                } else {
                    board[row][col] = fenCharToBoard(c);
                    col++;
                }
            }
        }

        return board;
    }

    /**
     * Wer ist laut FEN am Zug?
     * "a" = Angreifer = Schwarz, "d" = Verteidiger = Weiß
     */
    public static boolean isBlackToMoveFromFen(String fen) {
        String[] parts = fen.trim().split(" ");
        if (parts.length < 2) return true; // Schwarz beginnt
        return parts[1].equals("a");
    }

    /**
     * Erzeugt eine FEN aus dem aktuellen Board-Zustand.
     * Nützlich zum Debuggen.
     */
    public static String boardToFen(char[][] board, boolean blackToMove,
                                    int halfmoveClock, int halfmoveCount) {
        StringBuilder sb = new StringBuilder();

        for (int row = 0; row < 9; row++) {
            int emptyCount = 0;
            for (int col = 0; col < 9; col++) {
                char c = board[row][col];
                if (c == '-' || c == 'x') {
                    emptyCount++;
                } else {
                    if (emptyCount > 0) {
                        sb.append(emptyCount);
                        emptyCount = 0;
                    }
                    sb.append(boardCharToFen(c));
                }
            }
            if (emptyCount > 0) sb.append(emptyCount);
            if (row < 8) sb.append('/');
        }

        sb.append(' ').append(blackToMove ? 'a' : 'd');
        sb.append(' ').append(halfmoveClock);
        sb.append(' ').append(halfmoveCount);

        return sb.toString();
    }

    // ── FEN-Zeichen → Board-Zeichen ───────────────────────────────────
    private static char fenCharToBoard(char c) {
        return switch (Character.toLowerCase(c)) {
            case 'a' -> 's';   // Angreifer → schwarz
            case 'd' -> 'w';   // Verteidiger → weiß
            case 'k' -> 'k';   // König
            default  -> '-';   // leer
        };
    }

    // ── Board-Zeichen → FEN-Zeichen ───────────────────────────────────
    private static char boardCharToFen(char c) {
        return switch (c) {
            case 's' -> 'a';
            case 'w' -> 'd';
            case 'k' -> 'k';
            default  -> '-';
        };
    }

    private static boolean isCorner(int row, int col) {
        return (row == 0 || row == 8) && (col == 0 || col == 8);
    }
}

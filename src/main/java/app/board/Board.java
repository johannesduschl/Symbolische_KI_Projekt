package app.board;

import app.KI.ZobristHash;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
//@AllArgsConstructor entfernt für Zobrist Hash
//@NoArgsConstructor entfernt für Zobrist Hash
public class Board {
    private static final ZobristHash ZOBRIST = ZobristHash.getInstance();
    private long zobristHash = 0L;
    private char bewegt= ' ';
    private boolean blackToMove = true; // Schwarz beginnt

    /**
     * 's' = schwarze Figur
     * 'w' = weiße Figur
     * 'k' = (weißer) König
     * 'x' = Eckfeld
     * '-' = leer
     */
    private char[][] board = new char[][]{
            { 'x','-','-','s','s','s','-','-','x' },
            { '-','-','-','-','s','-','-','-','-' },
            { '-','-','-','-','w','-','-','-','-' },
            { 's','-','-','-','w','-','-','-','s' },
            { 's','s','w','w','k','w','w','s','s' },
            { 's','-','-','-','w','-','-','-','s' },
            { '-','-','-','-','w','-','-','-','-' },
            { '-','-','-','-','s','-','-','-','-' },
            { 'x','-','-','s','s','s','-','-','x' }
    }; //a1 unten links i9 oben rechts

    private static final boolean[][] BLOCKED = {
            {  true, false, false, false, false, false, false, false,  true },
            { false, false, false, false, false, false, false, false, false },
            { false, false, false, false, false, false, false, false, false },
            { false, false, false, false, false, false, false, false, false },
            { false, false, false, false,  true, false, false, false, false },
            { false, false, false, false, false, false, false, false, false },
            { false, false, false, false, false, false, false, false, false },
            { false, false, false, false, false, false, false, false, false },
            {  true, false, false, false, false, false, false, false,  true }
    };

    /**
     * Änderungen für Zobrist Hash
     */
    public void initHash() {
        this.zobristHash = ZOBRIST.compute(this);
    }
    /** Primärkonstruktor – Startstellung, Hash wird initialisiert */
    public Board() {
        initHash();
    }

    /** Sekundärkonstruktor – für Tests und copy(), Hash wird übernommen oder neu berechnet */
    public Board( char[][] board) {
        this.bewegt = ' ';
        this.board = board;
        initHash(); // frisch berechnen, da wir einen beliebigen Zustand übergeben
    }

    public boolean isBlackToMove() { return blackToMove; }


    public boolean isGameOver() {

        int size = board.length;

        int kingX = -1;
        int kingY = -1;

        // 1. König finden
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (board[i][j] == 'k') {
                    kingX = i;
                    kingY = j;
                    break;
                }
            }
        }

        // falls König nicht mehr da → verloren
        if (kingX == -1) {
            return true;
        }

        // 2. Sieg durch Ecke
        if ((kingX == 0 && kingY == 0) ||
                (kingX == 0 && kingY == size - 1) ||
                (kingX == size - 1 && kingY == 0) ||
                (kingX == size - 1 && kingY == size - 1)) {
            return true;
        }
        // 3. Sieg durch Schachmatt
        return isCheckMate(kingX, kingY);
    }


    /**
     * Führt einen Zug aus und prüft, ob das Spiel vorbei ist
     * @param zug der Zug der ausgeführt werden soll
     * @return True, wenn das Spiel vorbei ist.
     */
    public boolean move(Zug zug) {

        this.bewegt = zug.getPiece();

        char[][] board = this.board;
        int size = board.length;

        int fromX = size - zug.getFromRow(); // Row 9 wird zu index 0, row 8 wird zu index 1 usw.
        int fromY = zug.getFromColumn() - 'a'; // Spalte 'a' wird zu 0, 'b' zu 1 usw.
        int toX = size - zug.getToRow();
        int toY = zug.getToColumn() - 'a';


        board[fromX][fromY] = '-';
        board[toX][toY] = zug.getPiece();

        eliminatePieces(board, zug.getPiece(), toX, toY);

        blackToMove = !blackToMove; //Flag umschalten

        // snapshot und updateAfterMove() entfernt – compute() nach Zustandsänderung
        this.zobristHash = ZOBRIST.compute(this);

        return isGameOver();
    }

    public boolean isCheckMate(int x, int y){
        //prüfen ob shcwarz überhaupt am Zug ist
        if(this.bewegt!='s'){ return false;}

        char[][] board = this.board;
        boolean onThrone = (x == 4 && y == 4);
        int size = board.length;

        if(onThrone){
            //No array out of bound possible!
            return board[x - 1][y] == 's' && board[x + 1][y] == 's' &&
                    board[x][y - 1] == 's' && board[x][y + 1] == 's';
        }else{
            //Mated vertically?:
            return ((x - 1 >= 0 && x + 1 < size) && //without check array out of bound error on top/bottom edge!
                    (board[x - 1][y] == 's' || BLOCKED[x - 1][y]) &&
                    (board[x + 1][y] == 's' || BLOCKED[x + 1][y])) ||
                    //Mated horizontally?:
                    ((y - 1 >= 0 && y + 1 < size) && //without check array out of bound error on left/right edge!
                            (board[x][y - 1] == 's' || BLOCKED[x][y - 1]) &&
                            (board[x][y + 1] == 's' || BLOCKED[x][y + 1]));

        }
    }


    private void eliminatePieces(char[][] board, char piece, int toX, int toY) {

        int size = board.length;

        if (piece == 'k') {
            piece = 'w';
        }

        char enemyPiece = (piece == 's') ? 'w' : 's';

        int[][] directions = {
                {1,0}, {-1,0}, {0,1}, {0,-1}
        };

        for (int[] dir : directions) {

            int enemyX = toX + dir[0];
            int enemyY = toY + dir[1];

            if (enemyX < 0 || enemyX >= size || enemyY < 0 || enemyY >= size) {
                continue;
            }

            char target = board[enemyX][enemyY];

            if (target != enemyPiece) {
                continue;
            }

            int behindX = enemyX + dir[0];
            int behindY = enemyY + dir[1];

            if (behindX < 0 || behindX >= size || behindY < 0 || behindY >= size) {
                continue;
            }

            char behind = board[behindX][behindY];

            if (
                    behind == piece ||
                            behind == 'x' ||
                            (behind == 'k' && piece == 'w') ||
                            (behindX == 4 && behindY == 4 && board[4][4] == '-')
            ) {
                board[enemyX][enemyY] = '-';
            }
        }
    }


    public Board copy() {
        char[][] newBoard = new char[board.length][board[0].length];

        for (int i = 0; i < board.length; i++) {
            System.arraycopy(board[i], 0, newBoard[i], 0, board[i].length);
        }

        Board copy = new Board(newBoard);
        copy.bewegt      = this.bewegt;
        copy.blackToMove = this.blackToMove;      // Flag mit kopieren
        copy.zobristHash = this.zobristHash; // Hash direkt übertragen statt neu berechnen
        return copy;
    }


    public void printBoard(){

        StringBuilder sb = new StringBuilder();

        sb.append(' ');
        for (char c = 'A'; c <= 'I'; c++) sb.append(' ').append(c);

        for(int y=0; y < board.length; y++){
            sb.append('\n');
            sb.append(9-y);
            sb.append(' ');
            for(int x = 0; x < board[0].length; x++){
                sb.append(board[y][x]);
                sb.append(' ');
            }
        }
        sb.append('\n');
        System.out.println(sb);
    }

    public long getZobristHash() {
        return zobristHash;
    }


}

package app.board;

import app.KI.ZobristHash;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayDeque;
import java.util.Deque;

@Getter
@Setter
//@AllArgsConstructor entfernt für Zobrist Hash
//@NoArgsConstructor entfernt für Zobrist Hash
public class Board {
    private final Deque<UndoInfo> history = new ArrayDeque<>();
    private static final ZobristHash ZOBRIST = ZobristHash.getInstance();
    private int nullMoveStack = 0;
    private long zobristHash = 0L;
    private char bewegt= ' ';
    private boolean blackToMove = true; // Schwarz beginnt
    private Zug lastMove;

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

        UndoInfo ui = new UndoInfo();

        ui.move = zug;
        ui.blackToMove = blackToMove;
        ui.bewegt = this.bewegt;
        ui.zobristHash = this.zobristHash;

        char[][] board = this.board;
        int size = board.length;

        int fromX = size - zug.getFromRow();
        int fromY = zug.getFromColumn() - 'a';
        int toX = size - zug.getToRow();
        int toY = zug.getToColumn() - 'a';

        ui.fromX = fromX;
        ui.fromY = fromY;
        ui.toX = toX;
        ui.toY = toY;

        char piece = zug.getPiece();

        // 1. Figur entfernen / setzen
        board[fromX][fromY] = '-';
        board[toX][toY] = piece;

        // 2. Captures mitloggen (EXTREM WICHTIG)
        captureWithUndo(board, piece, toX, toY, ui);

        // State updates
        lastMove = zug;
        bewegt = piece;
        blackToMove = !blackToMove;

        zobristHash = ZOBRIST.compute(this);

        // speichern
        history.push(ui);

        return isGameOver();
    }

    private void captureWithUndo(char[][] board, char piece, int toX, int toY, UndoInfo ui) {

        int size = board.length;

        if (piece == 'k') piece = 'w';

        char enemy = (piece == 's') ? 'w' : 's';

        int[][] dirs = {
                {1,0}, {-1,0}, {0,1}, {0,-1}
        };

        for (int[] d : dirs) {

            int ex = toX + d[0];
            int ey = toY + d[1];

            if (ex < 0 || ex >= size || ey < 0 || ey >= size) continue;

            if (board[ex][ey] != enemy) continue;

            int bx = ex + d[0];
            int by = ey + d[1];

            if (bx < 0 || bx >= size || by < 0 || by >= size) continue;

            char behind = board[bx][by];

            if (
                    behind == piece ||
                            behind == 'x' ||
                            (behind == 'k' && piece == 'w') ||
                            (bx == 4 && by == 4 && board[4][4] == '-')
            ) {
                // speichern
                ui.capX[ui.capCount] = ex;
                ui.capY[ui.capCount] = ey;
                ui.captured[ui.capCount] = board[ex][ey];
                ui.capCount++;

                // löschen
                board[ex][ey] = '-';
            }
        }
    }

    public void undoMove() {

        if (history.isEmpty()) return;

        UndoInfo ui = history.pop();

        char[][] board = this.board;

        // 1. Zug zurücksetzen
        board[ui.toX][ui.toY] = '-';
        board[ui.fromX][ui.fromY] = ui.move.getPiece();

        // 2. geschlagene Figuren wiederherstellen
        for (int i = 0; i < ui.capCount; i++) {
            board[ui.capX[i]][ui.capY[i]] = ui.captured[i];
        }

        // 3. State restore
        blackToMove = ui.blackToMove;
        bewegt = ui.bewegt;
        lastMove = ui.move;
        zobristHash = ui.zobristHash;
    }

    public void makeNullMove() {
        // Kein history push!
        nullMoveStack++;

        bewegt = ' ';
        blackToMove = !blackToMove;

        // Wichtig: Hash muss konsistent bleiben
        // → wir müssen inkrementell updaten oder neu berechnen
        zobristHash = ZOBRIST.compute(this);
    }

    public void undoNullMove() {
        if (nullMoveStack == 0) return;

        nullMoveStack--;

        blackToMove = !blackToMove;

        // restore hash exakt wie vorheriger Zustand
        zobristHash = ZOBRIST.compute(this);
    }

    public boolean isCheckMate(int x, int y){
        //prüfen ob shcwarz überhaupt am Zug ist
        Zug lastMove = this.getLastMove();
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
                    (board[x + 1][y] == 's' || BLOCKED[x + 1][y]) &&
                    (lastMove.toX == (x - 1) || lastMove.toX == (x + 1))) ||
                    //Mated horizontally?:
                    ((y - 1 >= 0 && y + 1 < size) && //without check array out of bound error on left/right edge!
                            (board[x][y - 1] == 's' || BLOCKED[x][y - 1]) &&
                            (board[x][y + 1] == 's' || BLOCKED[x][y + 1]) &&
                            (lastMove.toY == (y - 1) || lastMove.toY == (y + 1)));

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
        copy.lastMove = this.lastMove;
        copy.bewegt      = this.bewegt;
        copy.blackToMove = this.blackToMove;      // Flag mit kopieren
        copy.zobristHash = this.zobristHash; // Hash direkt übertragen statt neu berechnen //TODO: schauen ob dies zu korrupten Hashes führt
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

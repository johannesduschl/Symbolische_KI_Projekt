package app.board;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Board {

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

        int[][] dirs = {
                {1,0}, {-1,0}, {0,1}, {0,-1}
        };

        int blocked = 0;

        boolean isOnThrone = (kingX == 4 && kingY == 4);

        for (int[] d : dirs) {

            int nx = kingX + d[0];
            int ny = kingY + d[1];

            if (nx < 0 || nx >= size || ny < 0 || ny >= size) {
                blocked++;
                continue;
            }

            char c = board[nx][ny];

            // schwarze Figuren blocken
            if (c == 's') {
                blocked++;
                continue;
            }

            // Thron ist blockierend für den König (wenn er nicht drauf steht)
            if (nx == 4 && ny == 4 && !isOnThrone) {
                blocked++;
                continue;
            }

            // Ecken blockieren ebenfalls
            if (c == 'x') {
                blocked++;
            }
        }

        // auf Thron: 4 blockiert nötig
        if (isOnThrone) {
            return blocked == 4;
        }

        // normal oder neben Thron: 4 blockiert nötig
        return blocked == 4;
    }


    /**
     * Führt einen Zug aus und prüft, ob das Spiel vorbei ist
     * @param zug der Zug der ausgeführt werden soll
     * @return True, wenn das Spiel vorbei ist.
     */
    public boolean move(Zug zug) {

        char[][] board = this.board;
        int size = board.length;

        int fromX = size - zug.getFromRow(); // Row 9 wird zu index 0, row 8 wird zu index 1 usw.
        int fromY = zug.getFromColumn() - 'a'; // Spalte 'a' wird zu 0, 'b' zu 1 usw.
        int toX = size - zug.getToRow();
        int toY = zug.getToColumn() - 'a';


        board[fromX][fromY] = '-';
        board[toX][toY] = zug.getPiece();

        eliminatePieces(board, zug.getPiece(), toX, toY);

        return isGameOver();
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

        return new Board(newBoard);
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


}

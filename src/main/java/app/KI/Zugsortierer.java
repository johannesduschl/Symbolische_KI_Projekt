package app.KI;

import app.board.Board;
import app.board.Zug;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Zugsortierung sortiert Züge nach der Bewertungsfunktion mit der Tiefe 1
 */
public class Zugsortierer {

    private final Zug[][] killerMoves;

    public Zugsortierer(int maxDepth){
        this.killerMoves = new Zug[maxDepth][2];
    }


    public List<Zug> getSortedList(List<Zug> moves, Board board, int depth) {

        moves.sort((a,b) -> Integer.compare(
                scoreMove(b, board, depth, killerMoves),
                scoreMove(a, board, depth, killerMoves)));

        return moves;
    }


    private int scoreMove(Zug move, Board board, int depth, Zug[][] killerMoves) {

        int score = 0;

        if (isCapture(move, board)) {
            score += 1000;
        }

        if (depth < killerMoves.length) {

            if (move.equals(killerMoves[depth][0])) {
                score += 500;
            }

            else if (move.equals(killerMoves[depth][1])) {
                score += 400;
            }
        }
        return score;
    }


    public void storeKillerMove(Zug move, int depth) {

        if (depth < 0 || depth >= killerMoves.length) return;

        if (killerMoves[depth][0] == null || !killerMoves[depth][0].equals(move)) {
            killerMoves[depth][1] = killerMoves[depth][0];
            killerMoves[depth][0] = move;
        }
    }


    private boolean isCapture(Zug move, Board board) {

        char[][] b = board.getBoard();
        int size = b.length;

        int toX = size - move.getToRow();
        int toY = move.getToColumn() - 'a';

        char piece = move.getPiece();
        if (piece == 'k') piece = 'w';

        char enemy = (piece == 's') ? 'w' : 's';

        int[][] dirs = {{1,0},{-1,0},{0,1},{0,-1}};

        for (int[] d : dirs) {

            int ex = toX + d[0];
            int ey = toY + d[1];

            if (ex < 0 || ex >= size || ey < 0 || ey >= size) continue;

            if (b[ex][ey] != enemy) continue;

            int bx = ex + d[0];
            int by = ey + d[1];

            if (bx < 0 || bx >= size || by < 0 || by >= size) continue;

            char behind = b[bx][by];

            if (behind == piece || behind == 'x' ||
                    (behind == 'k' && piece == 'w') ||
                    (bx == 4 && by == 4 && b[4][4] == '-')) {
                return true;
            }
        }

        return false;
    }
}

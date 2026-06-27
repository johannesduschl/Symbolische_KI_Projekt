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
    private final int[][] historyTable;

    public Zugsortierer(int maxDepth){
        this.killerMoves = new Zug[maxDepth][2];
        this.historyTable = new int[81][81];
    }


    public List<Zug> getSortedList(List<Zug> moves, Board board, int depth) {

        moves.sort((a,b) -> Integer.compare(
                scoreMove(b, board, depth, killerMoves),
                scoreMove(a, board, depth, killerMoves)));

        return moves;
    }


    private int scoreMove(Zug move, Board board, int depth, Zug[][] killerMoves) {

        int score = 0;

        score += getCaptureScore(move, board);
        score += getKillerScore(depth, move);
        score += getHistoryScore(move, depth);

        return score;
    }


    public void addHistory(Zug move, int depth) {

        int from = moveIndex(move.getFromRow(), move.getFromColumn());
        int to = moveIndex(move.getToRow(), move.getToColumn());

        historyTable[from][to] += depth * depth;
    }


    public void storeKillerMove(Zug move, int depth) {

        if (depth < 0 || depth >= killerMoves.length) return;

        if (killerMoves[depth][0] == null || !killerMoves[depth][0].equals(move)) {
            killerMoves[depth][1] = killerMoves[depth][0];
            killerMoves[depth][0] = move;
        }
    }


    private int getHistoryScore(Zug move, int depth) {
        int from = moveIndex(move.getFromRow(), move.getFromColumn());
        int to = moveIndex(move.getToRow(), move.getToColumn());
        return historyTable[from][to];
    }


    private int getKillerScore(int depth, Zug move){
        if (depth < killerMoves.length) {

            if (move.equals(killerMoves[depth][0])) {
                return 500;
            }

            else if (move.equals(killerMoves[depth][1])) {
                return 400;
            }
        }
        return 0;
    }


    private int moveIndex(int row, char col) {
        int r = 9 - row;
        int c = col - 'a';
        return r * 9 + c;
    }


    private int getCaptureScore(Zug move, Board board) {

        char[][] b = board.getBoard();
        int size = b.length;

        int toX = size - move.getToRow();
        int toY = move.getToColumn() - 'a';

        char piece = move.getPiece();
        if (piece == 'k') piece = 'w';

        char enemy = (piece == 's') ? 'w' : 's';

        int score = 0;

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

            if (behind == piece ||
                    behind == 'x' ||
                    (behind == 'k' && piece == 'w') ||
                    (bx == 4 && by == 4 && b[4][4] == '-')) {

                score += 600;
            }
        }

        return score;
    }
}

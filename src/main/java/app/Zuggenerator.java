package app;

import java.util.ArrayList;
import java.util.List;

public class Zuggenerator {

    public List<Zug> getAllLegalMoves(char[][] board){

        List<Zug> possibleMoves = new ArrayList<>();

        for (int i = 0; i < board.length; i++){
            for (int j = 0; j < board[i].length; j++){

                char figure = board[i][j];
                if (figure == '-' || figure == 'x') continue;
                possibleMoves.addAll(move(board, i, j));

            }
        }
        return possibleMoves;
    }

    public List<Zug> move(char [][] board, int x, int y){
        List<Zug> moves = new ArrayList<>();
        char piece = board[x][y];

        int size = board.length;
        int throneX = 4;
        int throneY = 4;

        int[][] directions = {
                {1,0}, {-1,0}, {0,1}, {0,-1}
        };

        for (int[] dir : directions) {
            int runningX = x + dir[0];
            int runningY = y + dir[1];

            while (runningX >= 0 && runningX < size && runningY >= 0 && runningY < size){

                char target = board[runningX][runningY];
                if (target == 's' || target == 'w' || target == 'k') break;
                if (target == 'x') break;

                if (runningX == throneX && runningY == throneY) {
                    if (piece == 'k' && !(x == throneX && y == throneY)) {
                        break;
                    }
                    runningX += dir[0];
                    runningY += dir[1];
                    continue;
                }

                moves.add(new Zug(
                        (char)('a' + y),
                        (char)('1' + (size - 1 - x)),
                        (char)('a' + runningY),
                        (char)('1' + (size - 1 - runningX))
                ));
                runningX += dir[0];
                runningY += dir[1];
            }
        }
        return moves;
    }
}

package app.board;

import java.util.ArrayList;
import java.util.List;

public class Zuggenerator {

    public List<Zug> getAllLegalMoves(char[][] board, boolean isWhiteToMove){

        List<Zug> possibleMoves = new ArrayList<>();

        for (int i = 0; i < board.length; i++){
            for (int y = 0; y < board[i].length; y++){

                char piece = board[i][y];
                // only calculate possible moves for relevant pieces:
                if (isWhiteToMove && (piece == 'w' || piece == 'k')) {
                    possibleMoves.addAll(getPossibleMoves(board, i, y));
                }
                if (!isWhiteToMove && piece == 's') {
                    possibleMoves.addAll(getPossibleMoves(board, i, y));
                }
            }
        }
        return possibleMoves;
    }

    public List<Zug> getPossibleMoves(char [][] board, int x, int y){
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
                if (target == 'x' && piece != 'k') break;

                if (runningX == throneX && runningY == throneY) {
                    runningX += dir[0];
                    runningY += dir[1];
                    continue;
                }

                moves.add(new Zug(
                        (char)('a' + y),
                        (1 + (size - 1 - x)),
                        (char)('a' + runningY),
                        1 + (size - 1 - runningX),
                        piece
                ));
                runningX += dir[0];
                runningY += dir[1];
            }
        }
        return moves;
    }
}

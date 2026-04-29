package app;

import java.util.ArrayList;
import java.util.List;

public class Zuggenerator {

    public String[] getAllLegalMoves(char[][] board){
        return null;
    }

    /**
     * Berechnet für übergebene Koordinaten auf dem Board alle möglichen Züge des dort liegenden Steins
     * @param board aktuelles Spielbrett
     * @param x x Koordinate des Spielsteins
     * @param y y Koordinate des Spielsteins
     * @return Liste aller Züge
     */
    public List<String> move(char [][] board, int x, int y){
        List<String> moves = new ArrayList<>();
        System.out.println(board[x][y]);

        //Bedingung das King in die Ecken kann
        if(board[x][y] == 'k'){
            board[0][0] = '-';
            board[0][8] = '-';
            board[8][0] = '-';
            board[8][8] = '-';
        }

        int helpx = x+1;
        int helpy = y;
        //für x++
        while(helpx<=8&& board[helpx][helpy] == '-'){
            moves.add(helpx+""+helpy);
            helpx++;
        }
        //für x--
        helpx = x-1;
        System.out.println(board[helpx][helpy]);
        while(helpx >=0 && board[helpx][helpy] == '-'){
            moves.add(helpx+""+helpy);
            helpx--;
        }
        //für y++
        helpy = y+1;
        helpx = x;
        while(helpy<=8 && board[helpx][helpy] == '-'){
            moves.add(helpx+""+helpy);
            helpy++;
        }
        //für x++
        helpy = y-1;
        while(helpy>=0 && board[helpx][helpy] == '-'){
            moves.add(helpx+""+helpy);
            helpy--;
        }
        return moves;
    }

}

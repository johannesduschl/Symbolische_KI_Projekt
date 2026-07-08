package app.KI.evoLearn;

import java.util.Arrays;
import java.util.stream.Collectors;

public class EvoMain {
    public static void main(String[] args) {
        int [][] board = {
                {1,2,3,4,5},
                {99,6,7,8,9},
                {99,99,10,11,12},
                {99,99,99,13,14},
                {99,99,99,99,15}
        };
        int [][] result = EvoMain.flipIt(flipItHalf(board));
        String ergebnis = Arrays.stream(result)
                .map(Arrays::toString)
                .collect(Collectors.joining("\n"));

        System.out.println(ergebnis);
    }


    /**
     * nimmt ein gevierteltes 9x9 array in form eines 5x5 wo die untere seite einer diagonale von [0][0] bis [4][4] leer ist und verfollständigt diese
     * @param flipper
     * @return
     */
    public static int[][] flipItHalf(int[][] flipper){
        int[][] result = new int[5][5];
        for( int x =0; x < 5; x++){
            for(int y = 0; y < 5; y++){
                if(x>y) result[x][y] = flipper[y][x];
                else result[x][y] = flipper[x][y];
            }
        }
        return result;
    }

    /**
     * Flip the board from a 5x5 to a 9x9
     * @param flipper original board 5x5
     * @return 9x9 board mirrored on x and y axes
     */
    public static int[][] flipIt(int[][] flipper){
        int [][] result = new int[9][9];
        for( int x =0; x < 9; x++){
            for(int y = 0; y < 9; y++){
                if(x<=4 && y<=4){
                    result[x][y] = flipper[x][y];
                }
                else if(x>4 && y<=4){
                    result[x][y] = flipper[8-x][y];
                }
                else if(x<4 && y>4){
                    result[x][y] = flipper[x][8-y];
                }
                else{
                    result[x][y] = flipper[8-x][8-y];
                }
            }
        }
        return result;
    }
}


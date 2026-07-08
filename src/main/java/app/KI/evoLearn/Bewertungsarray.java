package app.KI.evoLearn;

import lombok.Getter;

public class Bewertungsarray {
    // Die 25 Parameter, die die Koordinaten repräsentieren
    private final int r0c0, r0c1, r0c2, r0c3, r0c4;
    private final int r1c0, r1c1, r1c2, r1c3, r1c4;
    private final int r2c0, r2c1, r2c2, r2c3, r2c4;
    private final int r3c0, r3c1, r3c2, r3c3, r3c4;
    private final int r4c0, r4c1, r4c2, r4c3, r4c4;

    // Das daraus resultierende 5x5 Array
    @Getter
    private final int[][] grid;

    // Konstruktor, der die 25 Parameter entgegennimmt und das Array baut
    public Bewertungsarray(
            int r0c0, int r0c1, int r0c2, int r0c3, int r0c4,
            int r1c0, int r1c1, int r1c2, int r1c3, int r1c4,
            int r2c0, int r2c1, int r2c2, int r2c3, int r2c4,
            int r3c0, int r3c1, int r3c2, int r3c3, int r3c4,
            int r4c0, int r4c1, int r4c2, int r4c3, int r4c4
    ) {
        // Zuweisung an die Instanzvariablen
        this.r0c0 = r0c0; this.r0c1 = r0c1; this.r0c2 = r0c2; this.r0c3 = r0c3; this.r0c4 = r0c4;
        this.r1c0 = r1c0; this.r1c1 = r1c1; this.r1c2 = r1c2; this.r1c3 = r1c3; this.r1c4 = r1c4;
        this.r2c0 = r2c0; this.r2c1 = r2c1; this.r2c2 = r2c2; this.r2c3 = r2c3; this.r2c4 = r2c4;
        this.r3c0 = r3c0; this.r3c1 = r3c1; this.r3c2 = r3c2; this.r3c3 = r3c3; this.r3c4 = r3c4;
        this.r4c0 = r4c0; this.r4c1 = r4c1; this.r4c2 = r4c2; this.r4c3 = r4c3; this.r4c4 = r4c4;

        // Erstellung des 5x5 Arrays aus den Parametern
        this.grid = new int[][] {
                { r0c0, r0c1, r0c2, r0c3, r0c4 },
                { r1c0, r1c1, r1c2, r1c3, r1c4 },
                { r2c0, r2c1, r2c2, r2c3, r2c4 },
                { r3c0, r3c1, r3c2, r3c3, r3c4 },
                { r4c0, r4c1, r4c2, r4c3, r4c4 }
        };
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

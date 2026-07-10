package app.KI.evoLearn;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class EvoMain {

    static List<EvoKi> kisWeiß = new ArrayList<>();
    static List<EvoKi> kisSchwarz = new ArrayList<>();

    //es folgen 30 weiße und 30 schwarze KI Objekte
    /** Weiße */
    static EvoKi w1 = new EvoKi(new Genom(4,3,3,3,2,1,1,1,-1,-1,-1,-1,-3,5,0,0,1,1,1,1,2,2,2,3,3,3,4,0,0,2,2,2,0,0,1,2,0,1,3,3,5,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1));
    static EvoKi w2 = new EvoKi(new Genom(7,5,5,5,4,3,3,3,-2,-2,-2,-2,-4,7,0,0,1,1,1,1,2,2,2,3,3,3,4,0,0,2,2,2,0,0,1,2,0,1,3,3,5,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1));

    /** Schwarze */
    static EvoKi b1 = new EvoKi(new Genom(4,3,3,3,2,1,1,1,-1,-1,-1,-1,-3,5,0,0,1,1,1,1,2,2,2,3,3,3,4,0,0,2,2,2,0,0,1,2,0,1,3,3,5,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1));

    public static void main(String[] args) {
        kisSchwarz.add(b1);
        kisWeiß.add(w1);
        kisWeiß.add(w2);
        Scanner scanner = new Scanner(System.in);
        String input = "";
        while(true) {
            System.out.println("neue Runde \n press 1 to start: \n 2 to get AI Info \n 3 to stop");
            input = scanner.nextLine();
            if (input.equals("3")) break;
            else if (input.equals("2")) {

                for (int x = 0; x < kisWeiß.size(); x++) {
                    System.out.println("Weiße Ki Nummer: "+x+"\n winrate: "+ kisWeiß.get(x).winrate.get()+"\n fitness: "+kisWeiß.get(x).fitness.get());
                }
                for (int x = 0; x < kisSchwarz.size(); x++) {
                    System.out.println("Schwarze Ki Nummer: "+x+"\n winrate: "+kisSchwarz.get(x).winrate.get()+"\n fitness: "+kisSchwarz.get(x).fitness.get());
                }
            }
            else if (input.equals("1")) {
                EvoContest contest = new EvoContest(kisWeiß,kisSchwarz);
            }
            else System.out.println("ungültiger Input Try again:");
        }

    }
}


/**
 * alter Main Inhalt:
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
    */


package app;

public class Main {

    public static void main(String[] args) {

        System.out.println("Hello and welcome!");

        //Init game
        Spiel spiel = new Spiel();
        Zuggenerator zuggenerator = new Zuggenerator();


        spiel.printBoard();
        System.out.println(zuggenerator.getAllLegalMoves(spiel.board.board, false));

    }
}
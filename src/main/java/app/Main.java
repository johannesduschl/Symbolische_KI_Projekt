package app;

public class Main {

    public static void main(String[] args) {

        System.out.println("Hello and welcome!");

        //SpielDummy spielDummy = new SpielDummy();
        //spielDummy.startGame();

        SpielGameserver spielGameserver = new SpielGameserver();
        spielGameserver.startGame();

    }
}
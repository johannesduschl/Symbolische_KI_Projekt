package app.KI.evoLearn;

import java.util.*;

public class EvoMain {

    static int abbruchCounterFitnessGotWorseWhite = 0;
    static int abbruchCounterFitnessGotWorseBlack = 0;

    static EvoLog log = new EvoLog("logs/tablut_evolution.log");

    static List<EvoKi> kisWeiß = new ArrayList<>();
    static List<EvoKi> kisSchwarz = new ArrayList<>();

    //enthält die 5 besten KIs der letzten Generation White, die die weiß spielen und Black, die die schwarz spielen
    static List<EvoKi> bestFiveOfAllTimeWhite = new ArrayList<>();
    static List<EvoKi> bestFiveOfAllTimeBlack = new ArrayList<>();

    static int generationsCounter = 0;

    //es folgen 15 weiße und 15 schwarze KI-Objekte
    /** Weiße */
    static EvoKi w1 = new EvoKi(new Genom(4,3,3,3,2,1,1,1,-1,-1,-1,-1,-3,5,0,0,1,1,1,1,2,2,2,3,3,3,4,0,2,2,2,0,0,1,2,0,1,3,3,5,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1));
    static EvoKi w2 = new EvoKi(new Genom(7,5,5,5,4,3,3,1,-2,-2,-2,-2,-4,7,0,0,1,1,1,1,2,2,2,3,3,3,4,0,2,2,2,0,0,1,2,0,1,3,3,5,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1));
    static EvoKi w3 = new EvoKi(new Genom(9,7,7,7,6,5,5,5,-3,-3,-3,-3,-5,6,0,0,1,1,1,1,2,2,2,3,3,3,4,0,2,2,2,0,0,1,2,0,1,3,3,5,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1));
    static EvoKi w4 = new EvoKi(new Genom(11,9,9,9,8,7,3,3,-2,-2,-2,-2,-4,7,0,0,1,1,1,1,2,2,2,3,3,3,4,0,2,2,2,0,0,1,2,0,1,3,3,5,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1));
    static EvoKi w5 = new EvoKi(new Genom(13,11,11,11,10,9,1,1,-1,-1,-1,-1,-3,5,0,0,1,1,1,1,2,2,2,3,3,3,4,0,2,2,2,0,0,1,2,0,1,3,3,5,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1));

    /** Schwarze */
    static EvoKi b1 = new EvoKi(new Genom(4,3,3,3,2,1,1,1,-1,-1,-1,-1,-3,5,0,0,1,1,1,1,2,2,2,3,3,3,4,0,2,2,2,0,0,1,2,0,1,3,3,5,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1));

    public static void main(String[] args) {
        kisSchwarz.add(b1);
        kisWeiß.add(w1);
        kisWeiß.add(w2);
        Scanner scanner = new Scanner(System.in);
        String input;
        while(true) {
            System.out.println("neue Runde \n press 1 to start: \n 2 to get AI Info \n 3 to stop \n 4 to start Evolutions-Simulation");
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
                System.out.println("test ob die ausgabe wirklich erst am ende erfolgt");
            } else if (input.equals("4")) {
                boolean abbruch = false;
                while(!abbruch) {
                    //aktuelle Generation loggen

                    log.writeText("=== Generation "+generationsCounter+" gestartet ===");
                    EvoContest contest = new EvoContest(kisWeiß, kisSchwarz);
                    log.log(kisWeiß, kisSchwarz);
                    log.writeText("Generation "+generationsCounter+" abgeschlossen, starte Selektion...");
                    //stats nach contest
                    sortAILists(kisSchwarz);
                    sortAILists(kisWeiß);
                    //Prüfen, ob Abbruch
                    abbruch = checkAbbruch();
                    if(abbruch) {
                        break;
                    }
                    //beste 5 der letzten Generation speichern und akutalisieren
                    updateBestFiveOfAllTime();
                    //neue Generation erstellen
                    newPop();
                    //weiter machen
                    generationsCounter++;
                    for (int x = 0; x < kisWeiß.size(); x++) {
                        System.out.println("Weiße Ki Nummer: "+x+"\n winrate: "+ kisWeiß.get(x).winrate.get()+"\n fitness: "+kisWeiß.get(x).fitness.get());
                    }
                    for (int x = 0; x < kisSchwarz.size(); x++) {
                        System.out.println("Schwarze Ki Nummer: "+x+"\n winrate: "+kisSchwarz.get(x).winrate.get()+"\n fitness: "+kisSchwarz.get(x).fitness.get());
                    }
                }
            } else System.out.println("ungültiger Input Try again:");
        }

    }
    //Todo Evolutions-Logik implementieren, wie vermehren sich KIs und wie kommt es zu Mutationen
    public static void newPop(){

    }
    //Todo abbruchlogik und logging für ergebnisse der Prüfung
    public static boolean checkAbbruch(){
        //Schwellenwert
        if (kisWeiß.get(0).winrate.get() >= 15){
            log.writeText("Abbruch durch Winrate Weiß");
            return true;
        }
        if (kisSchwarz.get(0).winrate.get() >= 15){
            log.writeText("Abbruch durch Winrate Weiß");
            return true;
        }
        //maximale Generationenanzahl
        if(generationsCounter > 100){
            log.writeText("Abbruch durch max. Generationenanzahl von 100");
            return true;
        }
        //Abbruch durch Verschlechterung der KI (Vgl spielen gegen vorherige Generation anhand Fitness der Top 5
        //Contest von jeweils bester KI gegen die besten 5 der letzten Generationen wenn winrate unter 3 dann abbruch
        EvoKi bestBlackCurrentPop = new EvoKi(kisSchwarz.get(0).genom);
        EvoKi bestWhiteCurrentPop = new EvoKi(kisWeiß.get(0).genom);

        //Listen für Contestdurchführung zum Vergleich der Fitness-Werte
        List<EvoKi> bestBlackCurrentPopList = new ArrayList<>();
        List<EvoKi> bestWhiteCurrentPopList = new ArrayList<>();

        //hinzufügen, spielen gegeneinander, sortieren und prüfen ob unsere Ki mindestens in top 3, sonst counter um 1 erhöhen wenn counter == 3 abbruch
        bestBlackCurrentPopList.add(bestBlackCurrentPop);
        bestWhiteCurrentPopList.add(bestWhiteCurrentPop);
        for (EvoKi ki : bestFiveOfAllTimeBlack) {
            bestBlackCurrentPopList.add(ki);
        }
        for (EvoKi ki : bestFiveOfAllTimeWhite) {
            bestWhiteCurrentPopList.add(ki);
        }

        EvoContest contest = new EvoContest(bestBlackCurrentPopList, bestWhiteCurrentPopList);

        if (checkAbbruchHelp(bestBlackCurrentPopList, bestBlackCurrentPop)) abbruchCounterFitnessGotWorseBlack++;
        if (checkAbbruchHelp(bestWhiteCurrentPopList, bestWhiteCurrentPop)) abbruchCounterFitnessGotWorseWhite++;

        if (bestBlackCurrentPop.winrate.get() >= 3) {
            log.writeText("Abbruch da schwarze Ki über 3 Generationen schlechter geworden ist");
            return true;}
        if (bestWhiteCurrentPop.winrate.get() >= 3) {
            log.writeText("Abbruch da weiße Ki über 3 Generationen schlechter geworden ist");
            return true;
        }





        return false;
    }

    public static boolean checkAbbruchHelp(List<EvoKi> list, EvoKi ki){
        boolean abbruch = true;
        //könnte einen Fehler verursachen wenn Ki durch contest falsch aktualisiert wird, sollte aber richtig funktionieren und das tatsächliche KI Objekt in der Liste suchen und vergleichen
        for(int x = 0; x < list.size()&&x<3; x++){
            if (list.get(x).equals(ki)) {
                abbruch = false;
                break;
            }
        }

        return abbruch;
    }

    //ToDo beste 5 der letzten Generationen speichern (Vergleich aktuell beste mit denen der letzen und ggf aktualisieren
    public static void updateBestFiveOfAllTime(){
        for (int x = 0; x<kisSchwarz.size(); x++) {
            bestFiveOfAllTimeBlack.add(new EvoKi(kisSchwarz.get(0).genom));
        }
        sortAILists(bestFiveOfAllTimeBlack);
        while(bestFiveOfAllTimeBlack.size() > 5){
            bestFiveOfAllTimeBlack.remove(bestFiveOfAllTimeBlack.size()-1);
        }
        for (int x = 0; x<kisWeiß.size(); x++) {
            bestFiveOfAllTimeWhite.add(new EvoKi(kisWeiß.get(0).genom));
        }
        sortAILists(bestFiveOfAllTimeWhite);
        while(bestFiveOfAllTimeWhite.size() > 5){
            bestFiveOfAllTimeWhite.remove(bestFiveOfAllTimeWhite.size()-1);
        }
    }

    public static void sortAILists(List<EvoKi> sortierMich){
        sortierMich.sort(
                Comparator
                        .comparingInt((EvoKi ki) -> ki.winrate.get())
                        .thenComparingInt(ki -> ki.fitness.get())
                        .reversed()
        );
    }
}


/** alter Main Inhalt:
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


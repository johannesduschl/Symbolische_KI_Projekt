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
    //static EvoKi w1 = new EvoKi(new Genom(4,3,3,3,2,1,1,1,-1,-1,-1,-1,-3,5,0,0,1,1,1,1,2,2,2,3,3,3,4,0,2,2,2,0,0,1,2,0,1,3,3,5,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1));
    // ===== WEISS: 15 Startgenome (58 Parameter, kritische Gewichte >= 1) =====
    static EvoKi w1 = new EvoKi(new Genom(8,6,6,6,6,5,6,3,5,5,2,4,1,0,3,3,2,0,2,1,2,0,0,1,-1,-1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,70,0,5,11,-1,6,2,2,0,1,5,0,0,0,0,0,0,0));
    static EvoKi w2 = new EvoKi(new Genom(7,7,6,4,8,5,6,4,6,5,2,2,3,2,5,2,2,0,4,3,0,1,0,1,-1,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,50,0,4,10,5,4,1,3,3,2,7,0,0,0,0,0,0,0));
    static EvoKi w3 = new EvoKi(new Genom(7,8,5,4,8,6,6,4,5,5,3,3,3,0,3,4,2,0,3,1,1,0,0,1,-1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,64,0,6,13,1,4,3,1,4,3,9,0,0,0,0,0,0,0));
    static EvoKi w4 = new EvoKi(new Genom(7,6,6,5,8,7,4,3,6,5,3,4,3,2,4,3,3,1,4,2,0,0,1,-1,1,-1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,66,0,0,8,5,4,-1,4,1,3,8,0,0,0,0,0,0,0));
    static EvoKi w5 = new EvoKi(new Genom(7,6,6,5,8,6,4,4,6,4,4,3,2,2,4,2,1,0,2,1,0,1,0,-1,0,1,-1,0,0,0,0,0,0,0,0,0,0,0,0,0,55,0,2,9,-1,3,1,3,2,4,9,0,0,0,0,0,0,0));
    static EvoKi w6 = new EvoKi(new Genom(7,8,7,6,8,7,6,3,5,5,4,3,2,1,4,2,2,2,3,1,0,-1,0,0,-1,-1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,36,0,4,7,-1,2,2,0,4,0,7,0,0,0,0,0,0,0));
    static EvoKi w7 = new EvoKi(new Genom(7,6,7,5,6,7,5,4,6,4,3,2,1,1,4,3,2,1,2,1,0,1,1,1,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,36,0,1,11,-1,3,2,1,1,5,9,0,0,0,0,0,0,0));
    static EvoKi w8 = new EvoKi(new Genom(9,7,7,4,8,6,6,4,4,4,2,4,3,2,4,4,1,2,2,1,1,1,0,-1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,57,0,5,7,-1,8,0,2,2,1,10,0,0,0,0,0,0,0));
    static EvoKi w9 = new EvoKi(new Genom(8,8,6,5,6,5,4,3,5,3,3,2,2,2,5,2,2,2,3,3,0,1,0,0,1,-1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,64,0,1,10,5,7,0,-1,6,5,8,0,0,0,0,0,0,0));
    static EvoKi w10 = new EvoKi(new Genom(8,8,5,6,6,5,4,3,4,5,3,4,1,2,5,3,3,1,2,3,2,-1,0,-1,1,1,-1,0,0,0,0,0,0,0,0,0,0,0,0,0,36,0,4,12,0,5,4,0,6,6,6,0,0,0,0,0,0,0));
    static EvoKi w11 = new EvoKi(new Genom(8,6,6,6,6,7,5,4,6,4,2,2,3,1,4,4,3,2,3,3,0,1,0,1,1,-1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,65,0,6,8,3,2,4,5,1,1,6,0,0,0,0,0,0,0));
    static EvoKi w12 = new EvoKi(new Genom(9,8,5,6,6,6,6,5,6,5,3,2,3,0,3,2,2,0,2,3,1,1,0,-1,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,65,0,4,11,3,3,3,1,3,4,9,0,0,0,0,0,0,0));
    static EvoKi w13 = new EvoKi(new Genom(9,6,7,6,7,7,4,4,4,4,2,3,2,1,3,4,1,1,2,1,2,0,0,-1,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,66,0,2,8,1,3,1,0,5,0,8,0,0,0,0,0,0,0));
    static EvoKi w14 = new EvoKi(new Genom(7,8,5,4,8,6,6,4,5,4,2,3,2,0,5,3,1,1,4,2,1,1,0,0,0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,52,0,2,11,-1,2,4,0,0,0,7,0,0,0,0,0,0,0));
    static EvoKi w15 = new EvoKi(new Genom(7,6,6,4,7,7,5,4,4,5,4,4,2,2,4,2,2,0,4,1,1,-1,1,-1,1,-1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,35,0,0,11,5,3,-2,1,6,0,8,0,0,0,0,0,0,0));

    // ===== SCHWARZ: 15 Startgenome (58 Parameter, kritische Gewichte >= 1) =====
    static EvoKi b1 = new EvoKi(new Genom(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,4,4,2,1,4,1,0,1,2,-1,-1,-1,0,0,48,0,0,0,0,0,0,0,0,0,0,1,8,5,7,0,3));
    static EvoKi b2 = new EvoKi(new Genom(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,4,3,3,2,2,2,1,-1,1,-1,-1,-1,1,0,41,0,0,0,0,0,0,0,0,0,4,4,8,7,5,-1,2));
    static EvoKi b3 = new EvoKi(new Genom(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,5,4,2,2,3,3,1,1,1,1,-1,-1,0,0,57,0,0,0,0,0,0,0,0,0,1,6,12,8,7,-1,2));
    static EvoKi b4 = new EvoKi(new Genom(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,3,2,1,0,4,3,1,0,0,-1,-1,1,0,0,53,0,0,0,0,0,0,0,0,0,6,4,12,5,6,-1,4));
    static EvoKi b5 = new EvoKi(new Genom(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,3,3,1,0,3,2,0,0,1,0,1,0,-1,0,59,0,0,0,0,0,0,0,0,0,0,2,8,5,3,-2,1));
    static EvoKi b6 = new EvoKi(new Genom(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,3,3,2,2,4,1,0,1,0,-1,0,-1,-1,0,49,0,0,0,0,0,0,0,0,0,3,4,7,6,2,0,1));
    static EvoKi b7 = new EvoKi(new Genom(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,3,4,3,0,4,3,2,0,1,1,0,-1,0,0,67,0,0,0,0,0,0,0,0,0,5,4,12,4,2,4,5));
    static EvoKi b8 = new EvoKi(new Genom(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,5,3,3,2,4,1,2,1,2,-1,1,1,1,0,43,0,0,0,0,0,0,0,0,0,5,5,12,4,2,-2,-1));
    static EvoKi b9 = new EvoKi(new Genom(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,5,3,1,1,3,3,0,1,0,1,1,1,-1,0,67,0,0,0,0,0,0,0,0,0,3,2,7,6,8,-2,4));
    static EvoKi b10 = new EvoKi(new Genom(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,5,2,3,2,2,3,2,0,1,-1,0,-1,1,0,59,0,0,0,0,0,0,0,0,0,6,1,8,8,7,1,2));
    static EvoKi b11 = new EvoKi(new Genom(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,3,3,3,1,2,3,2,1,0,-1,1,-1,0,0,43,0,0,0,0,0,0,0,0,0,2,5,12,8,4,2,3));
    static EvoKi b12 = new EvoKi(new Genom(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,3,3,1,1,3,3,0,1,0,1,0,0,1,0,70,0,0,0,0,0,0,0,0,0,4,2,10,6,5,4,-1));
    static EvoKi b13 = new EvoKi(new Genom(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,3,3,1,1,2,2,1,-1,2,0,0,0,-1,0,51,0,0,0,0,0,0,0,0,0,1,0,11,3,3,3,3));
    static EvoKi b14 = new EvoKi(new Genom(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,4,2,3,2,4,2,0,1,1,-1,0,0,0,0,54,0,0,0,0,0,0,0,0,0,0,1,7,6,7,1,2));
    static EvoKi b15 = new EvoKi(new Genom(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,5,2,2,1,3,2,0,0,0,0,0,0,-1,0,39,0,0,0,0,0,0,0,0,0,1,5,7,8,4,0,1));

    public static void main(String[] args) {
        kisSchwarz.add(b1);
        kisSchwarz.add(b2);
        kisSchwarz.add(b3);
        kisSchwarz.add(b4);
        kisSchwarz.add(b5);
        kisSchwarz.add(b6);
        kisSchwarz.add(b7);
        kisSchwarz.add(b8);
        kisSchwarz.add(b9);
        kisSchwarz.add(b10);
        kisSchwarz.add(b11);
        kisSchwarz.add(b12);
        kisSchwarz.add(b13);
        kisSchwarz.add(b14);
        kisSchwarz.add(b15);
        kisWeiß.add(w1);
        kisWeiß.add(w2);
        kisWeiß.add(w3);
        kisWeiß.add(w4);
        kisWeiß.add(w5);
        kisWeiß.add(w6);
        kisWeiß.add(w7);
        kisWeiß.add(w8);
        kisWeiß.add(w9);
        kisWeiß.add(w10);
        kisWeiß.add(w11);
        kisWeiß.add(w12);
        kisWeiß.add(w13);
        kisWeiß.add(w14);
        kisWeiß.add(w15);
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
                    //beste 5 der letzten Generation speichern und akutalisieren jetzt in abbruch()
                    //updateBestFiveOfAllTime();
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
        int eliteCount = 3;
        int breedingPoolSize = 6; // beste 6 dürfen sich fortpflanzen

        kisWeiß = EvoBreeder.breedGeneration(kisWeiß, true, eliteCount, breedingPoolSize);
        kisSchwarz = EvoBreeder.breedGeneration(kisSchwarz, false, eliteCount, breedingPoolSize);

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

        bestFiveOfAllTimeWhite.add(bestWhiteCurrentPop);
        bestFiveOfAllTimeBlack.add(bestBlackCurrentPop);

        //hinzufügen, spielen gegeneinander, sortieren und prüfen ob unsere Ki mindestens in top 3, sonst counter um 1 erhöhen wenn counter == 3 abbruch
        for (EvoKi ki : bestFiveOfAllTimeBlack) {
            bestBlackCurrentPopList.add(ki);
        }
        for (EvoKi ki : bestFiveOfAllTimeWhite) {
            bestWhiteCurrentPopList.add(ki);
        }

        EvoContest contest = new EvoContest(bestWhiteCurrentPopList,bestBlackCurrentPopList);

        updateBestFiveOfAllTime();

        sortAILists(bestBlackCurrentPopList);
        sortAILists(bestWhiteCurrentPopList);

        if (checkAbbruchHelp(bestBlackCurrentPopList, bestBlackCurrentPop)) abbruchCounterFitnessGotWorseBlack++;
        if (checkAbbruchHelp(bestWhiteCurrentPopList, bestWhiteCurrentPop)) abbruchCounterFitnessGotWorseWhite++;

        if (abbruchCounterFitnessGotWorseBlack >= 3) {
            log.writeText("Abbruch da schwarze Ki über 3 Generationen schlechter geworden ist");
            return true;}
        if (abbruchCounterFitnessGotWorseWhite >= 3) {
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
        List<EvoKi> helpBlack = new ArrayList<>();
        List<EvoKi> helpWhite = new ArrayList<>();

        sortAILists(bestFiveOfAllTimeBlack);
        for (EvoKi ki : bestFiveOfAllTimeBlack){
            helpBlack.add(new EvoKi(ki.genom));
        }
        bestFiveOfAllTimeBlack.clear();
        for (EvoKi ki : helpBlack){
            bestFiveOfAllTimeBlack.add(ki);
        }

        while(bestFiveOfAllTimeBlack.size() > 5){
            bestFiveOfAllTimeBlack.remove(bestFiveOfAllTimeBlack.size()-1);
        }

        sortAILists(bestFiveOfAllTimeWhite);
        for (EvoKi ki : bestFiveOfAllTimeWhite){
            helpWhite.add(new EvoKi(ki.genom));
        }
        bestFiveOfAllTimeWhite.clear();
        for (EvoKi ki : helpWhite){
            bestFiveOfAllTimeWhite.add(ki);
        }


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


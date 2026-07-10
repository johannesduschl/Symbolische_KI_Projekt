package app.KI.evoLearn;

import java.util.ArrayList;
import java.util.List;

public class EvoContest {
    List<EvoKi> kisWeiß = new ArrayList<>();
    List<EvoKi> kisSchwarz = new ArrayList<>();
    List<EvoGame> games = new ArrayList<>();

    public EvoContest(List<EvoKi> kisWeiß, List<EvoKi> kisSchwarz) {
        this.kisWeiß = kisWeiß;
        this.kisSchwarz = kisSchwarz;
        this.contest();
    }

    public void contest() {
        for (EvoKi ki1 : this.kisWeiß) {
            for (EvoKi ki2 : this.kisSchwarz) {
                EvoGame game = new EvoGame(ki1, ki2);
                games.add(game);
                game.start();
            }
        }

        // Warten, bis ALLE Spiele fertig sind
        for (EvoGame game : games) {
            try {
                game.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Interrupt-Status wiederherstellen
                throw new RuntimeException("Warten auf EvoGame wurde unterbrochen", e);
            }
        }

        // Ab hier ist garantiert: alle Spiele sind beendet,
        // alle fitness/winrate Werte sind final und sichtbar.
        System.out.println("Alle Spiele beendet, starte Auswertung...");
    }
}

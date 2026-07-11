package app.KI.evoLearn;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class EvoContest {
    //Logik zur Durchführung der Contests zwischen den KIs der aktuellen Pop um Winrate(Fitness) zu bestimmen
    List<EvoKi> kisWeiß = new ArrayList<>();
    List<EvoKi> kisSchwarz = new ArrayList<>();

    public EvoContest(List<EvoKi> kisWeiß, List<EvoKi> kisSchwarz) {
        this.kisWeiß = kisWeiß;
        this.kisSchwarz = kisSchwarz;
        this.contest();
    }

    public void contest() {
        int cores = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(cores);

        try {
            List<Future<?>> futures = new ArrayList<>();

            for (EvoKi ki1 : this.kisWeiß) {
                for (EvoKi ki2 : this.kisSchwarz) {
                    EvoGame game = new EvoGame(ki1, ki2);
                    futures.add(executor.submit(game::startGame));
                }
            }

            for (Future<?> f : futures) {
                try {
                    f.get();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("Warten auf EvoGame wurde unterbrochen", e);
                } catch (ExecutionException e) {
                    throw new RuntimeException("Fehler in EvoGame", e.getCause());
                }
            }
        } finally {
            executor.shutdown();
        }

        System.out.println("Alle Spiele beendet.");
    }
}

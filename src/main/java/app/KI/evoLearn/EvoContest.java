package app.KI.evoLearn;

import java.util.ArrayList;
import java.util.List;

public class EvoContest {
    //Logik zur Durchführung der Contests zwischen den KIs der aktuellen Pop um Winrate(Fitness) zu bestimmen
     List<EvoKi> kisWeiß = new ArrayList<>();
     List<EvoKi> kisSchwarz = new ArrayList<>();

    public EvoContest(List<EvoKi> kisWeiß,List<EvoKi> kisSchwarz) {
        this.kisWeiß = kisWeiß;
        this.kisSchwarz = kisSchwarz;
        this.contest();
    }

    public void contest() {
        for (EvoKi ki1 : this.kisWeiß){
            for (EvoKi ki2 : this.kisSchwarz){
                EvoGame game = new EvoGame(ki1, ki2);
                game.start();
            }
        }
    }
}

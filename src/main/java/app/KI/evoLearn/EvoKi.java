package app.KI.evoLearn;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EvoKi {
    //Hilfsklasse zur verwalten der KI Objekte mit den Parametern für die BF
    Genom genom;
    @Getter
    Double winrate = 0.0;
    //addiert die Punktzahl aus final score der matches um zu sehen wer den höchsten avg erreicht
    int fitness = 0;
    List<Integer> matches = new ArrayList<Integer>();

    public EvoKi(Genom genom){
        this.genom = genom;
    }

    @Deprecated
    public void calculateWinrate(){
       winrate = ( matches.stream().collect(java.util.stream.Collectors.summingInt(Integer::intValue)))*1.0;
    }

}

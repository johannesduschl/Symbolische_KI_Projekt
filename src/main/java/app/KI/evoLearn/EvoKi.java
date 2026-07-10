package app.KI.evoLearn;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class EvoKi {
    //Hilfsklasse zur verwalten der KI Objekte mit den Parametern für die BF
    Genom genom;
    AtomicInteger winrate = new AtomicInteger(0);
    //addiert die Punktzahl aus final score der matches um zu sehen wer den höchsten avg erreicht
    AtomicInteger fitness = new AtomicInteger(0);
    List<Integer> matches = new ArrayList<Integer>();

    public EvoKi(Genom genom){
        this.genom = genom;
    }



}

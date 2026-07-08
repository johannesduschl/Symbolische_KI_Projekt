package app.KI.evoLearn;

public class EvoKi {
    //Hilfsklasse zur verwalten der KI Objekte mit den Parametern für die BF
Genom genom;
Double winrate = 0.0;
int[] matches = new int[100];

public EvoKi(Genom genom){
    this.genom = genom;
}
}

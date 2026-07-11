package app.KI;

import app.board.Board;

public interface Bewertungsfunktion {

    int evaluate(Board board);

    int pieceCount(Board board);

    boolean kingHasDirectEdgeSight(Board board);

    void debugEvaluation(Board board);

    //fürs Bestimmen der Fitness unserer KIs beim Evolutionären Lernen
    int getScore(Board board);
}

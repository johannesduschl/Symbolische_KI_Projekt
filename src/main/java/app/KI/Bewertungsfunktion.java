package app.KI;

import app.board.Board;

public interface Bewertungsfunktion {

    int evaluate(Board board);

    int pieceCount(Board board);

    boolean kingHasDirectEdgeSight(Board board);
}

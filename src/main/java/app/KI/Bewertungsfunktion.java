package app.KI;

import app.board.Board;

public interface Bewertungsfunktion {

    int evaluate(Board board, boolean isWhiteToMove);
}

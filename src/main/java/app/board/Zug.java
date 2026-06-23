package app.board;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Zug {

    char fromColumn;
    int fromRow;
    char toColumn;
    int toRow;
    char piece;

    int fromX, fromY;
    int toX, toY;

    @Override
    public String toString() {
        return "" + fromColumn + fromRow + " -> " + toColumn + toRow;
    }
}

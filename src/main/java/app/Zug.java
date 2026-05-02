package app;

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
}

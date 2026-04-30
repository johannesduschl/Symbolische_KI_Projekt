package app;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Zug {

    char fromColumn;
    char fromRow;
    char toColumn;
    char toRow;
}

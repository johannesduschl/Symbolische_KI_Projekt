package app.board;

import app.board.Zug;

public class UndoInfo {
    public Zug move;

    public boolean blackMovesNext;
    public char bewegt;
    public long zobristHash;

    public int fromX, fromY;
    public int toX, toY;

    public int[] capX = new int[4];
    public int[] capY = new int[4];
    public char[] captured = new char[4];
    public int capCount = 0;
}
package app.KI;

import app.board.Zug;

public class TTEntry {
    public static final byte EXACT = 0;   // exakter Score
    public static final byte LOWER = 1;   // Alpha-Cutoff (Lower Bound)
    public static final byte UPPER = 2;   // Beta-Cutoff  (Upper Bound)

    public long hash;      // Zobrist-Hash zur Kollisionsprüfung
    public int  score;
    public int  depth;
    public byte flag;
    public Zug bestMove;  // kodierter Zug, z.B. von-nach als int

    public TTEntry(long hash, int score, int depth, byte flag, Zug bestMove) {
        this.hash     = hash;
        this.score    = score;
        this.depth    = depth;
        this.flag     = flag;
        this.bestMove = bestMove;
    }
}

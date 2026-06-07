package app.KI;

import java.util.HashMap;

public class transpositionTable {
    private final TTEntry[] table;
    private final int size;

    // Größe als Zweierpotenz – Index per Bit-Maske, kein Modulo nötig
    public transpositionTable(int sizeBits) {
        this.size  = 1 << sizeBits;   // z.B. 22 → ~4 Mio. Slots ≈ 96 MB
        this.table = new TTEntry[size];
    }

    public void store(long hash, int score, int depth, byte flag, int bestMove) {
        int index = (int)(hash & (size - 1));
        TTEntry existing = table[index];

        //Todo erweitern auf TwoDeep
        // Ersetzungsstrategie aktuell Deep später erweitern auf TwoDeep
        if (existing == null || depth >= existing.depth) {
            table[index] = new TTEntry(hash, score, depth, flag, bestMove);
        }
    }

    /** Gibt null zurück wenn kein Treffer oder Hash-Kollision */
    public TTEntry lookup(long hash) {
        TTEntry entry = table[(int)(hash & (size - 1))];
        if (entry != null && entry.hash == hash) return entry;
        return null;
    }

    public void clear() {
        java.util.Arrays.fill(table, null);
    }

}

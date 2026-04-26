package app;

public class Zuggenerator {

    //Bitboards:
    long blackTop;
    int blackBottom;

    long whiteTop;
    int whiteBottom;

    long whiteKingTop;
    int whiteKingBottom;

    long restrictedFieldsTop;
    int restrictedFieldsBottom;

    /**
     * Maps the given index to the top or bottom part of the black bitboards
     * @param i index [0, 80]
     */
    private void setBlack(int i) {
        if (i < 64) blackTop |= (1L << i);
        else blackBottom |= (1 << (i - 64));
    }

    private void setWhite(int i) {
        if (i < 64) whiteTop |= (1L << i);
        else whiteBottom |= (1 << (i - 64));
    }

    private void setKing(int i) {
        if (i < 64) whiteKingTop |= (1L << i);
        else whiteKingBottom |= (1 << (i - 64));
    }

    private void setRestricted(int i) {
        if (i < 64) restrictedFieldsTop |= (1L << i);
        else restrictedFieldsBottom |= (1 << (i - 64));
    }

    private int idx(int r, int c) {
        return r * 9 + c;
    }

    public Zuggenerator() {

        //Init the Bitboards:
        setRestricted(idx(0,0));
        setRestricted(idx(0,8));
        setRestricted(idx(8,0));
        setRestricted(idx(8,8));
        setRestricted(idx(4,4));

        setKing(idx(4,4));

        int[][] white = {
                {2,4},{3,4},{4,2},{4,3},{4,5},{4,6},{5,4},{6,4}
        };

        for (int[] p : white) setWhite(idx(p[0], p[1]));

        int[][] black = {
                {0,3},{0,4},{0,5},
                {1,4},
                {3,0},{3,8},
                {4,0},{4,1},{4,7},{4,8},
                {5,0},{5,8},
                {7,4},
                {8,3},{8,4},{8,5}
        };

        for (int[] p : black) setBlack(idx(p[0], p[1]));
    }
}

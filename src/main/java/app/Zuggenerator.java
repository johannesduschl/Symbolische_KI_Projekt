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
    public void printBoard(){

        StringBuilder sb = new StringBuilder(9*9);

        sb.append(' ');
        for (char c = 'A'; c <= 'I'; c++) sb.append(' ').append(c);
        sb.append('\n');
        int r = 9;

        for (int i = 0; i<81; i++){
            if (i%9 == 0) sb.append(r--).append(' ');
            long helpTop = (i < 64) ? (1L << i ) :0;
            long helpBottom = (i >= 64) ? (1L << (i - 64)) :0;

            char c ='_';
            if ((i < 64 && (helpTop & blackTop) != 0)
                    || (i>= 64 && (helpBottom & blackBottom) != 0)) c = 'B';
            else if ((i < 64 && (helpTop & whiteTop) != 0)
                    || (i>= 64 && (helpBottom & whiteBottom) != 0)) c = 'W';
            else if ((i < 64 && (helpTop & whiteKingTop) != 0)
                    || (i>= 64 && (helpBottom & whiteKingBottom) != 0)) c = 'K';
            else if ((i < 64 && (helpTop & restrictedFieldsTop) != 0)
                    || (i>= 64 && (helpBottom & restrictedFieldsBottom) != 0)) c = 'X';
            sb.append(c);
            sb.append(' ');
            if ((i+1) % 9 == 0) sb.append('\n');
        }
        System.out.println(sb);
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

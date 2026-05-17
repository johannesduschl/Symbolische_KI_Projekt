package app;

import app.board.Board;
import app.board.Zug;
import app.board.Zuggenerator;

import java.util.Arrays;
import java.util.List;

import static java.lang.Math.abs;

public class Bewertungsfunktion {

    private static final boolean[][] BLOCKED = {
            { true, false, false, false, false, false, false, false, true },
            { false, false, false, false, false, false, false, false, false },
            { false, false, false, false, false, false, false, false, false },
            { false, false, false, false, false, false, false, false, false },
            { false, false, false, false, false, false, false, false, false },
            { false, false, false, false, false, false, false, false, false },
            { false, false, false, false, false, false, false, false, false },
            { false, false, false, false, false, false, false, false, false },
            { true, false, false, false, false, false, false, false, true }
    };

    private static final int[][] KING_PST = {
            {  99,   3,   3,   3,   3,   3,   3,   3,  99 },
            {   3,  -1,  -1,  -1,  -1,  -1,  -1,  -1,   3 },
            {   3,  -1,  -1,  -1,  -1,  -1,  -1,  -1,   3 },
            {   3,  -1,  -1,  -1,   1,  -1,  -1,  -1,   3 },
            {   3,  -1,  -1,   1,   2,   1,  -1,  -1,   3 },
            {   3,  -1,  -1,  -1,   1,  -1,  -1,  -1,   3 },
            {   3,  -1,  -1,  -1,  -1,  -1,  -1,  -1,   3 },
            {   3,  -1,  -1,  -1,  -1,  -1,  -1,  -1,   3 },
            {  99,   3,   3,   3,   3,   3,   3,   3,  99 }
    };

    private static final int[][] BLACK_PST = {
            // a  b  c  d  e  f  g  h  i
            {  0, 0, 0, 2, 2, 2, 0, 0,  0 }, // 9
            {  0, 0, 0, 2, 2, 2, 0, 0,  0 }, // 8
            {  0, 0, 0, 3, 2, 3, 0, 0,  0 }, // 7
            {  2, 2, 3, 4, 5, 4, 3, 2,  2 }, // 6
            {  2, 2, 2, 5, 0 ,5, 2, 2,  2 }, // 5
            {  2, 2, 3, 4, 5, 4, 3, 2,  2 }, // 4
            {  0, 0, 0, 3, 2, 3, 0, 0,  0 }, // 3
            {  0, 0, 0, 2, 2, 2, 0, 0,  0 }, // 2
            {  0, 0, 0, 2, 2, 2, 0, 0,  0 }  // 1
    };

    private static final int[][] WHITE_PST = {
            { 0, 0, 0, 1, 1, 1, 0, 0, 0 },
            { 0, 1, 1, 2, 2, 2, 1, 1, 0 },
            { 0, 1, 2, 3, 3, 3, 2, 1, 0 },
            { 1, 2, 3, 4, 4, 4, 3, 2, 1 },
            { 1, 2, 3, 4, 0, 4, 3, 2, 1 },
            { 1, 2, 3, 4, 4, 4, 3, 2, 1 },
            { 0, 1, 2, 3, 3, 3, 2, 1, 0 },
            { 0, 1, 1, 2, 2, 2, 1, 1, 0 },
            { 0, 0, 0, 1, 1, 1, 0, 0, 0 }
    };

    // =========================
    // HIGH-LEVEL WEIGHTS
    // =========================
    private static final int W_WHITE_GOAL = 1;
    private static final int W_BLACK_GOAL = 1;

    // =========================
    // WHITE FEATURE WEIGHTS
    // =========================
    private static final int W_KING_PROGRESS = 1;
    private static final int W_CORNER = 1;
    private static final int W_ESCAPE_PROGRESS = 5;

    // =========================
    // BLACK FEATURE WEIGHTS
    // =========================
    private static final int W_KING_THREAT = 10;
    private static final int W_MATERIAL_PRESSURE = 10;
    private static final int W_MOBILITY_PRESSURE = 5;


    public static int evaluateDebug(char[][] board) {

        int white = evaluateWhite(board);
        int black = evaluateBlack(board);

        int total = white - black;

        System.out.println("WHITE: " + white);
        System.out.println("BLACK: " + black);
        System.out.println("TOTAL: " + total);

        return total;
    }

    public static int evaluate(char[][] board) {

        int white = evaluateWhite(board);
        int black = evaluateBlack(board);

        return white - black;
    }

    private static int evaluateWhite(char[][] board) {

        return whitePST(board)
                + W_KING_PROGRESS * kingEscapeScore(board)
                + W_CORNER * cornerProgress(board);
    }

    private static int evaluateBlack(char[][] board) {

        return blackPST(board);
    }

    private static int[] findKing(char[][] board) {
        if (board[4][4] == 'k'){
            return new int[]{4, 4};
        }
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (board[i][j] == 'k') {
                    return new int[]{i, j};
                }
            }
        }
        return null;
    }

    /**          dx  dy
     * oben   = (-1, 0)
     * unten  = ( 1, 0)
     * links  = ( 0,-1)
     * rechts = ( 0, 1)
     *
     * Prüft, ob in einer Richtung des Königs bis zum Rand freie Bahn ist
     */
    private static boolean isPathClear(char[][] board, int x, int y, int dx, int dy) {

        x += dx;
        y += dy;

        while (x >= 0 && x < 9 && y >= 0 && y < 9) {
            if (board[x][y] != '-') {
                return false;
            }
            x += dx;
            y += dy;
        }
        return true;
    }

    private static int blackPST(char[][] board) {

        int score = 0;

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (BLOCKED[i][j]) continue;
                if (board[i][j] == 's') {
                    score += BLACK_PST[i][j];
                }
            }
        }

        return score;
    }

    private static int whitePST(char[][] board) {

        int score = 0;

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {

                char c = board[i][j];

                if (c == 'k') {
                    score += KING_PST[i][j];
                }

                if (c == 'w') {
                    score += WHITE_PST[i][j];
                }
            }
        }

        return score;
    }
    //Boni für freie Linien/Reihen und Nähe zum Rand
    private static int kingEscapeScore(char[][] board) {
        //TODO: Fine Tuning der Scores!
        int[] king = findKing(board);
        if (king == null) return -10000;

        int x = king[0];
        int y = king[1];
        int score = 0;

        // Gewinn?
        if (x == 0 || x == 8 || y == 0 || y == 8) {
            return 999;
        }
        // Freie Wege prüfen
        if (isPathClear(board, x, y, -1, 0)) score += 10; // oben
        if (isPathClear(board, x, y,  1, 0)) score += 10; // unten
        if (isPathClear(board, x, y,  0,-1)) score += 10; // links
        if (isPathClear(board, x, y,  0, 1)) score += 10; // rechts
        // Distanz zum Rand
        int distTop = x;
        int distBottom = 8 - x;
        int distLeft = y;
        int distRight = 8 - y;
        int minDist = Math.min(Math.min(distTop, distBottom), Math.min(distLeft, distRight));

        //4 da dies die im worst Case maximale Anzahl an Feldern ist, die der König vom Rand entfernt sein kann
        // (4,4) = Mitte
        score += (4 - minDist) * 2;

        return score;
    }


    //TODO: Teils redundant mit kingEscapeScore!
    //Keine Prüfung auf Blockaden durch Schwarz, reine Distanzmessung
    //Ersetzen durch vorgefertigte Tabelle möglich...
    private static int cornerProgress(char[][] board) {
        int[] k = findKing(board);
        if (k == null) return -10000;
        int bestProgress = 0;
        int[][] corners = {{0,0},{0,8},{8,0},{8,8}};

        for (int[] c : corners) {
            int dist = Math.abs(k[0]-c[0]) + Math.abs(k[1]-c[1]);
            int progress = 16 - dist; // 16 = worst case approx

            bestProgress = Math.max(bestProgress, progress);
        }

        return bestProgress;
    }

    private static int kingMobility(char[][] board) {
        int[] kingPos = findKing(board);
        if (kingPos == null) return -10000;
        Zuggenerator zg = new Zuggenerator();
        List<Zug> moves = zg.getPossibleMoves(board, kingPos[0], kingPos[1]);
        int mobility = moves.size();
        return mobility;
    }

    /**
     * Bewertet die Position aller Steine auf dem Spielfeld basierend auf der wichtgkeit der Position
     * abhängig vom aktuellen Spielfortschritt.
     * @param board
     * @return
     */
    public int steinPositionBasedOnSpielfortschritt(Board board){
        int score = 0;
        int wcount = 0;
        int scount = 0;
        double spielfortschritt = spielfortschritt(board);
        spielfortschritt = (spielfortschritt-0.5) *2; //rechnet zu parameter von -1 bis 1 für lineartransformation um
        //wichtig! Eckfelder müssen für König immer hohen wert haben, hier in wertung 0 da externe Königspositionsbewertung das bereits auf max legt
        int[][] bewertungsBoard = {
                { 0,  8,  7,  3,  2,  3,  7,  8,  0},
                { 8,  7,  5,  2,  1,  2,  5,  7,  8},
                { 7,  5,  1, -1, -3, -1,  1,  5,  7},
                { 3,  2, -3, -6, -6, -6, -3,  2,  3},
                { 2,  1, -3, -6,-10, -6, -3,  1,  2},
                { 3,  2, -3, -6, -6, -6, -3,  2,  3},
                { 7,  5,  1, -1, -3, -1,  1,  5,  7},
                { 8,  7,  5,  2,  1,  2,  5,  7,  8},
                { 0,  8,  7,  3,  2,  3,  7,  8,  0}
        };; //a1 unten links i9 oben rechts
        double[][] transformiertesBoard = new double[9][9];

        // 3. Über das Array iterieren und Transformation anwenden
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                transformiertesBoard[r][c] = bewertungsBoard[r][c] * spielfortschritt;
            }
        }
        for(int i=0;i<9;i++){
            for(int j=0;j<9;j++){
                if(board.getBoard()[i][j]=='w'){
                    wcount+= transformiertesBoard[i][j];
                }
                if(board.getBoard()[i][j]=='s'){
                    scount+= transformiertesBoard[i][j];
                }
            }
        }


        return score;
    }

    public double spielfortschritt(Board board){
        double score = 0;
        char[][] startBoard = new char[][]{
                { 'x','-','-','s','s','s','-','-','x' },
                { '-','-','-','-','s','-','-','-','-' },
                { '-','-','-','-','w','-','-','-','-' },
                { 's','-','-','-','w','-','-','-','s' },
                { 's','s','w','w','k','w','w','s','s' },
                { 's','-','-','-','w','-','-','-','s' },
                { '-','-','-','-','w','-','-','-','-' },
                { '-','-','-','-','s','-','-','-','-' },
                { 'x','-','-','s','s','s','-','-','x' }
        }; //a1 unten links i9 oben rechts

        //Abweichung von Spielfeld zu Startfeld
        //max Abweichungen 81
        int abweichung = 0;
        for (int i = 0; i < startBoard.length; i++) {
            for (int j = 0; j < startBoard[i].length; j++) {
                if (startBoard[i][j] != board.getBoard()[i][j]) {
                    abweichung++;
                }
            }
        }
        //Anzahl Figuren
        //max Figuren 25
        int figuren = 0;
        figuren += count('w', Arrays.toString(board.getBoard()));
        figuren += count('s', Arrays.toString(board.getBoard()));
        figuren += count('k', Arrays.toString(board.getBoard()));

        //Fortschritt König
        //max value 8 min value 0
        int königFortschritt = 0;
        int[] kcords= findCharPosition(board.getBoard(), 'k');
        königFortschritt = abs(4-kcords[0]) + abs(4-kcords[1]);

        //
        score = (abweichung*0.005)+(königFortschritt*0.05)+(0.25-(0.01*figuren));
        return score;
    }
    public int count(char c, String s) {
        int count = 0;
        for(int i=0;i<s.length();i++){
            if(s.charAt(i)==c) count++;
        }
        return count;
    }
    /**
     * findet die Koordinaten eines Char auf dem Spielfeld
     */
    int[] findCharPosition(char[][] board, char target) {
        if (target == 'k') {
            if (board[4][4] == 'k') { //Königsfeld sehr wahrscheinlich
                return new int[]{4, 4};
            }
            for (int i = 0; i < board.length; i++) {
                for (int j = 0; j < board[i].length; j++) {
                    if (board[i][j] == 'k') {
                        return new int[]{i, j};
                    }
                }
            }

            return null; //möglicherweise Problem für alpha beta?
        }

        // worst-case: alle Steine einer Farbe vorhanden (8/16 Stück)
        int maxPieces = (target == 's') ? 16 : (target == 'w') ? 8 : 1;
        int[] temp = new int[maxPieces * 2]; //(x,y) Koordinaten pro Stein
        int index = 0;

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] == target) {
                    temp[index++] = i;
                    temp[index++] = j;
                }
            }
        }
        return java.util.Arrays.copyOf(temp, index); //reservierten 0-Indizes abschneiden
    }

}
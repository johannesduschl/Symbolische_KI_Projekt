package app.KI;

import app.board.Board;
import app.board.Zug;
import app.board.Zuggenerator;

import java.util.Arrays;
import java.util.List;

import static java.lang.Math.PI;
import static java.lang.Math.abs;

public class BewertungsfunktionImpl implements Bewertungsfunktion {

    private final char[][] debug_board = new char[][]{
            { 'k','w','-','-','-','w','-','-','x' },
            { 'w','w','-','-','-','w','-','-','-' },
            { '-','-','-','-','-','w','-','-','-' },
            { '-','-','-','-','-','w','-','-','-' },
            { 'w','w','w','w','w','w','-','-','-' },
            { '-','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','-' },
            { '-','-','-','-','-','-','-','-','-' },
            { 'x','-','-','-','-','-','-','-','x' }
    };

    private static final boolean[][] BLOCKED = {
            {  true, false, false, false, false, false, false, false,  true },
            { false, false, false, false, false, false, false, false, false },
            { false, false, false, false, false, false, false, false, false },
            { false, false, false, false, false, false, false, false, false },
            { false, false, false, false,  true, false, false, false, false },
            { false, false, false, false, false, false, false, false, false },
            { false, false, false, false, false, false, false, false, false },
            { false, false, false, false, false, false, false, false, false },
            {  true, false, false, false, false, false, false, false,  true }
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
            {  0, 0, 0, 2, 2, 2, 0, 0,  0 },
            {  0, 0, 0, 2, 2, 2, 0, 0,  0 },
            {  0, 0, 0, 3, 2, 3, 0, 0,  0 },
            {  2, 2, 3, 4, 5, 4, 3, 2,  2 },
            {  2, 2, 2, 5, 0 ,5, 2, 2,  2 },
            {  2, 2, 3, 4, 5, 4, 3, 2,  2 },
            {  0, 0, 0, 3, 2, 3, 0, 0,  0 },
            {  0, 0, 0, 2, 2, 2, 0, 0,  0 },
            {  0, 0, 0, 2, 2, 2, 0, 0,  0 }
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

    private static boolean onThrone = true;

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
    private static final int W_KING_MOBILITY = 1;
    private static final int W_KING_SAFETY = 2;

    // =========================
    // BLACK FEATURE WEIGHTS
    // =========================
    private static final int W_KING_THREAT = 3;
    private static final int W_MATERIAL_PRESSURE = 10;
    private static final int W_MOBILITY_PRESSURE = 5;

    // =========================
    // PIECE SQUARES
    // =========================
    private static int[] kingSquare;
    private static int[] whiteSquares;
    private static int[] blackSquares;

    private static int test = 0;
    @Override
    public int evaluate(Board board) {
        kingSquare = findCharPosition(board.getBoard(), 'k');
        whiteSquares = findCharPosition(board.getBoard(), 'w');
        blackSquares = findCharPosition(board.getBoard(), 's');

        int white = evaluateWhite(board.getBoard());
        int black = evaluateBlack(board.getBoard());

        return white - black;
    }

    public int evaluateDebug(char[][] board) {

        int white = evaluateWhite(board);
        int black = evaluateBlack(board);
        int total = white - black;

        System.out.println("WHITE: " + white);
        System.out.println("BLACK: " + black);
        System.out.println("TOTAL: " + total);

        return total;
    }

    private int evaluateWhite(char[][] board) {

        return whitePST(board)
                + W_KING_PROGRESS * kingEscapeScore(board)
                + W_CORNER * cornerProgress(board)
                + W_KING_MOBILITY * kingMobility(board)
                + W_KING_SAFETY * kingSafety(board);
    }

    private int evaluateBlack(char[][] board) {

        return blackPST(board)
                + W_KING_THREAT * kingThreat(board);
    }

    /**
     * oben   = (-1, 0)
     * unten  = ( 1, 0)
     * links  = ( 0,-1)
     * rechts = ( 0, 1)
     *
     * Prüft, ob in einer Richtung des Königs bis zum Rand freie Bahn ist
     */
    private boolean isPathClear(char[][] board, int x, int y, int dx, int dy) {

        x += dx;
        y += dy;

        while (x >= 0 && x < 9 && y >= 0 && y < 9) {

            if (!BLOCKED[x][y]) {
                if (board[x][y] != '-') {
                    return false;
                }
            }

            x += dx;
            y += dy;
        }

        return true;
    }
    // Determines whether movement along a direction is blocked by other pieces.
    private boolean isSquareRestricted(char[][] board, int x, int y, int dx, int dy){

        x += dx;
        y += dy;

        while (x >= 0 && x < 9 && y >= 0 && y < 9) {

            if (!BLOCKED[x][y]) {
                if (board[x][y] != '-') {
                    return true;
                }
            }

            x += dx;
            y += dy;
        }

        return false;
    }

    private boolean isSquareThreatenedBy(char[][] board, char attackingPiece, int x, int y, int dx, int dy ){

        //TODO
        x += dx;
        y += dy;

        while (x >= 0 && x < 9 && y >= 0 && y < 9) {
            if (board[x][y] == attackingPiece) {
                return true;
            }

            x += dx;
            y += dy;
        }

        return false;
    }

    private boolean canAnyPieceInSourceReachTarget(char[][] board, char PieceType, int dx, int dy,
                                                   int sourceMinX, int sourceMaxX, int sourceMinY, int sourceMaxY,
                                                   int targetMinX, int targetMaxX, int targetMinY, int targetMaxY
                                                   ){
        int[] PieceSquares = switch (PieceType) {
            case 'k' -> kingSquare;
            case 'w' -> whiteSquares;
            case 's' -> blackSquares;
            default -> throw new IllegalArgumentException("Unknown PieceType: " + PieceType);
        };

        for (int i = 0; i < PieceSquares.length; i += 2){
            int x = PieceSquares[i];
            int y = PieceSquares[i+1];
            if (x >= targetMinX && x <= targetMaxX && y >= targetMinY && y <= targetMaxY) {
                if(canPieceReachTarget(board, x, y, dx, dy,
                        targetMinX, targetMaxX, targetMinY, targetMaxY)){
                    return true;
                }
            }
        }

        return false;
    }
    //TODO:Reduce duplicate code...
    private boolean canPieceReachTarget(char[][] board, int x, int y, int dx, int dy,
                                        int targetMinX, int targetMaxX, int targetMinY, int targetMaxY){
        if(dy == 0){
            //is y not in range of targetMinY and targetMaxY
            if(y < targetMinY || y > targetMaxY){
                return false;
            }
        }else{
            y += dy;

            while (y >= 0 && y < 9){
                if (board[x][y] != '-') {
                    return false;
                }

                if (y >= targetMinY && y <= targetMaxY){
                    return true;
                }

                y += dy;
            }
        }

        if(dx == 0){
            if(x < targetMinX || x > targetMaxX){
                return false;
            }
        }else{
            x += dx;
            while (x >= 0 && x < 9) {
                if (board[x][y] != '-') {
                    return false;
                }
                if (x >= targetMinX && x <= targetMaxX){
                    return true;
                }
                x += dx;
            }
        }

        return false;
    }
    /**
     * oben   = (-1, 0)
     * unten  = ( 1, 0)
     * links  = ( 0,-1)
     * rechts = ( 0, 1)
     *
     */
    private int countMoves(char[][] board, int x, int y, int dx, int dy){

        int moves = 0;
        x += dx;
        y += dy;

        while (x >= 0 && x < 9 && y >= 0 && y < 9) {
            if(BLOCKED[x][y]){
                x += dx;
                y += dy;
                continue;
            }

            if(board[x][y] != '-'){
                break;
            }
            moves++;
            x += dx;
            y += dy;
        }
        return moves;
    }

    //TODO:delete debug function drawLine()
    private void drawLine(char[][] board, char target, int squares,
                          int x, int y, int dx, int dy) {

        int steps = 0;

        while (x >= 0 && x < board.length &&
                y >= 0 && y < board.length &&
                steps <= squares) {

            if (board[x][y] == '-') {
                board[x][y] = target;
            }

            x += dx;
            y += dy;
            steps++;
        }
    }

    private int countPieces(char[][] board, char target, int squares,
                            int x, int y, int dx, int dy) {

        int pieces = 0;
        int steps = 0;

        while (x >= 0 && x < 9 && y >= 0 && y < 9 && steps <= squares) {

            if (board[x][y] == target) {
                pieces++;
            }

            x += dx;
            y += dy;
            steps++;
        }

        return pieces;
    }
    /**
     * oben   = (-1, 0)
     * unten  = ( 1, 0)
     * links  = ( 0,-1)
     * rechts = ( 0, 1)
     *
     */
    private int countRadius(char[][] board, char target, int radius, int x, int y){

        //RADIUS: just use corners as reference points for loops
        //corners might be outside the board -> problem!
        //Calculate the closest legal corner and ignore blocked (x) fields, just use blocked pst afterwards
        //for debug purposes, generate and print board as pieces for radius

        //topLeft:     (x - radius, y - radius)
        //if x < 0: ignoreTopRow = true and x(topLeft) = 0
        //if y < 0: ignoreLeftColumn = true and y(topLeft) = 0
        //topLeft = (x,y)

        //topRight:    (x - radius, y + radius)
        //if x < 0: ignoreTopRow = true and x(topRight) = 0
        //if y > 8: ignoreRightColumn= true and y(topRight) = 8
        //topRight = (x,y)

        //bottomLeft:  (x + radius, y - radius)
        //if x > 8: ignoreBottomRow = true and x(bottomLeft) = 8
        //if y < 0: ignoreLeftColumn = true and y(bottomLeft) = 0
        //bottomLeft = (x,y)

        //bottomRight: (x + radius, y + radius)
        //if x > 8: ignoreBottomRow = true and x(bottomRight) = 8
        //if y > 8: ignoreRightColumn = true and y(bottomRight) = 8
        //bottomRight = (x,y)

        //topRow:
        //from topRight to topLeft count pieces
        //use y(topRight) - y(topLeft) to get number of fields for countPieces() function
        //start at topLeft and add numberOfFields -> rechts = ( 0, 1)
        //bottomRow:
        //from bottomRight to bottomLeft count pieces
        //use y(bottomRight) - y(bottomLeft) to get number of fields for countPieces() function
        //start at bottomLeft and add numberOfFields -> rechts = ( 0, 1)
        //leftColumn:
        //from bottomLeft to topLeft count pieces
        //use x(bottomLeft) - x(topLeft) to get number of fields for countPieces() function
        //start at topLeft and add numberOfFields -> unten = (1, 0)
        //rightColumn:
        //from topRight to bottomRight count pieces
        //use x(bottomRight) - x(topRight) to get number of fields for countPieces() function
        //start at topRight and add numberOfFields -> unten = (1, 0)

        int count = 0;
        int max = board.length - 1;

        int rawLeft = y - radius;
        int rawRight = y + radius;
        int rawTop = x - radius;
        int rawBottom = x + radius;

        boolean ignoreTopRow = rawTop < 0;
        boolean ignoreBottomRow = rawBottom > max;
        boolean ignoreLeftColumn = rawLeft < 0;
        boolean ignoreRightColumn = rawRight > max;

        int left = Math.max(0, Math.min(max, y - radius));
        int right = Math.max(0, Math.min(max, y + radius));
        int top = Math.max(0, Math.min(max, x - radius));
        int bottom = Math.max(0, Math.min(max, x + radius));

        int rowLength = right - left;
        int columnLength = bottom - top;

        //TODO:Delete debug stuff
        //Mögliches Problem: Ecken werden doppelt gezählt! --> FIXED!!!
        if(!ignoreTopRow){
            count += countPieces(board, target, rowLength, top, left, 0, 1);
            //fix counting corners twice:
            columnLength--;
            top++;
        }
        if(!ignoreBottomRow){
            count += countPieces(board, target, rowLength, bottom, left, 0, 1);
            //fix counting corners twice:
            columnLength--;

        }
        if(!ignoreLeftColumn){
            count += countPieces(board, target, columnLength, top, left, 1, 0);
        }
        if(!ignoreRightColumn){
            count += countPieces(board, target, columnLength, top, right, 1, 0);
        }

        return count;
    }

    private int blackPST(char[][] board) {

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

    private int whitePST(char[][] board) {

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
    private int kingEscapeScore(char[][] board) {

        if (kingSquare == null) return -10000;

        int x = kingSquare[0];
        int y = kingSquare[1];
        int score = 0;

        if (x == 0 || x == 8 || y == 0 || y == 8) {
            return 999;
        }

        if (isPathClear(board, x, y, -1, 0)) score += 10;
        if (isPathClear(board, x, y,  1, 0)) score += 10;
        if (isPathClear(board, x, y,  0,-1)) score += 10;
        if (isPathClear(board, x, y,  0, 1)) score += 10;

        int distTop = x;
        int distBottom = 8 - x;
        int distLeft = y;
        int distRight = 8 - y;
        int minDist = Math.min(Math.min(distTop, distBottom), Math.min(distLeft, distRight));

        score += (4 - minDist) * 2;

        return score;
    }

    //TODO: Teils redundant mit kingEscapeScore!
    //Keine Prüfung auf Blockaden durch Schwarz, reine Distanzmessung
    //Ersetzen durch vorgefertigte Tabelle möglich...
    private int cornerProgress(char[][] board) {

        if (kingSquare == null) return -10000;

        int bestProgress = 0;
        int[][] corners = {{0,0},{0,8},{8,0},{8,8}};

        for (int[] c : corners) {

            int dist = Math.abs(kingSquare[0]-c[0]) + Math.abs(kingSquare[1]-c[1]);
            int progress = 16 - dist;

            bestProgress = Math.max(bestProgress, progress);
        }

        return bestProgress;
    }

    private int kingMobility(char[][] board) {
        //TODO: fixing inefficient Zuggenerator generating for every call
        //IMPORTANT: Throne is safe therefore mobility less important

        int moves = 0;
        if (kingSquare == null) return -10000;
        int x = kingSquare[0];
        int y = kingSquare[1];

        if(!onThrone){
            //more Moves make him vulnerable too!

            //apply this logic by rewarding black for (in-)direct view to the king
            //keep it balanced with kingSafety -> more white pieces around are better
            moves += countMoves(board, x, y, -1, 0);
            moves += countMoves(board, x, y,  1, 0);
            moves += countMoves(board, x, y,  0,-1);
            moves += countMoves(board, x, y,  0, 1);
        }
        return moves;
    }


    //IDEA: reward white for having white pieces around the kind
    //the closer to the king the better
    //QUESTION: possible to move a smaller PST within board and arrange it to the king?
    //-> bad idea, just use sth like countMoves but for general purposes
    private int kingSafety(char[][] board){
        int score = 0;
        if (kingSquare == null) return -10000;
        int x = kingSquare[0];
        int y = kingSquare[1];
        //Use multiplier for nearer pieces
        score += 2 * countRadius(board, 'w', 1, x, y);
        score +=     countRadius(board, 'w', 2, x, y);
        return score;
    }

    private int kingThreat(char[][] board){
        int score = 0;
        if (kingSquare == null) return +10000;
        int x = kingSquare[0];
        int y = kingSquare[1];
        score += 2 * countRadius(board, 's', 1, x, y);
        score +=     countRadius(board, 's', 2, x, y);
        return score;
    }


    public static void printBoard(char[][] board) {
        if (board == null || board.length == 0 || board[0].length == 0) {
            System.out.println("Board ist leer oder null");
            return;
        }

        StringBuilder sb = new StringBuilder();

        sb.append("  ");
        for (int c = 0; c < board[0].length; c++) {
            sb.append((char) ('A' + c)).append(' ');
        }

        for (int y = 0; y < board.length; y++) {
            sb.append('\n');
            sb.append(board.length - y).append(' ');

            for (int x = 0; x < board[0].length; x++) {
                sb.append(board[y][x]).append(' ');
            }
        }

        sb.append('\n');
        System.out.println(sb.toString());
    }

    /**
     * Bewertet die Position aller Steine auf dem Spielfeld basierend auf der wichtgkeit der Position
     * abhängig vom aktuellen Spielfortschritt.
     */
    public int steinPositionBasedOnSpielfortschritt(Board board){

        int score = 0;
        int wcount = 0;
        int scount = 0;

        double spielfortschritt = spielfortschritt(board);
        spielfortschritt = (spielfortschritt-0.5) *2;

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
        };

        double[][] transformiertesBoard = new double[9][9];

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
        };

        int abweichung = 0;

        for (int i = 0; i < startBoard.length; i++) {
            for (int j = 0; j < startBoard[i].length; j++) {
                if (startBoard[i][j] != board.getBoard()[i][j]) {
                    abweichung++;
                }
            }
        }

        int figuren = 0;
        figuren += count('w', Arrays.toString(board.getBoard()));
        figuren += count('s', Arrays.toString(board.getBoard()));
        figuren += count('k', Arrays.toString(board.getBoard()));

        int königFortschritt = 0;
        int[] kcords= findCharPosition(board.getBoard(), 'k');

        königFortschritt = abs(4-kcords[0]) + abs(4-kcords[1]);

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
    private static int[] findCharPosition(char[][] board, char target) {

        if (target == 'k') {

            if (board[4][4] == 'k') {
                return new int[]{4, 4};
            }

            for (int i = 0; i < board.length; i++) {
                for (int j = 0; j < board[i].length; j++) {
                    if (board[i][j] == 'k') {
                        return new int[]{i, j};
                    }
                }
            }

            return null;
        }

        int maxPieces = (target == 's') ? 16 : (target == 'w') ? 8 : 1;
        int[] temp = new int[maxPieces * 2];
        int index = 0;

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] == target) {
                    temp[index++] = i;
                    temp[index++] = j;
                }
            }
        }

        return Arrays.copyOf(temp, index);
    }
}
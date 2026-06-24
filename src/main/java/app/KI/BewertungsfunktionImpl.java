package app.KI;

import app.board.Board;
import app.board.Zug;
import app.board.Zuggenerator;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static java.lang.Math.PI;
import static java.lang.Math.abs;

public class BewertungsfunktionImpl implements Bewertungsfunktion {

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

    private static final boolean[][] BLOCKED_KING = {
            { false, false, false, false, false, false, false, false, false },
            { false, false, false, false, false, false, false, false, false },
            { false, false, false, false, false, false, false, false, false },
            { false, false, false, false, false, false, false, false, false },
            { false, false, false, false,  true, false, false, false, false },
            { false, false, false, false, false, false, false, false, false },
            { false, false, false, false, false, false, false, false, false },
            { false, false, false, false, false, false, false, false, false },
            { false, false, false, false, false, false, false, false, false }
    };

    private static final int[][] KING_PST = {
            {  99,   4,   3,   3,   3,   3,   3,   4,  99 },
            {   4,   2,   1,   1,   1,   1,   1,   2,   4 },
            {   3,   1,  -1,  -1,  -1,  -1,  -1,   1,   3 },
            {   3,   1,  -1,  -1,  -3,  -1,  -1,   1,   3 },
            {   3,   1,  -1,  -3,   4,  -3,  -1,   1,   3 },
            {   3,   1,  -1,  -1,  -3,  -1,  -1,   1,   3 },
            {   3,   1,  -1,  -1,  -1,  -1,  -1,   1,   3 },
            {   4,   2,   1,   1,   1,   1,   1,   2,   4 },
            {  99,   4,   3,   3,   3,   3,   3,   4,  99 }
    };

    private static int[][] BLACK_PST = {
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

    private static int[][] WHITE_PST = {
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

    private static int[][] BLACK_PST_THREAT = new int[9][9];

    private static int[][] WHITE_PST_THREAT = new int[9][9];

    private static final int[][] BLACK_PATTERN = {
            {4, 5, 4},
            {0, 3, 2, 3, 0},
            {0, 0, 2, 2, 2, 0, 0},
            {0, 0, 0, 2, 2, 2, 0, 0, 0},
            {0, 0, 0, 0, 2, 2, 2, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 2, 2, 2, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 2, 2, 2, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 2, 2, 2, 0, 0, 0, 0, 0, 0, 0}
    };

    private static final int[][] WHITE_PATTERN = {
            {4, 4, 4},
            {2, 3, 3, 3, 2},
            {1, 1, 2, 2, 2, 1, 1},
            {0, 0, 0, 1, 1, 1, 0, 0, 0},
            {0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0}
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
    private static final int W_KING_MOBILITY = 1;
    private static final int W_WHITE_MATERIAL = 1;
    private static final int W_WHITE_PST = 1;
    private static final int W_WHITE_PST_THREAT = 1;

    // =========================
    // BLACK FEATURE WEIGHTS
    // =========================
    private static final int W_EDGES_SECURE_SCORE = 1;
    private static final int W_EDGES_ACCESS_BLOCKED = 1;
    private static final int W_CHECKMATE_SCORE = 1;
    private static final int W_BLACK_MATERIAL = 1;
    private static final int W_BLACK_PST = 1;
    private static final int W_BLACK_PST_THREAT = 1;

    // =========================
    // PIECE SQUARES
    // =========================
    private static int[] kingSquare;
    private static int[] whiteSquares;
    private static int[] blackSquares;
    private static boolean[] isBlackOnColumn = new boolean[9];
    private static boolean[] isBlackOnRow = new boolean[9];

    private static boolean[] isWhiteOnColumn = new boolean[9];
    private static boolean[] isWhiteOnRow = new boolean[9];

    private static int[] blackAndWhiteRows;
    private static int[] blackAndWhiteColumns;

    @Getter
    private static int kingMoves;
    @Setter
    private static boolean onThrone = true;


    public int[] getWhiteSquares() {
        return whiteSquares;
    }

    public int[] getBlackSquares() {
        return blackSquares;
    }

    public int[] getKingSquare() {
        return kingSquare;
    }

    public boolean getOnThrone(){
        return onThrone;
    }

    @Override
    public int evaluate(Board board) {
        kingSquare = findCharPosition(board.getBoard(), 'k');
        whiteSquares = findCharPosition(board.getBoard(), 'w');
        blackSquares = findCharPosition(board.getBoard(), 's');

        resetPST(BLACK_PST_THREAT);
        resetPST(WHITE_PST_THREAT); //not necessary if per move updates will work

        initArrays();
        initThreatPST(board.getBoard(), BLACK_PST_THREAT, whiteSquares, 's');
        initThreatPST(board.getBoard(), WHITE_PST_THREAT, blackSquares, 'w');

        //TODO: move specific PST updates
        if(BLACK_PST_THREAT == null){
            //init
        }

        if(WHITE_PST_THREAT == null){
            //init
        }

        if(onThrone){
            if(kingSquare[0] != 4 || kingSquare[1] != 4){
                onThrone = false;
            }
        }

        kingMoves = kingMoves(board.getBoard(), kingSquare[0], kingSquare[1]);

        if(!onThrone){
            if(board.getBewegt() == 'k') {
                BLACK_PST = createPSTFromPattern(BLACK_PATTERN, kingSquare[0], kingSquare[1]);
                WHITE_PST = createPSTFromPattern(WHITE_PATTERN, kingSquare[0], kingSquare[1]);
            }
        }

        int white = evaluateWhite(board.getBoard());
        int black = evaluateBlack(board.getBoard());

        return white - black;
    }

    private int evaluateWhite(char[][] board) {

        return whitePST(board) //also includes threat pst
                + W_WHITE_MATERIAL * whiteMaterial()
                + W_KING_PROGRESS * kingEscapeScore(board)
                //+ W_CORNER * cornerProgress(board) //already included in kingEscapeScore -> delete!
                + W_KING_MOBILITY * kingMobility(); //might lead to too unsafe king moves -> check amount of black pieces first, then reward...
    }

    private int evaluateBlack(char[][] board) {

        return blackPST(board) //also includes threat pst
                + W_BLACK_MATERIAL * blackMaterial()
                + W_EDGES_SECURE_SCORE * edgesSecureScore(board) //simplify those functions to be more efficient
                + W_EDGES_ACCESS_BLOCKED * edgesAccessBlocked(board)  //over-engineering might be counter-intuitive!
                + W_CHECKMATE_SCORE * checkmateScore(board);
    }
    //might be useful in the future for faster computing
    public static int[] sortByAxis(int[] data, boolean sortByX) {

        int n = data.length / 2;

        Integer[] indices = new Integer[n];
        for (int i = 0; i < n; i++) {
            indices[i] = i;
        }

        // Auswahl der Vergleichsachse
        if (sortByX) {
            Arrays.sort(indices, Comparator.comparingInt(i -> data[2 * i]));
        } else {
            Arrays.sort(indices, Comparator.comparingInt(i -> data[2 * i + 1]));
        }

        int[] sorted = new int[data.length];

        for (int i = 0; i < n; i++) {
            int idx = indices[i];
            sorted[2 * i]     = data[2 * idx];
            sorted[2 * i + 1] = data[2 * idx + 1];
        }

        return sorted;
    }

    public int pieceCount(Board board) {
        int count = 0;
        char[][] b = board.getBoard();

        for (int i = 0; i < b.length; i++) {
            for (int j = 0; j < b[i].length; j++) {
                char c = b[i][j];
                if (c == 'w' || c == 's' || c == 'k') count++;
            }
        }
        return count;
    }

    public boolean kingCanMove(Board board) {
        int[] king = findCharPosition(board.getBoard(), 'k');
        if (king == null) return false;
        return kingMoves(board.getBoard(), king[0], king[1]) > 0;
    }

    public boolean kingHasDirectEdgeSight(Board board) {
        int[] king = findCharPosition(board.getBoard(), 'k');
        if (king == null) return false;

        int x = king[0];
        int y = king[1];
        char[][] b = board.getBoard();

        return !isSquareRestricted(b, x, y, -1, 0)
                || !isSquareRestricted(b, x, y,  1, 0)
                || !isSquareRestricted(b, x, y,  0,-1)
                || !isSquareRestricted(b, x, y,  0, 1);
    }

    private void initArrays(){

        Arrays.fill(isBlackOnRow, false);
        Arrays.fill(isBlackOnColumn, false);
        Arrays.fill(isWhiteOnRow, false);
        Arrays.fill(isWhiteOnColumn, false);

        List<Integer> tempRows = new ArrayList<>();
        List<Integer> tempCols = new ArrayList<>();

        for (int i = 0; i < blackSquares.length - 1; i += 2){
            int row = blackSquares[i];
            int col = blackSquares[i + 1];
            isBlackOnRow[row] = true;
            isBlackOnColumn[col] = true;
        }

        for (int i = 0; i < whiteSquares.length - 1; i += 2){
            int row = whiteSquares[i];
            int col = whiteSquares[i + 1];
            isWhiteOnRow[row] = true;
            isWhiteOnColumn[col] = true;
        }

        for (int i = 0; i < 9; i++){
            if(isBlackOnRow[i] && isWhiteOnRow[i]){
                tempRows.add(i);
            }

            if(isBlackOnColumn[i] && isWhiteOnColumn[i]){
                tempCols.add(i);
            }
        }
        blackAndWhiteRows = tempRows.stream().mapToInt(Integer::intValue).toArray();
        blackAndWhiteColumns = tempCols.stream().mapToInt(Integer::intValue).toArray();
    }

    private void resetPST(int[][] pst) {
        for (int i = 0; i < pst.length; i++) {
            Arrays.fill(pst[i], 0);
        }
    }

    // =========================
    // WHITE EVAL FUNCTIONS
    // =========================
    private int whitePST(char[][] board) {

        int score = 0;

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {

                char c = board[i][j];

                if (c == 'k') {
                    score += KING_PST[i][j];
                    //TODO: fix threat pst for king (included in white pieces)
                }

                if (c == 'w') {
                    score += W_WHITE_PST * WHITE_PST[i][j];
                    score += W_WHITE_PST_THREAT * WHITE_PST_THREAT[i][j];
                }
            }
        }
        return score;
    }

    private int whiteMaterial(){
        return whiteSquares.length; //one white piece give two points, division unnecessary
    }

    private int kingEscapeScore(char[][] board) {

        if (kingSquare == null) return -10000;

        int x = kingSquare[0];
        int y = kingSquare[1];
        int score = 0;

        if (x == 0 || x == 8 || y == 0 || y == 8) {
            return 999;
        }

        if (!isSquareRestricted(board, x, y, -1, 0)) score += 2;
        if (!isSquareRestricted(board, x, y,  1, 0)) score += 2;
        if (!isSquareRestricted(board, x, y,  0,-1)) score += 2;
        if (!isSquareRestricted(board, x, y,  0, 1)) score += 2;

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

    private int kingMobility() {
        //IMPORTANT: Throne is safe therefore mobility less important
        //Mobility makes white play too risky and unsafe!
        int score = 0;
        int multiplier;

        if(!onThrone){
            int blackCount = blackSquares.length / 2;

            //more Moves make him vulnerable too!

            //apply this logic by rewarding black for (in-)direct view to the king
            //keep it balanced with kingSafety -> more white pieces around are better

            //MANY
            if(blackCount <= 16 && blackCount > 12){
                multiplier = 1;
                //SOME
            }else if(blackCount <= 12 && blackCount > 6){
                multiplier = 2;
                //FEW
            }else{
                multiplier = 3;
            }

            //MANY
            if(kingMoves <= 16 && kingMoves > 11){
                score += multiplier * 4;
            //SOME
            }else if(kingMoves <= 11 && kingMoves > 6){
                score += multiplier * 2;
            //FEW
            }else{
                score += multiplier;
            }


        }

        return score;
    }


    // =========================
    // BLACK EVAL FUNCTIONS
    // =========================
    private int blackPST(char[][] board) {

        int score = 0;

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (BLOCKED[i][j]) continue;
                if (board[i][j] == 's') {
                    score += W_BLACK_PST * BLACK_PST[i][j];
                    score += W_BLACK_PST_THREAT * BLACK_PST_THREAT[i][j];
                }
            }
        }
        return score;
    }

    private void fillThreatPST(char[][] board, int[][] PST, char PieceType, boolean trapped, int x, int y, int dx, int dy){
        x += dx;
        y += dy;

        while (x >= 0 && x < 9 && y >= 0 && y < 9) {
                if(board[x][y] == PieceType){
                    if(trapped){
                        PST[x][y] += 2;
                    }else{
                        PST[x][y] += 1;
                    }
                    break;
                }
            x += dx;
            y += dy;
        }
    }
    //generate ThreatPST for white/black pieces
    //use whiteSquares for black threat PST and vise versa!
    //TODO: if possible, only change PST where move effects rows/columns
    private void initThreatPST(char[][] board, int[][] PST, int[] Squares, char PieceType){
        for (int i = 0; i < Squares.length - 1; i += 2){
            int x = Squares[i];
            int y = Squares[i + 1];

            //check all rows:
            for (int j = 0; j < blackAndWhiteRows.length; j++){
                if(Squares[i] == blackAndWhiteRows[j]){
                    //left
                    if(isSquareThreatenedBy(board, PieceType, x, y, 0, -1)){
                        if(isSquareThreatenedBy(board, PieceType, x, y, 0, 1)){
                            //figur wird von beiden schwarzen/weißen Steinen bereits eingeklemmt, nur einzelne steine belohnen, nicht alle felder
                            fillThreatPST(board, PST, PieceType, true, x, y, 0, -1);
                            fillThreatPST(board, PST, PieceType, true, x, y, 0, 1);
                        }else{
                            //black/white piece must be on the left side
                            fillThreatPST(board, PST, PieceType, false, x, y, 0, -1);
                        }

                    }else{
                        //black/white piece must be on the right side
                        fillThreatPST(board, PST, PieceType, false, x, y, 0, 1);
                    }

                }
            }
            //check all columns:
            for (int j = 0; j < blackAndWhiteColumns.length; j++) {
                if(Squares[i + 1] == blackAndWhiteColumns[j]){
                    //up
                    if(isSquareThreatenedBy(board, PieceType, x, y, -1, 0)){
                        if(isSquareThreatenedBy(board, PieceType, x, y, 1, 0)){
                            //figur wird von beiden schwarzen/weißen Steinen bereits eingeklemmt, nur einzelne steine belohnen, nicht alle felder
                            fillThreatPST(board, PST, PieceType, true, x, y, -1, 0);
                            fillThreatPST(board, PST, PieceType, true, x, y, 1, 0);
                        }else{
                            //black/white piece must be on top
                            fillThreatPST(board, PST, PieceType, false, x, y, -1, 0);
                        }

                    }else{
                        //black/white piece must be at the bottom
                        fillThreatPST(board, PST, PieceType, false, x, y, 1, 0);
                    }

                }
            }
        }
    }

    private int blackMaterial(){
        return blackSquares.length / 2; // explanation: every piece has an x- and y-coordinate, division returns piece count
    }

    private int edgesSecureScore(char[][] board){
        int score = 0;
        int x = kingSquare[0];
        int y = kingSquare[1];
        score += edgeSecureScore(board, x, y, -1, 0); //up
        score += edgeSecureScore(board, x, y, 1, 0); //down
        score += edgeSecureScore(board, x, y, 0, -1); //left
        score += edgeSecureScore(board, x, y, 0, 1); //right
        return score;
    }

    //Function that checks if black checkmated white's king, may be replaced in the future by implementing logic from board class
    private int checkmateScore(char[][] board){
        int score = 100;

        if(onThrone){
            //No array out of bound possible!
            if(board[kingSquare[0] - 1][kingSquare[1]] == 's' && board[kingSquare[0] + 1][kingSquare[1]] == 's' &&
                    board[kingSquare[0]][kingSquare[1] - 1] == 's' && board[kingSquare[0]][kingSquare[1] + 1] == 's'){
                return score;
            }
        }else{
            //Mated vertically?:
            if((    (kingSquare[0] - 1 >= 0 && kingSquare[0] + 1 < board.length) && //without check array out of bound error on top/bottom edge!
                    (board[kingSquare[0] - 1][kingSquare[1]] == 's' || BLOCKED[kingSquare[0] - 1][kingSquare[1]]) &&
                    (board[kingSquare[0] + 1][kingSquare[1]] == 's' || BLOCKED[kingSquare[0] + 1][kingSquare[1]])) ||
                    //Mated horizontally?:
                    ((kingSquare[1] - 1 >= 0 && kingSquare[1] + 1 < board.length) && //without check array out of bound error on left/right edge!
                            (board[kingSquare[0]][kingSquare[1] - 1] == 's' || BLOCKED[kingSquare[0]][kingSquare[1] - 1]) &&
                            (board[kingSquare[0]][kingSquare[1] + 1] == 's' || BLOCKED[kingSquare[0]][kingSquare[1] + 1]))

            ){
                return score;
            }

        }

        return 0;
    }

    private int edgesAccessBlocked(char[][] board){
        int score = 0;
        int x = kingSquare[0];
        int y = kingSquare[1];
        score += edgeAccessBlockedBy(board, 's', x, y, -1, 0); //up
        score += edgeAccessBlockedBy(board, 's', x, y, 1, 0);  //down
        score += edgeAccessBlockedBy(board, 's', x, y, 0, -1); //left
        score += edgeAccessBlockedBy(board, 's', x, y, 0, 1);  //right
        return score;
    }

    /**
     * oben   = (-1, 0)
     * unten  = ( 1, 0)
     * links  = ( 0,-1)
     * rechts = ( 0, 1)
     *
     * Prüft, ob in einer Richtung des Königs bis zum Rand freie Bahn ist
     */


    // Determines whether movement along a direction is blocked by other pieces.
    private boolean isSquareRestricted(char[][] board, int x, int y, int dx, int dy){

        x += dx;
        y += dy;

        while (x >= 0 && x < 9 && y >= 0 && y < 9) {
        //TODO: fix logic for (4,4) if king on Throne...
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

    private boolean isRowRestrictedInOneDirection(char[][] board, int x, int y){
        return isSquareRestricted(board, x, y, 0, -1) || isSquareRestricted(board, x, y, 0, 1);
    }

    private boolean isRowRestrictedInBothDirections(char[][] board, int x, int y){
        return isSquareRestricted(board, x, y, 0, -1) && isSquareRestricted(board, x, y, 0, 1);
    }

    private boolean isColumnRestrictedInOneDirection(char[][] board, int x, int y){
        return isSquareRestricted(board, x, y, -1, 0) || isSquareRestricted(board, x, y, 1, 0);
    }

    private boolean isColumnRestrictedInBothDirections(char[][] board, int x, int y){
        return isSquareRestricted(board, x, y, -1, 0) && isSquareRestricted(board, x, y, 1, 0);
    }

    private boolean isRowThreatenedInOneDirectionBy(char[][] board, char attackingPiece, int x, int y){
        return isSquareThreatenedBy(board, attackingPiece, x, y, 0, -1) || isSquareThreatenedBy(board, attackingPiece, x, y, 0, 1);
    }

    private boolean isRowThreatenedInBothDirectionsBy(char[][] board, char attackingPiece, int x, int y){
        return isSquareThreatenedBy(board, attackingPiece, x, y, 0, -1) && isSquareThreatenedBy(board, attackingPiece, x, y, 0, 1);
    }

    private boolean isColumnThreatenedInOneDirectionBy(char[][] board, char attackingPiece, int x, int y){
        return isSquareThreatenedBy(board, attackingPiece, x, y, -1, 0) || isSquareThreatenedBy(board, attackingPiece, x, y, 1, 0);
    }

    private boolean isColumnThreatenedInBothDirectionsBy(char[][] board, char attackingPiece, int x, int y){
        return isSquareThreatenedBy(board, attackingPiece, x, y, -1, 0) && isSquareThreatenedBy(board, attackingPiece, x, y, 1, 0);
    }


    private boolean isSquareThreatenedBy(char[][] board, char attackingPiece, int x, int y, int dx, int dy ){

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
            if (x >= sourceMinX && x <= sourceMaxX && y >= sourceMinY && y <= sourceMaxY) {
                if(canPieceReachTarget(board, PieceType, x, y, dx, dy,
                        targetMinX, targetMaxX, targetMinY, targetMaxY)){
                    return true;
                }
            }
        }

        return false;
    }
    //TODO:Reduce duplicate code... --> DONE
    private boolean canPieceReachTarget(char[][] board, char PieceType, int x, int y, int dx, int dy,
                                        int targetMinX, int targetMaxX, int targetMinY, int targetMaxY){

        if(dy == 0) {
            if (y < targetMinY || y > targetMaxY) {
                return false;
            }
        }
        if(dx == 0) {
            if (x < targetMinX || x > targetMaxX) {
                return false;
            }
        }

        x += dx;
        y += dy;

        while (x >= 0 && x < 9 && y >= 0 && y < 9){
            if(PieceType == 'k'){
                if(BLOCKED_KING[x][y]){
                    x += dx;
                    y += dy;
                    continue;
                }
                if (board[x][y] != '-' && !BLOCKED[x][y]) {
                    return false;
                }
            }else{
                if(onThrone && x == 4 && y == 4){
                    return false;
                }
                if(BLOCKED[x][y]){
                    x += dx;
                    y += dy;
                    continue;
                }
                if (board[x][y] != '-') {
                    return false;
                }
            }
            if (x >= targetMinX && x <= targetMaxX && y >= targetMinY && y <= targetMaxY){
                return true;
            }
            x += dx;
            y += dy;
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

        char PieceType = board[x][y];

        int moves = 0;
        x += dx;
        y += dy;

        while (x >= 0 && x < 9 && y >= 0 && y < 9) {
            if(PieceType == 'k'){
                if(BLOCKED_KING[x][y]){
                    x += dx;
                    y += dy;
                    continue;
                }
                if (board[x][y] != '-' && !BLOCKED[x][y]) {
                    break;
                }
            }else{
                if(onThrone && x == 4 && y == 4){
                    break;
                }
                if(BLOCKED[x][y]){
                    x += dx;
                    y += dy;
                    continue;
                }
                if(board[x][y] != '-'){
                    break;
                }
            }

            moves++;
            x += dx;
            y += dy;
        }
        return moves;
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

    private void createLineForPST(int[] pattern, int startIdx, int squares,
                                       int x, int y, int dx, int dy, int[][] PST) {

        int steps = 0;

        while (x >= 0 && x < 9 && y >= 0 && y < 9 && steps <= squares) {
            PST[x][y] = pattern[startIdx];
            x += dx;
            y += dy;
            steps++;
            startIdx++;
        }
    }
    /**
     * oben   = (-1, 0)
     * unten  = ( 1, 0)
     * links  = ( 0,-1)
     * rechts = ( 0, 1)
     *
     */
    private int countRadius(char[][] board, char target, int radius, int x, int y){
        //TODO:Delete in the future, may no longer be necessary!
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

    private void createRadiusForPSTFromPattern(int[] pattern, int radius, int x, int y, int[][] PST){

        int max = 8;

        int rawLeft = y - radius;
        int rawRight = y + radius;
        int rawTop = x - radius;
        int rawBottom = x + radius;

        boolean ignoreTopRow = rawTop < 0;
        boolean ignoreBottomRow = rawBottom > max;
        boolean ignoreLeftColumn = rawLeft < 0;
        boolean ignoreRightColumn = rawRight > max;

        int left_y = Math.max(0, Math.min(max, y - radius));
        int right_y = Math.max(0, Math.min(max, y + radius));
        int top_x = Math.max(0, Math.min(max, x - radius));
        int bottom_x = Math.max(0, Math.min(max, x + radius));

        int rowLength = right_y - left_y;
        int columnLength = bottom_x - top_x;
        int offset_x = 0;
        int offset_y = 0;

        if(rawTop < top_x ){
            offset_x = -(rawTop);
        }
        if(rawLeft < left_y){
            offset_y =-(rawLeft);
        }

        if (!ignoreTopRow) {
            createLineForPST(pattern, offset_y,
                    rowLength, top_x, left_y, 0, 1, PST);
        }
        if (!ignoreBottomRow) {
            createLineForPST(pattern, offset_y,
                    rowLength, bottom_x, left_y, 0, 1, PST);
        }
        if (!ignoreLeftColumn) {
            createLineForPST(pattern, offset_x,
                    columnLength, top_x, left_y, 1, 0, PST);
        }
        if (!ignoreRightColumn) {
            createLineForPST(pattern, offset_x,
                    columnLength, top_x, right_y, 1, 0, PST);
        }
    }

    private int[][] createPSTFromPattern(int[][] pattern, int x, int y){

        int boardSize = 9;
        int length = boardSize - 1;

        int[][] PST = new int[boardSize][boardSize];

        int distRight  = length - y;
        int distBottom = length - x;

        int requiredPatterns = Math.max(
                Math.max(y, distRight),
                Math.max(x, distBottom)
        );

        for (int i = 0; i < requiredPatterns; i++){
            createRadiusForPSTFromPattern(pattern[i], i + 1, x, y, PST);
        }

        return PST;
    }
    //TODO: new idea -> use WHITE_PST when king is on throne and pre-generated (implementation still missing)
    //                  king position related PST (for now just use kingThreat and kingSafety functions as they will
    //                  be used for the PST generation and hash tables later on)

    //TODO: another problem --> when not onThrone, white/black only get rewarded for proximity to the king, but not for general piece count


    private int edgeAccessBlockedBy(char [][] board, char PieceType, int x, int y, int dx, int dy){
        int score = 0;
        int edge_x = x;
        int edge_y = y;
        if(isSquareThreatenedBy(board, PieceType, x, y, dx, dy)){
            if(dy == 0){ //Move up/down
                if(dx == -1){ //up
                    edge_x = 0;
                }else if(dx == 1){ //down
                    edge_x = 8;
                }
                if(!isRowRestrictedInBothDirections(board, edge_x, edge_y)) {
                    score += 2;
                }else if(isRowRestrictedInOneDirection(board, edge_x, edge_y)){
                    //Determine which direction is not restricted:
                    if(!isSquareRestricted(board, edge_x, edge_y, 0, -1)){ //left = (0,-1)
                        if(!canAnyPieceInSourceReachTarget(board, PieceType, dx, dy, 0, 8, 0, edge_y - 1, edge_x, edge_x, 0, edge_y - 1)){
                            score += 2;
                        }
                    }else{
                        if(!canAnyPieceInSourceReachTarget(board, PieceType, dx, dy, 0, 8, edge_y + 1, 8, edge_x, edge_x, edge_y + 1, 8)){
                            score += 2;
                        }
                    }
                }
                }else if(dx == 0){
                if(dy == -1){ //left
                    edge_y = 0;
                }else if(dy == 1){ //right
                    edge_y = 8;
                }
                if(!isColumnRestrictedInBothDirections(board, edge_x, edge_y)) {
                    score += 2;
                }else if(isColumnRestrictedInOneDirection(board, edge_x, edge_y)){
                    //Determine which direction is not restricted:
                    if(!isSquareRestricted(board, edge_x, edge_y, -1, 0)){
                        if(!canAnyPieceInSourceReachTarget(board, PieceType, dx, dy, 0, edge_x - 1, 0, 8, 0, edge_x - 1, edge_y, edge_y)){
                            score += 2;
                        }
                    }else{
                        if(!canAnyPieceInSourceReachTarget(board, PieceType, dx, dy, edge_x + 1, 8, 0, 8, edge_x + 1, 8, edge_y, edge_y)){
                            score += 2;
                        }
                    }
                }
            }
        }

        return score;
    }

    private int edgeSecureScore(char[][] board, int x, int y, int dx, int dy){

        int score = 0;
        int count = 1;
        int edge_x = x;
        int edge_y = y;
        if(!isSquareRestricted(board, x, y, dx, dy)){
            if(dy == 0){ //Move up/down
                if(dx == -1){ //up
                    edge_x = 0;
                }else if(dx == 1){ //down
                    edge_x = 8;
                }
                if(isRowRestrictedInBothDirections(board, edge_x, edge_y)){
                    if(isRowThreatenedInBothDirectionsBy(board, 's', edge_x, edge_y)){
                        score += 2 * kingMoves;
                    }else if(isRowThreatenedInOneDirectionBy(board, 's', edge_x, edge_y)){
                        score += kingMoves; //TODO: KingMobility * 2: add KingMobility counter -> DONE
                    }
                }else if(isRowRestrictedInOneDirection(board, edge_x, edge_y)){
                    if(isRowThreatenedInOneDirectionBy(board, 's', edge_x, edge_y)){
                        //TODO:macht kein sinn, logik verbessern -> DONE
                        count++;
                    }
                    //Determine which direction is not restricted:
                    if(!isSquareRestricted(board, edge_x, edge_y, 0, -1)){ //left = (0,-1)
                        if(canAnyPieceInSourceReachTarget(board, 's', dx, dy, 0, 8, 0, edge_y - 1, edge_x, edge_x, 0, edge_y - 1)){
                            score += count * kingMoves;
                        }
                    }else{
                        if(canAnyPieceInSourceReachTarget(board, 's', dx, dy, 0, 8, edge_y + 1, 8, edge_x, edge_x, edge_y + 1, 8)){
                            score += count * kingMoves;
                        }
                    }
                }
            }else if(dx == 0){ //Move left/right
                if(dy == -1){ //left
                    edge_y = 0;
                }else if(dy == 1){ //right
                    edge_y = 8;
                }
                if(isColumnRestrictedInBothDirections(board, edge_x, edge_y)){
                    if(isColumnThreatenedInOneDirectionBy(board, 's', edge_x, edge_y)){
                        score += kingMoves;
                    }else if(isColumnThreatenedInBothDirectionsBy(board, 's', edge_x, edge_y)){
                        score += 2 * kingMoves;
                    }
                }else if(isColumnRestrictedInOneDirection(board, edge_x, edge_y)){
                    if(isColumnThreatenedInOneDirectionBy(board, 's', edge_x, edge_y)){
                        count++;
                    }
                    //Determine which direction is not restricted:
                    if(!isSquareRestricted(board, edge_x, edge_y, -1, 0)){
                        if(canAnyPieceInSourceReachTarget(board, 's', dx, dy, 0, edge_x - 1, 0, 8, 0, edge_x - 1, edge_y, edge_y)){
                            score += count * kingMoves;
                        }
                    }else{
                        if(canAnyPieceInSourceReachTarget(board, 's', dx, dy, edge_x + 1, 8, 0, 8, edge_x + 1, 8, edge_y, edge_y)){
                            score += count * kingMoves;
                        }
                    }
                }
            }
        }


        return score;
    }

    //Boni für freie Linien/Reihen und Nähe zum Rand
    //TODO:only reward white if edge is not defendable by black

    private int kingMoves(char[][] board, int x, int y) {
        int moves = 0;
        moves += countMoves(board, x, y, -1, 0);
        moves += countMoves(board, x, y,  1, 0);
        moves += countMoves(board, x, y,  0,-1);
        moves += countMoves(board, x, y,  0, 1);

        return moves;
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

        for (int i = 0; i < board.length; i++) { //x-Indizes
            for (int j = 0; j < board[i].length; j++) { //y-Indizes
                if (board[i][j] == target) {
                    temp[index++] = i;
                    temp[index++] = j;
                }
            }
        }

        return Arrays.copyOf(temp, index);
    }
}
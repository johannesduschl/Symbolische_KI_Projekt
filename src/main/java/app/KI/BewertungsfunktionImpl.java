package app.KI;

import app.board.Board;
import app.board.Zug;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;

public class BewertungsfunktionImpl implements Bewertungsfunktion {

    //für Fitness der EvoKi´s
    public int finalGameScore = 0;

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
            {  999,   4,   3,   3,   3,   3,   3,   4,  999 },
            {   4,   2,   1,   1,   1,   1,   1,   2,   4 },
            {   3,   1,  -1,  -1,  -1,  -1,  -1,   1,   3 },
            {   3,   1,  -1,  -1,  -3,  -1,  -1,   1,   3 },
            {   3,   1,  -1,  -3,   5,  -3,  -1,   1,   3 },
            {   3,   1,  -1,  -1,  -3,  -1,  -1,   1,   3 },
            {   3,   1,  -1,  -1,  -1,  -1,  -1,   1,   3 },
            {   4,   2,   1,   1,   1,   1,   1,   2,   4 },
            {  999,   4,   3,   3,   3,   3,   3,   4,  999 }
    };

    private static int[][] BLACK_PST = {
            {  0, 0, 2, 2, 2, 2, 2, 0,  0 },
            {  0, 0, 0, 1, 2, 1, 0, 0,  0 },
            {  2, 0, 0, 1, 3, 1, 0, 0,  2 },
            {  2, 1, 1, 3, 5, 3, 1, 1,  2 },
            {  2, 2, 3, 5, 0 ,5, 3, 2,  2 },
            {  2, 1, 1, 3, 5, 3, 1, 1,  2 },
            {  2, 0, 0, 1, 3, 1, 0, 0,  2 },
            {  0, 0, 0, 1, 2, 1, 0, 0,  0 },
            {  0, 0, 2, 2, 2, 2, 2, 0,  0 }
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

    private static final int[][] BLACK_PST_THREAT = new int[9][9];

    private static final int[][] WHITE_PST_THREAT = new int[9][9];

    private static final int[][] BLACK_PATTERN = {
            {3, 5, 3},
            {0, 1, 3, 1, 0},
            {0, 0, 1, 2, 1, 0, 0},
            {0, 0, 0, 1, 2, 1, 0, 0, 0},
            {0, 0, 0, 0, 1, 2, 1, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 1, 2, 1, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 1, 2, 1, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 1, 2, 1, 0, 0, 0, 0, 0, 0, 0},

            //last element is always an edge pattern!
            {0, 0, 2, 2, 2, 2, 2, 0, 0},
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
    private static final int W_KING_MOBILITY = 1;
    private static final int W_WHITE_MATERIAL = 1;
    private static final int W_WHITE_PST = 1;
    private static final int W_WHITE_PST_THREAT = 1;
    private static final int W_KING_EDGE_SECURE = 1;
    private static final int W_WINNING_THREAT = 1;

    // =========================
    // BLACK FEATURE WEIGHTS
    // =========================
    private static final int W_EDGES_SECURE_SCORE = 1;
    private static final int W_EDGES_ACCESS_BLOCKED = 1;
    private static final int W_CHECKMATE_SCORE = 1;
    private static final int W_CHECKMATE_THREAT = 1;
    private static final int W_BLACK_MATERIAL = 1;
    private static final int W_BLACK_PST = 1;
    private static final int W_BLACK_PST_THREAT = 1;

    // =========================
    // PIECE SQUARES
    // =========================
    private static final int[] kingSquare = new int[2];
    private static final int[] whiteSquares = new int[16];
    private static int whiteCount;
    private static final int[] blackSquares = new int[32];
    private static int blackCount;
    private static Zug lastMove;

    private static final boolean[] isBlackOnColumn = new boolean[9];
    private static final boolean[] isBlackOnRow = new boolean[9];

    private static final boolean[] isWhiteOnColumn = new boolean[9];
    private static final boolean[] isWhiteOnRow = new boolean[9];

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
        extractPieces(board.getBoard());
        lastMove = board.getLastMove();
        onThrone = (kingSquare[0] == 4 && kingSquare[1] == 4);

        resetPST(BLACK_PST_THREAT);
        resetPST(WHITE_PST_THREAT); //not necessary if per move updates will work

        initArrays();
        initThreatPST(board.getBoard(), BLACK_PST_THREAT, whiteSquares, whiteCount, 's');
        initThreatPST(board.getBoard(), WHITE_PST_THREAT, blackSquares, blackCount, 'w');

        kingMoves = kingMoves(board.getBoard(), kingSquare[0], kingSquare[1]);

        if(!onThrone){
            if(board.getBewegt() == 'k') {
                BLACK_PST = createPSTFromPattern(BLACK_PATTERN, kingSquare[0], kingSquare[1], true);
                WHITE_PST = createPSTFromPattern(WHITE_PATTERN, kingSquare[0], kingSquare[1], false);
            }
        }

        int white = evaluateWhite(board);
        int black = evaluateBlack(board);

        return white - black;

    }

    private int evaluateWhite(Board boardObject) {
        char[][] board = boardObject.getBoard();

        return whitePST(board) //also includes threat pst
                + W_WHITE_MATERIAL * whiteMaterial()
                + W_KING_PROGRESS * kingEscapeScore(board)
                + W_KING_EDGE_SECURE * secureKingOnEdge(board)
                + W_WINNING_THREAT * threatensWin(boardObject)
                + W_KING_MOBILITY * kingMobility();
    }

    private int evaluateBlack(Board boardObject) {
        char[][] board = boardObject.getBoard();

        return blackPST(board) //also includes threat pst
                + W_BLACK_MATERIAL * blackMaterial()
                + W_EDGES_SECURE_SCORE * edgesSecureScore(board) //simplify those functions to be more efficient, fix edge case where king can capture white piece in front of x square
                + W_EDGES_ACCESS_BLOCKED * edgesAccessBlocked(board)  //over-engineering might be counter-intuitive!
                + W_CHECKMATE_THREAT * threatensCheckmate(boardObject)
                + W_CHECKMATE_SCORE * checkmateScore(boardObject);
    }

    //TODO: change to array.length operation for every kind of piece
    public int pieceCount(Board board) {
        int count = 0;
        char[][] b = board.getBoard();

        for (char[] chars : b) {
            for (char c : chars) {
                if (c == 'w' || c == 's' || c == 'k') count++;
            }
        }
        return count;
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

    private void initArrays() {

        final boolean[] blackRow = isBlackOnRow;
        final boolean[] blackCol = isBlackOnColumn;
        final boolean[] whiteRow = isWhiteOnRow;
        final boolean[] whiteCol = isWhiteOnColumn;

        Arrays.fill(blackRow, false);
        Arrays.fill(blackCol, false);
        Arrays.fill(whiteRow, false);
        Arrays.fill(whiteCol, false);

        for (int i = 0; i < blackCount; i += 2) {
            int row = blackSquares[i];
            int col = blackSquares[i + 1];
            blackRow[row] = true;
            blackCol[col] = true;
        }

        for (int i = 0; i < whiteCount; i += 2) {
            int row = whiteSquares[i];
            int col = whiteSquares[i + 1];
            whiteRow[row] = true;
            whiteCol[col] = true;
        }

        int[] tempRows = new int[9];
        int[] tempCols = new int[9];
        int r = 0, c = 0;

        for (int i = 0; i < 9; i++) {
            if (blackRow[i] & whiteRow[i]) {
                tempRows[r++] = i;
            }
            if (blackCol[i] & whiteCol[i]) {
                tempCols[c++] = i;
            }
        }

        blackAndWhiteRows = java.util.Arrays.copyOf(tempRows, r);
        blackAndWhiteColumns = java.util.Arrays.copyOf(tempCols, c);
    }

    private void resetPST(int[][] pst) {
        for (int[] ints : pst) {
            Arrays.fill(ints, 0);
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
        return whiteCount; //one white piece give two points, division unnecessary
    }

    private int kingEscapeScore(char[][] board) {

        int x = kingSquare[0];
        int y = kingSquare[1];
        int score = 0;

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

    private int threatensWin(Board boardObject){
        boolean isBlackToMove = !boardObject.blackMovesNext();
        char[][] board = boardObject.getBoard();
        int x = kingSquare[0];
        int y = kingSquare[1];

        int winning = 500;
        int threat = 10;

        int x_min = 0;
        int x_max = 8;
        int y_min = 0;
        int y_max = 8;

        if(isPieceOnTopEdge(x)){
            if(!isRowRestrictedInBothDirections(board, x, y)) {
                if(isBlackToMove) return winning;
                if(y == y_min + 1){
                    if(!isSquareDirectlyThreatenedBy(board, 's', x, y + 1, 1, 0)){
                        return winning;
                    }
                } else if (y == y_max - 1) {
                    if(!isSquareDirectlyThreatenedBy(board, 's', x, y - 1, 1, 0)){
                        return winning;
                    }
                }else{
                    return winning;
                }
            }
            //left
            if(!isSquareRestricted(board, x, y, 0, -1)){
                //regular case
                if(y > (y_min + 2)) {
                    if(isBlackToMove){
                        return winning;
                    }
                    //anstatt ändern den case abfangen?
                    if(!canAnyPieceInSourceReachTarget(board, 's', -1, 0, x_min + 1, x_max, y_min + 2, y - 1, x_min, x_min, y_min + 2, y - 1)){
                        if(!isSquareDirectlyThreatenedBy(board, 's', x, y_min + 1, 1, 0)){
                            return winning;
                        }else{
                            return threat;
                        }
                    }
                    //edge case: unstoppable win
                }else if(y == y_min + 1){
                    if(!isBlackToMove){
                        if((!isSquareDirectlyThreatenedBy(board, 's', x, y, 0, 1) && !isSquareDirectlyThreatenedBy(board, 's', x, y + 1, 1, 0)) ||
                                board[x][y + 1] == 's'){
                            return winning;
                        }
                    }else{
                        if((!isSquareDirectlyThreatenedBy(board, 's', x, y, 0, 1) && !isSquareDirectlyThreatenedBy(board, 's', x, y + 1, 1, 0)) ||
                                (board[x][y + 1] == 's' && lastMove.getToX() != x && lastMove.getToY() != y + 1)){
                            return winning;
                        }
                    }
                    //edge case: stoppable winning threat
                }else if(y == y_min + 2){
                    if(!isSquareDirectlyThreatenedBy(board, 's', x_min, y_min + 1, 1, 0)){
                        return winning;
                    }else{
                        return threat;
                    }
                }
                //right
            }else if(!isSquareRestricted(board, x, y, 0, 1)){
                //regular case
                if(y < (y_max - 2) ){
                    if(isBlackToMove){
                        return winning;
                    }
                    if(!canAnyPieceInSourceReachTarget(board, 's', -1, 0, x_min + 1, x_max, y + 1, y_max - 2, x_min, x_min, y + 1, y_max - 2)){
                        if(!isSquareDirectlyThreatenedBy(board, 's', x, y_max - 1, 1, 0)){
                            return winning;
                        }else{
                            return threat;
                        }
                    }
                    //edge case: unstoppable win
                }else if(y == y_max - 1){
                    if(!isBlackToMove){
                        if((!isSquareDirectlyThreatenedBy(board, 's', x, y, 0, -1) && !isSquareDirectlyThreatenedBy(board, 's', x, y - 1, 1, 0)) ||
                                board[x][y - 1] == 's'){
                            return winning;
                        }
                    }else{
                        if((!isSquareDirectlyThreatenedBy(board, 's', x, y, 0, -1) && !isSquareDirectlyThreatenedBy(board, 's', x, y - 1, 1, 0)) ||
                                (board[x][y - 1] == 's' && lastMove.getToX() != x && lastMove.getToY() != y - 1)){
                            return winning;
                        }
                    }
                    //edge case: stoppable winning threat
                }else if(y == y_max - 2){
                    if(!isSquareDirectlyThreatenedBy(board, 's', x_min, y_max - 1, 1, 0)){
                        return winning;
                    }else{
                        return threat;
                    }
                }
            }
        }

        if(isPieceOnBottomEdge(x)){
            if(!isRowRestrictedInBothDirections(board, x, y)){
                if(isBlackToMove) return winning;
                if(y == y_min + 1){
                    if(!isSquareDirectlyThreatenedBy(board, 's', x, y + 1, -1, 0)){
                        return winning;
                    }
                } else if (y == y_max - 1) {
                    if(!isSquareDirectlyThreatenedBy(board, 's', x, y - 1, -1, 0)){
                        return winning;
                    }
                }else{
                    return winning;
                }
            }
            //left
            if(!isSquareRestricted(board, x, y, 0, -1)){
                //regular case
                if(y > (y_min + 2)) {
                    if(isBlackToMove){
                        return winning;
                    }
                    if(!canAnyPieceInSourceReachTarget(board, 's', 1, 0, x_min, x_max - 1, y_min + 2, y - 1, x_max, x_max, y_min + 2, y - 1)){
                        if(!isSquareDirectlyThreatenedBy(board, 's', x, y_min + 1, -1, 0)){
                            return winning;
                        }else{
                            return threat;
                        }
                    }
                    //edge case: unstoppable win
                }else if(y == y_min + 1){
                    if(!isBlackToMove){
                        if((!isSquareDirectlyThreatenedBy(board, 's', x, y, 0, 1) && !isSquareDirectlyThreatenedBy(board, 's', x, y + 1, -1, 0)) ||
                                board[x][y + 1] == 's'){
                            return winning;
                        }
                    }else{
                        if((!isSquareDirectlyThreatenedBy(board, 's', x, y, 0, 1) && !isSquareDirectlyThreatenedBy(board, 's', x, y + 1, -1, 0)) ||
                                (board[x][y + 1] == 's' && lastMove.getToX() != x && lastMove.getToY() != y +1)){
                            return winning;
                        }
                    }
                    //edge case: stoppable winning threat
                }else if(y == y_min + 2){
                    if(!isSquareDirectlyThreatenedBy(board, 's', x, y_min + 1, -1, 0)){
                        return winning;
                    }else{
                        return threat;
                    }
                }
                //right
            }else if(!isSquareRestricted(board, x, y, 0, 1)){
                //regular case
                if(y < (y_max - 2) ){
                    if(isBlackToMove){
                        return winning;
                    }
                    if(!canAnyPieceInSourceReachTarget(board, 's', 1, 0, x_min, x_max - 1, y + 1, y_max - 2, x_max, x_max, y + 1, y_max - 2)){
                        if(!isSquareDirectlyThreatenedBy(board, 's', x, y_max - 1, -1, 0)){
                            return winning;
                        }else{
                            return threat;
                        }
                    }
                    //edge case: unstoppable win
                }else if(y == y_max - 1){
                    if(!isBlackToMove){
                        if((!isSquareDirectlyThreatenedBy(board, 's', x, y, 0, -1) && !isSquareDirectlyThreatenedBy(board, 's', x, y - 1, -1, 0)) ||
                                board[x][y - 1] == 's'){
                            return winning;
                        }
                    }else{
                        if((!isSquareDirectlyThreatenedBy(board, 's', x, y, 0, -1) && !isSquareDirectlyThreatenedBy(board, 's', x, y - 1, -1, 0)) ||
                                (board[x][y - 1] == 's' && lastMove.getToX() != x && lastMove.getToY() != y - 1)){
                            return threat;
                        }
                    }
                    //edge case: stoppable winning threat
                }else if(y == y_max - 2){
                    if(!isSquareDirectlyThreatenedBy(board, 's', x, y_max - 1, -1, 0)){
                        return winning;
                    }else{
                        return threat;
                    }
                }
            }
        }

        if(isPieceOnLeftEdge(y)){
            if(!isColumnRestrictedInBothDirections(board, x, y)){
                if(isBlackToMove) return winning;
                if(x == x_min + 1){
                    if(!isSquareDirectlyThreatenedBy(board, 's', x + 1, y, 0, 1)){
                        return winning;
                    }
                }else if(x == x_max - 1){
                    if(!isSquareDirectlyThreatenedBy(board, 's', x - 1, y, 0, 1)){
                        return winning;
                    }
                }else{
                    return winning;
                }
            }
            //up
            if(!isSquareRestricted(board, x, y, -1, 0)){
                //regular case
                if(x > (x_min + 2)){
                    if(isBlackToMove){
                        return winning;
                    }
                    if(!canAnyPieceInSourceReachTarget(board, 's', 0, -1, x_min + 2, x - 1, y_min + 1, y_max, x_min + 2, x - 1, y_min, y_min)){
                        if(!isSquareDirectlyThreatenedBy(board, 's', x_min + 1, y, 0, 1)){
                            return winning;
                        }else{
                            return threat;
                        }
                    }
                    //edge case: unstoppable win
                }else if(x == x_min + 1){
                    if(!isBlackToMove){
                        if((!isSquareDirectlyThreatenedBy(board, 's', x, y, 1, 0) && !isSquareDirectlyThreatenedBy(board, 's', x + 1, y, 0, 1)) ||
                                board[x + 1][y] == 's'){
                            return winning;
                        }
                    }else{
                        if((!isSquareDirectlyThreatenedBy(board, 's', x, y, 1, 0) && !isSquareDirectlyThreatenedBy(board, 's', x + 1, y, 0, 1)) ||
                                (board[x + 1][y] == 's' && lastMove.getToX() != x + 1 && lastMove.getToY() != y)){
                            return winning;
                        }
                    }
                    //edge case: stoppable winning threat
                }else if(x == x_min + 2){
                    if(!isSquareDirectlyThreatenedBy(board, 's', x_min + 1, y, 0, 1)){
                        return winning;
                    }else{
                        return threat;
                    }
                }
                //down
            }else if(!isSquareRestricted(board, x, y, 1, 0)){
                //regular case
                if(x < (x_max - 2)){
                    if(isBlackToMove){
                        return winning;
                    }
                    if(!canAnyPieceInSourceReachTarget(board, 's', 0, -1, x + 1, x_max - 2, y_min + 1, y_max, x + 1, x_max - 2, y_min, y_min)){
                        if(!isSquareDirectlyThreatenedBy(board, 's', x_max - 1, y, 0, 1)){
                            return winning;
                        }else{
                            return threat;
                        }
                    }
                    //edge case: unstoppable win
                }else if(x == x_max - 1){
                    if(!isBlackToMove){
                        if((!isSquareDirectlyThreatenedBy(board, 's', x, y, -1, 0) && !isSquareDirectlyThreatenedBy(board, 's', x - 1, y, 0, 1)) ||
                                board[x - 1][y] == 's'){
                            return winning;
                        }
                    }else{
                        if((!isSquareDirectlyThreatenedBy(board, 's', x, y, -1, 0) && !isSquareDirectlyThreatenedBy(board, 's', x - 1, y, 0, 1)) ||
                                (board[x - 1][y] == 's' && lastMove.getToX() != x - 1 && lastMove.getToY() != y)){
                            return winning;
                        }
                    }
                    //edge case: stoppable winning threat
                }else if(x == x_max - 2){
                    if(!isSquareDirectlyThreatenedBy(board, 's', x_max - 1, y, 0, 1)){
                        return winning;
                    }else{
                        return threat;
                    }
                }
            }
        }

        if(isPieceOnRightEdge(y)){
            if(!isColumnRestrictedInBothDirections(board, x, y)){
                if(isBlackToMove) return winning;
                if(x == x_min + 1){
                    if(!isSquareDirectlyThreatenedBy(board, 's', x + 1, y, 0, -1)){
                        return winning;
                    }
                }else if(x == x_max - 1){
                    if(!isSquareDirectlyThreatenedBy(board, 's', x - 1, y, 0, -1)){
                        return winning;
                    }
                }else{
                    return winning;
                }
            }            //up
            if(!isSquareRestricted(board, x, y, -1, 0)){
                //regular case
                if(x > (x_min + 2)){
                    if(isBlackToMove){
                        return winning;
                    }
                    if(!canAnyPieceInSourceReachTarget(board, 's', 0, 1, x_min + 2, x - 1, y_min, y_max - 1, x_min + 2, x - 1, y_max, y_max)){
                        if(!isSquareDirectlyThreatenedBy(board, 's', x_min + 1, y, 0, -1)){
                            return winning;
                        }else{
                            return threat;
                        }
                    }
                    //edge case: unstoppable win
                }else if(x == x_min + 1){
                    if(!isBlackToMove){
                        if((!isSquareDirectlyThreatenedBy(board, 's', x, y, 1, 0) && !isSquareDirectlyThreatenedBy(board, 's', x + 1, y, 0, -1)) ||
                                board[x + 1][y] == 's'){
                            return winning;
                        }
                    }else{
                        if((!isSquareDirectlyThreatenedBy(board, 's', x, y, 1, 0) && !isSquareDirectlyThreatenedBy(board, 's', x + 1, y, 0, -1)) ||
                                (board[x + 1][y] == 's' && lastMove.getToX() != x + 1 && lastMove.getToY() != y)){
                            return winning;
                        }
                    }
                }else if(x == x_min + 2){
                    if(!isSquareDirectlyThreatenedBy(board, 's', x_min + 1, y, 0, -1)){
                        return winning;
                    }else{
                        return threat;
                    }
                }
                //down
            }else if(!isSquareRestricted(board, x, y, 1, 0)){
                //regular case
                if(x < (x_max - 2)){
                    if(isBlackToMove){
                        return winning;
                    }
                    if(!canAnyPieceInSourceReachTarget(board, 's', 0, 1, x + 1, x_max - 2, y_min, y_max - 1, x + 1, x_max - 2, y_max, y_max)){
                        if(!isSquareDirectlyThreatenedBy(board, 's', x_max - 1, y, 0, -1)){
                            return winning;
                        }else{
                            return threat;
                        }
                    }
                    //edge case: unstoppable win
                }else if(x == x_max - 1){
                    if(!isBlackToMove){
                        if((!isSquareDirectlyThreatenedBy(board, 's', x, y, -1, 0) && !isSquareDirectlyThreatenedBy(board, 's', x - 1, y, 0, -1)) ||
                                board[x - 1][y] == 's'){
                            return winning;
                        }
                    }else{
                        if((!isSquareDirectlyThreatenedBy(board, 's', x, y, -1, 0) && !isSquareDirectlyThreatenedBy(board, 's', x - 1, y, 0, -1)) ||
                                (board[x - 1][y] == 's' && lastMove.getToX() != x - 1 && lastMove.getToY() != y)){
                            return winning;
                        }
                    }
                    //edge case: stoppable winning threat
                }else if(x == x_max - 2){
                    if(!isSquareDirectlyThreatenedBy(board, 's', x_max - 1, y, 0, -1)){
                        return winning;
                    }else{
                        return threat;
                    }
                }
            }
        }

        return 0;
    }

    private int kingMobility() {
        //IMPORTANT: Throne is safe therefore mobility less important
        //Mobility makes white play too risky and unsafe!
        int score = 0;
        int multiplier;

        if(!onThrone){
            int blackPieces = blackCount / 2;

            //more Moves make him vulnerable too!

            //apply this logic by rewarding black for (in-)direct view to the king
            //keep it balanced with kingSafety -> more white pieces around are better

            //MANY
            if(blackPieces <= 16 && blackPieces > 12){
                multiplier = 1;
                //SOME
            }else if(blackPieces <= 12 && blackPieces > 6){
                multiplier = 2;
                //FEW
            }else{
                multiplier = 3;
            }

            //MANY
            if(kingMoves <= 16 && kingMoves > 11){
                score += multiplier * 4;
                //SOME
            }else if(kingMoves <= 11 && kingMoves > 8){
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

    private int threatensCheckmate(Board boardObject){
        char[][] board = boardObject.getBoard();
        boolean isBlackToMove = !boardObject.blackMovesNext();
        int score;

        if(isBlackToMove){
            //only threat that white can defend on the next move
            score = 5;
        }else{
            //checkmate for black on the next move -> punishment for white
            score = 500;
        }
        int x = kingSquare[0];
        int y = kingSquare[1];

        if(onThrone){
            if(board[x - 1][y] == 's' && board[x + 1][y] == 's'){
                if(board[x][y - 1] == 's'){
                    if((board[x][y + 1] == '-') && (isSquareDirectlyThreatenedBy(board, 's', x, y, 0, 1) ||
                            isSquareDirectlyThreatenedBy(board, 's', x, y + 1, -1, 0)||
                            isSquareDirectlyThreatenedBy(board, 's', x, y + 1, 1, 0))){
                        return score;
                    }
                }else if(board[x][y + 1] == 's'){
                    if((board[x][y - 1] == '-') && (isSquareDirectlyThreatenedBy(board, 's', x, y, 0, -1) ||
                            isSquareDirectlyThreatenedBy(board, 's', x, y - 1, -1, 0)||
                            isSquareDirectlyThreatenedBy(board, 's', x, y - 1, 1, 0))){
                        return score;
                    }
                }
            }else if(board[x][y - 1] == 's' && board[x][y + 1] == 's'){
                if(board[x - 1][y] == 's'){
                    if((board[x + 1][y] == '-') && (isSquareDirectlyThreatenedBy(board, 's', x, y, 1, 0) ||
                            isSquareDirectlyThreatenedBy(board, 's', x + 1, y, 0, -1)||
                            isSquareDirectlyThreatenedBy(board, 's', x + 1, y, 0, 1))){
                        return score;
                    }
                }else if(board[x + 1][y] == 's'){
                    if((board[x - 1][y] == '-') && (isSquareDirectlyThreatenedBy(board, 's', x, y, -1, 0) ||
                            isSquareDirectlyThreatenedBy(board, 's', x - 1, y, 0, -1)||
                            isSquareDirectlyThreatenedBy(board, 's', x - 1, y, 0, 1))){
                        return score;
                    }
                }
            }

        }else {
            if (y > 0 && y + 1 < board.length){
                if((board[x][y - 1] == 's' || BLOCKED[x][y - 1]) && board[x][y + 1] != 'w' && !BLOCKED[x][y + 1]){
                    if(isSquareDirectlyThreatenedBy(board, 's', x, y, 0, 1) ||
                            isSquareDirectlyThreatenedBy(board, 's', x, y + 1, -1, 0)||
                            isSquareDirectlyThreatenedBy(board, 's', x, y + 1, 1, 0)){
                        return (board[x][y + 1] == 's') ? 5 : score;
                    }
                }else if((board[x][y + 1] == 's' || BLOCKED[x][y + 1]) && board[x][y - 1] != 'w' && !BLOCKED[x][y - 1]){
                    if(isSquareDirectlyThreatenedBy(board, 's', x, y, 0, -1) ||
                            isSquareDirectlyThreatenedBy(board, 's', x, y - 1, -1, 0)||
                            isSquareDirectlyThreatenedBy(board, 's', x, y - 1, 1, 0)){
                        return (board[x][y - 1] == 's') ? 5 : score;
                    }
                }
            }
            if (x > 0 && x + 1 < board.length){
                if((board[x - 1][y] == 's' || BLOCKED[x - 1][y]) && board[x + 1][y] != 'w' && !BLOCKED[x + 1][y]) {
                    if(isSquareDirectlyThreatenedBy(board, 's', x, y, 1, 0) ||
                            isSquareDirectlyThreatenedBy(board, 's', x + 1, y, 0, -1)||
                            isSquareDirectlyThreatenedBy(board, 's', x + 1, y, 0, 1)){
                        return (board[x + 1][y] == 's') ? 5 : score;
                    }
                }else if((board[x + 1][y] == 's' || BLOCKED[x + 1][y]) && board[x - 1][y] != 'w' && !BLOCKED[x - 1][y]){
                    if(isSquareDirectlyThreatenedBy(board, 's', x, y, -1, 0) ||
                            isSquareDirectlyThreatenedBy(board, 's', x - 1, y, 0, -1)||
                            isSquareDirectlyThreatenedBy(board, 's', x - 1, y, 0, 1)){
                        return (board[x - 1][y] == 's') ? 5 : score;
                    }
                }
            }
        }
        return 0;
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
    private void initThreatPST(char[][] board, int[][] PST, int[] Squares, int pieceCount, char PieceType){
        for (int i = 0; i < pieceCount - 1; i += 2){
            int x = Squares[i];
            int y = Squares[i + 1];

            //check all rows:
            for (int blackAndWhiteRow : blackAndWhiteRows) {
                if (Squares[i] == blackAndWhiteRow) {
                    //left
                    if (isSquareThreatenedBy(board, PieceType, x, y, 0, -1)) {
                        if (isSquareThreatenedBy(board, PieceType, x, y, 0, 1)) {
                            //figur wird von beiden schwarzen/weißen Steinen bereits eingeklemmt, nur einzelne steine belohnen, nicht alle felder
                            fillThreatPST(board, PST, PieceType, true, x, y, 0, -1);
                            fillThreatPST(board, PST, PieceType, true, x, y, 0, 1);
                        } else {
                            //black/white piece must be on the left side
                            fillThreatPST(board, PST, PieceType, false, x, y, 0, -1);
                        }

                    } else {
                        //black/white piece must be on the right side
                        fillThreatPST(board, PST, PieceType, false, x, y, 0, 1);
                    }

                }
            }
            //check all columns:
            for (int blackAndWhiteColumn : blackAndWhiteColumns) {
                if (Squares[i + 1] == blackAndWhiteColumn) {
                    //up
                    if (isSquareThreatenedBy(board, PieceType, x, y, -1, 0)) {
                        if (isSquareThreatenedBy(board, PieceType, x, y, 1, 0)) {
                            //figur wird von beiden schwarzen/weißen Steinen bereits eingeklemmt, nur einzelne steine belohnen, nicht alle felder
                            fillThreatPST(board, PST, PieceType, true, x, y, -1, 0);
                            fillThreatPST(board, PST, PieceType, true, x, y, 1, 0);
                        } else {
                            //black/white piece must be on top
                            fillThreatPST(board, PST, PieceType, false, x, y, -1, 0);
                        }

                    } else {
                        //black/white piece must be at the bottom
                        fillThreatPST(board, PST, PieceType, false, x, y, 1, 0);
                    }

                }
            }
        }
    }

    private int blackMaterial(){
        return blackCount / 2; // explanation: every piece has an x- and y-coordinate, division returns piece count
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
    private int checkmateScore(Board boardObject){
        boolean isBlackToMove = !boardObject.blackMovesNext();
        if(!isBlackToMove) return 0;

        char[][] board = boardObject.getBoard();
        int score = 1000;
        int x = kingSquare[0];
        int y = kingSquare[1];

        if(onThrone){
            //No array out of bound possible!
            if(board[x - 1][y] == 's' && board[x + 1][y] == 's' &&
                    board[x][y - 1] == 's' && board[x][y + 1] == 's'){
                return score;
            }
        }else{
            //Mated vertically?:
            if((    (x - 1 >= 0 && x + 1 < board.length) && //without check array out of bound error on top/bottom edge!
                    (board[x - 1][y] == 's' || BLOCKED[x - 1][y]) &&
                    (board[x + 1][y] == 's' || BLOCKED[x + 1][y]) &&
                    (lastMove.getToX() == (x - 1) || lastMove.getToX() == (x + 1)) &&
                    lastMove.getToY() == y) ||
                    //Mated horizontally?:
                    ((y - 1 >= 0 && y + 1 < board.length) && //without check array out of bound error on left/right edge!
                            (board[x][y - 1] == 's' || BLOCKED[x][y - 1]) &&
                            (board[x][y + 1] == 's' || BLOCKED[x][y + 1]) &&
                            (lastMove.getToY() == (y - 1) || lastMove.getToY() == (y + 1)) &&
                            lastMove.getToX() == x)

            ){
                return score;
            }

        }

        return 0;
    }

    private int secureKingOnEdge(char[][] board) {
        int score = 0;
        int x = kingSquare[0];
        int y = kingSquare[1];
        if (isPieceOnTopEdge(x) || isPieceOnBottomEdge(x)) {
            if((y > 0 && board[x][y - 1] == 'w') || (y < 8 && board[x][y + 1] == 'w')){
                score += 5;
            }
        }else if(isPieceOnLeftEdge(y) || isPieceOnRightEdge(y)){
            if((x > 0 && board[x - 1 ][y] == 'w') || (x < 8 && board[x + 1][y] == 'w')){
                score += 5;
            }
        }
        return score;
    }

    /**
     * oben   = (-1, 0)
     * unten  = ( 1, 0)
     * links  = ( 0,-1)
     * rechts = ( 0, 1)
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

    private boolean isPieceOnTopEdge(int x){
        return x == 0;
    }

    private boolean isPieceOnBottomEdge(int x){
        return x == 8;
    }

    private boolean isPieceOnLeftEdge(int y){
        return y == 0;
    }

    private boolean isPieceOnRightEdge(int y){
        return y == 8;
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

    private boolean isSquareDirectlyThreatenedBy(char[][] board, char attackingPiece, int x, int y, int dx, int dy){

        x += dx;
        y += dy;

        while (x >= 0 && x < 9 && y >= 0 && y < 9) {
            if (board[x][y] == attackingPiece) {
                return true;
            }else if(board[x][y] != attackingPiece && board[x][y] != '-' && board[x][y] != 'x') {
                if(!(onThrone && x == 4 && y == 4)){
                    return false;
                }
            }
            x += dx;
            y += dy;
        }

        return false;
    }

    private boolean canAnyPieceInSourceReachTarget(
            char[][] board,
            char pieceType,
            int dx, int dy,
            int sourceMinX, int sourceMaxX, int sourceMinY, int sourceMaxY,
            int targetMinX, int targetMaxX, int targetMinY, int targetMaxY
    ) {

        final int[] squares;
        final int count;

        if (pieceType == 'w') {
            squares = whiteSquares;
            count = whiteCount;
        } else if (pieceType == 's') {
            squares = blackSquares;
            count = blackCount;
        } else {
            squares = kingSquare;
            count = 2;
        }

        for (int i = 0; i < count; i += 2) {
            int x = squares[i];
            int y = squares[i + 1];

            if (x >= sourceMinX && x <= sourceMaxX &&
                    y >= sourceMinY && y <= sourceMaxY) {

                if (canPieceReachTarget(board, pieceType, x, y, dx, dy,
                        targetMinX, targetMaxX, targetMinY, targetMaxY)) {
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

    private int[][] createPSTFromPattern(int[][] pattern, int x, int y, boolean withEdgePattern){

        int boardSize = 9;
        int length = boardSize - 1;
        int edge_radius = 4;

        int[][] PST = new int[boardSize][boardSize];

        int distRight  = length - y;
        int distBottom = length - x;

        int requiredPatterns = Math.max(
                Math.max(y, distRight),
                Math.max(x, distBottom)
        );

        if(withEdgePattern){
            createRadiusForPSTFromPattern(pattern[pattern.length - 1], edge_radius, 4, 4, PST);
            requiredPatterns--;
        }

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
                        if(edge_y >= 2){
                            if(canAnyPieceInSourceReachTarget(board, 's', dx, dy, 0, 8, 2, edge_y - 1, edge_x, edge_x, 2, edge_y - 1)){
                                score += count * kingMoves;
                            }
                        }
                    }else{
                        if(edge_y <= 6){
                            if(canAnyPieceInSourceReachTarget(board, 's', dx, dy, 0, 8, edge_y + 1, 6, edge_x, edge_x, edge_y + 1, 6)){
                                score += count * kingMoves;
                            }
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
                    //up
                    if(!isSquareRestricted(board, edge_x, edge_y, -1, 0)){
                        if(edge_x >= 2){
                            if(canAnyPieceInSourceReachTarget(board, 's', dx, dy, 2, edge_x - 1, 0, 8, 2, edge_x - 1, edge_y, edge_y)){
                                score += count * kingMoves;
                            }
                        }
                        //down
                    }else{
                        if(edge_x <= 6){
                            if(canAnyPieceInSourceReachTarget(board, 's', dx, dy, edge_x + 1, 6, 0, 8, edge_x + 1, 6, edge_y, edge_y)){
                                score += count * kingMoves;
                            }
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

    private static void extractPieces(char[][] board) {

        int wIndex = 0;
        int bIndex = 0;

        boolean kingFound = false;

        for (int i = 0; i < board.length; i++) {
            char[] row = board[i];
            for (int j = 0; j < row.length; j++) {
                char c = row[j];

                if (c == 'k' && !kingFound) {
                    kingSquare[0] = i;
                    kingSquare[1] = j;
                    kingFound = true;
                }
                else if (c == 'w') {
                    whiteSquares[wIndex++] = i;
                    whiteSquares[wIndex++] = j;
                }
                else if (c == 's') {
                    blackSquares[bIndex++] = i;
                    blackSquares[bIndex++] = j;
                }
            }
        }
        whiteCount = wIndex;
        blackCount = bIndex;
    }

    public void debugEvaluation(Board board) {
        char[][] b = board.getBoard();

        // ===== WHITE =====
        int whitePST = whitePST(b);
        int whiteMaterial = W_WHITE_MATERIAL * whiteMaterial();
        int kingProgress = W_KING_PROGRESS * kingEscapeScore(b);
        int kingEdgeSecure = W_KING_EDGE_SECURE * secureKingOnEdge(b);
        int winningThreat = W_WINNING_THREAT * threatensWin(board);
        int kingMobility = W_KING_MOBILITY * kingMobility();

        int totalWhite =
                whitePST +
                        whiteMaterial +
                        kingProgress +
                        kingEdgeSecure +
                        winningThreat +
                        kingMobility;

        // ===== BLACK =====
        int blackPST = blackPST(b);
        int blackMaterial = W_BLACK_MATERIAL * blackMaterial();
        int edgesSecure = W_EDGES_SECURE_SCORE * edgesSecureScore(b);
        int edgesBlocked = W_EDGES_ACCESS_BLOCKED * edgesAccessBlocked(b);
        int checkmateThreat = W_CHECKMATE_THREAT * threatensCheckmate(board);
        int checkmateScore = W_CHECKMATE_SCORE * checkmateScore(board);

        int totalBlack =
                blackPST +
                        blackMaterial +
                        edgesSecure +
                        edgesBlocked +
                        checkmateThreat +
                        checkmateScore;

        int finalScore = totalWhite - totalBlack;

        // ===== OUTPUT =====
        System.out.println("\n========== DEBUG EVALUATION ==========");

        System.out.println("WHITE:");
        System.out.printf("  PST                : %6d%n", whitePST);
        System.out.printf("  Material           : %6d%n", whiteMaterial);
        System.out.printf("  King Progress      : %6d%n", kingProgress);
        System.out.printf("  King Edge Secure   : %6d%n", kingEdgeSecure);
        System.out.printf("  Winning Threat     : %6d%n", winningThreat);
        System.out.printf("  King Mobility      : %6d%n", kingMobility);
        System.out.printf("  -> TOTAL WHITE     : %6d%n", totalWhite);

        System.out.println();

        System.out.println("BLACK:");
        System.out.printf("  PST                : %6d%n", blackPST);
        System.out.printf("  Material           : %6d%n", blackMaterial);
        System.out.printf("  Edges Secure       : %6d%n", edgesSecure);
        System.out.printf("  Edges Blocked      : %6d%n", edgesBlocked);
        System.out.printf("  Checkmate Threat   : %6d%n", checkmateThreat);
        System.out.printf("  Checkmate Score    : %6d%n", checkmateScore);
        System.out.printf("  -> TOTAL BLACK     : %6d%n", totalBlack);

        System.out.println();

        System.out.printf("FINAL SCORE (W - B)  : %6d%n", finalScore);
        System.out.println("======================================\n");

        finalGameScore = finalScore;
    }

    @Override
    public int getScore(Board board) {
        return finalGameScore;
    }
}
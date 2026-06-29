public class TestCountRadiusDrawPST {

    public static void main(String[] args) {

        // --- Board Setup ---
         char[][] board = new char[][]{
                { 'x', '-', '-', '-', '-', '-', '-', '-', 'x' },
                { '-', '-', '-', '-', '-', '-', '-', '-', '-' },
                { '-', '-', 's', 's', 's', 's', 's', '-', '-' },
                { '-', '-', 's', '-', '-', '-', 's', '-', '-' },
                { '-', '-', 's', '-', 'k', '-', 's', '-', '-' },
                { '-', '-', 's', '-', '-', '-', 's', '-', '-' },
                { '-', '-', 's', 's', 's', 's', 's', '-', '-' },
                { '-', '-', '-', '-', '-', '-', '-', '-', '-' },
                { 'x', '-', '-', '-', '-', '-', '-', '-', 'x' }
        };

        // --- Pattern Injection ---
        int[][] pattern = {
                {4, 5, 4},
                {0, 3, 2, 3, 0},
                {0, 0, 2, 2, 2, 0, 0},
                {0, 0, 0, 2, 2, 2, 0, 0, 0},
                {0, 0, 0, 0, 2, 2, 2, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 2, 2, 2, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 2, 2, 2, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 2, 2, 2, 0, 0, 0, 0, 0, 0, 0}
        };

        // --- PST ---
        int[][] PST = new int[board.length][board[0].length];

        // --- Parameter ---
        int x = 2;
        int y = 2;

        int max = board.length - 1;

        int distLeft   = y;
        int distRight  = max - y;
        int distTop    = x;
        int distBottom = max - x;

        int farthest = Math.max(
                Math.max(distLeft, distRight),
                Math.max(distTop, distBottom)
        );
        System.out.println("Wie viele Pattern sind nötig?: " + farthest);

        char target = 's';
        int count = 0;
        for (int i = 0; i < farthest; i++){
            count += countRadiusDrawPST(board, pattern[i], target, i + 1, x, y, PST);
        }

        System.out.println("Value of count: " + count);
        printBoard(board);
        printPST(PST);
    }

    // =========================================================
    // === AUSGELAGERTE TEST-FUNKTION (kann später gelöscht werden)
    // =========================================================

    private static int countRadiusDrawPST(char[][] board, int[] pattern, char target,
                                          int radius, int x, int y, int[][] PST) {

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
            count += debugCountPiecesDrawPST(board, pattern, offset_y, target,
                    rowLength, top_x, left_y, 0, 1, PST);
        }
        if (!ignoreBottomRow) {
            count += debugCountPiecesDrawPST(board, pattern, offset_y, target,
                    rowLength, bottom_x, left_y, 0, 1, PST);
        }
        if (!ignoreLeftColumn) {
            count += debugCountPiecesDrawPST(board, pattern, offset_x, target,
                    columnLength, top_x, left_y, 1, 0, PST);
        }
        if (!ignoreRightColumn) {
            count += debugCountPiecesDrawPST(board, pattern, offset_x, target,
                    columnLength, top_x, right_y, 1, 0, PST);
        }

        return count;
    }

    // =========================================================
    // === DEBUG / SIMULATIONS-LOGIK
    // =========================================================

    private static int debugCountPiecesDrawPST(char[][] board, int[] pattern, int startIdx, char target, int squares,
                                        int x, int y, int dx, int dy, int[][] PST) {

        int score = 0;
        int steps = 0;

        while (x >= 0 && x < 9 && y >= 0 && y < 9 && steps <= squares) {

            PST[x][y] = pattern[startIdx];
            if (board[x][y] == target) {
                score += pattern[startIdx];
            }

            x += dx;
            y += dy;
            steps++;
            startIdx++;
        }

        return score;
    }

    // =========================================================
    // === OUTPUT
    // =========================================================

    public static void printBoard(char[][] board) {
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
        System.out.println(sb);
    }

    public static void printPST(int[][] pst) {
        System.out.println("PST:");

        for (int y = 0; y < pst.length; y++) {
            for (int x = 0; x < pst[0].length; x++) {
                System.out.printf("%3d ", pst[y][x]);
            }
            System.out.println();
        }
    }
}
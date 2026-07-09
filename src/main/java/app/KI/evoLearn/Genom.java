package app.KI.evoLearn;

import lombok.Getter;
@Getter
public class Genom {

    // ==========================================
    // 0. Die 3 9x9 Arrays die sich aus den 5x5 ergeben
    // ==========================================
    @Getter
    private final int[][] KING_PST_9x9;
    @Getter
    private final int[][] WHITE_PST_9x9;
    @Getter
    private final int[][] BLACK_PST_9x9;
    // ==========================================
    // 1. Die 3 5x5 Arrays (Piece Square Tables)
    // ==========================================
    @Getter
    private  Bewertungsarray KING_PST;
    @Getter
    private  Bewertungsarray WHITE_PST;
    @Getter
    private  Bewertungsarray BLACK_PST;

    // ==========================================
    // 2. Die 75 einzelnen Feld-Variablen
    // ==========================================
    // KING_PST Felder
    private final int KING_PST_R0C1, KING_PST_R0C2, KING_PST_R0C3, KING_PST_R0C4;
    private final int KING_PST_R1C1, KING_PST_R1C2, KING_PST_R1C3, KING_PST_R1C4;
    private final int KING_PST_R2C2, KING_PST_R2C3, KING_PST_R2C4;
    private final int KING_PST_R3C3, KING_PST_R3C4;
    private final int KING_PST_R4C4;

    // WHITE_PST Felder
    private final int WHITE_PST_R0C1, WHITE_PST_R0C2, WHITE_PST_R0C3, WHITE_PST_R0C4;
    private final int WHITE_PST_R1C1, WHITE_PST_R1C2, WHITE_PST_R1C3, WHITE_PST_R1C4;
    private final int WHITE_PST_R2C2, WHITE_PST_R2C3, WHITE_PST_R2C4;
    private final int WHITE_PST_R3C3, WHITE_PST_R3C4;
    private final int WHITE_PST_R4C4;

    // BLACK_PST Felder
    private final int BLACK_PST_R0C1, BLACK_PST_R0C2, BLACK_PST_R0C3, BLACK_PST_R0C4;
    private final int BLACK_PST_R1C1, BLACK_PST_R1C2, BLACK_PST_R1C3, BLACK_PST_R1C4;
    private final int BLACK_PST_R2C2, BLACK_PST_R2C3, BLACK_PST_R2C4;
    private final int BLACK_PST_R3C3, BLACK_PST_R3C4;
    private final int BLACK_PST_R4C4;

    // ==========================================
    // 3. Allgemeine Gewichte
    // ==========================================
    @Getter
    private final int W_WHITE_GOAL;
    @Getter
    private final int W_BLACK_GOAL;

    // ==========================================
    // 4. WHITE FEATURE WEIGHTS
    // ==========================================
    @Getter
    private final int W_KING_PROGRESS;
    @Getter
    private final int W_CORNER;
    @Getter
    private final int W_KING_MOBILITY;
    @Getter
    private final int W_WHITE_MATERIAL;
    @Getter
    private final int W_WHITE_PST;
    @Getter
    private final int W_WHITE_PST_THREAT;
    @Getter
    private final int W_KING_EDGE_ACCESS;
    @Getter
    private final int W_KING_EDGE_SECURE;
    private final int W_WINNING_THREAT;

    // ==========================================
    // 5. BLACK FEATURE WEIGHTS
    // ==========================================
    @Getter
    private final int W_EDGES_SECURE_SCORE;
    @Getter
    private final int W_EDGES_ACCESS_BLOCKED;
    @Getter
    private final int W_CHECKMATE_SCORE;
    @Getter
    private final int W_CHECKMATE_THREAT;
    @Getter
    private final int W_BLACK_MATERIAL;
    @Getter
    private final int W_BLACK_PST;
    @Getter
    private final int W_BLACK_PST_THREAT;

    // ==========================================
    // Konstruktor
    // ==========================================
    public Genom(
            // Parameter für KING_PST (25 Werte)
            int king_r0c1, int king_r0c2, int king_r0c3, int king_r0c4,
            int king_r1c1, int king_r1c2, int king_r1c3, int king_r1c4,
            int king_r2c2, int king_r2c3, int king_r2c4,
            int king_r3c3, int king_r3c4,
            int king_r4c4,

            // Parameter für WHITE_PST (25 Werte)
            int white_r0c1, int white_r0c2, int white_r0c3, int white_r0c4,
            int white_r1c1, int white_r1c2, int white_r1c3, int white_r1c4,
            int white_r2c2, int white_r2c3, int white_r2c4,
            int white_r3c3, int white_r3c4,
            int white_r4c4,

            // Parameter für BLACK_PST (25 Werte)
            int black_r0c1, int black_r0c2, int black_r0c3, int black_r0c4,
            int black_r1c1, int black_r1c2, int black_r1c3, int black_r1c4,
            int black_r2c2, int black_r2c3, int black_r2c4,
            int black_r3c3, int black_r3c4,
            int black_r4c4,

            // Allgemeine Gewichte
            int w_white_goal, int w_black_goal,

            // White Feature Weights
            int w_king_progress, int w_corner, int w_king_mobility, int w_white_material,
            int w_white_pst, int w_white_pst_threat, int w_king_edge_access, int w_king_edge_secure,
            int w_winning_threat,

            // Black Feature Weights
            int w_edges_secure_score, int w_edges_access_blocked, int w_checkmate_score,
            int w_checkmate_threat, int w_black_material, int w_black_pst, int w_black_pst_threat
    ) {
        // Zuweisung an die 75 einzelnen Variablen
        this.KING_PST_R0C1 = king_r0c1;
        this.KING_PST_R0C2 = king_r0c2;
        this.KING_PST_R0C3 = king_r0c3;
        this.KING_PST_R0C4 = king_r0c4;
        this.KING_PST_R1C1 = king_r1c1;
        this.KING_PST_R1C2 = king_r1c2;
        this.KING_PST_R1C3 = king_r1c3;
        this.KING_PST_R1C4 = king_r1c4;
        this.KING_PST_R2C2 = king_r2c2;
        this.KING_PST_R2C3 = king_r2c3;
        this.KING_PST_R2C4 = king_r2c4;
        this.KING_PST_R3C3 = king_r3c3;
        this.KING_PST_R3C4 = king_r3c4;
        this.KING_PST_R4C4 = king_r4c4;

        this.WHITE_PST_R0C1 = white_r0c1;
        this.WHITE_PST_R0C2 = white_r0c2;
        this.WHITE_PST_R0C3 = white_r0c3;
        this.WHITE_PST_R0C4 = white_r0c4;
        this.WHITE_PST_R1C1 = white_r1c1;
        this.WHITE_PST_R1C2 = white_r1c2;
        this.WHITE_PST_R1C3 = white_r1c3;
        this.WHITE_PST_R1C4 = white_r1c4;
        this.WHITE_PST_R2C2 = white_r2c2;
        this.WHITE_PST_R2C3 = white_r2c3;
        this.WHITE_PST_R2C4 = white_r2c4;
        this.WHITE_PST_R3C3 = white_r3c3;
        this.WHITE_PST_R3C4 = white_r3c4;
        this.WHITE_PST_R4C4 = white_r4c4;

        this.BLACK_PST_R0C1 = black_r0c1;
        this.BLACK_PST_R0C2 = black_r0c2;
        this.BLACK_PST_R0C3 = black_r0c3;
        this.BLACK_PST_R0C4 = black_r0c4;
        this.BLACK_PST_R1C1 = black_r1c1;
        this.BLACK_PST_R1C2 = black_r1c2;
        this.BLACK_PST_R1C3 = black_r1c3;
        this.BLACK_PST_R1C4 = black_r1c4;
        this.BLACK_PST_R2C2 = black_r2c2;
        this.BLACK_PST_R2C3 = black_r2c3;
        this.BLACK_PST_R2C4 = black_r2c4;
        this.BLACK_PST_R3C3 = black_r3c3;
        this.BLACK_PST_R3C4 = black_r3c4;
        this.BLACK_PST_R4C4 = black_r4c4;

        // Erstellung der 5x5 Arrays
        this.KING_PST = new Bewertungsarray(
                999, KING_PST_R0C1, KING_PST_R0C2, KING_PST_R0C3, KING_PST_R0C4,
                0, KING_PST_R1C1, KING_PST_R1C2, KING_PST_R1C3, KING_PST_R1C4,
                0, 0, KING_PST_R2C2, KING_PST_R2C3, KING_PST_R2C4,
                0, 0, 0, KING_PST_R3C3, KING_PST_R3C4,
                0, 0, 0, 0, KING_PST_R4C4
        );

        this.WHITE_PST = new Bewertungsarray(
                -999, WHITE_PST_R0C1, WHITE_PST_R0C2, WHITE_PST_R0C3, WHITE_PST_R0C4,
                0, WHITE_PST_R1C1, WHITE_PST_R1C2, WHITE_PST_R1C3, WHITE_PST_R1C4,
                0, 0, WHITE_PST_R2C2, WHITE_PST_R2C3, WHITE_PST_R2C4,
                0, 0, 0, WHITE_PST_R3C3, WHITE_PST_R3C4,
                0, 0, 0, 0, WHITE_PST_R4C4);

        this.BLACK_PST = new Bewertungsarray(
                -999, BLACK_PST_R0C1, BLACK_PST_R0C2, BLACK_PST_R0C3, BLACK_PST_R0C4,
                0, BLACK_PST_R1C1, BLACK_PST_R1C2, BLACK_PST_R1C3, BLACK_PST_R1C4,
                0, 0, BLACK_PST_R2C2, BLACK_PST_R2C3, BLACK_PST_R2C4,
                0, 0, 0, BLACK_PST_R3C3, BLACK_PST_R3C4,
                0, 0, 0, 0, BLACK_PST_R4C4);

        // Gewichte zuweisen
        this.W_WHITE_GOAL = w_white_goal;
        this.W_BLACK_GOAL = w_black_goal;
        this.W_KING_PROGRESS = w_king_progress;
        this.W_CORNER = w_corner;
        this.W_KING_MOBILITY = w_king_mobility;
        this.W_WHITE_MATERIAL = w_white_material;
        this.W_WHITE_PST = w_white_pst;
        this.W_WHITE_PST_THREAT = w_white_pst_threat;
        this.W_KING_EDGE_ACCESS = w_king_edge_access;
        this.W_KING_EDGE_SECURE = w_king_edge_secure;
        this.W_WINNING_THREAT = w_winning_threat;
        this.W_EDGES_SECURE_SCORE = w_edges_secure_score;
        this.W_EDGES_ACCESS_BLOCKED = w_edges_access_blocked;
        this.W_CHECKMATE_SCORE = w_checkmate_score;
        this.W_CHECKMATE_THREAT = w_checkmate_threat;
        this.W_BLACK_MATERIAL = w_black_material;
        this.W_BLACK_PST = w_black_pst;
        this.W_BLACK_PST_THREAT = w_black_pst_threat;

        this.BLACK_PST_9x9 = Bewertungsarray.flipIt(Bewertungsarray.flipItHalf(BLACK_PST.getGrid()));
        this.WHITE_PST_9x9 = Bewertungsarray.flipIt(Bewertungsarray.flipItHalf(WHITE_PST.getGrid()));
        this.KING_PST_9x9 = Bewertungsarray.flipIt(Bewertungsarray.flipItHalf(KING_PST.getGrid()));

    }
}

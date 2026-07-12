package app.KI.evoLearn;

import java.util.List;
import java.util.Random;

/**
 * Kapselt die evolutionäre Fortpflanzungslogik (Crossover + Mutation)
 * getrennt für Weiß- und Schwarz-Population.
 *
 * Genom-Feldreihenfolge (58 Parameter gesamt):
 *   King_PST (14) | White_PST (13) | Black_PST (13) | w_white_goal | w_black_goal
 *   | White-Gewichte (9) | Black-Gewichte (7)
 *
 * Weiß-relevante Gene (37): King_PST(14) + White_PST(13) + w_white_goal(1) + White-Gewichte(9)
 * Schwarz-relevante Gene (21): Black_PST(13) + w_black_goal(1) + Black-Gewichte(7)
 */
public class EvoBreeder {

    private static final Random random = new Random();

    // Wahrscheinlichkeit, dass ein einzelnes Gen mutiert (kleine Mutation)
    private static final double MUTATION_RATE = 0.10;
    // Maximale Abweichung (+/-) bei einer kleinen Mutation
    private static final int MUTATION_STRENGTH = 2;

    // Wahrscheinlichkeit für eine seltene, starke Mutation (ermöglicht Vorzeichenwechsel
    // und größere Sprünge, damit die Population nicht dauerhaft im positiven/negativen
    // Bereich "hängen bleibt", nur weil die Startwerte dort lagen)
    private static final double BIG_MUTATION_RATE = 0.02;
    private static final int BIG_MUTATION_STRENGTH = 10;

    // Mindestwert für Gewichte, die direkt Sieg-/Schach-Situationen bewerten.
    // Diese Gewichte werden sowohl in EvoBF.evaluate() (aktive Alpha-Beta-Suche)
    // als auch in getScore() (Fitness) verwendet - ein negativer Wert würde die
    // Suche aktiv dazu bringen, drohende Siege/Schachs zu MEIDEN statt anzustreben.
    // Daher werden diese drei Gewichte nach jeder Mutation auf mindestens 1 geklemmt.
    private static final int MIN_CRITICAL_WEIGHT = 1;
    // Index von w_winning_threat im Weiß-Gene-Array (siehe toWhiteGeneArray)
    private static final int WHITE_IDX_WINNING_THREAT = 36;
    // Indizes von w_checkmate_score / w_checkmate_threat im Schwarz-Gene-Array (siehe toBlackGeneArray)
    private static final int BLACK_IDX_CHECKMATE_SCORE = 16;
    private static final int BLACK_IDX_CHECKMATE_THREAT = 17;

    // ==========================================
    // Öffentliche Einstiegspunkte
    // ==========================================

    public static Genom breedWhite(Genom parentA, Genom parentB) {
        int[] childGenes = crossover(toWhiteGeneArray(parentA), toWhiteGeneArray(parentB));
        mutate(childGenes);
        childGenes[WHITE_IDX_WINNING_THREAT] = Math.max(MIN_CRITICAL_WEIGHT, childGenes[WHITE_IDX_WINNING_THREAT]);
        return buildWhiteGenom(childGenes);
    }

    public static Genom breedBlack(Genom parentA, Genom parentB) {
        int[] childGenes = crossover(toBlackGeneArray(parentA), toBlackGeneArray(parentB));
        mutate(childGenes);
        childGenes[BLACK_IDX_CHECKMATE_SCORE] = Math.max(MIN_CRITICAL_WEIGHT, childGenes[BLACK_IDX_CHECKMATE_SCORE]);
        childGenes[BLACK_IDX_CHECKMATE_THREAT] = Math.max(MIN_CRITICAL_WEIGHT, childGenes[BLACK_IDX_CHECKMATE_THREAT]);
        return buildBlackGenom(childGenes);
    }

    /**
     * Erzeugt eine komplette neue Generation für eine Farbe:
     * - die besten {@code eliteCount} Individuen werden unverändert übernommen (Elitismus)
     * - der Rest wird durch Crossover + Mutation aus den besten {@code breedingPoolSize}
     *   Individuen der aktuellen Generation gezüchtet
     *
     * Erwartet, dass currentPop bereits nach Fitness absteigend sortiert ist
     * (siehe EvoMain.sortAILists).
     */
    public static List<EvoKi> breedGeneration(List<EvoKi> currentPop, boolean isWhite,
                                              int eliteCount, int breedingPoolSize) {
        int populationSize = currentPop.size();
        List<EvoKi> newGeneration = new java.util.ArrayList<>();

        // 1. Elitismus: Top N unverändert übernehmen (als frisches EvoKi, fitness/winrate = 0)
        for (int i = 0; i < eliteCount && i < currentPop.size(); i++) {
            newGeneration.add(new EvoKi(currentPop.get(i).genom));
        }

        // 2. Eltern-Pool: die besten n Individuen dürfen sich fortpflanzen
        int poolSize = Math.min(breedingPoolSize, currentPop.size());
        List<EvoKi> breedingPool = currentPop.subList(0, poolSize);

        // 3. Auffüllen bis zur vollen Populationsgröße
        while (newGeneration.size() < populationSize) {
            EvoKi parentA = breedingPool.get(random.nextInt(breedingPool.size()));
            EvoKi parentB = breedingPool.get(random.nextInt(breedingPool.size()));

            // vermeiden, dass sich ein Individuum mit sich selbst paart (falls Pool > 1)
            int attempts = 0;
            while (parentA == parentB && breedingPool.size() > 1 && attempts < 5) {
                parentB = breedingPool.get(random.nextInt(breedingPool.size()));
                attempts++;
            }

            Genom childGenom = isWhite
                    ? breedWhite(parentA.genom, parentB.genom)
                    : breedBlack(parentA.genom, parentB.genom);

            newGeneration.add(new EvoKi(childGenom));
        }

        return newGeneration;
    }

    // ==========================================
    // Crossover & Mutation (generisch auf int[])
    // ==========================================

    /** Uniform Crossover: pro Gen 50/50 Chance auf Elternteil A oder B */
    private static int[] crossover(int[] genesA, int[] genesB) {
        int[] child = new int[genesA.length];
        for (int i = 0; i < genesA.length; i++) {
            child[i] = random.nextBoolean() ? genesA[i] : genesB[i];
        }
        return child;
    }

    private static void mutate(int[] genes) {
        for (int i = 0; i < genes.length; i++) {
            double roll = random.nextDouble();
            if (roll < BIG_MUTATION_RATE) {
                // seltene, starke Mutation: erlaubt groessere Spruenge inkl. Vorzeichenwechsel,
                // damit die Population nicht dauerhaft im positiven Bereich haengen bleibt
                int delta = random.nextInt(2 * BIG_MUTATION_STRENGTH + 1) - BIG_MUTATION_STRENGTH;
                genes[i] += delta;
            } else if (roll < BIG_MUTATION_RATE + MUTATION_RATE) {
                // normale, kleine Mutation zur Feinabstimmung
                int delta = random.nextInt(2 * MUTATION_STRENGTH + 1) - MUTATION_STRENGTH;
                genes[i] += delta;
            }
        }
    }

    // ==========================================
    // Genom <-> Array Konvertierung
    // ==========================================

    // Reihenfolge: King(14), White_PST(13), w_white_goal(1), White-Gewichte(9) = 37
    private static int[] toWhiteGeneArray(Genom g) {
        return new int[]{
                g.getKING_PST_R0C1(), g.getKING_PST_R0C2(), g.getKING_PST_R0C3(), g.getKING_PST_R0C4(),
                g.getKING_PST_R1C1(), g.getKING_PST_R1C2(), g.getKING_PST_R1C3(), g.getKING_PST_R1C4(),
                g.getKING_PST_R2C2(), g.getKING_PST_R2C3(), g.getKING_PST_R2C4(),
                g.getKING_PST_R3C3(), g.getKING_PST_R3C4(),
                g.getKING_PST_R4C4(),

                g.getWHITE_PST_R0C1(), g.getWHITE_PST_R0C2(), g.getWHITE_PST_R0C3(), g.getWHITE_PST_R0C4(),
                g.getWHITE_PST_R1C1(), g.getWHITE_PST_R1C2(), g.getWHITE_PST_R1C3(), g.getWHITE_PST_R1C4(),
                g.getWHITE_PST_R2C2(), g.getWHITE_PST_R2C3(), g.getWHITE_PST_R2C4(),
                g.getWHITE_PST_R3C3(), g.getWHITE_PST_R3C4(),

                g.getW_WHITE_GOAL(),

                g.getW_KING_PROGRESS(), g.getW_CORNER(), g.getW_KING_MOBILITY(), g.getW_WHITE_MATERIAL(),
                g.getW_WHITE_PST(), g.getW_WHITE_PST_THREAT(), g.getW_KING_EDGE_ACCESS(),
                g.getW_KING_EDGE_SECURE(), g.getW_WINNING_THREAT()
        };
    }

    // Reihenfolge: Black_PST(13), w_black_goal(1), Black-Gewichte(7) = 21
    private static int[] toBlackGeneArray(Genom g) {
        return new int[]{
                g.getBLACK_PST_R0C1(), g.getBLACK_PST_R0C2(), g.getBLACK_PST_R0C3(), g.getBLACK_PST_R0C4(),
                g.getBLACK_PST_R1C1(), g.getBLACK_PST_R1C2(), g.getBLACK_PST_R1C3(), g.getBLACK_PST_R1C4(),
                g.getBLACK_PST_R2C2(), g.getBLACK_PST_R2C3(), g.getBLACK_PST_R2C4(),
                g.getBLACK_PST_R3C3(), g.getBLACK_PST_R3C4(),

                g.getW_BLACK_GOAL(),

                g.getW_EDGES_SECURE_SCORE(), g.getW_EDGES_ACCESS_BLOCKED(), g.getW_CHECKMATE_SCORE(),
                g.getW_CHECKMATE_THREAT(), g.getW_BLACK_MATERIAL(), g.getW_BLACK_PST(),
                g.getW_BLACK_PST_THREAT()
        };
    }

    // baut ein volles Genom, Schwarz-Seite bleibt neutral (0)
    private static Genom buildWhiteGenom(int[] w) {
        return new Genom(
                w[0], w[1], w[2], w[3], w[4], w[5], w[6], w[7], w[8], w[9], w[10], w[11], w[12], w[13],
                w[14], w[15], w[16], w[17], w[18], w[19], w[20], w[21], w[22], w[23], w[24], w[25], w[26],
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // Black_PST neutral (13)
                w[27], 0, // w_white_goal, w_black_goal neutral
                w[28], w[29], w[30], w[31], w[32], w[33], w[34], w[35], w[36],
                0, 0, 0, 0, 0, 0, 0 // Black-Gewichte neutral (7)
        );
    }

    // baut ein volles Genom, Weiß-Seite bleibt neutral (0)
    private static Genom buildBlackGenom(int[] b) {
        return new Genom(
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // King_PST neutral (14)
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,    // White_PST neutral (13)
                b[0], b[1], b[2], b[3], b[4], b[5], b[6], b[7], b[8], b[9], b[10], b[11], b[12],
                0, b[13], // w_white_goal neutral, w_black_goal
                0, 0, 0, 0, 0, 0, 0, 0, 0, // White-Gewichte neutral (9)
                b[14], b[15], b[16], b[17], b[18], b[19], b[20]
        );
    }
}
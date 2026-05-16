package app.board;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.lang.Math.abs;

public class Zuggenerator {

    public List<Zug> getAllLegalMoves(char[][] board, boolean isWhiteToMove){

        List<Zug> possibleMoves = new ArrayList<>();

        for (int i = 0; i < board.length; i++){
            for (int y = 0; y < board[i].length; y++){

                char piece = board[i][y];
                // only calculate possible moves for relevant pieces:
                if (isWhiteToMove && (piece == 'w' || piece == 'k')) {
                    possibleMoves.addAll(getPossibleMoves(board, i, y));
                }
                if (!isWhiteToMove && piece == 's') {
                    possibleMoves.addAll(getPossibleMoves(board, i, y));
                }
            }
        }
        return possibleMoves;
    }

    public List<Zug> getPossibleMoves(char [][] board, int x, int y){
        List<Zug> moves = new ArrayList<>();
        char piece = board[x][y];

        int size = board.length;
        int throneX = 4;
        int throneY = 4;

        int[][] directions = {
                {1,0}, {-1,0}, {0,1}, {0,-1}
        };

        for (int[] dir : directions) {
            int runningX = x + dir[0];
            int runningY = y + dir[1];

            while (runningX >= 0 && runningX < size && runningY >= 0 && runningY < size){

                char target = board[runningX][runningY];
                if (target == 's' || target == 'w' || target == 'k') break;
                if (target == 'x' && piece != 'k') break;

                if (runningX == throneX && runningY == throneY) {
                    runningX += dir[0];
                    runningY += dir[1];
                    continue;
                }

                moves.add(new Zug(
                        (char)('a' + y),
                        (1 + (size - 1 - x)),
                        (char)('a' + runningY),
                        1 + (size - 1 - runningX),
                        piece
                ));
                runningX += dir[0];
                runningY += dir[1];
            }
        }
        return moves;
    }

    /**
     * Berechnet Spielfortschritt des Spiels zu einem Index von 0 bis 1
     * aktuelle Gewichtung:
     * Abweichgung startfeld 40,5%
     * KÖnigsfortschritt 40%
     * FigurenGleichgewicht 25%
     * @param board
     * @return
     */
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

    public int evaluate(Board board, boolean isWhiteToMove){
        int score = 0;

        //FigurenGleichgewicht und SteinCounter für SpielfortschrittIndex
        String arrString= Arrays.toString(board.getBoard());
        int wcount = count('w', arrString) *3;
        int scount = count('s', arrString) *2;
        int kcount = arrString.contains("k")?8:-999;

        int figurenGleichgewicht = (wcount + kcount) - scount;

        score +=figurenGleichgewicht;
        score += fieldValue(board, isWhiteToMove);
        return score;
    }

    /**
     * 32:32 Gleichgewicht, schwarzer Stein = 2, w=3, K=12
     * @param board
     * @return
     */
    @Deprecated
    public int figurenGleichgewicht(Board board){
    String arrString= Arrays.toString(board.getBoard());
        int wcount = count('w', arrString) *3;
        int scount = count('s', arrString) *2;
        int kcount = arrString.contains("k")?8:-999;

        return (wcount + kcount) - scount;
    }

    /**
     * hilfsfunktion für Berechnung von figurenGleichgewicht, zählt Anzahl Steine in String
     * @param c
     * @param s
     * @return
     */
    public int count(char c, String s) {
        int count = 0;
        for(int i=0;i<s.length();i++){
            if(s.charAt(i)==c) count++;
        }
        return count;
    }

    /**
     * Berechnet Vorteilhaftigkeit des Spielfelds
     * @param board
     * @return
     */
    public int fieldValue(Board board,boolean isWhiteToMove){
        int score = 0;
        //position des Königs Fortschritt am Board
        int[][] bewertungsMatrix = {
                {999,  10,  10,   8,   6,   8,  10,  10, 999},
                { 10,   8,   8,   5,   4,   5,   8,   8,  10},
                { 10,   8,   1,   1,   1,   1,   1,   8,  10},
                {  8,   5,   1,   3,   3,   3,   1,   5,   8},
                {  6,   4,   1,   3,   5,   3,   1,   4,   6},
                {  8,   5,   1,   3,   3,   3,   1,   5,   8},
                { 10,   8,   1,   1,   1,   1,   1,   8,  10},
                { 10,   8,   8,   5,   4,   5,   8,   8,  10},
                {999,  10,  10,   8,   6,   8,  10,  10, 999}
        };
        int[] kcords= findCharPosition(board.getBoard(), 'k');
        score += bewertungsMatrix[kcords[0]][kcords[1]];

        //Position der weißen Steine am Board
        int[] wcords= findCharPosition(board.getBoard(), 'w');
        return score;
    }
    /**
     * findet die Koordinaten eines Char auf dem Spielfeld
     */
    int[] findCharPosition(char[][] spielfeld, char gesuchterChar){
        ArrayList<Integer> koordinaten = new ArrayList<>();
        for (int zeile = 0; zeile < spielfeld.length; zeile++) {
            for (int spalte = 0; spalte < spielfeld[zeile].length; spalte++) {

                // Wenn der aktuelle Char dem gesuchten entspricht
                if (spielfeld[zeile][spalte] == gesuchterChar) {
                    // Koordinaten sofort als Array zurückgeben [Zeile, Spalte]
                    koordinaten.add(zeile);
                    koordinaten.add(spalte);
                }

            }
        }
        int[] koordinatenArray = new int[koordinaten.size()];
        for (int i = 0; i < koordinaten.size(); i=2) {
            koordinatenArray[i] = koordinaten.get(i);
        }
        return koordinatenArray;
    }
}

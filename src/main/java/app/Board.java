package app;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class Board {

    /**
     * 's' = schwarze Figur
     * 'w' = weiße Figur
     * 'k' = (weißer) König
     * 'x' = Eckfeld
     * '-' = leer
     */
    public char[][] board = new char[][]{
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

    public void printBoard(){

        StringBuilder sb = new StringBuilder();

        sb.append(' ');
        for (char c = 'A'; c <= 'I'; c++) sb.append(' ').append(c);

        for(int y=0; y < board.length; y++){
            sb.append('\n');
            sb.append(y+1);
            sb.append(' ');
            for(int x = 0; x < board[0].length; x++){
                sb.append(board[y][x]);
                sb.append(' ');
            }
        }
        sb.append('\n');
        System.out.println(sb);
    }
}

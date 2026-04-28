package app;

public class ArrayZuggenerator {
    char[][] board = {
            {'x','_','_','s','s','s','_','_','x'},
            {'_','_','_','_','s','_','_','_','_'},
            {'_','_','_','_','w','_','_','_','_'},
            {'s','_','_','_','w','_','_','_','s'},
            {'s','s','w','w','k','w','w','s','s'},
            {'s','_','_','_','w','_','_','_','s'},
            {'_','_','_','_','w','_','_','_','_'},
            {'_','_','_','_','s','_','_','_','_'},
            {'x','_','_','s','s','s','_','_','x'}
    }; //a1 unten links a9 oben links

    /**
     * gibt ein board zurück das den übergebenen Spielzug umgesetzt hat
     * @param input ein String aus 4 Zeichen der Startkoordinaten und Endkoordinaten beinhält
     * @return
     */
    public static char[][] move(char[][] board,String input) {
        int xFrom =  input.charAt(0) -97;
        int yFrom = input.charAt(1) -49;
        int xTo = input.charAt(2) -97;
        int yTo = input.charAt(3) -49;

        int xFrom2 =  input.charAt(0);
        int yFrom2 =  input.charAt(1);
        int xTo2 =  input.charAt(2);
        int yTo2 =  input.charAt(3);

        board[xTo][yTo] = board[xFrom][yFrom];
        board[xFrom][yFrom] = '_';

        //Schlaglogik eingeschlossen in 4 himmelsrichtungen, nicht gleicher stein oder x dann feld leer (geschlagen)

        if(xTo<=6&& board[xTo][yTo]==board[xTo+2][yTo] && board[xTo][yTo] != board[xTo+1][yTo] && board[xTo][yTo] != 'X'){board[xTo+1][yTo] = ' ';}
        if(xTo>=2&& board[xTo][yTo]==board[xTo-2][yTo] && board[xTo][yTo] != board[xTo-1][yTo] && board[xTo][yTo] != 'X'){board[xTo-1][yTo] = ' ';}
        if(yTo<=6&& board[xTo][yTo]==board[xTo][yTo+2] && board[xTo][yTo] != board[xTo][yTo+1] && board[xTo][yTo] != 'X'){board[xTo][yTo+1] = ' ';}
        if(yTo>=2&& board[xTo][yTo]==board[xTo][yTo-2] && board[xTo][yTo] != board[xTo][yTo-1] && board[xTo][yTo] != 'X'){board[xTo][yTo-1] = ' ';}
        return board;
    }

    public void printBoard(){

        StringBuilder sb = new StringBuilder();

        sb.append(' ');
        for (char c = 'A'; c <= 'I'; c++) sb.append(' ').append(c);

        for(int y=0;y<board.length;y++){
            sb.append('\n');
            sb.append(y+1);
            sb.append(' ');
            for(int x=0;x<board[0].length;x++){
                sb.append(board[y][x]);
                sb.append(' ');
            }
        }
        sb.append('\n');
        System.out.println(sb);
    }

}

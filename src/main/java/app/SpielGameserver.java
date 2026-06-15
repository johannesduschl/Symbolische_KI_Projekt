package app;

import app.KI.AlphaBetaKI;
import app.board.Board;
import app.board.Zug;
import app.board.Zuggenerator;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;

public class SpielGameserver {

    public Board board = new Board();
    Zuggenerator zuggenerator = new Zuggenerator();
    AlphaBetaKI ki = new AlphaBetaKI();
    boolean isWhiteToMove = false;
    public String fen = "3aaa3/4a4/4d4/a3d3a/aaddkddaa/a3d3a/4d4/4a4/3aaa3";

    public void startGame() {

        try (Socket socket = new Socket("127.0.0.1", 5000);
             OutputStream out = socket.getOutputStream();
             BufferedReader in = new BufferedReader(
                     new InputStreamReader(socket.getInputStream()))
        ) {

            Scanner scanner = new Scanner(System.in);
            String response = sendCommandToGameServer("gspy", out, in);

            if (!response.equals("ok")) throw new RuntimeException("Unexpected response from game server: " + response);


            String clientToken = sendCommandToGameServer("register", out, in);
            System.out.println("Client token: " + clientToken);

            sendCommandToGameServer("login " + clientToken, out, in);

            while(true) {
                System.out.println("Hallo was wollen sie tun? \n 1: join lobby \n 2: create lobby \n 3: search lobbies \n 4: leave lobby \n 5: quit");
                String input = scanner.next();
                if (input.equals("1")) {
                    System.out.println("Enter lobby name: ");
                    String lobbyName = scanner.next();
                    sendCommandToGameServer("join " + lobbyName, out, in);

                    System.out.println("welche Farbe sind wir?\n 1:weiß \n 2:schwarz");
                    if (scanner.next().equals("1")) { isWhiteToMove = true;}
                    else if (scanner.next().equals("2")) {isWhiteToMove = false;}
                    else{ System.out.println("Fehler");break;};

                }
                if (input.equals("2")) {
                    System.out.println("Enter lobby name: ");
                    String lobbyName = scanner.next();
                    sendCommandToGameServer("create " + lobbyName, out, in);
                    sendCommandToGameServer("set game.type tablut", out, in);
                }
                if (input.equals("3")) {
                    System.out.println("Alle lobbies: ");
                    sendCommandToGameServer("ls", out, in);
                }
                if (input.equals("4")) {
                    System.out.println("Leaving lobby!");
                    sendCommandToGameServer("leave ", out, in);
                }
                if (input.equals("5")) {
                    System.out.println("Bye!");
                    break;
                }
            }


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String sendCommandToGameServer(String msg, OutputStream out, BufferedReader in) throws IOException {
        System.out.println(msg);
        out.write((msg + "\n").getBytes());
        out.flush();

        String response = in.readLine();
        System.out.println("Response: " + response);
        return response;
    }
    public String getMove(String Fen){

    }
}

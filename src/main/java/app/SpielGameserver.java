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

            sendCommandToGameServer("login " + clientToken, out, in);

            System.out.println("Enter lobby name: ");
            String lobbyName = scanner.next();

            sendCommandToGameServer("create " +lobbyName, out, in);
            sendCommandToGameServer("join " + lobbyName, out, in);

            sendCommandToGameServer("set game.type tablut", out, in);


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
}

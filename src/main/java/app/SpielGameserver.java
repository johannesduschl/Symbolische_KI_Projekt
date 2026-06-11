package app;

import app.KI.AlphaBetaKI;
import app.board.Board;
import app.board.Zug;
import app.board.Zuggenerator;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
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

        try {

            Scanner scanner = new Scanner(System.in);
            String response = sendCommandToGameServer("gspy").get();

            if (!response.equals("ok")) throw new RuntimeException("Unexpected response from game server: " + response);

            String clientToken = sendCommandToGameServer("register").get();

            sendCommandToGameServer("login " + clientToken).get();

            System.out.println("Enter lobby name: ");
            String lobbyName = scanner.next();

            sendCommandToGameServer("create " +lobbyName);
            sendCommandToGameServer("join " + lobbyName);

            sendCommandToGameServer("set game.type tablut");



        } catch (Exception e) {
            System.out.println("Got exception in SpielGameserver: " + e.getMessage());
        }

        boolean isWhiteToMove = false;
        boolean isGameOver = false;

        while (!isGameOver) {

            List<Zug> possibleMoves = zuggenerator.getAllLegalMoves(this.board.getBoard(), isWhiteToMove);
            if (possibleMoves.isEmpty()) {
                System.out.println("No legal moves available!");
                break;
            }

            Zug chosenMove = ki.findBestMove(this.board, isWhiteToMove);
            System.out.println("Move for " + (isWhiteToMove ? "White" : "Black") + ": " + chosenMove);
            isGameOver = board.move(chosenMove);

            if (isGameOver) {
                this.board.printBoard();
                String winner = isWhiteToMove ? "White" : "Black";
                System.out.printf("Game is over. %s has won.", winner);
                break;
            }

            this.board.printBoard();

            isWhiteToMove = !isWhiteToMove;
        }
    }

    public CompletableFuture<String> sendCommandToGameServer(String msg) {
        return CompletableFuture.supplyAsync(() -> {
            try (Socket socket = new Socket("127.0.0.1", 5000);
                 OutputStream out = socket.getOutputStream();
                 BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))
            ) {

                out.write((msg + "\n").getBytes());
                out.flush();

                return in.readLine();

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
}

package app;

import app.KI.AlphaBetaKI;
import app.board.Board;
import app.FenParser;
import app.board.Zug;
import app.board.Zuggenerator;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class SpielGameserver {

    private Board board = new Board();
    private final Zuggenerator zuggenerator = new Zuggenerator();
    private final AlphaBetaKI ki = new AlphaBetaKI();
    private boolean isWhiteToMove = false;

    private boolean farbeGesetzt = false;
    private String ersterGegnerZug = null; // falls Weiß: erster move vom Server

    private OutputStream out;
    private BufferedReader in;

    // Halfmove-Zähler mitführen für FEN-Erzeugung
    private int halfmoveClock = 0;
    private int halfmoveCount = 0;

    public void startGame() {
        try (Socket socket = new Socket("bore.pub", 37403);
             OutputStream outStream = socket.getOutputStream();
             BufferedReader inReader = new BufferedReader(
                     new InputStreamReader(socket.getInputStream()))
        ) {
            this.out = outStream;
            this.in  = inReader;

            Scanner scanner = new Scanner(System.in);

            // ── Handshake ─────────────────────────────────────────────
            String response = send("gspy");
            if (!response.equals("ok"))
                throw new RuntimeException("Handshake fehlgeschlagen: " + response);

            // ── Authentifizierung ──────────────────────────────────────
            String token = send("register");
            System.out.println("Token: " + token);
            send("login " + token);

            // ── Lobby-Menü ─────────────────────────────────────────────
            boolean bereit = false;
            while (!bereit) {
                System.out.println("""
                    \n=== Menü ===
                    1: Lobby beitreten
                    2: Lobby erstellen
                    3: Lobbies anzeigen
                    4: Lobby verlassen
                    5: Beenden""");

                switch (scanner.next()) {
                    case "1" -> {
                        System.out.print("Lobby-Name: ");
                        String name = scanner.next();
                        String resp = send("join " + name);
                        if (resp.startsWith("err")) {
                            System.out.println("Fehler: " + resp);
                        } else {
                            bereit = true;
                        }
                    }
                    case "2" -> {
                        System.out.print("Lobby-Name: ");
                        String name = scanner.next();
                        send("create " + name);
                        send("set game.type tablut");

                        // Zeitkonto setzen
                        System.out.print("Zeit pro Spieler in Sekunden: ");
                        String zeit = scanner.next();
                        send("set game.time_account " + zeit);

                        System.out.println("'start' eingeben wenn bereit:");
                        if (scanner.next().equalsIgnoreCase("start"))
                            send("start");
                        bereit = true;
                    }
                    case "3" -> System.out.println("Lobbies: " + send("ls"));
                    case "4" -> send("leave");
                    case "5" -> { return; }
                }
            }

            // ── Spielstart-Handshake + Farberkennung ──────────────────────────────
            waitForGameStart();

            // ── Spielschleife ──────────────────────────────────────────
            playGame();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // ── Wartet auf config / set / ok vom Server ────────────────────────
    private void waitForGameStart() throws IOException {
        System.out.println("Warte auf Spielstart...");
        while (true) {
            String line = in.readLine();
            System.out.println("Server: " + line);

            if (line == null) throw new IOException("Verbindung unterbrochen");
            if (line.equals("config") || line.startsWith("set")) continue;

            if (line.equals("ok")) {
                String afterOk = send("ok");

                if (afterOk.equals("wait")) {
                    // Wir sind Weiß – Gegner (Schwarz) zieht zuerst
                    isWhiteToMove = true;
                    farbeGesetzt = true;
                    System.out.println("Farbe: Weiß – warte auf gegnerischen Zug...");
                } else {
                    // Wir sind Schwarz – sofort ziehen
                    isWhiteToMove = false;
                    farbeGesetzt = true;
                    System.out.println("Farbe: Schwarz – wir ziehen zuerst.");
                    // afterOk könnte bereits ein move sein, merken für playGame()
                    ersterGegnerZug = afterOk;
                }
                return;
            }
        }
    }

    private void playGame() throws IOException {
        board = new Board();

        if (!isWhiteToMove) {
            // ── Wir sind Schwarz – sofort ziehen ─────────────────────
            board.printBoard();
            if (macheZug()) return;
        }

        // ── Hauptschleife ─────────────────────────────────────────────
        while (true) {
            // Gegnerischen Zug empfangen
            String line = ersterGegnerZug != null ? ersterGegnerZug : in.readLine();
            ersterGegnerZug = null; // nur einmal verwenden
            System.out.println("Server: " + line);

            if (line == null || line.startsWith("over")) {
                System.out.println("Spiel beendet.");
                return;
            }

            if (line.startsWith("move ")) {
                // Gegnerischen Zug aufs Brett anwenden
                String moveData = line.substring(5).trim();
                Zug gegnerZug = serverFormatToZug(moveData);

                if (gegnerZug == null) {
                    System.out.println("Ungültiges Zugformat: " + moveData);
                    return;
                }

                System.out.println("Gegner zieht: " + gegnerZug);
                boolean gameOver = board.move(gegnerZug);
                board.printBoard();

                if (gameOver) {
                    System.out.println("Spiel vorbei – Gegner hat gewonnen.");
                    return;
                }

                // Jetzt sind wir dran
                if (macheZug()) return;

            } else if (looksLikeFen(line)) {
                // Fallback falls Server doch FEN schickt
                updateBoardFromFen(line);
                board.printBoard();
                if (board.isGameOver()) { System.out.println("Spiel vorbei."); return; }
                if (macheZug()) return;
            }
        }
    }

    // ── Eigenen Zug berechnen und senden ──────────────────────────────────
// Gibt true zurück wenn das Spiel vorbei ist
    private boolean macheZug() throws IOException {
        System.out.println("\nWir ziehen (" + (isWhiteToMove ? "Weiß" : "Schwarz") + ")...");

        Zug zug = ki.findBestMove(board, isWhiteToMove);
        if (zug == null) {
            System.out.println("Keine Züge möglich.");
            return true;
        }

        String moveStr = zugToServerFormat(zug);
        System.out.println("Sende: move " + moveStr);
        String response = send("move " + moveStr);

        if (response == null || response.startsWith("err")) {
            System.out.println("Zug abgelehnt: " + response);
            return true;
        }

        if (response.startsWith("over")) {
            System.out.println("Spiel vorbei – wir haben gewonnen!");
            return true;
        }

        // "time <seconds>"
        System.out.println("Verbleibende Zeit: " + response);

        boolean gameOver = board.move(zug);
        board.printBoard();

        if (gameOver) {
            System.out.println("Spiel vorbei – wir haben gewonnen!");
            return true;
        }

        return false;
    }

    // ── Board aus FEN aktualisieren ────────────────────────────────────
    private void updateBoardFromFen(String fen) {
        char[][] newBoardArray = FenParser.parseBoardFromFen(fen);
        boolean blackToMove    = FenParser.isBlackToMoveFromFen(fen);

        // Halfmove-Zähler aus FEN lesen
        String[] parts = fen.trim().split(" ");
        if (parts.length >= 3) halfmoveClock = Integer.parseInt(parts[2]);
        if (parts.length >= 4) halfmoveCount = Integer.parseInt(parts[3]);

        // Board neu aufsetzen mit dem FEN-Zustand
        board = new Board(newBoardArray);
        board.setBewegt(blackToMove ? 'w' : 's'); // letzter Zug war von der anderen Seite
        board.setBlackToMove(blackToMove);
        board.initHash();
    }

    // ── Prüft ob eine Serverzeile eine FEN ist ─────────────────────────
    private boolean looksLikeFen(String line) {
        // FEN enthält '/' und Ziffern/Buchstaben – einfache Heuristik
        return line != null && line.contains("/") && line.split("/").length == 9;
    }

    // ── Sind wir als nächstes dran? ────────────────────────────────────
    private boolean sindWirDran() {
        boolean schwarzDran = board.isBlackToMove();
        return (schwarzDran && !isWhiteToMove) || (!schwarzDran && isWhiteToMove);
    }

    // ── Zug → Serverformat "fromRow,fromCol,toRow,toCol" (0-basiert) ──
    private String zugToServerFormat(Zug zug) {
        int fromRow = 9 - zug.getFromRow();       // Row 9 → Index 0
        int fromCol = zug.getFromColumn() - 'a';  // 'a' → 0
        int toRow   = 9 - zug.getToRow();
        int toCol   = zug.getToColumn() - 'a';
        return fromRow + "," + fromCol + "," + toRow + "," + toCol;
    }

    // ── Farbwahl ──────────────────────────────────────────────────────
    private boolean frageNachFarbe(Scanner scanner) {
        System.out.print("Welche Farbe? (w = Weiß/Verteidiger, s = Schwarz/Angreifer): ");
        return scanner.next().equalsIgnoreCase("w");
    }

    // ── Sendet einen Befehl und liest die Antwort ──────────────────────
    public String send(String msg) throws IOException {
        System.out.println("→ " + msg);
        out.write((msg + "\n").getBytes());
        out.flush();
        String response = in.readLine();
        System.out.println("← " + response);
        return response;
    }
    private Zug serverFormatToZug(String moveStr) {
        try {
            String[] parts = moveStr.split(",");
            if (parts.length != 4) return null;

            int fromRowIdx = Integer.parseInt(parts[0].trim());
            int fromColIdx = Integer.parseInt(parts[1].trim());
            int toRowIdx   = Integer.parseInt(parts[2].trim());
            int toColIdx   = Integer.parseInt(parts[3].trim());

            char fromCol = (char)('a' + fromColIdx);
            int  fromRow = 9 - fromRowIdx;
            char toCol   = (char)('a' + toColIdx);
            int  toRow   = 9 - toRowIdx;

            // Figur direkt aus Board lesen
            char piece = board.getBoard()[fromRowIdx][fromColIdx];
            if (piece == '-' || piece == 'x') piece = isWhiteToMove ? 's' : 'w'; // Fallback

            return new Zug(fromCol, fromRow, toCol, toRow, piece,fromRowIdx,fromColIdx,toRowIdx,toColIdx);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}

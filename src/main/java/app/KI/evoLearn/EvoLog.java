package app.KI.evoLearn;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class EvoLog {

    private final String filePath;
    private static final DateTimeFormatter TIMESTAMP_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public EvoLog(String filePath) {
        this.filePath = filePath;
        try {
            Path parent = Paths.get(filePath).getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
        } catch (IOException e) {
            System.err.println("Konnte Log-Verzeichnis nicht anlegen: " + e.getMessage());
        }
    }

    /**
     * Loggt eine komplette Population (weiß und schwarz getrennt) in die Datei.
     * Thread-sicher (synchronized), damit paralleles Logging aus mehreren
     * EvoContest-Läufen sich nicht gegenseitig die Zeilen zerschießt.
     */
    public synchronized void log(List<EvoKi> weiß, List<EvoKi> schwarz) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath, true))) {
            String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMAT);
            writer.println("===== Generation Log: " + timestamp + " =====");

            writer.println("--- WEISS ---");
            logPopulation(writer, weiß, "WEISS");

            writer.println("--- SCHWARZ ---");
            logPopulation(writer, schwarz, "SCHWARZ");

            writer.println();
        } catch (IOException e) {
            System.err.println("Fehler beim Schreiben des EvoLog: " + e.getMessage());
        }
    }

    private void logPopulation(PrintWriter writer, List<EvoKi> population, String farbe) {
        int index = 0;
        for (EvoKi ki : population) {
            writer.printf("[%s #%d] Winrate: %d | Fitness: %d%n",
                    farbe, index++, ki.winrate.get(), ki.fitness.get());
            writer.println("Genom: " + ki.genom.toString());
            writer.println();
        }
    }

    /**
     * Schreibt beliebigen Freitext (z.B. Generationsnummer, Kommentare,
     * Zwischenstände) in dieselbe Log-Datei.
     */
    public synchronized void writeText(String text) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath, true))) {
            String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMAT);
            writer.println("[" + timestamp + "] " + text);
        } catch (IOException e) {
            System.err.println("Fehler beim Schreiben des EvoLog: " + e.getMessage());
        }
    }
}

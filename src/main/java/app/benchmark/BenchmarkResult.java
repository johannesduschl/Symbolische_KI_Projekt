package app.benchmark;

public class BenchmarkResult {

    public final String positionName;
    public final double timeMs;
    public final int iterations;
    public final int totalMovesGenerated;

    public BenchmarkResult(String positionName, double timeMs, int iterations, int totalMovesGenerated) {
        this.positionName = positionName;
        this.timeMs = timeMs;
        this.iterations = iterations;
        this.totalMovesGenerated = totalMovesGenerated;
    }
}
package app.benchmark;

import app.board.Zug;

public class KIBenchmarkResult {

    public final String positionName;
    public final double timeMs;
    public final int depthReached;
    public final long nodesSearched;
    public final double nodesPerSecond;
    public final Zug bestMove;

    public KIBenchmarkResult(String positionName,
                             double timeMs,
                             int depthReached,
                             long nodesSearched,
                             Zug bestMove) {

        this.positionName = positionName;
        this.timeMs = timeMs;
        this.depthReached = depthReached;
        this.nodesSearched = nodesSearched;
        this.bestMove = bestMove;

        this.nodesPerSecond = nodesSearched / (timeMs / 1000.0);
    }
}
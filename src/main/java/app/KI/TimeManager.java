package app.KI;

public class TimeManager {

    private long startTime;
    private long timeLimitNanos;

    public void start(long timeLimitMillis) {
        this.startTime = System.nanoTime();
        this.timeLimitNanos = timeLimitMillis * 1_000_000;
    }

    public boolean isTimeUp() {
        return (System.nanoTime() - startTime) >= timeLimitNanos;
    }

    public long elapsedMillis() {
        return (System.nanoTime() - startTime) / 1_000_000;
    }
}
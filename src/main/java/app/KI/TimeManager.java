package app.KI;

public class TimeManager {

    private long endTimeNanos;
    private boolean unlimited = false;

    public void start(long timeLimitMs) {

        if (timeLimitMs <= 0 || timeLimitMs == Long.MAX_VALUE) {
            unlimited = true;
            return;
        }

        unlimited = false;
        endTimeNanos = System.nanoTime() + timeLimitMs * 1_000_000L;
    }

    public boolean isTimeUp() {
        if (unlimited) return false;
        return System.nanoTime() >= endTimeNanos;
    }

    public void forceStop() {
        unlimited = false;
        endTimeNanos = 0;
    }
}
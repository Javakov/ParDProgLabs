package org.javakov.md5;

/**
 * Простая анимация прогресса в консоли.
 */
final class ProgressBar implements AutoCloseable {

    private static final char[] FRAMES = {'|', '/', '—', '\\'};

    private final String label;
    private final long intervalMillis;
    private volatile boolean running;
    private Thread worker;

    ProgressBar(String label, long intervalMillis) {
        this.label = label;
        this.intervalMillis = intervalMillis;
    }

    void start() {
        if (running) {
            return;
        }
        running = true;
        worker = new Thread(this::animate, "progress-bar");
        worker.setDaemon(true);
        worker.start();
    }

    void stop() {
        running = false;
        if (worker != null) {
            try {
                worker.join();
            } catch (InterruptedException ignored) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private void animate() {
        int frame = 0;
        while (running) {
            final char symbol = FRAMES[frame % FRAMES.length];
            System.out.printf("\r%s %c", label, symbol);
            frame++;
            sleep();
        }
        System.out.printf("\r%s ✓%n", label);
    }

    private void sleep() {
        try {
            Thread.sleep(intervalMillis);
        } catch (InterruptedException ignored) {
            Thread.currentThread().interrupt();
            running = false;
        }
    }

    @Override
    public void close() {
        stop();
    }
}


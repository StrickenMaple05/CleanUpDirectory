package project;

import java.io.File;

public class CleanUpDirectory {

    /**
     * cleaning thread
     */
    private final Thread thread;

    public CleanUpDirectory(File directory, int interval) {
        CleanUpRunnable cleanUpRunnable = new CleanUpRunnable(directory, interval);
        thread = new Thread(cleanUpRunnable);
        thread.setDaemon(true);
    }

    public void start() {
        thread.start();
    }

    public void stop() throws InterruptedException {
        thread.interrupt();
    }

    public Thread getThread() {
        return thread;
    }
}

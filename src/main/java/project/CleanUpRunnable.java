package project;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.Path;

public class CleanUpRunnable implements Runnable {

    /**
     * path of the directory to cleanup
     */
    private final File directory;

    /**
     * cycle refresh time
     */
    private final int interval;

    public CleanUpRunnable(File directory, int secInterval) {
        this.directory = directory;
        this.interval = secInterval * 1000;
    }

    @Override
    public void run() {

        while (true) {
            try {
                cleanDirectory(directory);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(interval);
            } catch (InterruptedException ignored) {
                break;
            }
        }
    }

    public void cleanDirectory(File directory) throws IOException {

        for (File file : directory.listFiles()) {
            try {
                Files.delete(Path.of(file.getCanonicalPath()));
            } catch (DirectoryNotEmptyException exception) {
                cleanDirectory(file);
            } catch (IOException exception) {
                System.out.println(
                        "File permission problems were caught deleting " + file.getCanonicalPath());
            }
        }
    }
}

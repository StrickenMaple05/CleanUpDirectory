package project;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class CleanUpDirectoryTest {

    private static File directory;

    @BeforeAll
    public static void initDirectory() {
        directory = new File("directory");
        directory.mkdir();
    }

    @AfterAll
    public static void removeDirectory() {
        removeDirectory(directory);
    }

    public static void removeDirectory(File directory) {
        for (File temp : directory.listFiles()) {
            if (temp.isDirectory()) {
                removeDirectory(temp);
            } else {
                temp.delete();
            }
        }
        directory.delete();
    }

    public void createTestData() throws IOException {
        File file = new File(directory, "test.txt");
        file.createNewFile();
        File tempDir = new File(directory, "dir");
        tempDir.mkdir();
        file = new File(tempDir, "test.txt");
        file.createNewFile();
    }

    @Test
    public void cleanUpDirectoryTest() throws IOException, InterruptedException {

        CleanUpDirectory cleanUpDirectory = new CleanUpDirectory(directory, 1);

        createTestData();
        cleanUpDirectory.start();
        while (directory.listFiles().length != 0) {}
        LocalDateTime first = LocalDateTime.now();

        createTestData();
        while (directory.listFiles().length != 0) {}
        LocalDateTime second = LocalDateTime.now();

        Assertions.assertEquals(2,
                Math.round(first.until(second, ChronoUnit.MILLIS)/1000f));

        cleanUpDirectory.stop();
    }

    @Test
    public void stopThreadTest() throws InterruptedException {

        int threadsCountBefore = Thread.activeCount();
        CleanUpDirectory cleanUpDirectory = new CleanUpDirectory(directory,1);
        cleanUpDirectory.start();
        cleanUpDirectory.stop();
        while (cleanUpDirectory.getThread().isAlive()) {}
        int threadsCountAfter = Thread.activeCount();
        Assertions.assertEquals(threadsCountBefore, threadsCountAfter);
    }
}

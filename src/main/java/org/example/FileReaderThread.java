package org.example;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class FileReaderThread implements Runnable {

    CyclicBarrier cyclicBarrier;
    AtomicInteger readCount;
    List<List<String>> partialResults;
    int index;

    @Override
    public void run() {
        readByScanner();
    }

    @SneakyThrows
    private void readByScanner() {
        System.out.println(index + " starting...");
        var file = new File("src/main/resources/read" + index + ".txt");
        try (var scanner = new Scanner(file)) {
            String threadName = Thread.currentThread().getName();
            System.out.println(threadName);
            var partialResult = new ArrayList<String>();
            while (scanner.hasNextLine()) {
                var line = scanner.nextLine();
                partialResult.add(line);
                System.out.println(line);
            }
            partialResults.add(partialResult);
            readCount.incrementAndGet();
            System.out.println(threadName + " with index " + index + " waiting for others to reach barrier.");
            cyclicBarrier.await();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}

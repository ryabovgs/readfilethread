package org.example;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class FileReaderThread implements Runnable {

    ReentrantReadWriteLock lock;
    CyclicBarrier cyclicBarrier;
    AtomicInteger readCount;

    @Override
    public void run() {

        readByScanner();
    }

    @SneakyThrows
    private void readByScanner() {
        var file = new File("src/main/resources/read.txt");
        try (var scanner = new Scanner(file)) {
            String threadName = Thread.currentThread().getName();
            System.out.println(threadName);
            while (scanner.hasNextLine()) {
                System.out.println(scanner.nextLine());
            }
            readCount.incrementAndGet();
            System.out.println(threadName + " waiting for others to reach barrier.");
            cyclicBarrier.await();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}

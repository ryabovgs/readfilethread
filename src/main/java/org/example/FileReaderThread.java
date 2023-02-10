package org.example;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;

import static org.example.ThreadConstants.COMMA_DELIMITER;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class FileReaderThread implements Runnable {

    CyclicBarrier cyclicBarrier;
    List<Map<String, AtomicInteger>> partialResults;
    int index;

    @Override
    public void run() {
        readByScanner();
    }

    @SneakyThrows
    private void readByScanner() {
        System.out.println(index + " starting...");
        var resultMap = new HashMap<String, AtomicInteger>();
        partialResults.add(resultMap);
        var file = new File("src/main/resources/click" + index + ".csv");
        try (var scanner = new Scanner(file)) {
            String threadName = Thread.currentThread().getName();
            System.out.println(threadName);
            var header = scanner.nextLine();
            while (scanner.hasNextLine()) {
                var line = scanner.nextLine();
                var lineArray = line.split(COMMA_DELIMITER);
                var type = lineArray[1];
                resultMap.computeIfAbsent(type, type1 -> new AtomicInteger());
                resultMap.get(type).getAndIncrement();
            }
            System.out.println(partialResults);
            System.out.println(threadName + " with index " + index + " waiting for others to reach barrier.");
            cyclicBarrier.await();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}

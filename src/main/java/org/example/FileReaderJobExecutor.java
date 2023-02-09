package org.example;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import static org.example.ThreadConstants.*;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class FileReaderJobExecutor {

    AtomicInteger readCount = new AtomicInteger();
    List<List<String>> partialResults = Collections.synchronizedList(new ArrayList<>());
    CyclicBarrier cyclicBarrier = new CyclicBarrier(THREADS_NUMBER, new AggregatorThread(readCount, partialResults));

    public void run() {
        var executorService = Executors.newFixedThreadPool(THREADS_NUMBER);
        IntStream.range(START_NUMBER, END_NUMBER)
                .forEach(number ->
                        executorService.submit(
                                new FileReaderThread(cyclicBarrier, readCount, partialResults, number)));
        System.out.println(readCount.get());
        executorService.shutdown();
    }
}

package org.example;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.IntStream;

import static org.example.ThreadConstants.N_THREADS;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class FileReaderJobExecutor {

    ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    AtomicInteger readCount = new AtomicInteger();
    CyclicBarrier cyclicBarrier = new CyclicBarrier(N_THREADS, new AggregatorThread(readCount));

    public void run(){
        var executorService = Executors.newFixedThreadPool(N_THREADS);
        IntStream.range(0, N_THREADS)
                .forEach(number ->
                        executorService.submit(
                                new FileReaderThread(lock, cyclicBarrier, readCount)));
        System.out.println(readCount.get());
        executorService.shutdown();
    }
}

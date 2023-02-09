package org.example;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.concurrent.atomic.AtomicInteger;

import static org.example.ThreadConstants.N_THREADS;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
class AggregatorThread implements Runnable {

    AtomicInteger readCount;

    @Override
    public void run() {
        String thisThreadName = Thread.currentThread().getName();
        System.out.println(thisThreadName + ": Computing final result of " + N_THREADS + " " +
                "workers, having " + 10 + " results each.");
        System.out.println(Thread.currentThread().getName() + ": Final result = " + readCount.get());
    }
}

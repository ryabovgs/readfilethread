package org.example;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
class AggregatorThread implements Runnable {

    List<Map<String, AtomicInteger>> partialResults;

    @Override
    public void run() {
        String thisThreadName = Thread.currentThread().getName();
        System.out.println(thisThreadName + ": Computing final result of " + partialResults.size() + " " +
                "workers.");
    }
}

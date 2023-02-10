package org.example;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.example.ThreadConstants.THREADS_NUMBER;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
class AggregatorThread implements Runnable {

    List<Map<String, AtomicInteger>> partialResults;
    Map<String, Integer> finalResults = new TreeMap<>();

    @Override
    public void run() {
        String thisThreadName = Thread.currentThread().getName();
        System.out.println(thisThreadName + ": Computing final result of " + partialResults.size() + " " +
                "workers.");
        partialResults.forEach(
                partialResults -> partialResults.forEach(
                        (type, count) -> finalResults.merge(type, count.intValue(), Integer::sum)));
        var sortedMap = sortByValueAndReturnTopOnes();
        System.out.println(sortedMap);
    }

    private LinkedHashMap<String, Integer> sortByValueAndReturnTopOnes() {
        var sortedMap = new LinkedHashMap<String, Integer>();
        finalResults.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(THREADS_NUMBER)
                .forEachOrdered(x -> sortedMap.put(x.getKey(), x.getValue()));
        return sortedMap;
    }
}

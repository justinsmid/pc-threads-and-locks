package com.mycompany.threadsandlocks;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        int maxThreads = Runtime.getRuntime().availableProcessors();
        final int ARRAY_SIZE = 2_000_000;

        BucketSortSolver sorter = new BucketSortSolver(maxThreads, ARRAY_SIZE);

        List<Long> parallelSortedList = sorter.parallel();
        List<Long> sequentiallySortedList = sorter.sequential();
    }
}

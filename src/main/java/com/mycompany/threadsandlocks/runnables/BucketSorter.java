package com.mycompany.threadsandlocks.runnables;

import com.mycompany.threadsandlocks.BucketSortSolver;

import java.util.Comparator;
import java.util.List;

public class BucketSorter implements Runnable {
    private final BucketSortSolver context;
    private final List<List<Long>> bucketsToSort;

    public BucketSorter(BucketSortSolver context, List<List<Long>> bucketsToSort) {
        this.context = context;
        this.bucketsToSort = bucketsToSort;
    }

    @Override
    public void run() {
        Comparator<Long> comparator = Comparator.naturalOrder();
        for (List<Long> bucket : this.bucketsToSort) {
            bucket.sort(comparator);
        }
    }
}

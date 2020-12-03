package com.mycompany.threadsandlocks;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class BucketSorter implements Runnable {
    private int threadId;
    private BucketHolder bucketHolder;
    private List<List<Long>> bucketsToSort;

    public BucketSorter(int id, BucketHolder bucketHolder, List<List<Long>> bucketsToSort) {
        this.threadId = id;
        this.bucketHolder = bucketHolder;
        this.bucketsToSort = bucketsToSort;
    }

    @Override
    public void run() {
        System.out.printf("BucketSorter %d running...\n", this.threadId);
        Comparator<Long> comparator = Comparator.naturalOrder();
        for (List<Long> bucket : this.bucketsToSort) {
            bucket.sort(comparator);
        }
    }

    public int getThreadId() {
        return threadId;
    }

    public void setThreadId(int threadId) {
        this.threadId = threadId;
    }
}

package com.mycompany.threadsandlocks;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class ElementDistributor implements Runnable {
    private int threadId;
    private BucketHolder bucketHolder;
    private List<Long> elementsToDistribute;
    long max;

    public ElementDistributor(int id, BucketHolder bucketHolder, List<Long> elementsToDistribute, long max) {
        this.threadId = id;
        this.bucketHolder = bucketHolder;
        this.elementsToDistribute = elementsToDistribute;
        this.max = max;
    }

    @Override
    public void run() {
        System.out.printf("ElementDistributor %d running...\n", this.threadId);
        int nBuckets = bucketHolder.getBuckets().size();
        for (Long element : this.elementsToDistribute) {
            int originalBucketIdx = (int) Math.floor(nBuckets * element / this.max);
            int bucketIdx = element == this.max ? originalBucketIdx - 1 : originalBucketIdx;
            List<Long> bucket = this.bucketHolder.getBuckets().get(bucketIdx);
            bucket.add(element);
        }
    }


    public int getThreadId() {
        return threadId;
    }

    public void setThreadId(int threadId) {
        this.threadId = threadId;
    }
}

package com.mycompany.threadsandlocks;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BucketCreator implements Runnable {
    private int threadId;
    private BucketHolder bucketHolder;
    private int nThreads;
    private int nBucketsTotal;
    private int nBucketsToCreate;

    public BucketCreator(int id, BucketHolder bucketHolder, int nThreads, int nBucketsTotal, int nBucketsToCreate) {
        this.threadId = id;
        this.bucketHolder = bucketHolder;
        this.nThreads = nThreads;
        this.nBucketsTotal = nBucketsTotal;
        this.nBucketsToCreate = nBucketsToCreate;
    }

    @Override
    public void run() {
        System.out.printf("BucketCreator %d running...\n", this.threadId);
        for (int i = 0; i < this.nBucketsToCreate; i++) {
            this.bucketHolder.getBuckets().add(Collections.synchronizedList(new ArrayList<>()));
        }
    }

    public int getThreadId() {
        return threadId;
    }

    public void setThreadId(int threadId) {
        this.threadId = threadId;
    }

    public int getnThreads() {
        return nThreads;
    }

    public void setnThreads(int nThreads) {
        this.nThreads = nThreads;
    }

    public int getnBucketsTotal() {
        return nBucketsTotal;
    }

    public void setnBucketsTotal(int nBucketsTotal) {
        this.nBucketsTotal = nBucketsTotal;
    }
}

package com.mycompany.threadsandlocks;

import java.util.List;
import java.util.Random;

public class BucketFiller implements Runnable {
    private int threadId;
    private BucketHolder bucketHolder;
    private List<Long> list;
    private int nThreads;
    private int nElementsTotal;
    private int nElementsToCreate;
    private Random rng;
    private BucketSortSolver sorter;

    // TODO: Rename to ListCreator or smth
    public BucketFiller(int id, BucketHolder bucketHolder, List<Long> list, int nThreads, int nElementsTotal, int nElementsToCreate, Random rng, BucketSortSolver sorter) {
        this.threadId = id;
        this.bucketHolder = bucketHolder;
        this.list = list;
        this.nThreads = nThreads;
        this.nElementsTotal = nElementsTotal;
        this.nElementsToCreate = nElementsToCreate;
        this.rng = rng;
        this.sorter = sorter;
    }

    @Override
    public void run() {
        System.out.printf("BucketFiller %d running...\n", this.threadId);
        for (int i = 0; i < this.nElementsToCreate; i++) {
            long value = Math.abs(rng.nextInt());
            list.add(value);
            if (value > sorter.getMax()) sorter.setMax(value);
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
}

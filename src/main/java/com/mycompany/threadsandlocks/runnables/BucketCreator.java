package com.mycompany.threadsandlocks.runnables;

import com.mycompany.threadsandlocks.BucketSortSolver;

import java.util.ArrayList;
import java.util.Collections;

public class BucketCreator implements Runnable {
    private final BucketSortSolver context;
    private final int nBucketsToCreate;

    public BucketCreator(BucketSortSolver context, int nBucketsToCreate) {
        this.context = context;
        this.nBucketsToCreate = nBucketsToCreate;
    }

    @Override
    public void run() {
        for (int i = 0; i < this.nBucketsToCreate; i++) {
            this.context.getBuckets().add(Collections.synchronizedList(new ArrayList<>()));
        }
    }
}

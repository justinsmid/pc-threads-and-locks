package com.mycompany.threadsandlocks.runnables;

import com.mycompany.threadsandlocks.BucketSortSolver;

import java.util.List;

public class ElementDistributor implements Runnable {
    private final BucketSortSolver context;
    private final List<Long> elementsToDistribute;

    public ElementDistributor(BucketSortSolver context, List<Long> elementsToDistribute) {
        this.context = context;
        this.elementsToDistribute = elementsToDistribute;
    }

    @Override
    public void run() {
        int nBuckets = context.getBuckets().size();
        for (Long element : this.elementsToDistribute) {
            int originalBucketIdx = (int) Math.floor(nBuckets * element / (double) this.context.getMax());
            int bucketIdx = element == this.context.getMax() ? originalBucketIdx - 1 : originalBucketIdx;
            List<Long> bucket = this.context.getBuckets().get(bucketIdx);
            bucket.add(element);
        }
    }
}

package com.mycompany.threadsandlocks.runnables;

import com.mycompany.threadsandlocks.BucketSortSolver;

import java.util.Random;

public class BucketFiller implements Runnable {
    private final BucketSortSolver context;
    private final int nElementsToCreate;
    private final Random rng;

    // TODO: Rename to ListCreator or smth
    public BucketFiller(BucketSortSolver context, int nElementsToCreate, Random rng) {
        this.context = context;
        this.nElementsToCreate = nElementsToCreate;
        this.rng = rng;
    }

    @Override
    public void run() {
        for (int i = 0; i < this.nElementsToCreate; i++) {
            long value = Math.abs(rng.nextInt());
            context.getUnsortedList().add(value);
            if (value > context.getMax()) context.setMax(value);
        }
    }
}

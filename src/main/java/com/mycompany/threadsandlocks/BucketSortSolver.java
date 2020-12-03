package com.mycompany.threadsandlocks;

import com.mycompany.threadsandlocks.runnables.BucketCreator;
import com.mycompany.threadsandlocks.runnables.BucketFiller;
import com.mycompany.threadsandlocks.runnables.BucketSorter;
import com.mycompany.threadsandlocks.runnables.ElementDistributor;

import java.util.*;

import static com.mycompany.threadsandlocks.Util.joinAll;

public class BucketSortSolver {
    private volatile long max = Integer.MIN_VALUE;
    private List<List<Long>> buckets;
    private List<Long> unsortedList;
    private int nThreads;
    private int nElements;

    public BucketSortSolver(int nThreads, int nElements) {
        this.nThreads = nThreads;
        this.nElements = nElements;
    }

    public List<Long> parallel() {
        this.setMax(Integer.MIN_VALUE);
        List<Thread> threads = new ArrayList<>(nThreads);

        int nBuckets = (int) Math.sqrt(nElements);

        // Create buckets
        this.setBuckets(Collections.synchronizedList(new ArrayList<>(nBuckets)));

        int nAvailableBuckets = nBuckets;
        int nBucketsEach = nBuckets / nThreads;
        for (int i = 0; i < nThreads; i++) {
            boolean isLast = i == nThreads - 1;
            int nBucketsToCreate =  isLast ? Math.max(nBucketsEach, nAvailableBuckets) : nBucketsEach; // Have last thread create remaining buckets - needed if sqrt(nElements) is uneven
            Thread thread = new Thread(new BucketCreator(this, nBucketsToCreate));
            threads.add(thread);
            thread.start();
            nAvailableBuckets -= nBucketsToCreate;
        }

        joinAll(threads);

        // Create list to be sorted
        this.setUnsortedList(Collections.synchronizedList(new ArrayList<>(nElements)));
        Random rng = new Random();
        int nAvailableElements = nElements;
        int nElementsEach = nElements / nThreads;
        for (int i = 0; i < threads.size(); i++) {
            boolean isLast = i == threads.size() - 1;
            int nElementsToCreate =  isLast ? Math.max(nElementsEach, nAvailableElements) : nElementsEach;
            Thread thread = new Thread(new BucketFiller(this, nElementsToCreate, rng));
            threads.set(i, thread);
            thread.start();
            nAvailableElements -= nElementsToCreate;
        }

        joinAll(threads);

        // Distribute elements into their respective bucket
        int begin = 0;
        nAvailableElements = nElements;
        for (int i = 0; i < nThreads; i++) {
            boolean isLast = i == threads.size() - 1;
            int nElementsToDistribute =  isLast ? Math.max(nElementsEach, nAvailableElements) : nElementsEach;
            List<Long> sublist = unsortedList.subList(begin, begin + nElementsToDistribute);
            Thread thread = new Thread(new ElementDistributor(this, sublist));
            threads.set(i, thread);
            thread.start();
            nAvailableElements -= nElementsToDistribute;
            begin += nElementsToDistribute;
        }

        joinAll(threads);

        // Sort buckets
        begin = 0;
        nAvailableBuckets = nBuckets;
        nBucketsEach = nBuckets / nThreads;
        for (int i = 0; i < nThreads; i++) {
            boolean isLast = i == threads.size() - 1;
            int nBucketsToSort =  isLast ? Math.max(nBucketsEach, nAvailableBuckets) : nBucketsEach;
            List<List<Long>> sublist = buckets.subList(begin, begin + nBucketsToSort);
            Thread thread = new Thread(new BucketSorter(this, sublist));
            threads.set(i, thread);
            thread.start();
            nAvailableBuckets -= nBucketsToSort;
            begin += nBucketsToSort;
        }

        joinAll(threads);

        // Join buckets together into the final sorted list
        List<Long> sortedList = Collections.synchronizedList(new ArrayList<>(nElements));
        buckets.forEach(sortedList::addAll);

        return sortedList;
    }

    public List<Long> sequential() {
        this.setMax(Integer.MIN_VALUE);
        int nBuckets = (int) Math.sqrt(nElements);

        // Create buckets
        List<List<Long>> buckets = new ArrayList<>(nBuckets);
        for (int i = 0; i < nBuckets; i++) {
            buckets.add(new ArrayList<>());
        }

        // Create list to be sorted
        List<Long> list = new ArrayList<>(nElements);
        Random rng = new Random();
        for (int i = 0; i < nElements; i++) {
            long value = Math.abs(rng.nextInt());
            list.add(value);
            if (value > this.max) this.max = value;
        }

        // Distribute elements into their respective bucket
        for (int i = 0; i < nElements; i++) {
            Long element = list.get(i);
            int originalBucketIdx = (int) Math.floor(nBuckets * element / (double) max);
            int bucketIdx = element == max ? originalBucketIdx - 1 : originalBucketIdx;
            List<Long> bucket = buckets.get(bucketIdx);
            bucket.add(element);
        }

        // Sort buckets
        Comparator<Long> comparator = Comparator.naturalOrder();
        for (List<Long> bucket : buckets) {
            bucket.sort(comparator);
        }

        // Join buckets together into the final sorted list
        List<Long> sortedList = new ArrayList<>(nElements);
        buckets.forEach(sortedList::addAll);

        return sortedList;
    }

    public long getMax() {
        return max;
    }

    public void setMax(long max) {
        this.max = max;
    }

    public List<List<Long>> getBuckets() {
        return buckets;
    }

    public void setBuckets(List<List<Long>> buckets) {
        this.buckets = buckets;
    }

    public List<Long> getUnsortedList() {
        return unsortedList;
    }

    public void setUnsortedList(List<Long> unsortedList) {
        this.unsortedList = unsortedList;
    }

    public int getNThreads() {
        return nThreads;
    }

    public void setNThreads(int nThreads) {
        this.nThreads = nThreads;
    }

    public int getNElements() {
        return nElements;
    }

    public void setNElements(int nElements) {
        this.nElements = nElements;
    }
}

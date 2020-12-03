package com.mycompany.threadsandlocks;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.mycompany.threadsandlocks.Util.joinAll;

public class BucketSortSolver {
    volatile long max = -1;

    public List<Long> parallel(int nThreads, int nElements) {
        System.out.printf("Starting algorithm in parallel with %d threads and %d elements to sort...\n", nThreads, nElements);

        List<Thread> threads = new ArrayList<>(nThreads);

        int nBuckets = (int) Math.sqrt(nElements);

        // Create buckets
        System.out.printf("Creating %d buckets...\n", nBuckets);
        BucketHolder bucketHolder = new BucketHolder(Collections.synchronizedList(new ArrayList<>(nBuckets)));

        int nAvailableBuckets = nBuckets;
        int nBucketsEach = nBuckets / nThreads;
        for (int i = 0; i < nThreads; i++) {
            boolean isLast = i == nThreads - 1;
            int nBucketsToCreate =  isLast ? Math.max(nBucketsEach, nAvailableBuckets) : nBucketsEach; // Have last thread create remaining buckets - needed if sqrt(nElements) is uneven
            Thread thread = new Thread(new BucketCreator(i, bucketHolder, nThreads, nBuckets, nBucketsToCreate));
            threads.add(thread);
            thread.start();
            nAvailableBuckets -= nBucketsToCreate;
        }

        joinAll(threads);

        // Create list to be sorted
        List<Long> list = Collections.synchronizedList(new ArrayList<>(nElements));
        Random rng = new Random();
        int nAvailableElements = nElements;
        int nElementsEach = nElements / nThreads;
        for (int i = 0; i < threads.size(); i++) {
            boolean isLast = i == threads.size() - 1;
            int nElementsToCreate =  isLast ? Math.max(nElementsEach, nAvailableElements) : nElementsEach;
            Thread thread = new Thread(new BucketFiller(i, bucketHolder, list, nThreads, nBuckets, nElementsToCreate, rng, this));
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
            List<Long> sublist = list.subList(begin, begin + nElementsToDistribute);
            Thread thread = new Thread(new ElementDistributor(i, bucketHolder, sublist, max));
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
            List<List<Long>> sublist = bucketHolder.getBuckets().subList(begin, begin + nBucketsToSort);
            Thread thread = new Thread(new BucketSorter(i, bucketHolder, sublist));
            threads.set(i, thread);
            thread.start();
            nAvailableBuckets -= nBucketsToSort;
            begin += nBucketsToSort;
        }

        joinAll(threads);

//        // Verify that each bucket is sorted
        for (List<Long> bucket : bucketHolder.getBuckets()) {
            if (!bucket.isEmpty()) {
                for (int i = 0; i < bucket.size() - 1; i++) {
                    assert (bucket.get(i) <= bucket.get(i + 1));
                }
            }
        }

        // Join buckets together into the final sorted list
        List<Long> sortedList = Collections.synchronizedList(new ArrayList<>(nElements));
        bucketHolder.getBuckets().forEach(sortedList::addAll);

        // Verify that final list is sorted
        for (int i = 0; i < sortedList.size() - 1; i++) {
            assert (sortedList.get(i) <= sortedList.get(i + 1));
        }

        System.out.println("Done");

        return sortedList;
    }

    public List<Long> sequential(int nThreads, int nElements) {
        System.out.printf("Starting algorithm sequentially with %d threads and %d elements to sort...\n", nThreads, nElements);

        int nBuckets = (int) Math.sqrt(nElements);

        // Create buckets
        System.out.printf("Creating %d buckets...\n", nBuckets);
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
            int originalBucketIdx = (int) Math.floor(nBuckets * element / max);
            int bucketIdx = element == max ? originalBucketIdx - 1 : originalBucketIdx;
            List<Long> bucket = buckets.get(bucketIdx);
            bucket.add(element);
        }

        // Sort buckets
        Comparator<Long> comparator = Comparator.naturalOrder();
        for (List<Long> bucket : buckets) {
            bucket.sort(comparator);
        }

        // Verify that each bucket is sorted
        for (List<Long> bucket : buckets) {
            if (!bucket.isEmpty()) {
                for (int i = 0; i < bucket.size() - 1; i++) {
                    assert (bucket.get(i) <= bucket.get(i + 1));
                }
            }
        }

        // Join buckets together into the final sorted list
        List<Long> sortedList = new ArrayList<>(nElements);
        buckets.forEach(sortedList::addAll);

        // Verify that final list is sorted
        for (int i = 0; i < sortedList.size() - 1; i++) {
            assert (sortedList.get(i) <= sortedList.get(i + 1));
        }

        System.out.println("Done");

        return sortedList;
    }

    public long getMax() {
        return max;
    }

    public void setMax(long max) {
        this.max = max;
    }
}

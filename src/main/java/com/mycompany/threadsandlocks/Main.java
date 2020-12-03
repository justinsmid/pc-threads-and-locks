package com.mycompany.threadsandlocks;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        int maxThreads = Runtime.getRuntime().availableProcessors();
        final int ARRAY_SIZE = 2_000_000;

//        sequential(maxThreads, ARRAY_SIZE);
        parallel(maxThreads, ARRAY_SIZE);
    }

    public static List<Long> parallel(int nThreads, int nElements) {
        System.out.printf("Starting algorithm in parallel with %d threads and %d elements to sort...\n", nThreads, nElements);

        int nBuckets = (int) Math.sqrt(nElements);

        // Create buckets
        System.out.printf("Creating %d buckets...\n", nBuckets);
        List<List<Long>> buckets = Collections.synchronizedList(new ArrayList<>(nBuckets));
        for (int i = 0; i < nBuckets; i++) {
            buckets.add(Collections.synchronizedList(new ArrayList<>()));
        }

        // Create list to be sorted
        List<Long> list = Collections.synchronizedList(new ArrayList<>(nElements));
        Random rng = new Random();
        for (int i = 0; i < nElements; i++) {
            list.add((long) Math.abs(rng.nextInt()));
        }

        // Distribute elements into their respective bucket
        for (int i = 0; i < nElements; i++) {
            Long element = list.get(i);
            int bucketIdx = (int) ((element * nBuckets) % nBuckets);
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
        List<Long> sortedList = Collections.synchronizedList(new ArrayList<>(nElements));
        buckets.forEach(sortedList::addAll);

        // Verify that final list is sorted
        for (int i = 0; i < sortedList.size() - 1; i++) {
            assert (sortedList.get(i) <= sortedList.get(i + 1));
        }

        System.out.println("Done");

        return sortedList;
    }

    public static List<Long> sequential(int nThreads, int nElements) {
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
            list.add((long) Math.abs(rng.nextInt()));
        }

        // Distribute elements into their respective bucket
        for (int i = 0; i < nElements; i++) {
            Long element = list.get(i);
            int bucketIdx = (int) ((element * nBuckets) % nBuckets);
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
}

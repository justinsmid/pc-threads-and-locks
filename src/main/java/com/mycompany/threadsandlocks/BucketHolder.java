package com.mycompany.threadsandlocks;

import java.util.List;

public class BucketHolder {
    private List<List<Long>> buckets;

    public BucketHolder(List<List<Long>> buckets) {
        this.buckets = buckets;
    }

    public List<List<Long>> getBuckets() {
        return buckets;
    }

    public void setBuckets(List<List<Long>> buckets) {
        this.buckets = buckets;
    }
}

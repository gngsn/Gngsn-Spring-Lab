package com.gngsn.s3.service;


import com.amazonaws.services.s3.model.Bucket;

import java.util.List;

public interface S3BucketService {

    void createS3Bucket(String bucketName);

    List<Bucket> listBuckets();

    void deleteBucket(String bucketName);
}

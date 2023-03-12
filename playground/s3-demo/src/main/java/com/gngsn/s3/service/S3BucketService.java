package com.gngsn.s3.service;

import software.amazon.awssdk.services.s3.model.ListBucketsResponse;

public interface S3BucketService {

    void createS3Bucket(String bucketName);

    ListBucketsResponse listBuckets();
}

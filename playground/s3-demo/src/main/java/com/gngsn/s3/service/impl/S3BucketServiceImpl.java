package com.gngsn.s3.service.impl;

import com.gngsn.s3.service.S3BucketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ListBucketsResponse;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3BucketServiceImpl implements S3BucketService {

    private final S3Client s3;

    @Override
    public void createS3Bucket(String bucketName) {
    }

    @Override
    public ListBucketsResponse listBuckets() {
        return s3.listBuckets();
    }
}

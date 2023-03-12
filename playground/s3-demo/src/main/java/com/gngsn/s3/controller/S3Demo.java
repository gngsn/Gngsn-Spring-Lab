package com.gngsn.s3.controller;

import com.gngsn.s3.service.S3BucketService;
import com.gngsn.s3.service.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import software.amazon.awssdk.services.s3.model.Bucket;
import software.amazon.awssdk.services.s3.model.ListBucketsResponse;
import software.amazon.awssdk.services.s3.model.S3Object;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class S3Demo {

    private final S3BucketService s3BucketService;
    private final S3Service s3Service;

    @GetMapping("/buckets")
    public List<Bucket> bucketList() {
        ListBucketsResponse bucketsResponse = s3BucketService.listBuckets();

        List<Bucket> bucketList = bucketsResponse.buckets();
        bucketList.forEach(bucket -> log.info(bucket.toString()));
        return bucketList;
    }

    @GetMapping("/buckets/{bucketName}/objects")
    public List<S3Object> objectList(@PathVariable String bucketName) {
        List<S3Object> summaries = s3Service.listObjects(bucketName);

        summaries.forEach(bucket -> log.info(bucket.toString()));
        return summaries;
    }
}

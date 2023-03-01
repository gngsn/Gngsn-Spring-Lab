package com.gngsn.s3;

import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class S3Demo {

    private final S3BucketService s3BucketService;
    private final S3Service s3Service;

    @GetMapping("/buckets")
    public List<Bucket> bucketList() {
        List<Bucket> bucketList = s3BucketService.listBuckets();

        bucketList.forEach(bucket -> log.info(bucket.toString()));
        return bucketList;
    }

    @GetMapping("/buckets/{bucketName}/objects")
    public List<S3ObjectSummary> objectList(@PathVariable String bucketName) {
        List<S3ObjectSummary> summaries = s3Service.listObjects(bucketName);

        summaries.forEach(bucket -> log.info(bucket.toString()));
        return summaries;
    }
}

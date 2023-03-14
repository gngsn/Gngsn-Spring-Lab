package com.gngsn.s3.service;

import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;

import java.util.List;

public interface S3Service {

    void putObject(String bucketName, String filePath, String keyName);

    List<S3ObjectSummary> listObjects(String bucketName);

    S3Object downloadObject(String bucketName, String key);
}

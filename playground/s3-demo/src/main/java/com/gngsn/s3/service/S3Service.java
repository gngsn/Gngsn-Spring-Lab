package com.gngsn.s3.service;

import software.amazon.awssdk.services.s3.model.S3Object;

import java.util.List;

public interface S3Service {

    void putObject(String bucketName, String filePath, String keyName);

    List<S3Object> listObjects(String bucketName);

    String downloadObject(String bucketName, String key);
}

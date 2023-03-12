package com.gngsn.s3.service.impl;

import com.gngsn.s3.service.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.util.annotation.Nullable;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.*;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3ServiceImpl implements S3Service {

    private final S3Client s3;

    @Override
    public void putObject(String bucketName, String filePath, String keyName) {
        log.info("Uploading {} to S3 bucket {}...\n", filePath, bucketName);
        // TODO
    }

    @Override
    public List<S3Object> listObjects(String bucketName) {
        log.info("Objects in S3 bucket {}:\n", bucketName);

        ListObjectsRequest listObjects = ListObjectsRequest
            .builder()
            .bucket(bucketName)
            .build();

        ListObjectsResponse res = s3.listObjects(listObjects);
        List<S3Object> objects = res.contents();
        for (S3Object myValue : objects) {
            log.info("The name of the key is " + myValue.key());
            log.info("The object is " + myValue.size() + " KBs");
            log.info("The owner is " + myValue.owner());
        }

        return res.contents();
    }

    @Nullable
    @Override
    public String downloadObject(String bucketName, String key) {

        try {
            GetObjectRequest objectRequest = GetObjectRequest
                .builder()
                .key(key)
                .bucket(bucketName)
                .build();

            ResponseBytes<GetObjectResponse> objectBytes = s3.getObjectAsBytes(objectRequest);
            byte[] data = objectBytes.asByteArray();

            File myFile = new File("/s3/");
            OutputStream os = new FileOutputStream(myFile);
            os.write(data);
            log.info("Successfully obtained bytes from an S3 object");
            os.close();

            return myFile.getPath();
        } catch (IOException ex) {
            log.error(ex.getMessage());
        } catch (S3Exception e) {
            log.error(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
        
        return null;
    }
}

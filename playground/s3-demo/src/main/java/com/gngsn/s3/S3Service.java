package com.gngsn.s3;


import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3Service {

    private final AmazonS3 amazonS3Client;

    public void putObject(String bucketName, String filePath, String keyName) {
        log.info("Uploading {} to S3 bucket {}...\n", filePath, bucketName);

        try {
            amazonS3Client.putObject(bucketName, keyName, new File(filePath));
        } catch (AmazonServiceException e) {
            throw new RuntimeException();
        }
    }

    public List<S3ObjectSummary> listObjects(String bucketName) {
        log.info("Objects in S3 bucket {}:\n", bucketName);

        ListObjectsV2Result result = amazonS3Client.listObjectsV2(bucketName);
        return result.getObjectSummaries();
    }

    public S3Object downloadObject(String bucketName, String key) {

        GetObjectRequest request = new GetObjectRequest(bucketName, key);

        try (
            S3Object fullObject = amazonS3Client.getObject(request);
        ) {
            displayTextInputStream(fullObject.getObjectContent());
            return fullObject;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void displayTextInputStream(InputStream input) throws IOException {
        // Read the text input stream one line at a time and display each line.
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        String line = null;
        while ((line = reader.readLine()) != null) {
            log.info(line);
        }
        log.info("\n");
    }
}

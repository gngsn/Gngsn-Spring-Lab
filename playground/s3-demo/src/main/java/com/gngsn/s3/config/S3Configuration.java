package com.gngsn.s3.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class S3Configuration {

    @Value("${aws.s3.access.key}")
    private String s3AccessKey;

    @Value("${aws.s3.secret.key}")
    private String s3SecretKey;


    private final Regions BUCKET_REGION = Regions.AP_NORTHEAST_2;

    @Bean
    public AmazonS3 amazonS3() {
        return AmazonS3ClientBuilder
            .standard()
            .withCredentials(new AWSStaticCredentialsProvider(credentials()))
            .withRegion(BUCKET_REGION)
            .build();
    }

    private AWSCredentials credentials() {
        AWSCredentials credentials = new BasicAWSCredentials(
            s3AccessKey,
            s3SecretKey
        );
        return credentials;
    }
}

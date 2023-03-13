package com.gngsn.s3.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
@RequiredArgsConstructor
public class AWSConfig {

    @Value("${aws.s3.access.key}")
    private String s3AccessKey;

    @Value("${aws.s3.secret.key}")
    private String s3SecretKey;

    private final Region BUCKET_REGION = Region.AP_NORTHEAST_2;

    @Bean
    public S3Client amazoneS3(){
        AwsBasicCredentials credentials = AwsBasicCredentials.create(s3AccessKey, s3SecretKey);

        return S3Client.builder()
            .region(BUCKET_REGION)
            .credentialsProvider(StaticCredentialsProvider.create(credentials))
            .build();
    }
}

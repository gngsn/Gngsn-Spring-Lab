package com.gngsn.s3.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.InstanceProfileCredentialsProvider;
import software.amazon.awssdk.http.apache.ApacheHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import java.time.Duration;

@Configuration
public class AWSConfig {

    private final Region BUCKET_REGION = Region.AP_NORTHEAST_2;

    @Bean
    public S3Client amazonS3() {
        return S3Client.builder()
            .region(BUCKET_REGION)
            .httpClientBuilder(ApacheHttpClient.builder().connectionTimeout(Duration.ofSeconds(5)))
            .credentialsProvider(InstanceProfileCredentialsProvider.builder().build())
            .build();
    }
}

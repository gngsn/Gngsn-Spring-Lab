package com.gngsn.s3batch.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.InstanceProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.http.apache.ApacheHttpClient;

@Configuration
public class S3Configuration {
    /**
     * @return an instance of S3Client
     */
    @Bean
    public S3Client s3Client() {
        return S3Client.builder()
            .region(Region.AP_SOUTHEAST_1)
            .httpClientBuilder(ApacheHttpClient.builder())
            .credentialsProvider(InstanceProfileCredentialsProvider.builder().build())
            .build();
    }
}

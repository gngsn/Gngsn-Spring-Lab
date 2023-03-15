package com.gngsn.s3batch.step;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.http.apache.ApacheHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.paginators.ListObjectsV2Iterable;

import java.nio.ByteBuffer;
import java.time.Duration;
import java.util.Random;

@Component
@JobScope
public class UploadS3Tasklet implements Tasklet {
    private S3Client s3;

    @Value("${aws.accessKeyId}")
    private String accessKeyId;

    @Value("${aws.secretAccessKey}")
    private String secretAccessKey;

    private final String BUCKET_NAME = "s3-bucket-example-gngsn";

    public UploadS3Tasklet(
        @Value("#{jobExecutionContext[filePath]}") String filePath,
        @Value("#{jobExecutionContext[fileName]}") String fileName) {
//        this.FILE_NAME = fileName;
        AwsBasicCredentials awsCreds = AwsBasicCredentials.create(accessKeyId, secretAccessKey);

        this.s3 = S3Client.builder()
            .region(Region.AP_SOUTHEAST_1)
            .httpClientBuilder(ApacheHttpClient.builder().connectionTimeout(Duration.ofSeconds(5)))
//            .credentialsProvider(InstanceProfileCredentialsProvider.builder().profileName("local").build())
            .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
            .build();
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        try {
            ListObjectsV2Request listReq = ListObjectsV2Request.builder()
                .bucket(BUCKET_NAME)
                .maxKeys(1)
                .build();
            ListObjectsV2Iterable listRes = s3.listObjectsV2Paginator(listReq);


            listRes.stream()
                .flatMap(r -> r.contents().stream())
                .forEach(content -> System.out.println(" Key: " + content.key() + " size = " + content.size()));

//            s3.putObject(objectRequest, RequestBody.fromByteBuffer(getRandomByteBuffer(10_000)));
            return RepeatStatus.FINISHED;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private static ByteBuffer getRandomByteBuffer(int size) {
        byte[] b = new byte[size];
        new Random().nextBytes(b);
        return ByteBuffer.wrap(b);
    }
}

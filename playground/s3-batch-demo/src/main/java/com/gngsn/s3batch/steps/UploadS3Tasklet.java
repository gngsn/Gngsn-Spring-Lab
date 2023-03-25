package com.gngsn.s3batch.steps;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.InstanceProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ListObjectsRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsResponse;
import software.amazon.awssdk.services.s3.model.S3Object;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.Random;

@Component
//@JobScope
public class UploadS3Tasklet implements Tasklet {
    private S3Client s3;
//    private String filePath;
//    private String fileName;
//    private String accessKeyId;
//    private String secretAccessKey;
    private final String BUCKET_NAME = "s3-bucket-example-gngsn";


    public UploadS3Tasklet(
//        @Value("#{jobExecutionContext[filePath]}") String filePath,
//        @Value("#{jobExecutionContext[fileName]}") String fileName,
        @Value("${aws.accessKeyId}") String accessKeyId,
        @Value("${aws.secretAccessKey}") String secretAccessKey) {

//        this.fileName = fileName;
//        this.filePath = filePath;

        accessKeyId = "AKIAUVMAQGZ26C7XJAWR";
        secretAccessKey = "uUzbX4|%";
//        this.accessKeyId = accessKeyId;
//        this.secretAccessKey = secretAccessKey;
        s3 = S3Client.builder()
            .region(Region.AP_NORTHEAST_2)
            .credentialsProvider(InstanceProfileCredentialsProvider.builder().build())
            .build();
    }

    private static long calKb(Long val) {
        return val/1024;
    }
    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        try {

            ListObjectsRequest listObjects = ListObjectsRequest
                .builder()
                .bucket(BUCKET_NAME)
                .build();

            ListObjectsResponse res = s3.listObjects(listObjects);
            List<S3Object> objects = res.contents();
            for (S3Object myValue : objects) {
                System.out.print("\n The name of the key is " + myValue.key());
                System.out.print("\n The object is " + calKb(myValue.size()) + " KBs");
                System.out.print("\n The owner is " + myValue.owner());
            }
//            ListObjectsV2Request listReq = ListObjectsV2Request.builder()
//                .bucket(BUCKET_NAME)
//                .maxKeys(1)
//                .build();
//
//            ListObjectsV2Iterable listRes = s3.listObjectsV2Paginator(listReq);
//
//            listRes.stream()
//                .flatMap(r -> r.contents().stream())
//                .forEach(content -> System.out.println(" Key: " + content.key() + " size = " + content.size()));

            return RepeatStatus.FINISHED;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static ByteBuffer getRandomByteBuffer(int size) {
        byte[] b = new byte[size];
        new Random().nextBytes(b);
        return ByteBuffer.wrap(b);
    }
}

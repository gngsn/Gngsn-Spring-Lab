package com.gngsn.s3batch.step;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.time.Instant;

@Component
@JobScope
public class SetExecutionContextTasklet implements Tasklet {

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

        ExecutionContext executionContext = chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext();
        String filePath = "s3-batch-demo/src/main/resources/";
        String fileName = "movie-"+ Instant.now().getEpochSecond() + ".csv";

        executionContext.put("filePath", filePath);
        executionContext.put("fileName", fileName);
        try {
            new File(filePath + fileName).createNewFile();
        } catch (IOException e) {
            System.out.println("Error attempting to create file " + filePath + fileName + "for writing raw output." + e);
        }

        return RepeatStatus.FINISHED;
    }
}

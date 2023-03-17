package com.gngsn.s3batch;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.gngsn.s3batch.steps.SetExecutionContextTasklet;
import com.gngsn.s3batch.steps.UploadS3Tasklet;
import com.gngsn.s3batch.vo.Movie;
import com.gngsn.s3batch.vo.MovieCsv;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.step.tasklet.TaskletStep;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.core.io.FileSystemResource;

import javax.sql.DataSource;
import java.nio.charset.StandardCharsets;

@Configuration
public class CsvToS3Job {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final DataSource dataSource;

    private final UploadS3Tasklet uploadS3Tasklet;

    private final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

    private final String GET_MOVIE_SELECT = "SELECT movie_id as movieId, title, budget, overview, runtime, revenue FROM movie";

    private final int chunkSize = 50;


    public CsvToS3Job(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory, DataSource dataSource, UploadS3Tasklet uploadS3Tasklet) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.dataSource = dataSource;
        this.uploadS3Tasklet = uploadS3Tasklet;
    }

    @Bean
    public Job makeMovieDataCsvToS3Job() {
        return jobBuilderFactory
            .get("makeMovieDataCsvToS3Job")
            .start(simpleTestTasklet())
            .next(makeCsvFromDbStep())
            .next(thirdUploadS3Tasklet())
            .build();
    }

    @Bean
    public TaskletStep simpleTestTasklet() {
        return stepBuilderFactory.get("simpleTestTasklet")
            .tasklet(new SetExecutionContextTasklet())
            .build();
    }

    @Bean
    public Step makeCsvFromDbStep() {
        return stepBuilderFactory.get("makeCsvFromDb")
            .<Movie, MovieCsv>chunk(chunkSize)
            .reader(jdbcCursorItemReader())
            .processor(itemProcessor())
            .writer(batchItemWriter(null, null))
            .build();
    }

    @Bean
    public TaskletStep thirdUploadS3Tasklet() {
        return stepBuilderFactory.get("uploadS3Tasklet")
            .tasklet(uploadS3Tasklet)
            .build();
    }

    @Bean
    public JdbcCursorItemReader<Movie> jdbcCursorItemReader() {
        return new JdbcCursorItemReaderBuilder<Movie>()
            .name("jdbcCursorItemReader")
            .fetchSize(chunkSize)
            .dataSource(dataSource)
            .rowMapper(new BeanPropertyRowMapper<>(Movie.class))
            .sql(GET_MOVIE_SELECT)
            .build();
    }

    @Bean
    public ItemProcessor<Movie, MovieCsv> itemProcessor() {
        return movie -> mapper.convertValue(mapper, MovieCsv.class);
    }

    @Bean
    @JobScope
    public FlatFileItemWriter<MovieCsv> batchItemWriter(
        @Value("#{jobExecutionContext[filePath]}") String filePath,
        @Value("#{jobExecutionContext[fileName]}") String fileName
    ) {
        return new FlatFileItemWriterBuilder<MovieCsv>()
            .name("batchItemWriter")
            .resource(new FileSystemResource(filePath + fileName))
            .append(true)
            .lineAggregator(new DelimitedLineAggregator<>() {
                {
                    setDelimiter(",");
                    setFieldExtractor(new BeanWrapperFieldExtractor<>() {
                        {
                            setNames(new String[] { "movieId", "title", "budget", "overview", "runtime", "revenue" });
                        }
                    });
                }
            })
            .headerCallback(writer -> writer.write("Movie ID, Title, Overview, Runtime (Sec), Revenue"))
            .encoding(StandardCharsets.UTF_8.name())
            .build();
    }
}
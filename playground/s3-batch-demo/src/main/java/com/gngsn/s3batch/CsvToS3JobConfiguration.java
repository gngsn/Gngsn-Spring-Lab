package com.gngsn.s3batch;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.gngsn.s3batch.vo.Movie;
import com.gngsn.s3batch.vo.MovieCsv;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.core.step.tasklet.TaskletStep;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;

@Configuration
public class CsvToS3JobConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final DataSource dataSource;


    private final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

    private File fileName;

    private static final String GET_MOVIE_SELECT = "SELECT movie_id as movieId, title, budget, overview, runtime, revenue FROM movie";

    private static final int chunkSize = 50;


    public CsvToS3JobConfiguration(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory, DataSource dataSource) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.dataSource = dataSource;
    }

    @Bean
    public Job makeMovieDataCsvToS3Job() {
        return jobBuilderFactory
            .get("makeMovieDataCsvToS3Job")
//            .start(simpleTestTaskletStep())
            .start(makeCsvFromDb())
            .build();
    }

    @Bean
    public TaskletStep simpleTestTaskletStep() {
        return stepBuilderFactory.get("simpleTestTaskletStep")
            .tasklet(tasklet())
            .build();
    }

    @Bean
    public Tasklet tasklet() {
        return new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

                fileName = new File("s3-batch-demo/src/main/resources/movie-"+ Instant.now().getEpochSecond() + ".csv");
                try {
                    fileName.createNewFile();
                } catch (IOException e) {
                    System.out.println("Error attempting to create file " + fileName + "for writing raw output." + e);
                }

                return RepeatStatus.FINISHED;
            }
        };
    }

    @Bean
    public Step makeCsvFromDb() {
        return stepBuilderFactory.get("makeCsvFromDb")
            .<Movie, MovieCsv>chunk(chunkSize)
            .reader(jdbcCursorItemReader())
            .processor(itemProcessor())
            .writer(batchItemWriter())
            .build();
    }

    public JdbcCursorItemReader<Movie> jdbcCursorItemReader() {
        return new JdbcCursorItemReaderBuilder<Movie>()
            .name("jdbcCursorItemReader")
//            .fetchSize(chunkSize)
            .dataSource(dataSource)
            .rowMapper(new BeanPropertyRowMapper<>(Movie.class))
            .sql(GET_MOVIE_SELECT)
            .build();
    }

    public ItemProcessor<Movie, MovieCsv> itemProcessor() {
        fileName = new File("s3-batch-demo/src/main/resources/movie-"+ Instant.now().getEpochSecond() + ".csv");
        try {
            fileName.createNewFile();
        } catch (IOException e) {
            System.out.println("Error attempting to create file " + fileName + "for writing raw output." + e);
        }

        return movie -> mapper.convertValue(mapper, MovieCsv.class);
    }

    public FlatFileItemWriter<MovieCsv> batchItemWriter() {
        return new FlatFileItemWriterBuilder<MovieCsv>()
            .name("batchItemWriter")
            .resource(new FileSystemResource(fileName))
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
package com.gngsn.apressbatch.config;

import com.gngsn.apressbatch.batch.CustomerUpdateClassifier;
import com.gngsn.apressbatch.batch.CustomerUpdateItemReader;
import com.gngsn.apressbatch.batch.CustomerUpdateItemWriter;
import com.gngsn.apressbatch.domain.CustomerUpdate;
import com.gngsn.apressbatch.domain.Transaction;
import com.gngsn.apressbatch.utils.ConstantsJobSteps;
import com.gngsn.apressbatch.valid.CustomerItemValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.support.ClassifierCompositeItemWriter;
import org.springframework.batch.item.validator.ValidatingItemProcessor;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.batch.item.xml.builder.StaxEventItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.core.io.Resource;
import org.springframework.oxm.xstream.XStreamMarshaller;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.gngsn.apressbatch.utils.ConstantsJobSteps.IMPORT_TRANSACTIONS_STEP;


@Configuration // 기본적으로 구성클래스로 간주하기 때문에 Configauration을 명시할 필요가 없음
@RequiredArgsConstructor
public class ImportJobConfiguration {

    private final JobBuilderFactory jobBuilderFactory;

    private final StepBuilderFactory stepBuilderFactory;

    private final CustomerUpdateItemReader customerUpdateItemReader;
    private final CustomerUpdateItemWriter customerUpdateItemWriter;

    // Job은 JobBuilderFactory에 의해 정의되는데, importCustomerUpdates 스텝을 실행하도록 지정한 후 build 메서드를 호출
    @Bean
    public Job job() throws Exception {
        return this.jobBuilderFactory
            .get("importJob")
            .start(importCustomerUpdates())
            .next(importTransactions())
            .build();
    }

    /**
     * importCustomerUpdates Step
     *
     * StepBuilderFactory를 통해 정의되는데, 이 StepBuilderFactory를 사용하면 청크 기반으로 처리가 이뤄지도록 구성할 수 있는 StepBuilder를 가져올 수 있다.
     *
     * reader - CustomerUpdateItemReader(ItemReader)
     * writer - CustomerUpdateItemWriter(ItemWriter)
     *
     * @return
     * @throws Exception
     */
    @Bean(ConstantsJobSteps.IMPORT_CUSTOMER_UPDATE_STEP)
    public Step importCustomerUpdates() throws Exception {
        return this.stepBuilderFactory.get(ConstantsJobSteps.IMPORT_CUSTOMER_UPDATE_STEP)
            .<CustomerUpdate, CustomerUpdate> chunk(100)
            .reader(this.getCustomerUpdateItemReader(null))
            .processor(this.getCustomerValidatingItemProcessor(null))
            .writer(this.getCustomerUpdateItemWriter())
            .build();
    }

    /**
     * importTransactions() Step
     *
     * @return
     * @throws Exception
     */
    @Bean(IMPORT_TRANSACTIONS_STEP)
    public Step importTransactions() throws Exception {
        return this.stepBuilderFactory.get(IMPORT_TRANSACTIONS_STEP)
            .<Transaction, Transaction> chunk(100)
            .reader(transactionItemReader(null))
            .writer(transactionItemWriter(null))
            .build();
    }

    /*
        @StepScope 애너테이션을 적용한 transactionItemReader는 입력 파일의 위치를
        transactionItemReader메서드 내에서 새 Jaxb2Marshaller를 생성 후 Transaction 객체에 바인딩

        StaxEventItemReaderBuilder를 사용해 ItemReader 구성.
        해당 빌더에 Reader 이름(재시작이 가능하도록 하기 위함), 잡 파라미터에 주입되는 리소스, 각 파싱하기 위해 사용할 Jaxb2Marshaller도 전달
        build를 호출하면 StaxEventItemReader 제공.
     */
    @Bean
    @StepScope
    public StaxEventItemReader<Transaction> transactionItemReader(
        @Value("#{jobParameters['transactionFile']}") Resource transactionFile
    ) {
        return new StaxEventItemReaderBuilder<Transaction>()
            .name("fooReader")
            .resource(transactionFile)
            .addFragmentRootElements("transaction")
            .unmarshaller(unmarshaller())
            .build();
    }

    @Bean
    public XStreamMarshaller unmarshaller() {
        return new XStreamMarshaller();
    }

    /*
        JdbcBatchItemWriter는 DataSource와 SQL문을 전달받으며,
        ItemWriter가 아이텀의 프로퍼티 이름을 키로 사용해 SQL문의 값을 설정할 수 있게 하는 등의
        JdbcBatchItemWriter에 필요한 구성을 하는 데 사용된다.
     */
    @Bean
    public JdbcBatchItemWriter<Transaction> transactionItemWriter(
        DataSource dataSource
    ) {
        return new JdbcBatchItemWriterBuilder<Transaction>()
            .dataSource(dataSource)
            .sql("INSERT INTO transaction (transaction_id, account_id, credit, debit, timestamp) VALUES (:transactionId, :accountId, :credit, :debit, :timestamp)")
            .beanMapped()
            .build();
    }


    /**
     * customUpdateItemReader
     * Step Scope Bean - Job Parameter를 사용해서 읽을 파일의 위치를 지정
     *
     * @param inputFile
     * @return FlatFileItemReaderBuilder
     */
    @Bean
    @StepScope
    public FlatFileItemReader<CustomerUpdate> getCustomerUpdateItemReader(
        @Value("#{jobParameters['customerUpdateFile']}") Resource inputFile
    ) throws Exception {
        return new FlatFileItemReaderBuilder<CustomerUpdate>()
            .name("customerUpdateItemReader")
            .resource(inputFile)
            .lineTokenizer(customerUpdateItemReader.customerUpdatesLineTokenizer())
            .fieldSetMapper(customerUpdateItemReader.customerUpdateFieldSetMapper())
            .build();
    }

    @Bean
    public ValidatingItemProcessor<CustomerUpdate> getCustomerValidatingItemProcessor(
        CustomerItemValidator validator
    ) {
        ValidatingItemProcessor<CustomerUpdate> customerValidatingItemProcessor =
            new ValidatingItemProcessor<>(validator);

        customerValidatingItemProcessor.setFilter(true);
        return customerValidatingItemProcessor;
    }

    @Bean
    public ClassifierCompositeItemWriter<CustomerUpdate> getCustomerUpdateItemWriter() {
        CustomerUpdateClassifier classifier =
            new CustomerUpdateClassifier(
                customerUpdateItemWriter.customerNameUpdateItemWriter(),
                customerUpdateItemWriter.customerAddressUpdateItemWriter(),
                customerUpdateItemWriter.customerContactUpdateItemWriter()
            );
        ClassifierCompositeItemWriter<CustomerUpdate> compositeItemWriter = new ClassifierCompositeItemWriter<>();

        compositeItemWriter.setClassifier(classifier);
        return compositeItemWriter;
    }

}
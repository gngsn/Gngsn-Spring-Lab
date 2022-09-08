package com.gngsn.apressbatch.batch;

import com.gngsn.apressbatch.domain.CustomerUpdate;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;


/**
 * STEP1. 변경할 고객 정보 불러오기
 *
 * 3개의 ItemWriter를 등록한다.
 * COALESCE를 사용하는 이유는 입력파일에서 제공한 값만 갱신하기 위함
 *
 * p.508 ~ p.514
 */
@Configuration
@RequiredArgsConstructor
public class CustomerUpdateItemWriter {

    private final DataSource dataSource;

    @Bean("customerNameUpdateItemWriter")
    public JdbcBatchItemWriter<CustomerUpdate> customerNameUpdateItemWriter() {
        return new JdbcBatchItemWriterBuilder<CustomerUpdate>()
            .beanMapped()
            .sql("UPDATE customer SET first_name = COALESCE(:firstName, first_name), middle_name = COALESCE(:middleName, middle_name), last_name = COALESCE(:lastName, last_name) WHERE customer_id = :customerId")
            .dataSource(dataSource)
            .build();
    }

    @Bean("customerAddressUpdateItemWriter")
    public JdbcBatchItemWriter<CustomerUpdate> customerAddressUpdateItemWriter() {
        return new JdbcBatchItemWriterBuilder<CustomerUpdate>()
            .beanMapped()
            .sql("UPDATE customer SET address1 = COALESCE(:address1, address1), address2 = COALESCE(:address2, address2), city = COALESCE(:city, city), city = COALESCE(:city, city), state = COALESCE(:state, state), postal_code = COALESCE(:postalCode, postal_code) WHERE customer_id = :customerId")
            .dataSource(dataSource)
            .build();
    }


    @Bean("customerContactUpdateItemWriter")
    public JdbcBatchItemWriter<CustomerUpdate> customerContactUpdateItemWriter(

    ) {
        return new JdbcBatchItemWriterBuilder<CustomerUpdate>()
            .beanMapped()
            .sql("UPDATE customer SET email_address = COALESCE(:emailAddress, email_address), home_phone = COALESCE(:homePhone, home_phone), cell_phone = COALESCE(:cellPhone, cell_phone), work_phone = COALESCE(:workPhone, work_phone), notification_pref = COALESCE(:notificationPreference, notification_pref) WHERE customer_id = :customerId")
            .dataSource(dataSource)
            .build();
    }

}

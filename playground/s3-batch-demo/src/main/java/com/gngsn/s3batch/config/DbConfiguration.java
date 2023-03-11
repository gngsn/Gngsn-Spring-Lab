package com.gngsn.s3batch.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.jdbc.support.JdbcTransactionManager;

import javax.sql.DataSource;

@Configuration
public class DbConfiguration {

    @Bean
    public DataSource batchDataSource() {
        return new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
//            .generateUniqueName(true)
            .build();
    }

    @Bean
    public JdbcTransactionManager batchTransactionManager(DataSource dataSource) {
        return new JdbcTransactionManager(dataSource);
    }
}

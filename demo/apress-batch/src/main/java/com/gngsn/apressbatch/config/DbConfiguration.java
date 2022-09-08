package com.gngsn.apressbatch.config;

import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;


@Configuration
public class DbConfiguration {

    @Autowired
    JdbcProperties jdbcProperties;


    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(jdbcProperties.getDriverClass());
        dataSource.setUrl(jdbcProperties.getUrl());
        dataSource.setUsername(jdbcProperties.getUsername());
        dataSource.setPassword(jdbcProperties.getPassword());
        return dataSource;
    }

    @Bean
    public SqlSessionFactoryBean factoryBean() {
        SqlSessionFactoryBean factory = new SqlSessionFactoryBean();
        factory.setDataSource(dataSource());
        factory.setConfigLocation(new ClassPathResource("mybatis-config.xml"));

        return factory;
    }

    @Bean
    public SqlSessionTemplate sqlTemplate() throws Exception {
        SqlSessionFactoryBean factory = factoryBean();
        SqlSessionTemplate template = new SqlSessionTemplate(factory.getObject());

        return template;
    }
}

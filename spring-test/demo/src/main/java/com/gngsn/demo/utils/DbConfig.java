package com.gngsn.demo.utils;

import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
@MapperScan("com.gngsn.demo.UserMapper")
public class DbConfig {
    @Autowired
    JdbcProperties jdbcProperties;

    @Bean
    public DataSource ds() {
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
        factory.setDataSource(ds());
        factory.setConfigLocation(new ClassPathResource("mybatis-config.xml"));

        return factory;
    }

    @Bean
    public SqlSessionTemplate sqltemplate() throws Exception {
        SqlSessionFactoryBean factory = factoryBean();
        SqlSessionTemplate template = new SqlSessionTemplate(factory.getObject());

        return template;
    }

}

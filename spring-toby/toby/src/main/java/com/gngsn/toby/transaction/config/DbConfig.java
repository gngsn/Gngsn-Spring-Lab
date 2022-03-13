package com.gngsn.toby.transaction.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.Objects;

@Configuration
public class DbConfig {

    private final static String MAPPER_LOCATIONS = "classpath:sqlmapper.xml";

    @Resource
    private TestJdbcProperties jdbcProperties;

    @Bean(name = "testDataSource")
    public HikariDataSource dataSource() {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName(jdbcProperties.getDriverClassName());
        hikariConfig.setJdbcUrl(jdbcProperties.getUrl());
        hikariConfig.setUsername(jdbcProperties.getUsername());
        hikariConfig.setPassword(jdbcProperties.getPassword());
        return new HikariDataSource(hikariConfig);
    }

    @Bean(name = "masterSqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(@Qualifier("testDataSource") DataSource dataSource) throws Exception {
        return this.getSqlSessionFactory(dataSource);
    }

    @Bean(name = "masterSqlSessionTemplate")
    public SqlSessionTemplate sqlSessionTemplate(@Qualifier("masterSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

//    @Bean
//    public PlatformTransactionManager txManager(@Qualifier("testDataSource") DataSource dataSource) {
//        DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager(dataSource);
//        dataSourceTransactionManager.setNestedTransactionAllowed(true);
//        return dataSourceTransactionManager;
//    }

    protected SqlSessionFactory getSqlSessionFactory(DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
        sessionFactoryBean.setDataSource(dataSource);

        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        sessionFactoryBean.setMapperLocations(resolver.getResources(MAPPER_LOCATIONS));
        //mapper path
        Objects.requireNonNull(sessionFactoryBean.getObject()).getConfiguration().setMapUnderscoreToCamelCase(true); //camelCase
        return sessionFactoryBean.getObject();
    }

}
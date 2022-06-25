package com.gngsn.batchtest;


import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.batch.MyBatisBatchItemWriter;
import org.mybatis.spring.batch.builder.MyBatisBatchItemWriterBuilder;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;

//@SpringBootTest
class BatchTestApplicationTests {
	int INSERT_SIZE = 1000;

	// 1sec 36ms
	@Test
	void jdbcBatchItemWriter() throws Exception {
		JdbcBatchItemWriter<User> jdbcBatchItemWriter = new JdbcBatchItemWriterBuilder<User>()
			.dataSource(dataSource())
			.sql("INSERT INTO user_jdbc(name, email, password) VALUES (:name, :email, :password)")
			.beanMapped()
			.build();

		jdbcBatchItemWriter.afterPropertiesSet();

		List<User> users = new ArrayList<>(INSERT_SIZE);
		for (int i = 0; i < INSERT_SIZE; i++) {
			users.add(new User("gngsn" + i, "gngsn" + i + "@email.com", "password~"));
		}

		jdbcBatchItemWriter.write(users);
	}

	// 9sec 438ms
	@Test
	void mybatisBatchItemWriter() throws Exception {
		MyBatisBatchItemWriter<User> myBatisBatchItemWriter = new MyBatisBatchItemWriterBuilder<User>()
			.sqlSessionFactory(sqlSession())
			.statementId("user.insertUser")
			.assertUpdates(false)
			.build()
			;

		myBatisBatchItemWriter.afterPropertiesSet();

		List<User> users = new ArrayList<>(INSERT_SIZE);

		for (int i = 0; i < INSERT_SIZE; i++) {
			users.add(new User("gngsn" + i, "gngsn" + i + "@email.com", "password~"));
		}

		myBatisBatchItemWriter.write(users);
	}

	private SqlSessionFactory sqlSession() throws Exception {
	SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
		sessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResource("classpath:/batchTest.xml"));
		sessionFactoryBean.setDataSource(dataSource());
		return sessionFactoryBean.getObject();
	}

	private DataSource dataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
		dataSource.setUrl("jdbc:mysql://localhost:3306/test");
		dataSource.setUsername("gngsn");
		dataSource.setPassword("Test1234!");

		return dataSource;
	}
}

package com.gngsn.demo;

import com.gngsn.demo.bulkUpsert.BulkUpsertUserDAO;
import com.gngsn.demo.bulkUpsert.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@SpringBootTest
public class BulkUpsertTests {

    @Autowired
    public BulkUpsertUserDAO userDAO;

    int TEST_SIZE = 100_000;


    @BeforeEach
    public void initData() {  // 000 ~ 100
        userDAO.truncate();
        List<UserVO> insertList = new ArrayList<>(TEST_SIZE);

        for (int i = 1; i <= TEST_SIZE; i++) {
            insertList.add(new UserVO(String.format("UserVO%05d", i), "Init"));
        }

        int cnt = userDAO.insertInit(insertList);
        Assertions.assertEquals(cnt, TEST_SIZE);
    }

    @Test
    public void given_insert100() {

        // given
        List<UserVO> testSet = createTestData(TEST_SIZE);

        // when
        double beforeTime = System.currentTimeMillis();

        int cnt = userDAO.bulkUpsertUserList(testSet);

        double afterTime = System.currentTimeMillis();


        // then
        Assertions.assertEquals(cnt, TEST_SIZE);
        log.info("### run time : {}s", (afterTime - beforeTime) / 1000);
    }


    @Test
    public void given_insert80update20() {

        // given
        int insertSize = (int) (TEST_SIZE * 0.8);
        List<UserVO> testSet = createTestData(insertSize);

        // when
        double beforeTime = System.currentTimeMillis();
        int cnt = userDAO.bulkUpsertUserList(testSet);
        double afterTime = System.currentTimeMillis();

        // then
        Assertions.assertEquals(cnt, TEST_SIZE + (TEST_SIZE - insertSize));
        log.info("### run time : {}s", (afterTime - beforeTime) / 1000);
    }

    @Test
    public void given_insert20update80() {

        // given
        int insertSize = (int) (TEST_SIZE * 0.2);
        List<UserVO> testSet = createTestData(insertSize);

        // when
        double beforeTime = System.currentTimeMillis();
        int cnt = userDAO.bulkUpsertUserList(testSet);
        double afterTime = System.currentTimeMillis();

        // then
        Assertions.assertEquals(cnt, TEST_SIZE + (TEST_SIZE - insertSize));
        log.info("### run time : {}s", (afterTime - beforeTime) / 1000);
    }


    @Test
    public void given_update100() {

        // given
        List<UserVO> testSet = createTestData(0);

        // when
        double beforeTime = System.currentTimeMillis();
        int cnt = userDAO.bulkUpsertUserList(testSet);
        double afterTime = System.currentTimeMillis();

        // then
        Assertions.assertEquals(cnt, TEST_SIZE + TEST_SIZE);
        log.info("### run time : {}s", (afterTime - beforeTime) / 1000);
    }


    List<UserVO> createTestData(int insertSize) {  // 000 ~ 020
        List<UserVO> testSet = new ArrayList<>(TEST_SIZE);
        int outOfTestSizeNum = TEST_SIZE * 2;

        for (int i = 1; i <= insertSize; i++) {
            testSet.add(new UserVO(String.format("UserVO%05d", (outOfTestSizeNum + i)), "Insert"));
        }

        for (int i = 1; i <= (TEST_SIZE - insertSize); i++) {
            testSet.add(new UserVO(String.format("UserVO%05d", i), "Update"));
        }

        return testSet;
    }
}

package com.gngsn.demo;

import com.gngsn.demo.bulkUpsert.BulkUpsertUserDAO;
import com.gngsn.demo.bulkUpsert.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.jupiter.api.Assertions;
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
    int TEST_SIZE = 100;

//    @BeforeEach
//    public void setUp() throws Exception {
//        userDAO = new BulkUpsertUserDAO(this.sqlSession());
//    }

    void initData() {  // 000 ~ 100
        userDAO.truncate();
        List<UserVO> insertList = new ArrayList<>(TEST_SIZE);

        for (int i = 1; i <= TEST_SIZE; i++) {
            insertList.add(new UserVO(String.format("UserVO%03d", i), "Init"));
        }

        int cnt = userDAO.insertInit(insertList);
        Assertions.assertEquals(cnt, TEST_SIZE);
    }

    List<UserVO> createTestData(int updateSize) {  // 000 ~ 020
        List<UserVO> testSet = new ArrayList<>(TEST_SIZE);

        for (int i = 1; i <= updateSize; i++) {
            testSet.add(new UserVO(String.format("UserVO%03d", i), "Update"));
        }

        for (int i = 1; i <= (TEST_SIZE - updateSize); i++) {
            testSet.add(new UserVO(String.format("UserVO%03d", (600 + i)), "Insert"));
        }

        return testSet;
    }


    @Test
    public void given_insert80update20_when_tempTable2() {
        initData();
        List<UserVO> testSet = createTestData(20);
        int cnt = userDAO.bulkUpdate(testSet);
        Assertions.assertEquals(testSet.size(), TEST_SIZE);
        log.info(String.valueOf(cnt));
    }

    @Test
    public void given_insert100() {
        initData();
        List<UserVO> testSet = createTestData(0);
        int cnt = userDAO.bulkUpsertUserList(testSet);
        Assertions.assertEquals(testSet.size(), TEST_SIZE);
        Assertions.assertEquals(cnt, 100);
    }


    @Test
    public void given_update100() {
        initData();
        List<UserVO> testSet = createTestData(100);
        int cnt = userDAO.bulkUpsertUserList(testSet);
        Assertions.assertEquals(testSet.size(), TEST_SIZE);
        Assertions.assertEquals(cnt, 200);
    }

    @Test
    public void given_insert80update20() {
        initData();
        List<UserVO> testSet = createTestData(20);
        int cnt = userDAO.bulkUpsertUserList(testSet);
        Assertions.assertEquals(testSet.size(), TEST_SIZE);
        Assertions.assertEquals(cnt, 120);
    }


    @Test
    public void given_insert20update80() {
        initData();
        List<UserVO> testSet = createTestData(80);
        int cnt = userDAO.bulkUpsertUserList(testSet);
        Assertions.assertEquals(testSet.size(), TEST_SIZE);
        Assertions.assertEquals(cnt, 180);
    }

    @Test
    public void given_insert80update20_when_tempTable() {
        initData();
        List<UserVO> testSet = createTestData(20);
        int cnt = userDAO.bulkUpsertUsingTempTable(testSet);
        Assertions.assertEquals(testSet.size(), TEST_SIZE);
        log.info(String.valueOf(cnt));
//        Assertions.assertEquals(cnt, 120);
    }

    public class TemporaryTableTest {

    }

    void createInsertData(int num) { // 300 ~ 400
        List<UserVO> insertList = new ArrayList<>(num);

        for (int i = 0; i < num; i++) {
            insertList.add(new UserVO(String.format("UserVO%03d", i), "Insert"));
        }
    }

    void createUpdateData(int num) {  // 000 ~ 020
        List<UserVO> insertList = new ArrayList<>(num);

        for (int i = 0; i < num; i++) {
            insertList.add(new UserVO(String.format("UserVO%03d", i), "Update"));
        }
    }


    private SqlSessionFactory sqlSession() throws Exception {
        SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
        sessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResource("classpath:/bulk-upsert-sql.xml"));
        sessionFactoryBean.setDataSource(dataSource());
        return sessionFactoryBean.getObject();
    }

    private DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/bulk-update-test");
        dataSource.setUsername("gngsn");
        dataSource.setPassword("Test1234!");

        return dataSource;
    }
}

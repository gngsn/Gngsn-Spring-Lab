package com.gngsn.demo;

import com.gngsn.demo.bulkUpsert.BulkUpsertUserDAO;
import com.gngsn.demo.bulkUpsert.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;


@Slf4j
@SpringBootTest
public class BulkUpdateCompareTests {

    @Autowired
    public BulkUpsertUserDAO userDAO;

    public int TEST_SIZE = 500_000;

    @BeforeEach
    void setUp() {
        userDAO.truncate();
        List<UserVO> insertList = new ArrayList<>(TEST_SIZE);

        for (int i = 1; i <= TEST_SIZE; i++) {
            insertList.add(new UserVO(String.format("UserVO%05d", i), "Init"));
        }

        int cnt = userDAO.insertInit(insertList);
        Assertions.assertEquals(TEST_SIZE, cnt);
    }

    @Test
    public void when_TempTable() {

        // Given
        List<UserVO> updateList = createTestData(TEST_SIZE);

        // When : update bulk data using temporary table.
        double beforeTime = System.currentTimeMillis();
        userDAO.bulkUpdateFromTempTable(updateList);
        double afterTime = System.currentTimeMillis();


        // Then : the number of updated data should be equal to half of the test set.
        int updateUserCnt = userDAO.selectUpdateUserCnt();
        Assertions.assertEquals(TEST_SIZE, updateUserCnt);

        log.info("### run time : {}s", (afterTime - beforeTime) / 1000); // TEST SET: 500 - 0.108s, 5000 - 0.204s
    }

    @Test
    public void when_Upsert() {

        // Given : create updateList with half the size of test set.
//        int UPDATE_SIZE = TEST_SIZE / 2;
        List<UserVO> updateList = createTestData(TEST_SIZE);

        // When : update bulk data using temporary table.
        double beforeTime = System.currentTimeMillis();
        int cnt = userDAO.bulkUpsertUserList(updateList);
        double afterTime = System.currentTimeMillis();

        // Then : the number of updated data should be equal to half of the test set.
        Assertions.assertEquals(2 * TEST_SIZE, cnt);

        log.info("### run time : {}s", (afterTime - beforeTime) / 1000);
    }

    // 2 * TEST_SIZE -> select + update

    List<UserVO> createTestData(int UPDATE_SIZE) {
        List<UserVO> updateList = new ArrayList<>(UPDATE_SIZE);

        for (int i = 1; i <= TEST_SIZE; i++) {
            updateList.add(new UserVO(String.format("UserVO%05d", i), "Update"));
        }

        return updateList;
    }

}

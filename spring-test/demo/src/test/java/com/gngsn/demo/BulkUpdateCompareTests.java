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

    public int TEST_SIZE = 20000;

    @BeforeEach
    void setUp() {
        userDAO.truncate();
        List<UserVO> insertList = new ArrayList<>(TEST_SIZE);

        for (int i = 1; i <= TEST_SIZE; i++) {
            insertList.add(new UserVO(String.format("UserVO%03d", i), "Init"));
        }

        int cnt = userDAO.insertInit(insertList);
        Assertions.assertEquals(cnt, TEST_SIZE);
    }

    @Test
    public void when_UpdateFromTempTable__then_EqualsUpdatedCount() {

        // Given : create updateList with half the size of test set.
        int UPDATE_SIZE = TEST_SIZE / 2;
        List<UserVO> updateList = new ArrayList<>(UPDATE_SIZE);

        for (int i = 1; i <= TEST_SIZE; i += 2) {
            updateList.add(new UserVO(String.format("UserVO%03d", i), "Update"));
        }

        double beforeTime = System.currentTimeMillis();

        // When : update bulk data using temporary table.
        userDAO.bulkUpdateFromTempTable(updateList);

        double afterTime = System.currentTimeMillis();


        // Then : the number of updated data should be equal to half of the test set.
        int updateUserCnt = userDAO.selectUpdateUserCnt();
        Assertions.assertEquals(updateUserCnt, UPDATE_SIZE);

        log.info("### run time : {}s", (afterTime-beforeTime) / 1000); // TEST SET: 500 - 0.108s, 5000 - 0.204s
    }

    @Test
    public void when_UpdateFromTempTableCallEach__then_EqualsUpdatedCount() {
        // Given : create updateList with half the size of test set.
        int UPDATE_SIZE = TEST_SIZE / 2;
        List<UserVO> updateList = new ArrayList<>(UPDATE_SIZE);

        for (int i = 1; i <= TEST_SIZE; i += 2) {
            updateList.add(new UserVO(String.format("UserVO%03d", i), "Update"));
        }

        double beforeTime = System.currentTimeMillis();
        // When : update bulk data using temporary table.
        userDAO.createTempTable();
        userDAO.insertTempTable(updateList);
        userDAO.updateMainTable();
        userDAO.dropTempTable();

        double afterTime = System.currentTimeMillis();


        // Then : the number of updated data should be equal to half of the test set.
        int updateUserCnt = userDAO.selectUpdateUserCnt();
        Assertions.assertEquals(updateUserCnt, UPDATE_SIZE);

        log.info("### run time : {}s", (afterTime-beforeTime) / 1000); // TEST SET: 500 - 0.108s, 5000 - 0.204s
    }

    @Test
    public void when_updateMultiLine__then_equalsUserUpdatedCount() {

        // Given : create updateList with half the size of test set.
        int UPDATE_SIZE = TEST_SIZE / 2;
        List<UserVO> updateList = new ArrayList<>(UPDATE_SIZE);

        for (int i = 1; i <= TEST_SIZE; i += 2) {
            updateList.add(new UserVO(String.format("UserVO%03d", i), "Update"));
        }

        double beforeTime = System.currentTimeMillis();

        // When : update bulk data using temporary table.
        userDAO.bulkUpdateMultiLine(updateList);

        double afterTime = System.currentTimeMillis();

        // Then : the number of updated data should be equal to half of the test set.
        int updateUserCnt = userDAO.selectUpdateUserCnt();
        Assertions.assertEquals(updateUserCnt, UPDATE_SIZE);

        log.info("### run time : {}s", (afterTime-beforeTime) / 1000); // TEST SET: 500 - 0.705s, 5000 - 1.491s
    }

    /**
     *
     * 500   :  0.056s
     * 2500  :  0.24s
     * 5000  :  0.427s
     * 10000 :  0.772s
     *
     */
    @Test
    public void when_Upsert__then_EqualsUpdatedCount() {

        // Given : create updateList with half the size of test set.
        int UPDATE_SIZE = TEST_SIZE / 2;
        List<UserVO> updateList = new ArrayList<>(UPDATE_SIZE);

        for (int i = 1; i <= TEST_SIZE; i += 2) {
            updateList.add(new UserVO(String.format("UserVO%03d", i), "Update"));
        }


        // When : update bulk data using temporary table.
        double beforeTime = System.currentTimeMillis();

        userDAO.bulkUpsertUserList(updateList);

        double afterTime = System.currentTimeMillis();


        // Then : the number of updated data should be equal to half of the test set.
        int updateUserCnt = userDAO.selectUpdateUserCnt();
        Assertions.assertEquals(updateUserCnt, UPDATE_SIZE);

        log.info("### run time : {}s", (afterTime-beforeTime) / 1000);
    }

}

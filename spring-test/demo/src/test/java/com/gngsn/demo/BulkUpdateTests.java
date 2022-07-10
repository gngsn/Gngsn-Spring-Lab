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
public class BulkUpdateTests {

    @Autowired
    public BulkUpsertUserDAO userDAO;

    public int TEST_SIZE = 5000;

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
    public void given_updateHalfOfTestSet__when_update__then_equalsUserUpdatedCount() {
        double beforeTime = System.currentTimeMillis();

        // Given : create updateList with half the size of test set.
        int UPDATE_SIZE = TEST_SIZE / 2;
        List<UserVO> updateList = new ArrayList<>(UPDATE_SIZE);

        for (int i = 1; i <= TEST_SIZE; i += 2) {
            updateList.add(new UserVO(String.format("UserVO%03d", i), "Update"));
        }

        // When : update bulk data using temporary table.
        userDAO.bulkUpdateWithTempTable(updateList);

        double afterTime = System.currentTimeMillis();


        // Then : the number of updated data should be equal to half of the test set.
        int updateUserCnt = userDAO.selectUpdateUserCnt();
        Assertions.assertEquals(updateUserCnt, UPDATE_SIZE);

        log.info("### run time : {}s", (afterTime-beforeTime) / 1000); // TEST SET: 500 - 0.108s, 5000 - 0.204s
    }

    @Test
    public void given_updateHalfOfTestSet__when_updateMultiLine__then_equalsUserUpdatedCount() {
        double beforeTime = System.currentTimeMillis();

        // Given : create updateList with half the size of test set.
        int UPDATE_SIZE = TEST_SIZE / 2;
        List<UserVO> updateList = new ArrayList<>(UPDATE_SIZE);

        for (int i = 1; i <= TEST_SIZE; i += 2) {
            updateList.add(new UserVO(String.format("UserVO%03d", i), "Update"));
        }

        // When : update bulk data using temporary table.
        userDAO.bulkUpdateMultiLine(updateList);

        double afterTime = System.currentTimeMillis();

        // Then : the number of updated data should be equal to half of the test set.
        int updateUserCnt = userDAO.selectUpdateUserCnt();
        Assertions.assertEquals(updateUserCnt, UPDATE_SIZE);

        log.info("### run time : {}s", (afterTime-beforeTime) / 1000); // TEST SET: 500 - 0.705s, 5000 - 1.491s
    }
}

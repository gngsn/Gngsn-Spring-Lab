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

    public int TEST_SIZE = 100;

    @BeforeEach
    void initData() {
        userDAO.truncate();
        List<UserVO> insertList = new ArrayList<>(TEST_SIZE);

        for (int i = 1; i <= TEST_SIZE; i++) {
            insertList.add(new UserVO(String.format("UserVO%03d", i), "Init"));
        }

        int cnt = userDAO.insertInit(insertList);
        Assertions.assertEquals(cnt, TEST_SIZE);
    }

    @Test
    public void given_userNumEven__when_update__then_50() {
        int UPDATE_SIZE = TEST_SIZE / 2;
        List<UserVO> updateList = new ArrayList<>(UPDATE_SIZE);

        for (int i = 1; i <= UPDATE_SIZE; i += 2) {
            updateList.add(new UserVO(String.format("UserVO%03d", i), "Update"));
        }

        int cnt = userDAO.bulkUpdate(updateList);

        Assertions.assertEquals(cnt, UPDATE_SIZE);
    }
}

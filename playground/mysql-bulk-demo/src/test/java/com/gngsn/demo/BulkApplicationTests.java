package com.gngsn.demo;

import com.gngsn.demo.bulkInsert.CalcInsertTimeService;
import com.gngsn.demo.bulkInsert.UserVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class BulkApplicationTests {

    public static final Logger log = LoggerFactory.getLogger(BulkApplicationTests.class);
    @Autowired
    private CalcInsertTimeService insertService;

    private final int INSERT_SIZE = 1_00;

    private List<UserVO> users = new ArrayList<>();


    @BeforeEach
    void before() {
        log.info("===================================\n");

        for (int i = 0; i < INSERT_SIZE; i++) {
            users.add(new UserVO(
                "test" + i,
                "test@email.com",
                "vFnfSEsDj!@#$")
            );
        }

        log.info("Test Size : " + INSERT_SIZE);
    }

    @Test
    void basicInsert() {

        long basicDuration = insertService.basicInsert(users);

        log.info("Basic Insert Duration : " + basicDuration + "ms");
        log.info("\n=================================");
    }


    @Test
    void bulkInsert() {

        long bulkDuration = insertService.bulkInsert(users);

        log.info("Bulk Insert Duration : " + bulkDuration + "ms");
        log.info("\n=================================");
    }

}

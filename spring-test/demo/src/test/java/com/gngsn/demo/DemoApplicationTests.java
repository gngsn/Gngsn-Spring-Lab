package com.gngsn.demo;

import com.gngsn.demo.bulkInsert.InsertService;
import com.gngsn.demo.bulkInsert.User;
import com.gngsn.demo.bulkInsert.UserDAO;
import com.gngsn.demo.utils.RandomString;
import lombok.extern.log4j.Log4j2;
import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@Log4j2
@SpringBootTest
class DemoApplicationTests {

    @Autowired
    private InsertService insertService;

    private final int INSERT_SIZE = 1_000;

    private List<User> users = new ArrayList<>();


    @BeforeEach
    void before() {
        log.info("===================================\n");

        for (int i=0; i < INSERT_SIZE; i++) {
            users.add(new User(
                    "gngsn" + i,
                    "gngsn" + i + "@email.com",
                    new RandomString().nextString())
            );
        }

        log.info("Test Size : " + INSERT_SIZE);
    }

    @Test
    void basicInsert() {

        long basicDuration = insertService.basicInsert(users);

        log.info("Basic Insert Duration : " + basicDuration);
        log.info("\n=================================");
    }


    @Test
    void bulkInsert() {

        long bulkDuration = insertService.bulkInsert(users);

        log.info("Bulk Insert Duration : " + bulkDuration);
        log.info("\n=================================");
    }

}

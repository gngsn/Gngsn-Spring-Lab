package com.gngsn.demo;

import com.gngsn.demo.utils.RandomString;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class DemoApplicationTests {
    @Autowired
    private InsertService insertService;

    private List<User> users = new ArrayList<>();

    @BeforeEach
    void genUser() {
        for (int i=0; i < 1000; i++) {
            users.add(new User(
                    "gngsn" + i,
                    "gngsn" + i + "@email.com",
                    new RandomString().nextString())
            );
        }
    }

    @Test
    void contextLoads() {
        long basicDuration = insertService.basicInsert(users);
        long bulkDuration = insertService.bulkInsert(users);

        System.out.println("Basic Insert Duration : " + basicDuration);
        System.out.println("Bulk Insert Duration : " + bulkDuration);
    }
}

package com.gngsn.demo.bulkInsert;

import com.gngsn.demo.utils.RandomString;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Log4j2
//@Component
public class UserInsertRunner implements ApplicationRunner {
    @Autowired
    private CalcInsertTimeService insertService;

    private final int INSERT_SIZE = 10_000;

    private List<User> users = new ArrayList<>();

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("\n========== Runner ===========\n");
        log.info("Test Size : " + INSERT_SIZE);
        genUser();

        long basicDuration = insertService.basicInsert(users);
        long bulkDuration = insertService.bulkInsert(users);

        log.info("Basic Insert Duration : " + basicDuration);
        log.info("Bulk Insert Duration : " + bulkDuration);

        log.info("\n=============================\n");
    }

    private void genUser() {
        for (int i = 0; i < INSERT_SIZE; i++) {
            users.add(new User(
                    "gngsn" + i,
                    "gngsn" + i + "@email.com",
                    new RandomString().nextString())
            );
        }
    }
}
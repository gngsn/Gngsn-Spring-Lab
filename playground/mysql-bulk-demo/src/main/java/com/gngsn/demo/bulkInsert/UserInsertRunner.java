package com.gngsn.demo.bulkInsert;

import com.gngsn.demo.utils.RandomString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserInsertRunner implements ApplicationRunner {

    public static final Logger log = LoggerFactory.getLogger(UserInsertRunner.class);
    private final CalcInsertTimeService insertService;
    private final int INSERT_SIZE = 10;

    private List<UserVO> users = new ArrayList<>();

    public UserInsertRunner(final CalcInsertTimeService insertService) {
        this.insertService = insertService;
    }

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
            users.add(new UserVO(
                "gngsn" + i,
                "gngsn" + i + "@email.com",
                new RandomString().nextString())
            );
        }
    }
}
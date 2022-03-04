package com.gngsn.demo;

import com.gngsn.demo.utils.RandomString;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

//@RunWith(SpringRunner.class)
@MybatisTest
public class UserInsertTest {

//    @Mock
    @Autowired
    InsertService insertService;

    @Autowired
    UserMapper userMapper;

    @Before
    void init() {
        insertService = new InsertService();
    }

    @Test
    public void getBulkTest() {
        List<User> users = new ArrayList<>();

        for (int i=0; i < 10000; i++) {
            users.add(new User(
                    "gngsn" + i,
                    "gngsn" + i + "@email.com",
                    new RandomString().nextString())
            );
        }

        printDuration("Basic Insert", insertService.basicInsert(users));
        printDuration("Bulk Insert", insertService.bulkInsert(users));
    }

    private void printDuration(String title, long duration) {
        System.out.println(String.format("=============  %s  ==========", title));
        System.out.println(String.format(" Duration :      %s            ", duration));
        System.out.println(String.format("====================================="));
    }
}

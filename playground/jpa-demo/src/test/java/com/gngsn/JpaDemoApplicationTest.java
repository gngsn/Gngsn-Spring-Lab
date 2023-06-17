package com.gngsn;

import com.gngsn.entity.User;
import com.gngsn.repository.UserRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

//@DataJpaTest
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
abstract class JpaDemoApplicationTest {
    @Autowired
    UserRepository userRepository;

    @BeforeAll
    void setUp() {
        userRepository.saveAll(generateUsers());
    }
    private List<User> generateUsers() {
        List<User> users = new ArrayList<>();

        User john = new User("john", LocalDate.of(2020, Month.APRIL, 13));
        john.setEmail("john@somedomain.com");
        john.setLevel(1);
        john.setActive(true);

        users.add(john);

        return users;
    }

    @AfterAll
    public void resetDatabase() {
        userRepository.deleteAll();
    }
}

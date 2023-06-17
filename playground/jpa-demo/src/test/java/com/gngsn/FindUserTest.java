package com.gngsn;

import com.gngsn.entity.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class FindUserTest extends JpaDemoApplicationTest {

    @Test
    void testFindAll() {
        List<User> users = (List<User>) userRepository.findAll();
        Assertions.assertEquals(10, users.size());
    }
}

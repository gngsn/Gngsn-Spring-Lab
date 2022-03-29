package com.gngsn.demo.bulkInsert;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class InsertService {

    @Autowired
    private UserDAO userDAO;

    public long basicInsert(List<User> users) {
        long start = System.currentTimeMillis();

        users.forEach(user -> userDAO.insertUser(user));

        return System.currentTimeMillis() - start;
    }

    public long bulkInsert(List<User> users) {
        long start = System.currentTimeMillis();

        userDAO.bulkInsertUserList(users);

        return System.currentTimeMillis() - start;
    }
}
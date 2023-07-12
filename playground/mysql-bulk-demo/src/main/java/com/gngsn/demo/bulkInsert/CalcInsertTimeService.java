package com.gngsn.demo.bulkInsert;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CalcInsertTimeService {

    private final UserDAO userDAO;

    public CalcInsertTimeService(final UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public long basicInsert(List<UserVO> users) {
        long start = System.currentTimeMillis();

        users.forEach(user -> userDAO.insertUser(user));

        return System.currentTimeMillis() - start;
    }

    public long bulkInsert(List<UserVO> users) {
        long start = System.currentTimeMillis();

        userDAO.bulkInsertUserList(users);

        return System.currentTimeMillis() - start;
    }
}
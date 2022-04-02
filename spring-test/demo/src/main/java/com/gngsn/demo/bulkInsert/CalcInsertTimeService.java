package com.gngsn.demo.bulkInsert;

import com.gngsn.demo.common.user.UserDAO;
import com.gngsn.demo.common.user.UserVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CalcInsertTimeService {

    @Autowired
    private UserDAO userDAO;

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
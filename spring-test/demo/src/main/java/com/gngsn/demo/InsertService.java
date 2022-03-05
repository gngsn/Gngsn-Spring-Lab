package com.gngsn.demo;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class InsertService {

    @Autowired
    private UserMapper userMapper;

    public long basicInsert(List<User> users) {
        long start = System.currentTimeMillis();

        users.forEach(user -> userMapper.insertUser(user));

        return System.currentTimeMillis() - start;
    }

    public long bulkInsert(List<User> users) {
        long start = System.currentTimeMillis();

        userMapper.bulkInsertUserList(users);

        return System.currentTimeMillis() - start;
    }
}
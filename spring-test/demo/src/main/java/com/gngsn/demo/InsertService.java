package com.gngsn.demo;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class InsertService {
    private UserMapper userMapper;

    public long basicInsert(List<User> users) {
        long start = System.currentTimeMillis();
        userMapper.insertUserList(users);
        return System.currentTimeMillis() - start;
    }

    public long bulkInsert(List<User> users) {
        long start = System.currentTimeMillis();
        userMapper.bulkInsertUserList(users);
        return System.currentTimeMillis() - start;
    }


}
package com.gngsn.demo;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
interface UserMapper {
    int insertUserList(List<User> users);

    int bulkInsertUserList(List<User> users);
}
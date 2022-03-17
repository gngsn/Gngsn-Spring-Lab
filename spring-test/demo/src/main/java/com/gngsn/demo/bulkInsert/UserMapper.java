package com.gngsn.demo.bulkInsert;

import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface UserMapper {
    int insertUser(User users);

    int bulkInsertUserList(List<User> users);
}

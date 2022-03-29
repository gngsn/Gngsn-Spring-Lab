package com.gngsn.demo.bulkInsert;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserDAO {
    private static final String NAMESPACE = "com.gngsn.demo.bulkInsert.UserDAO.";

    @Autowired
    private SqlSession sqlSession;

    public int insertUser(User users) {
        return sqlSession.insert(NAMESPACE + "insertUser", users);
    }

    public int bulkInsertUserList(List<User> users) {
        return sqlSession.insert(NAMESPACE + "bulkInsertUserList", users);
    }
}
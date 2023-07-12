package com.gngsn.demo.bulkInsert;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserDAO {
    private static final String NAMESPACE = "demo.UserDAO.";

    private final SqlSession sqlSession;

    public UserDAO(final SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }

    public int insertUser(UserVO users) {
        return sqlSession.insert(NAMESPACE + "insertUser", users);
    }

    public int bulkInsertUserList(List<UserVO> users) {
        return sqlSession.insert(NAMESPACE + "bulkInsertUserList", users);
    }
}
package com.gngsn.demo.common.user;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserDAO {
    private static final String NAMESPACE = "demo.UserDAO.";

    @Autowired
    private SqlSession sqlSession;

    public int insertUser(UserVO users) {
        return sqlSession.insert(NAMESPACE + "insertUser", users);
    }

    public int bulkInsertUserList(List<UserVO> users) {
        return sqlSession.insert(NAMESPACE + "bulkInsertUserList", users);
    }

    public List<UserVO> selectUserList() {
        return sqlSession.selectList(NAMESPACE + "selectUserList");
    }

    public UserVO selectUserByName(String userName) {
        return sqlSession.selectOne(NAMESPACE + "selectUser", userName);
    }
}
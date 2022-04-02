package com.gngsn.demo.cache;

import com.gngsn.demo.common.user.UserVO;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CachedUserDAO {
    private static final String NAMESPACE = "demo.UserDAO.";

    @Autowired
    private SqlSession sqlSession;

    @Cacheable(cacheNames = "users", key = "#root.method")
    public List<UserVO> selectUserList() {
        return sqlSession.selectList(NAMESPACE + "selectUserList");
    }

    @Cacheable(cacheNames = "users", key = "#root.method + #p0")
    public UserVO selectUserByName(String userName) {
        return sqlSession.selectOne(NAMESPACE + "selectUser", userName);
    }
}
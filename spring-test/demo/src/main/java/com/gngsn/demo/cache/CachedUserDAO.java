package com.gngsn.demo.cache;

import com.gngsn.demo.common.user.UserVO;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CachedUserDAO {
    private static final String NAMESPACE = "demo.UserDAO.";

    @Autowired
    private SqlSession sqlSession;

    @Cacheable(cacheNames = "users", key = "#root.methodName")
    public List<UserVO> selectUserList() {
        return sqlSession.selectList(NAMESPACE + "selectUserList");
    }

    @Cacheable(cacheNames = "users", key = "#root.methodName + '_' + #a0")
    public UserVO selectUserByName(String userName) {
        return sqlSession.selectOne(NAMESPACE + "selectUser", userName);
    }

    @CacheEvict(cacheNames = "users", allEntries = true)
    public int insertUser(UserVO user) {
        return sqlSession.insert(NAMESPACE + "insertUser", user);
    }
}
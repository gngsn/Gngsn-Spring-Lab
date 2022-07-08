package com.gngsn.demo.bulkUpsert;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BulkUpsertUserDAO {

    private static final String NAMESPACE = "demo.bulkUpsert.";

    @Autowired
    private SqlSession sqlSession;

    public int insertInit(List<UserVO> users) {
        return sqlSession.insert(NAMESPACE + "insertInit", users);
    }

    public int bulkUpsertUserList(List<UserVO> users) {
        return sqlSession.update(NAMESPACE + "bulkUpsertUserList", users);
    }

    public int bulkUpsertUsingTempTable(List<UserVO> users) {
        return sqlSession.update(NAMESPACE + "bulkUpsertUsingTempTable", users);
    }

    public int bulkUpsertUsingTempTable2(List<UserVO> users) {
        return sqlSession.update(NAMESPACE + "bulkUpsertUsingTempTable2", users);
    }

    public int truncate() {
        return sqlSession.delete(NAMESPACE + "truncate");
    }
}
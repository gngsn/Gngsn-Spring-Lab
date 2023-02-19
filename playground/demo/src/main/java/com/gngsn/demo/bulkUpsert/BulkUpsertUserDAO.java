package com.gngsn.demo.bulkUpsert;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
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


    public int bulkUpdateFromTempTable(List<UserVO> users) {
        return sqlSession.update(NAMESPACE + "bulkUpdateFromTempTable", users);
    }


    public int bulkUpdateMultiLine(List<UserVO> users) {
        return sqlSession.update(NAMESPACE + "bulkUpdateMultiLine", users);
    }

    public int createTempTable() {
        return sqlSession.update(NAMESPACE + "createTempTable");
    }

    public int insertTempTable(List<UserVO> users) {
        return sqlSession.update(NAMESPACE + "insertTempTable", users);
    }

    public int updateMainTable() {
        return sqlSession.update(NAMESPACE + "updateMainTable");
    }

    public int dropTempTable() {
        return sqlSession.update(NAMESPACE + "dropTempTable");
    }

    public int selectUpdateUserCnt() {
        return sqlSession.selectOne(NAMESPACE + "selectUpdateUserCnt");
    }

    public int truncate() {
        return sqlSession.delete(NAMESPACE + "truncate");
    }
}
package com.gngsn.accesswhitecidr.service.dao;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class WhiteCidrDAO {

    private static final String NAMESPACE = "test.whiteCidrDAO.";

    @Autowired
    private SqlSession sqlSession;

    public List<String> selectWhiteCidrList() {
        return sqlSession.selectList(NAMESPACE + "selectWhiteCidrList");
    }
}

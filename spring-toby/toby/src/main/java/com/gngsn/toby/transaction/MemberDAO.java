package com.gngsn.toby.transaction;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository
public class MemberDAO {

    @Autowired
    @Qualifier("masterSqlSessionTemplate")
    private SqlSession sqlSession;

    private static final String NAMESPACE = "com.gngsn.toby.transaction.MemberDAO.";

    public int insertMember(Member member) {
        return sqlSession.insert(NAMESPACE + "insertMember", member);
    }

    public int selectMemberCnt() {
        return sqlSession.selectOne(NAMESPACE + "selectMemberCnt");
    }
}
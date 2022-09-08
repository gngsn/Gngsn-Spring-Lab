package com.gngsn.apressbatch.dao;


import com.gngsn.apressbatch.domain.CustomerUpdate;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CustomerUpdateDAO {

    private String NAMESPACE = "batch.customer.";

    private final SqlSession sqlSession;

    public int selectCnt(Long id) {
        return sqlSession.selectOne(NAMESPACE + "selectCnt", id);
    }

}

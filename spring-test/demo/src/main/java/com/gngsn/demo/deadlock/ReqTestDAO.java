package com.gngsn.demo.deadlock;

import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class ReqTestDAO {

	private static final String NAMESPACE = "demo.deadlock.";
	private final SqlSession sqlSession;

	/**
	 * DB에 저장: 리스트 N건의 데이터를 저장함
	 */
	public int insertTestList(ReqDTO param) {
		return sqlSession.insert(NAMESPACE + "insertTestList", param);
	}

	/**
	 * 이미 등록된 데이터를 삭제
	 */
	public int deleteAlreadyExistTestList(ReqDTO param) {
		return sqlSession.delete(NAMESPACE + "deleteAlreadyExistTestList", param);
	}
}

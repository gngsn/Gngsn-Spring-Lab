package com.gngsn.ch10.criteria;

import com.gngsn.ch10.jpql.Member;
import com.gngsn.secondaryTable.CustomEntityManager;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.util.List;

/**
 * criteria
 * - JPQL 빌더 클래스
 * - 문자가 아닌 query.select(m).where(...) 처럼 프로그래밍 코드로 JPQL을 작성할 수 있음
 *
 * ### 장점
 * - 컴파일 시점에 오류를 발견할 수 있음
 * - IDE를 사용하면 코드 자동완성을 지원
 * - 동적 쿼리를 작성하기 편함
 *
 * ### 단점
 * - 복잡하고 장황 → 사용하기 불편하고 코드가 한눈에 들어오지 않음
 */
public class Client {
    public static void main(String[] args) {
        CustomEntityManager.init();
        EntityManager em = CustomEntityManager.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            // Criteria 사용 준비
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Member> query = cb.createQuery(Member.class);

            // 루트 클래스 (조회를 시작할 클래스)
            Root<Member> m = query.from(Member.class);

            // 쿼리
            CriteriaQuery<Member> cq = query.select(m).where(cb.equal(m.get("username"), "kim"));
            List<Member> resultList = em.createQuery(cq).getResultList();

            System.out.println(resultList);

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }
}

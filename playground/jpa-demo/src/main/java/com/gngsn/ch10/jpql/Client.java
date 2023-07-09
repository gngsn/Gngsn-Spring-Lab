package com.gngsn.ch10.jpql;

import com.gngsn.secondaryTable.CustomEntityManager;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.List;

/**
 * jpql
 * - 엔티티 객체를 조회하는 객체지향 쿼리
 * - SQL을 추상화해서 특정 데이터베이스에 의존하지 않음
 * - SQL 보다 간결
 */
public class Client {
    public static void main(String[] args) {
        CustomEntityManager.init();
        EntityManager em = CustomEntityManager.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            /* 테이블 컬럼명이 아니라 엔티티 객체의 필드명 */
            String jpql = "select m from Member as m where m.username 'kim'";
            List<Member> resultList = em.createQuery(jpql, Member.class).getResultList();

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

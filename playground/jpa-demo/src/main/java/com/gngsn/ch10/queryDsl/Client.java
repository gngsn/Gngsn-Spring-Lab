package com.gngsn.ch10.queryDsl;


import com.gngsn.ch10.jpql.Member;
import com.gngsn.ch10.jpql.QMember;
import com.gngsn.secondaryTable.CustomEntityManager;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.List;


/**
 * jpql
 * - 엔티티 객체를 조회하는 객체지향 쿼리
 * - SQL을 추상화해서 특정 데이터베이스에 의존하지 않음
 * - SQL 보다 간결
 *
 * ```
 *   QueryDSL은 JPA 표준이 아니라 오픈소스 프로젝트임. JPA 뿐만 아니라,
 *   JDO, 몽고 DB, Java Collection, Lucene, Hibernate Search 도 거의 같은 문법으로 지원.
 *   스프링 데이터 프로젝트가 지원할 정도로 많이 기대 중인 프로젝트.
 * ```
 */
public class Client {

    public static void main(String[] args) {
        CustomEntityManager.init();
        EntityManager em = CustomEntityManager.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            JPAQuery query = new JPAQuery(em);
            QMember member = QMember.members;

            List<Member> members = query.from(member)
                    .where(member.username.eq("kim"))
                    .list(member);

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

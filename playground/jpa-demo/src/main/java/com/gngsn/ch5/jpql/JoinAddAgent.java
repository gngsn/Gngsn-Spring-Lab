package com.gngsn.ch5.jpql;

import com.gngsn.ch5.entity.Member;
import com.gngsn.secondaryTable.CustomEntityManager;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.List;



public class JoinAddAgent {

    public static void main(String[] args) {
        CustomEntityManager.init();
        EntityManager em = CustomEntityManager.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            queryLogicJoin(em);

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    private static void queryLogicJoin(EntityManager em) {
        String jpql = "select m from Member m join m.team t where " + "t.name=:teamName";

        List<Member> resultList = em.createQuery(jpql, Member.class)
                .setParameter("teamName", "팀1")
                .getResultList();

        for (Member member : resultList) {
            System.out.println("[query] member.username=" +
                    member.getUsername());
        }
    }
    // [query] member.username=5]-^l 1 //결과 : [query] member.username=5]-^2
}


package com.gngsn.ch5.jpql;

import com.gngsn.ch5.entity.Member;
import com.gngsn.ch5.entity.Team;
import com.gngsn.secondaryTable.CustomEntityManager;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;


public class JoinUpdateAgent {

    public static void main(String[] args) {
        CustomEntityManager.init();
        EntityManager em = CustomEntityManager.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            updateRelation(em);

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }
    private static void updateRelation(EntityManager em) {
        // 새로운 팀2
        Team team2 = new Team("team2", "팀2");
        em.persist(team2);
        // 회원1 에 새로운 팀2 설정
        Member member = em.find(Member.class, "member1");
        member.setTeam(team2);
    }
}


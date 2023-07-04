package com.gngsn.ch5.entity.twoway;

import com.gngsn.secondaryTable.CustomEntityManager;
import jakarta.persistence.EntityManager;

import java.util.List;

public class MappingAgent {
    public static void main(String[] args) {
        CustomEntityManager.init();
        EntityManager em = CustomEntityManager.createEntityManager();

        Team team = em.find(Team.class, "team1");
        List<Member> members = team.getMembers(); // (팀 → 회원), 객체 그래프 탐색

        for (Member member : members) {
            System.out.println("member.username = " + member.getUsername());
        }
    }
}


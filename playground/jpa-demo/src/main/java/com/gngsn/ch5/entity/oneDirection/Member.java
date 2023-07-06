package com.gngsn.ch5.entity.oneDirection;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;


@Entity
public class Member {

    @Id
    @Column(name = "MEMBER_ID")
    private String id;
    private String username;

    // 연관관계 매핑
    @ManyToOne  // 다대일(N:1) 관계를 나타내는 매핑 정보
    @JoinColumn(name = "TEAM_ID")   // 외래 키를 매핑할 때 사용
    private Team team;

    // 연관 관계 설정
    public void setTeam(Team team) {
        this.team = team;
    }

    public String getUsername() {
        return username;
    }

    public Team getTeam() {
        return team;
    }
}
package com.gngsn.ch5.entity.twoway;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.util.ArrayList;
import java.util.List;

public class Team {

    @Id
    @Column(name = "TEAM_ID")
    private String id;

    private String name;

    @OneToMany(mappedBy = "team")
    private List<Member> members = new ArrayList<Member>(); // 추가

    public Team() {
    }

    public Team(final String id, final String name) {
        this.id = id;
        this.name = name;
    }

    public List<Member> getMembers() {
        return members;
    }

    // Getter, Setter ...
}
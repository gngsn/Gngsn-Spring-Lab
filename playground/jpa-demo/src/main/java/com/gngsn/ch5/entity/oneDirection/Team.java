package com.gngsn.ch5.entity.oneDirection;

import jakarta.persistence.Column;
import jakarta.persistence.Id;public class Team {

    @Id
    @Column(name = "TEAM_ID")
    private String id;

    private String name;

    public Team() {
    }

    public Team(final String id, final String name) {
        this.id = id;
        this.name = name;
    }

    // Getter, Setter ...
}
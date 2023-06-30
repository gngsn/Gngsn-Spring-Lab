package com.gngsn.ch5.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Id;public class Team {

    @Id
    @Column(name = "TEAM_ID")
    private String id;

    private String name;

    // Getter, Setter ...
}
package com.gngsn.ch10.jpql;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity(name = "Member")
public class Member {

    @Column
    private String username;

    // ...
}

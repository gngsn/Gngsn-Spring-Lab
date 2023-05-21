package com.gngsn.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Table;
import jakarta.persistence.Id;

import java.time.LocalDate;

@Table
@Entity
public class User {

    @Id
    @GeneratedValue
    private Long id;
    private String username;
    private LocalDate registrationDate;

    public User() {
    }

    public User(String username, LocalDate registrationDate) {
        this.username = username;
        this.registrationDate = registrationDate;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", registrationDate=" + registrationDate +
                '}';
    }
}

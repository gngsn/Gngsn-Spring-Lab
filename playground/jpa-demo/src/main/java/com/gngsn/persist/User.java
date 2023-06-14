package com.gngsn.persist;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity // DB 테이블과 매핑 대상
@Table(name = "user") // user 테이블과 매핑
class User {
    @Id
    private String email;
    private String name;
    @Column(name = "create_date")
    private LocalDateTime createDate;

    public User() {
    }

    public User(final String email, final String name, final LocalDateTime createDate) {
        this.email = email;
        this.name = name;
        this.createDate = createDate;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setName(final String name) {
        this.name = name;
    }
}
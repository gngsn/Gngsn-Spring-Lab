package com.gngsn.demo.bulkInsert;

public class UserVO {
    private String name;
    private String email;
    private String password;

    public UserVO() {
    }

    public UserVO(final String name, final String email, final String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }
}
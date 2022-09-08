package com.gngsn.demo.common.user;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserVO {
    private String name;
    private String email;
    private String password;
}
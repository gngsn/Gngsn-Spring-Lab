package com.gngsn.demo.cache;

import com.gngsn.demo.common.ResJson;

public interface UserService {

    ResJson selectUserList();

    ResJson selectUserByName(String userName);
}
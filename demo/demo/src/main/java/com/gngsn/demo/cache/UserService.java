package com.gngsn.demo.cache;

import com.gngsn.demo.common.ResJson;
import com.gngsn.demo.common.user.UserVO;

public interface UserService {

    ResJson selectUserList();

    ResJson selectUserByName(String userName);

    ResJson insertUser(UserVO user);
}
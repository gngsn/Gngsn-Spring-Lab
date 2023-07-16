package com.gngsn.demo.cache;

import com.gngsn.demo.common.ResJson;
import com.gngsn.demo.common.user.UserVO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final CachedUserDAO userDAO;

    public UserServiceImpl(final CachedUserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public ResJson selectUserList() {
        List<UserVO> userVOList = userDAO.selectUserList();
        return new ResJson(200,
            "Successfully get users",
            userVOList);
    }

    public ResJson selectUserByName(String userName) {
        return new ResJson(200,
            "Successfully get user",
            userDAO.selectUserByName(userName));
    }

    public ResJson insertUser(UserVO user) {
        userDAO.insertUser(user);
        return new ResJson(201, "User added successfully");
    }
}
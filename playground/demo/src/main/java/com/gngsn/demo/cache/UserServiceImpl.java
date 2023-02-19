package com.gngsn.demo.cache;

import com.gngsn.demo.common.ResJson;
import com.gngsn.demo.common.user.UserDAO;
import com.gngsn.demo.common.user.UserVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final CachedUserDAO userDAO;

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
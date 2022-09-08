package com.gngsn.demo.cache;

import com.gngsn.demo.common.ResJson;
import com.gngsn.demo.common.user.UserVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/")
    public ResJson getList() {
        return userService.selectUserList();
    }

    @GetMapping("/{name}")
    public ResJson getUser(@PathVariable("name") String userName) {
        return userService.selectUserByName(userName);
    }

    @PostMapping("/")
    public ResJson addUser(@RequestBody UserVO user) {
        return userService.insertUser(user);
    }
}

package com.gngsn.demo.cache;

import com.gngsn.demo.common.ResJson;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResJson getList(@PathVariable("name") String userName) {
        return userService.selectUserByName(userName);
    }
}

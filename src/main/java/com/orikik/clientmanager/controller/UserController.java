package com.orikik.clientmanager.controller;

import com.orikik.clientmanager.dto.UserDto;
import com.orikik.clientmanager.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/user")
public class UserController {
    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);
    @Autowired
    UserService userService;

    @PostMapping("/create")
    public UserDto createUser(@RequestBody UserDto userDto) {
        return userService.createUser(userDto);
    }

    @PutMapping("/update")
    public UserDto updateUser(Principal principal, @RequestBody UserDto userDto) {
        return userService.updateUser(userDto);
    }

    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    @DeleteMapping("/delete")
    public void deleteUser(Principal principal) {
        userService.deleteUser(principal.getName());
    }
}

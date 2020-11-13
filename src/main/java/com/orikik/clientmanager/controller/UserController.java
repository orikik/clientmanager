package com.orikik.clientmanager.controller;

import com.orikik.clientmanager.dto.ClientDto;
import com.orikik.clientmanager.dto.ContractDto;
import com.orikik.clientmanager.dto.UserDto;
import com.orikik.clientmanager.exception.ClientManagerException;
import com.orikik.clientmanager.exception.ErrorCodeEnum;
import com.orikik.clientmanager.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

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
        if (!principal.getName().equals(userDto.getUsername())) {
            LOG.error("user={} try to change {}", principal.getName(), userDto.getUsername());
            throw new ClientManagerException(ErrorCodeEnum.OPERATION_IS_PROHIBITED);
        }
        return userService.updateUser(userDto);
    }

    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    @DeleteMapping("/delete")
    public void deleteUser(Principal principal) {
        userService.deleteUser(principal.getName());
    }

    @GetMapping("/clients")
    public List<ClientDto> findAllClientsOfUser(Principal principal) {
        return userService.findAllClientsOfUser(principal.getName());
    }

    @GetMapping("/contracts")
    public List<ContractDto> findAllContractsOfUser(Principal principal) {
        return userService.findAllContractsOfUser(principal.getName());
    }

    @GetMapping("/client/contracts")
    public List<ContractDto> findAllClientContractsOfUser(Principal principal,
                                                          @RequestParam(required = true) Long partnerCode) {
        return userService.findAllClientContractsOfUser(principal.getName(), partnerCode);
    }

    @PutMapping("/notify")
    public void updateUser(Principal principal, @RequestParam(required = false) String notifyType) {
        userService.setNotifierService(principal.getName(), notifyType);
    }
}

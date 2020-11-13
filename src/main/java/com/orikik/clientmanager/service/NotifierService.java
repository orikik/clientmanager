package com.orikik.clientmanager.service;

import com.orikik.clientmanager.dto.UserDto;
import org.springframework.lang.Nullable;

public interface NotifierService {
    void notifyUser(UserDto user, @Nullable String header, String message);
}

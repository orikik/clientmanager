package com.orikik.clientmanager.converter;

import com.orikik.clientmanager.dto.UserDto;
import com.orikik.clientmanager.entity.UserEntity;
import com.orikik.clientmanager.utils.NotifyEnum;

public class UserConverter {
    public static UserDto convert(UserEntity userEntity) {
        if (userEntity == null) {
            return null;
        }
        UserDto userDto = new UserDto();
        userDto.setId(userEntity.getId());
        userDto.setPassword(userEntity.getPassword());
        userDto.setUsername(userEntity.getUsername());
        userDto.setFirstname(userEntity.getFirstname());
        userDto.setLastname(userEntity.getLastname());
        userDto.setMiddlename(userEntity.getMiddlename());
        userDto.setEmail(userEntity.getEmail());
        userDto.setPhoneNumber(userEntity.getPhoneNumber());
        userDto.setTelegramId(userEntity.getTelegramId());
        if (userEntity.getNotifyType() != null) {
            userDto.setNotifyEnum(NotifyEnum.valueOf(userEntity.getNotifyType()));
        }
        return userDto;
    }

    public static UserEntity convert(UserDto userDto) {
        if (userDto == null) {
            return null;
        }
        UserEntity userEntity = new UserEntity();
        userEntity.setId(userDto.getId());
        userEntity.setPassword(userDto.getPassword());
        userEntity.setUsername(userDto.getUsername());
        userEntity.setFirstname(userDto.getFirstname());
        userEntity.setLastname(userDto.getLastname());
        userEntity.setMiddlename(userDto.getMiddlename());
        userEntity.setEmail(userDto.getEmail());
        userEntity.setPhoneNumber(userDto.getPhoneNumber());
        userEntity.setTelegramId(userDto.getTelegramId());
        if (userDto.getNotifyEnum() != null) {
            userEntity.setNotifyType(String.valueOf(userDto.getNotifyEnum()));
        }
        return userEntity;
    }
}

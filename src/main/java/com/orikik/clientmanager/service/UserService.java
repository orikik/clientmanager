package com.orikik.clientmanager.service;

import com.orikik.clientmanager.converter.UserConverter;
import com.orikik.clientmanager.dto.UserDto;
import com.orikik.clientmanager.entity.UserEntity;
import com.orikik.clientmanager.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class UserService {
    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;


    public UserDto createUser(UserDto userDto) {
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        UserEntity userEntity = userRepository.save(UserConverter.convert(userDto));
        return UserConverter.convert(userEntity);
    }

    public UserDto updateUser(UserDto userDto) {
        UserEntity userEntity = getUserEntity(userDto.getUsername());
        userDto.setId(userEntity.getId());
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        userEntity = userRepository.save(UserConverter.convert(userDto));
        return UserConverter.convert(userEntity);
    }

    public void deleteUser(String username) {
        UserEntity userEntity = getUserEntity(username);
        userRepository.deleteById(userEntity.getId());
    }

    public UserEntity getUserEntity(String username) {
        Optional<UserEntity> userEntityOptional = userRepository.findByUsername(username);
        if (!userEntityOptional.isPresent()) {
            LOG.error("user={} not found", username);
            throw new UsernameNotFoundException("User not found");
        }
        return userEntityOptional.get();
    }
}

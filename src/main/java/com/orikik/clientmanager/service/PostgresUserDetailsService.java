package com.orikik.clientmanager.service;

import com.orikik.clientmanager.entity.UserEntity;
import com.orikik.clientmanager.repository.UserRepository;
import com.orikik.clientmanager.utils.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Optional;

@Component
public class PostgresUserDetailsService implements UserDetailsService {
    private static final Logger LOG = LoggerFactory.getLogger(PostgresUserDetailsService.class);
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserEntity> userEntityOptional = userRepository.findByUsername(username);

        if (!userEntityOptional.isPresent()) {
            LOG.error("user={} not found", username);
            throw new UsernameNotFoundException("User not found");
        }
        UserEntity userEntity = userEntityOptional.get();

        SimpleGrantedAuthority userAuthority = new SimpleGrantedAuthority(Role.ROLE_USER.name());

        return new User(userEntity.getUsername(), userEntity.getPassword(), Collections.singleton(userAuthority));
    }
}

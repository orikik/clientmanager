package com.orikik.clientmanager.service;

import com.orikik.clientmanager.RepositoryTestBase;
import com.orikik.clientmanager.dto.UserDto;
import com.orikik.clientmanager.entity.UserEntity;
import com.orikik.clientmanager.repository.ClientRepository;
import com.orikik.clientmanager.repository.ContractRepository;
import com.orikik.clientmanager.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@Transactional
public class UserServiceTest extends RepositoryTestBase {
    @MockBean
    private ClientRepository clientRepository;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private ContractRepository contractRepository;
    @MockBean
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserService userService;

    @Test
    public void createUserTest() {
        UserDto userDto = generateUserDto();
        when(passwordEncoder.encode(any())).thenReturn(userDto.getPassword());
        when(userRepository.save(any())).thenReturn(generateUserEntity());
        UserDto res = userService.createUser(userDto);
        assertUser(res, userDto);
    }

    @Test
    public void updateUserTest() {
        Optional<UserEntity> userEntityOptional = Optional.of(generateUserEntity());
        UserDto userDto = generateUserDto();
        when(userRepository.findByUsername(any())).thenReturn(userEntityOptional);
        when(passwordEncoder.encode(any())).thenReturn(userDto.getPassword());
        when(userRepository.save(any())).thenReturn(generateUserEntity());
        UserDto res = userService.updateUser(userDto);
        assertUser(res, userDto);
    }

    @Test(expected = UsernameNotFoundException.class)
    public void updateUserTest_withoutUsername() {
        Optional<UserEntity> userEntityOptionalEmpty = Optional.empty();
        when(userRepository.findByUsername(any())).thenReturn(userEntityOptionalEmpty);
        userService.updateUser(generateUserDto());
    }

    @Test
    public void deleteUserTest() {
        Optional<UserEntity> userEntityOptional = Optional.of(generateUserEntity());
        UserDto userDto = generateUserDto();
        when(userRepository.findByUsername(any())).thenReturn(userEntityOptional);
        userService.deleteUser(userDto.getUsername());
        verify(userRepository).deleteById(userDto.getId());
    }

    @Test(expected = UsernameNotFoundException.class)
    public void deleteUserTest_withoutUsername() {
        Optional<UserEntity> userEntityOptionalEmpty = Optional.empty();
        when(userRepository.findByUsername(any())).thenReturn(userEntityOptionalEmpty);
        userService.deleteUser(generateUserDto().getUsername());
    }

    private UserEntity generateUserEntity() {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setUsername("testUsername");
        userEntity.setPassword("testPassword");
        userEntity.setFirstname("testFirstname");
        userEntity.setLastname("testLastname");
        userEntity.setMiddlename("testMiddlename");
        userEntity.setPhoneNumber("123456789");
        userEntity.setEmail("testEmail@test.test");
        return userEntity;
    }

    private UserDto generateUserDto() {
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setUsername("testUsername");
        userDto.setPassword("testPassword");
        userDto.setFirstname("testFirstname");
        userDto.setLastname("testLastname");
        userDto.setMiddlename("testMiddlename");
        userDto.setPhoneNumber("123456789");
        userDto.setEmail("testEmail@test.test");
        return userDto;
    }

    private void assertUser(UserDto actually, UserDto expected) {
        assertNotNull(actually);
        assertNotNull(expected);
        assertEquals(1, actually.getId().longValue());
        assertEquals("testUsername", actually.getUsername());
        assertEquals("testPassword", actually.getPassword());
        assertEquals("testFirstname", actually.getFirstname());
        assertEquals("testLastname", actually.getLastname());
        assertEquals("testMiddlename", actually.getMiddlename());
        assertEquals("123456789", actually.getPhoneNumber());
        assertEquals("testEmail@test.test", actually.getEmail());

        assertEquals(expected.getId(), actually.getId());
        assertEquals(expected.getUsername(), actually.getUsername());
        assertEquals(expected.getPassword(), actually.getPassword());
        assertEquals(expected.getFirstname(), actually.getFirstname());
        assertEquals(expected.getLastname(), actually.getLastname());
        assertEquals(expected.getMiddlename(), actually.getMiddlename());
        assertEquals(expected.getPhoneNumber(), actually.getPhoneNumber());
        assertEquals(expected.getEmail(), actually.getEmail());
    }
}

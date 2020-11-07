package com.orikik.clientmanager.repository;

import com.orikik.clientmanager.RepositoryTestBase;
import com.orikik.clientmanager.entity.UserEntity;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@Transactional
public class UserRepositoryTest extends RepositoryTestBase {
    private final UserEntity userEntity = new UserEntity();
    @Autowired
    private UserRepository userRepository;

    @Before
    public void init() {
        userEntity.setUsername("testUsername");
        userEntity.setPassword("testPassword");
        userEntity.setFirstname("testFirstname");
        userEntity.setLastname("testLastname");
        userEntity.setMiddlename("testMiddlename");
        userEntity.setPhoneNumber("123456789");
        userEntity.setEmail("testEmail@test.test");
        userRepository.save(userEntity);


    }

    @Test
    public void getUserByIdTest() {
        UserEntity res = userRepository.findById(userEntity.getId()).get();
        assertUser(res, userEntity);
    }

    @Test(expected = NoSuchElementException.class)
    public void deleteUserByIdTest() {
        userRepository.deleteById(userEntity.getId());
        userRepository.findById(userEntity.getId()).get();
    }

    private void assertUser(UserEntity expected, UserEntity actually) {
        assertNotNull(expected);
        assertNotNull(actually);
        assertEquals("testUsername", expected.getUsername());
        assertEquals("testPassword", expected.getPassword());
        assertEquals("testFirstname", expected.getFirstname());
        assertEquals("testLastname", expected.getLastname());
        assertEquals("testMiddlename", expected.getMiddlename());
        assertEquals("123456789", expected.getPhoneNumber());
        assertEquals("testEmail@test.test", expected.getEmail());
        assertEquals(actually.getUsername(), expected.getUsername());
        assertEquals(actually.getPassword(), expected.getPassword());
        assertEquals(actually.getFirstname(), expected.getFirstname());
        assertEquals(actually.getLastname(), expected.getLastname());
        assertEquals(actually.getMiddlename(), expected.getMiddlename());
        assertEquals(actually.getPhoneNumber(), expected.getPhoneNumber());
        assertEquals(actually.getEmail(), expected.getEmail());
        assertEquals(actually.getId(), expected.getId());
    }
}

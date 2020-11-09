package com.orikik.clientmanager.converter;

import com.orikik.clientmanager.dto.UserDto;
import com.orikik.clientmanager.entity.UserEntity;
import org.junit.Test;

import static org.junit.Assert.*;

public class UserConverterTest {
    @Test
    public void convertTest_entityToDto() {
        UserEntity userEntity = generateUserEntity();
        UserDto actually = UserConverter.convert(userEntity);
        assertUser(actually, userEntity);
    }

    @Test
    public void convertTest_entityNullToDto() {
        assertNull(UserConverter.convert((UserEntity) null));
    }

    @Test
    public void convertTest_dtoToEntity() {
        UserDto expected = generateUserDto();
        UserEntity actually = UserConverter.convert(expected);
        assertUser(expected, actually);
    }

    @Test
    public void convertTest_nullDtoToEntity() {
        assertNull(UserConverter.convert((UserDto) null));
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

    private void assertUser(UserDto dto, UserEntity entity) {
        assertNotNull(dto);
        assertNotNull(entity);
        assertEquals(1, dto.getId().longValue());
        assertEquals("testUsername", dto.getUsername());
        assertEquals("testPassword", dto.getPassword());
        assertEquals("testFirstname", dto.getFirstname());
        assertEquals("testLastname", dto.getLastname());
        assertEquals("testMiddlename", dto.getMiddlename());
        assertEquals("123456789", dto.getPhoneNumber());
        assertEquals("testEmail@test.test", dto.getEmail());

        assertEquals(entity.getId(), dto.getId());
        assertEquals(entity.getUsername(), dto.getUsername());
        assertEquals(entity.getPassword(), dto.getPassword());
        assertEquals(entity.getFirstname(), dto.getFirstname());
        assertEquals(entity.getLastname(), dto.getLastname());
        assertEquals(entity.getMiddlename(), dto.getMiddlename());
        assertEquals(entity.getPhoneNumber(), dto.getPhoneNumber());
        assertEquals(entity.getEmail(), dto.getEmail());
    }
}

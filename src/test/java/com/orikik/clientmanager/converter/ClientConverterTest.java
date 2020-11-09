package com.orikik.clientmanager.converter;

import com.orikik.clientmanager.dto.ClientDto;
import com.orikik.clientmanager.entity.ClientEntity;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ClientConverterTest {
    @Test
    public void convertTest_entityToDto() {
        ClientEntity expected = generateClientEntity();
        ClientDto actually = ClientConverter.convert(expected);
        assertClient(actually, expected);
    }

    @Test
    public void convertTest_entityNullToDto() {
        assertNull(ClientConverter.convert((ClientEntity) null));
    }

    @Test
    public void convertTest_dtoToEntity() {
        ClientDto expected = generateClientDto();
        ClientEntity actually = ClientConverter.convert(expected);
        assertClient(expected, actually);
    }

    @Test
    public void convertTest_nullDtoToEntity() {
        assertNull(ClientConverter.convert((ClientDto) null));
    }

    @Test
    public void convertListTest_entitiesToDtos() {
        List<ClientEntity> expected = createClientEntityList();
        List<ClientDto> actually = ClientConverter.convert(expected);
        Assert.assertEquals(2, expected.size());
        Assert.assertEquals(2, actually.size());
        assertClient(actually.get(0), expected.get(0));
        assertClient(actually.get(1), expected.get(1));
    }

    @Test
    public void convertListTest_entitiesNullToDtos() {
        List<ClientDto> result = ClientConverter.convert((List<ClientEntity>) null);
        assertNotNull(result);
        Assert.assertEquals(0, result.size());
    }

    private ClientDto generateClientDto() {
        ClientDto clientDto = new ClientDto();
        clientDto.setId(1L);
        clientDto.setFirstname("testFirstname");
        clientDto.setLastname("testLastname");
        clientDto.setMiddlename("testMiddlename");
        clientDto.setPhoneNumberList(generateListPhoneNumbers());
        clientDto.setPartnerCode(123456789L);
        return clientDto;
    }

    private ClientEntity generateClientEntity() {
        ClientEntity clientEntity = new ClientEntity();
        clientEntity.setId(1L);
        clientEntity.setFirstname("testFirstname");
        clientEntity.setLastname("testLastname");
        clientEntity.setMiddlename("testMiddlename");
        clientEntity.setPhones(generateListPhoneNumbers());
        clientEntity.setPartnerCode(123456789L);
        return clientEntity;
    }

    private List<ClientEntity> createClientEntityList() {
        List<ClientEntity> clientEntities = new ArrayList<>();
        clientEntities.add(generateClientEntity());
        clientEntities.add(generateClientEntity());
        return clientEntities;
    }

    private List<String> generateListPhoneNumbers() {
        List<String> phoneNumberList = new ArrayList<>();
        phoneNumberList.add("12");
        phoneNumberList.add("23");
        return phoneNumberList;
    }

    private void assertClient(ClientDto dto, ClientEntity entity) {
        assertNotNull(dto);
        assertNotNull(entity);
        assertEquals(1, dto.getId().longValue());
        assertEquals("testFirstname", dto.getFirstname());
        assertEquals("testLastname", dto.getLastname());
        assertEquals("testMiddlename", dto.getMiddlename());
        assertEquals(generateListPhoneNumbers(), dto.getPhoneNumberList());
        assertEquals(123456789L, dto.getPartnerCode().longValue());

        assertEquals(entity.getId(), dto.getId());
        assertEquals(entity.getFirstname(), dto.getFirstname());
        assertEquals(entity.getLastname(), dto.getLastname());
        assertEquals(entity.getMiddlename(), dto.getMiddlename());
        assertEquals(entity.getPhones(), dto.getPhoneNumberList());
        assertEquals(entity.getPartnerCode(), dto.getPartnerCode());
    }
}

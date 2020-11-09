package com.orikik.clientmanager.repository;

import com.orikik.clientmanager.RepositoryTestBase;
import com.orikik.clientmanager.entity.ClientEntity;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@Transactional
public class ClientRepositoryTest extends RepositoryTestBase {
    private final ClientEntity clientEntity = new ClientEntity();
    @Autowired
    private ClientRepository clientRepository;

    @Before
    public void init() {
        List<String> phones = new ArrayList<>();
        phones.add("123");
        phones.add("345");
        clientEntity.setPartnerCode(12345678L);
        clientEntity.setFirstname("testFirstname");
        clientEntity.setLastname("testLastname");
        clientEntity.setMiddlename("testMiddlename");
        clientEntity.setPhones(phones);
        clientRepository.save(clientEntity);
    }

    @Test
    public void getClientByIdTest() {
        ClientEntity res = clientRepository.findById(clientEntity.getId()).get();
        assertClient(res, clientEntity);
    }

    @Test
    public void getClientByPartnerCodeTest() {
        ClientEntity res = clientRepository.findByPartnerCode(clientEntity.getPartnerCode()).get();
        assertClient(res, clientEntity);
    }

    @Test(expected = NoSuchElementException.class)
    public void deleteClientByIdTest() {
        clientRepository.deleteById(clientEntity.getId());
        clientRepository.findById(clientEntity.getId()).get();
    }

    private void assertClient(ClientEntity expected, ClientEntity actually) {
        assertNotNull(expected);
        assertNotNull(actually);
        assertEquals(12345678L, expected.getPartnerCode().longValue());
        assertEquals("testFirstname", expected.getFirstname());
        assertEquals("testLastname", expected.getLastname());
        assertEquals("testMiddlename", expected.getMiddlename());
        assertEquals(2, expected.getPhones().size());
        assertEquals(actually.getPartnerCode(), expected.getPartnerCode());
        assertEquals(actually.getFirstname(), expected.getFirstname());
        assertEquals(actually.getLastname(), expected.getLastname());
        assertEquals(actually.getMiddlename(), expected.getMiddlename());
        assertEquals(actually.getPhones(), expected.getPhones());
        assertEquals(actually.getId(), expected.getId());
    }
}

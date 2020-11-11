package com.orikik.clientmanager.repository;

import com.orikik.clientmanager.RepositoryTestBase;
import com.orikik.clientmanager.entity.ClientEntity;
import com.orikik.clientmanager.entity.ContractEntity;
import com.orikik.clientmanager.entity.UserEntity;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@Transactional
public class ContractRepositoryTest extends RepositoryTestBase {
    private final ContractEntity contractEntity = new ContractEntity();
    private final UserEntity userEntity = new UserEntity();
    private final ClientEntity clientEntity = new ClientEntity();
    @Autowired
    private ContractRepository contractRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ClientRepository clientRepository;

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

        clientEntity.setPartnerCode(12345678L);
        clientEntity.setFirstname("testFirstname");
        clientEntity.setLastname("testLastname");
        clientEntity.setMiddlename("testMiddlename");
        clientRepository.save(clientEntity);

        contractEntity.setAddendumNumber(123L);
        contractEntity.setCost(12345678L);
        contractEntity.setExpirationDate(LocalDate.now());
        contractEntity.setProduct("osago");
        contractEntity.setClientEntity(clientEntity);
        contractEntity.setUserEntity(userEntity);
        contractRepository.save(contractEntity);
    }

    @Test
    public void getContractByIdTest() {
        ContractEntity res = contractRepository.findById(contractEntity.getId()).get();
        assertContract(res, contractEntity);
    }

    @Test
    public void getContractByAddendumNumberTest() {
        ContractEntity res = contractRepository.findByAddendumNumber(contractEntity.getAddendumNumber()).get();
        assertContract(res, contractEntity);
    }

    @Test
    public void getContractByUserEntityTest() {
        List<ContractEntity> res = contractRepository.findByUserEntity(contractEntity.getUserEntity()).get();
        assertEquals(1, res.size());
        assertContract(res.get(0), contractEntity);
    }

    @Test
    public void getContractByUserAndClientEntityTest() {
        List<ContractEntity> res = contractRepository.findByUserEntityAndClientEntity
                (contractEntity.getUserEntity(), contractEntity.getClientEntity()).get();
        assertEquals(1, res.size());
        assertContract(res.get(0), contractEntity);
    }

    @Test(expected = NoSuchElementException.class)
    public void deleteContractByIdTest() {
        contractRepository.deleteById(contractEntity.getId());
        contractRepository.findById(contractEntity.getId()).get();
    }

    @Test(expected = NoSuchElementException.class)
    public void deleteContractByUserAndClientEntityTest() {
        contractRepository.deleteAllByUserEntityAndClientEntity
                (contractEntity.getUserEntity(), contractEntity.getClientEntity());
        contractRepository.findById(contractEntity.getId()).get();
    }

    private void assertContract(ContractEntity expected, ContractEntity actually) {
        assertNotNull(expected);
        assertNotNull(actually);
        assertEquals(123L, expected.getAddendumNumber().longValue());
        assertEquals("osago", expected.getProduct());
        assertEquals(LocalDate.now(), expected.getExpirationDate());
        assertEquals(12345678, expected.getCost().longValue());
        assertEquals(userEntity, expected.getUserEntity());
        assertEquals(clientEntity, expected.getClientEntity());
        assertEquals(actually.getAddendumNumber(), expected.getAddendumNumber());
        assertEquals(actually.getProduct(), expected.getProduct());
        assertEquals(actually.getExpirationDate(), expected.getExpirationDate());
        assertEquals(actually.getCost(), expected.getCost());
        assertEquals(actually.getUserEntity(), expected.getUserEntity());
        assertEquals(actually.getClientEntity(), expected.getClientEntity());
        assertEquals(actually.getId(), expected.getId());
    }
}

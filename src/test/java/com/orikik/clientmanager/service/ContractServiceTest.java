package com.orikik.clientmanager.service;

import com.orikik.clientmanager.RepositoryTestBase;
import com.orikik.clientmanager.dto.ContractDto;
import com.orikik.clientmanager.entity.ClientEntity;
import com.orikik.clientmanager.entity.ContractEntity;
import com.orikik.clientmanager.entity.UserEntity;
import com.orikik.clientmanager.exception.ClientManagerException;
import com.orikik.clientmanager.repository.ClientRepository;
import com.orikik.clientmanager.repository.ContractRepository;
import com.orikik.clientmanager.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
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
public class ContractServiceTest extends RepositoryTestBase {
    @MockBean
    private ClientRepository clientRepository;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private ContractRepository contractRepository;
    @Autowired
    private ContractService contractService;

    @Test
    public void createContractTest() {
        Optional<UserEntity> userEntityOptional = Optional.of(generateUserEntity());
        ClientEntity clientEntity = generateClientEntity();
        ContractDto contractDto = generateContractDto();
        ContractEntity contractEntity = generateContractEntity();
        when(userRepository.findByUsername(any())).thenReturn(userEntityOptional);
        when(clientRepository.findByPartnerCode(any())).thenReturn(Optional.of(clientEntity));
        when(contractRepository.save(any())).thenReturn(contractEntity);
        long partnerCode = clientEntity.getPartnerCode();
        String username = userEntityOptional.get().getUsername();
        ContractDto res = contractService.createContract(contractDto, username, partnerCode);
        assertContract(res, contractDto);
    }

    @Test(expected = ClientManagerException.class)
    public void createContractTest_withoutClientInBase() {
        Optional<ClientEntity> clientEntityOptionalEmpty = Optional.empty();
        Optional<UserEntity> userEntityOptional = Optional.of(generateUserEntity());
        when(clientRepository.findByPartnerCode(any())).thenReturn(clientEntityOptionalEmpty);
        when(userRepository.findByUsername(any())).thenReturn(userEntityOptional);
        long partnerCode = generateClientEntity().getPartnerCode();
        String username = generateUserEntity().getUsername();
        contractService.createContract(generateContractDto(), username, partnerCode);
    }

    @Test
    public void updateContractTest() {
        Optional<ContractEntity> contractEntityOptional = Optional.of(generateContractEntity());
        Optional<UserEntity> userEntityOptional = Optional.of(generateUserEntity());
        when(contractRepository.findByAddendumNumber(any())).thenReturn(contractEntityOptional);
        when(userRepository.findByUsername(any())).thenReturn(userEntityOptional);
        when(contractRepository.save(any())).thenReturn(contractEntityOptional.get());
        ContractDto contractDto = generateContractDto();
        ContractDto res = contractService.updateContract(contractDto, userEntityOptional.get().getUsername());
        assertContract(res, contractDto);
    }

    @Test(expected = UsernameNotFoundException.class)
    public void updateContractTest_withoutUserInBase() {
        Optional<ContractEntity> contractEntityOptional = Optional.of(generateContractEntity());
        Optional<UserEntity> userEntityOptionalEmpty = Optional.empty();
        when(contractRepository.findByAddendumNumber(any())).thenReturn(contractEntityOptional);
        when(userRepository.findByUsername(any())).thenReturn(userEntityOptionalEmpty);
        contractService.updateContract(generateContractDto(), "testWrong");
    }

    @Test(expected = ClientManagerException.class)
    public void updateContractTest_withoutContractInBase() {
        Optional<ContractEntity> contractEntityOptionalEmpty = Optional.empty();
        Optional<UserEntity> userEntityOptional = Optional.of(generateUserEntity());
        when(contractRepository.findByAddendumNumber(any())).thenReturn(contractEntityOptionalEmpty);
        when(userRepository.findByUsername(any())).thenReturn(userEntityOptional);
        contractService.updateContract(generateContractDto(), generateUserEntity().getUsername());
    }

    @Test
    public void deleteContractTest() {
        Optional<ContractEntity> contractEntityOptional = Optional.of(generateContractEntity());
        Optional<UserEntity> userEntityOptional = Optional.of(generateUserEntity());
        when(contractRepository.findByAddendumNumber(any())).thenReturn(contractEntityOptional);
        when(userRepository.findByUsername(any())).thenReturn(userEntityOptional);
        ContractDto contractDto = generateContractDto();
        contractService.deleteContract(contractDto.getAddendumNumber(), userEntityOptional.get().getUsername());
        verify(contractRepository).deleteById(contractDto.getId());
    }

    @Test(expected = UsernameNotFoundException.class)
    public void deleteContractTest_withoutUserInBase() {
        Optional<ContractEntity> contractEntityOptional = Optional.of(generateContractEntity());
        Optional<UserEntity> userEntityOptionalEmpty = Optional.empty();
        when(contractRepository.findByAddendumNumber(any())).thenReturn(contractEntityOptional);
        when(userRepository.findByUsername(any())).thenReturn(userEntityOptionalEmpty);
        contractService.deleteContract(generateContractDto().getAddendumNumber(), "testWrong");
    }

    @Test(expected = ClientManagerException.class)
    public void deleteContractTest_withoutContractInBase() {
        Optional<ClientEntity> clientEntityOptionalEmpty = Optional.empty();
        Optional<UserEntity> userEntityOptionalEmpty = Optional.of(generateUserEntity());
        when(clientRepository.findByPartnerCode(any())).thenReturn(clientEntityOptionalEmpty);
        when(userRepository.findByUsername(any())).thenReturn(userEntityOptionalEmpty);
        contractService.deleteContract(generateContractDto().getAddendumNumber(), generateUserEntity().getUsername());
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

    private List<String> generateListPhoneNumbers() {
        List<String> phoneNumberList = new ArrayList<>();
        phoneNumberList.add("12");
        phoneNumberList.add("23");
        return phoneNumberList;
    }

    private ContractEntity generateContractEntity() {
        ContractEntity contractEntity = new ContractEntity();
        contractEntity.setId(1L);
        contractEntity.setAddendumNumber(123456789L);
        contractEntity.setCost(123456789L);
        contractEntity.setDateOfNextPayment(LocalDate.now().plusDays(2));
        contractEntity.setExpirationDate(LocalDate.now().minusDays(2));
        contractEntity.setProduct("testProduct");
        contractEntity.setUserEntity(generateUserEntity());
        contractEntity.setClientEntity(generateClientEntity());
        return contractEntity;
    }

    private ContractDto generateContractDto() {
        ContractDto contractDto = new ContractDto();
        contractDto.setId(1L);
        contractDto.setAddendumNumber(123456789L);
        contractDto.setCost(123456789L);
        contractDto.setDateOfNextPayment(LocalDate.now().plusDays(2));
        contractDto.setExpirationDate(LocalDate.now().minusDays(2));
        contractDto.setProduct("testProduct");
        return contractDto;
    }

    private List<ContractEntity> createContractEntityList() {
        List<ContractEntity> contractEntities = new ArrayList<>();
        ContractEntity contractEntity = generateContractEntity();
        contractEntity.setClientEntity(generateClientEntity());
        contractEntities.add(contractEntity);
        return contractEntities;
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

    private void assertContract(ContractDto actually, ContractDto expected) {
        assertNotNull(actually);
        assertNotNull(expected);
        assertEquals(1, actually.getId().longValue());
        assertEquals(123456789L, actually.getAddendumNumber().longValue());
        assertEquals(123456789L, actually.getCost());
        assertEquals(LocalDate.now().plusDays(2), actually.getDateOfNextPayment());
        assertEquals(LocalDate.now().minusDays(2), actually.getExpirationDate());
        assertEquals("testProduct", actually.getProduct());

        assertEquals(expected.getId(), actually.getId());
        assertEquals(expected.getAddendumNumber(), actually.getAddendumNumber());
        assertEquals(expected.getCost(), actually.getCost());
        assertEquals(expected.getDateOfNextPayment(), actually.getDateOfNextPayment());
        assertEquals(expected.getExpirationDate(), actually.getExpirationDate());
        assertEquals(expected.getProduct(), actually.getProduct());
    }
}

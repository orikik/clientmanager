package com.orikik.clientmanager.service;

import com.orikik.clientmanager.RepositoryTestBase;
import com.orikik.clientmanager.dto.ClientDto;
import com.orikik.clientmanager.dto.ContractDto;
import com.orikik.clientmanager.entity.ClientEntity;
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
import java.util.Collections;
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
public class ClientServiceTest extends RepositoryTestBase {
    @MockBean
    private ClientRepository clientRepository;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private ContractRepository contractRepository;
    @MockBean
    private ContractService contractService;
    @Autowired
    private ClientService clientService;

    @Test
    public void createClientAndHisContractTest() {
        Optional<UserEntity> userEntityOptional = Optional.of(generateUserEntity());
        ClientEntity clientEntity = generateClientEntity();
        ContractDto contractDto = generateContractDto();
        when(userRepository.findByUsername(any())).thenReturn(userEntityOptional);
        when(clientRepository.save(any())).thenReturn(clientEntity);
        when(contractService.createContract(any(), any(), any())).thenReturn(contractDto);
        ClientDto clientDto = generateClientDto();
        clientDto.setContractDtoList(Collections.singletonList(contractDto));
        ClientDto res = clientService.createClientAndHisContract(clientDto, userEntityOptional.get().getUsername());
        assertClient(res, clientDto);
        assertEquals(1, res.getContractDtoList().size());
        assertContract(res.getContractDtoList().get(0), contractDto);
    }

    @Test(expected = ClientManagerException.class)
    public void createClientAndHisContractTest_withExistClient() {
        Optional<ClientEntity> clientEntityOptional = Optional.of(generateClientEntity());
        when(clientRepository.findByPartnerCode(any())).thenReturn(clientEntityOptional);
        ClientDto clientDto = generateClientDto();
        clientService.createClientAndHisContract(clientDto, generateUserEntity().getUsername());
    }

    @Test
    public void updateClient() {
        Optional<ClientEntity> clientEntityOptional = Optional.of(generateClientEntity());
        ClientEntity clientEntity = generateClientEntity();
        ClientDto clientDto = generateClientDto();
        when(clientRepository.findByPartnerCode(any())).thenReturn(clientEntityOptional);
        when(clientRepository.save(any())).thenReturn(clientEntity);
        ClientDto res = clientService.updateClient(clientDto);
        assertClient(clientDto, res);
    }

    @Test(expected = ClientManagerException.class)
    public void updateClient_withoutClientInBase() {
        Optional<ClientEntity> clientEntityOptionalEmpty = Optional.empty();
        when(clientRepository.findByPartnerCode(any())).thenReturn(clientEntityOptionalEmpty);
        clientService.updateClient(generateClientDto());
    }

    @Test
    public void deleteClient() {
        Optional<UserEntity> userEntityOptional = Optional.of(generateUserEntity());
        Optional<ClientEntity> clientEntityOptional = Optional.of(generateClientEntity());
        when(userRepository.findByUsername(any())).thenReturn(userEntityOptional);
        when(clientRepository.findByPartnerCode(any())).thenReturn(clientEntityOptional);
        UserEntity userEntity = userEntityOptional.get();
        ClientEntity clientEntity = clientEntityOptional.get();
        clientService.deleteClient(clientEntity.getPartnerCode(), userEntity.getUsername());
        verify(contractRepository).deleteAllByUserEntityAndClientEntity(userEntity, clientEntity);
    }

    @Test(expected = UsernameNotFoundException.class)
    public void deleteClient_withoutUserInBase() {
        Optional<UserEntity> userEntityOptionalEmpty = Optional.empty();
        Optional<ClientEntity> clientEntityOptional = Optional.of(generateClientEntity());
        when(userRepository.findByUsername(any())).thenReturn(userEntityOptionalEmpty);
        when(clientRepository.findByPartnerCode(any())).thenReturn(clientEntityOptional);
        ClientDto clientDto = generateClientDto();
        clientService.deleteClient(clientDto.getPartnerCode(), generateUserEntity().getUsername());
    }

    @Test(expected = ClientManagerException.class)
    public void deleteClient_withoutClientInBase() {
        Optional<ClientEntity> clientEntityOptionalEmpty = Optional.empty();
        when(clientRepository.findByPartnerCode(any())).thenReturn(clientEntityOptionalEmpty);
        ClientDto clientDto = generateClientDto();
        clientService.deleteClient(clientDto.getPartnerCode(), generateUserEntity().getUsername());
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

    private List<String> generateListPhoneNumbers() {
        List<String> phoneNumberList = new ArrayList<>();
        phoneNumberList.add("12");
        phoneNumberList.add("23");
        return phoneNumberList;
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

    private void assertClient(ClientDto actually, ClientDto expected) {
        assertNotNull(actually);
        assertNotNull(expected);
        assertEquals(1, actually.getId().longValue());
        assertEquals("testFirstname", actually.getFirstname());
        assertEquals("testLastname", actually.getLastname());
        assertEquals("testMiddlename", actually.getMiddlename());
        assertEquals(generateListPhoneNumbers(), actually.getPhoneNumberList());
        assertEquals(123456789L, actually.getPartnerCode().longValue());

        assertEquals(expected.getId(), actually.getId());
        assertEquals(expected.getFirstname(), actually.getFirstname());
        assertEquals(expected.getLastname(), actually.getLastname());
        assertEquals(expected.getMiddlename(), actually.getMiddlename());
        assertEquals(expected.getPhoneNumberList(), actually.getPhoneNumberList());
        assertEquals(expected.getPartnerCode(), actually.getPartnerCode());
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

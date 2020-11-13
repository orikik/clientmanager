package com.orikik.clientmanager.service;

import com.orikik.clientmanager.RepositoryTestBase;
import com.orikik.clientmanager.dto.ClientDto;
import com.orikik.clientmanager.dto.ContractDto;
import com.orikik.clientmanager.dto.UserDto;
import com.orikik.clientmanager.entity.ClientEntity;
import com.orikik.clientmanager.entity.ContractEntity;
import com.orikik.clientmanager.entity.UserEntity;
import com.orikik.clientmanager.exception.ClientManagerException;
import com.orikik.clientmanager.repository.ClientRepository;
import com.orikik.clientmanager.repository.ContractRepository;
import com.orikik.clientmanager.repository.UserRepository;
import com.orikik.clientmanager.utils.NotifyEnum;
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

    @Test
    public void findAllClientsOfUserTest() {
        Optional<UserEntity> userEntityOptional = Optional.of(generateUserEntity());
        Optional<List<ContractEntity>> optionalContractEntityList = Optional.of(createContractEntityList());
        when(userRepository.findByUsername(any())).thenReturn(userEntityOptional);
        when(contractRepository.findByUserEntity(any())).thenReturn(optionalContractEntityList);
        List<ClientDto> res = userService.findAllClientsOfUser(generateUserDto().getUsername());
        assertEquals(1, res.size());
        assertClient(res.get(0), generateClientDto());
    }

    @Test(expected = UsernameNotFoundException.class)
    public void findAllClientsOfUserTest_withoutUsername() {
        Optional<UserEntity> userEntityOptionalEmpty = Optional.empty();
        Optional<List<ContractEntity>> optionalContractEntityList = Optional.of(createContractEntityList());
        when(userRepository.findByUsername(any())).thenReturn(userEntityOptionalEmpty);
        when(contractRepository.findByUserEntity(any())).thenReturn(optionalContractEntityList);
        userService.findAllClientsOfUser(generateUserDto().getUsername());
    }

    @Test(expected = ClientManagerException.class)
    public void findAllClientsOfUserTest_withoutContracts() {
        Optional<UserEntity> userEntityOptional = Optional.of(generateUserEntity());
        Optional<List<ContractEntity>> optionalContractEntityEmptyList = Optional.empty();
        when(userRepository.findByUsername(any())).thenReturn(userEntityOptional);
        when(contractRepository.findByUserEntity(any())).thenReturn(optionalContractEntityEmptyList);
        userService.findAllClientsOfUser(generateUserDto().getUsername());
    }

    @Test
    public void findAllContractsOfUserTest() {
        Optional<UserEntity> userEntityOptional = Optional.of(generateUserEntity());
        Optional<List<ContractEntity>> optionalContractEntityList = Optional.of(createContractEntityList());
        when(userRepository.findByUsername(any())).thenReturn(userEntityOptional);
        when(contractRepository.findByUserEntity(any())).thenReturn(optionalContractEntityList);

        List<ContractDto> res = userService.findAllContractsOfUser(generateUserDto().getUsername());
        assertEquals(2, res.size());
        assertContract(res.get(0), generateContractDto());
        assertContract(res.get(1), generateContractDto());

    }

    @Test(expected = UsernameNotFoundException.class)
    public void findAllContractsOfUserTest_withoutUsername() {
        Optional<UserEntity> userEntityOptionalEmpty = Optional.empty();
        Optional<List<ContractEntity>> optionalContractEntityList = Optional.of(createContractEntityList());
        when(userRepository.findByUsername(any())).thenReturn(userEntityOptionalEmpty);
        when(contractRepository.findByUserEntity(any())).thenReturn(optionalContractEntityList);
        userService.findAllClientsOfUser(generateUserDto().getUsername());
    }

    @Test(expected = ClientManagerException.class)
    public void findAllContractsOfUserTest_withoutContracts() {
        Optional<UserEntity> userEntityOptional = Optional.of(generateUserEntity());
        Optional<List<ContractEntity>> optionalContractEntityEmptyList = Optional.empty();
        when(userRepository.findByUsername(any())).thenReturn(userEntityOptional);
        when(contractRepository.findByUserEntity(any())).thenReturn(optionalContractEntityEmptyList);
        userService.findAllClientsOfUser(generateUserDto().getUsername());
    }

    @Test
    public void findAllClientContractsOfUserTest() {
        Optional<UserEntity> userEntityOptional = Optional.of(generateUserEntity());
        Optional<List<ContractEntity>> optionalContractEntityList = Optional.of(createContractEntityList());
        Optional<ClientEntity> clientEntityOptional = Optional.of(generateClientEntity());
        when(userRepository.findByUsername(any())).thenReturn(userEntityOptional);
        when(clientRepository.findByPartnerCode(any())).thenReturn(clientEntityOptional);
        when(contractRepository.findByUserEntityAndClientEntity(any(), any())).thenReturn(optionalContractEntityList);
        List<ContractDto> res = userService
                .findAllClientContractsOfUser(generateUserDto().getUsername(), generateClientDto().getPartnerCode());
        assertEquals(2, res.size());
        assertContract(res.get(0), generateContractDto());
        assertContract(res.get(1), generateContractDto());
    }

    @Test(expected = UsernameNotFoundException.class)
    public void findAllClientContractsOfUserTest_withoutUsername() {
        Optional<UserEntity> userEntityOptionalEmpty = Optional.empty();
        Optional<List<ContractEntity>> optionalContractEntityList = Optional.of(createContractEntityList());
        Optional<ClientEntity> clientEntityOptional = Optional.of(generateClientEntity());

        when(userRepository.findByUsername(any())).thenReturn(userEntityOptionalEmpty);
        when(clientRepository.findByPartnerCode(any())).thenReturn(clientEntityOptional);
        when(contractRepository.findByUserEntityAndClientEntity(any(), any())).thenReturn(optionalContractEntityList);

        userService.findAllClientContractsOfUser(generateUserDto().getUsername(), generateClientDto().getPartnerCode());
    }

    @Test(expected = ClientManagerException.class)
    public void findAllClientContractsOfUserTest_withoutClients() {
        Optional<UserEntity> userEntityOptionalEmpty = Optional.of(generateUserEntity());
        Optional<ClientEntity> clientEntityOptionalEmpty = Optional.empty();
        Optional<List<ContractEntity>> optionalContractEntityList = Optional.of(createContractEntityList());

        when(userRepository.findByUsername(any())).thenReturn(userEntityOptionalEmpty);
        when(clientRepository.findByPartnerCode(any())).thenReturn(clientEntityOptionalEmpty);
        when(contractRepository.findByUserEntityAndClientEntity(any(), any())).thenReturn(optionalContractEntityList);
        userService.findAllClientContractsOfUser(generateUserDto().getUsername(), generateClientDto().getPartnerCode());
    }

    @Test(expected = ClientManagerException.class)
    public void findAllClientContractsOfUserTest_withoutContracts() {
        Optional<UserEntity> userEntityOptionalEmpty = Optional.of(generateUserEntity());
        Optional<ClientEntity> clientEntityOptional = Optional.of(generateClientEntity());
        Optional<List<ContractEntity>> optionalContractEntityList = Optional.empty();

        when(userRepository.findByUsername(any())).thenReturn(userEntityOptionalEmpty);
        when(clientRepository.findByPartnerCode(any())).thenReturn(clientEntityOptional);
        when(contractRepository.findByUserEntityAndClientEntity(any(), any())).thenReturn(optionalContractEntityList);
        userService.findAllClientContractsOfUser(generateUserDto().getUsername(), generateClientDto().getPartnerCode());
    }

    @Test
    public void setNotifierService_allServices() {
        Optional<UserEntity> userEntityOptional = Optional.of(generateUserEntity());
        when(userRepository.findByUsername(any())).thenReturn(userEntityOptional);
        UserEntity userEntity = userEntityOptional.get();
        userService.setNotifierService(userEntity.getUsername(), NotifyEnum.ALL.name());
        userEntity.setNotifyType(NotifyEnum.ALL.name());
        verify(userRepository).save(userEntity);
    }

    @Test
    public void setNotifierService_telegramServices() {
        Optional<UserEntity> userEntityOptional = Optional.of(generateUserEntity());
        when(userRepository.findByUsername(any())).thenReturn(userEntityOptional);
        UserEntity userEntity = userEntityOptional.get();
        userService.setNotifierService(userEntity.getUsername(), NotifyEnum.TELEGRAM.name());
        userEntity.setNotifyType(NotifyEnum.TELEGRAM.name());
        verify(userRepository).save(userEntity);
    }

    @Test
    public void setNotifierService_emailServices() {
        Optional<UserEntity> userEntityOptional = Optional.of(generateUserEntity());
        when(userRepository.findByUsername(any())).thenReturn(userEntityOptional);
        UserEntity userEntity = userEntityOptional.get();
        userService.setNotifierService(userEntity.getUsername(), NotifyEnum.EMAIL.name());
        userEntity.setNotifyType(NotifyEnum.EMAIL.name());
        verify(userRepository).save(userEntity);
    }

    @Test
    public void setNotifierService_null() {
        Optional<UserEntity> userEntityOptional = Optional.of(generateUserEntity());
        when(userRepository.findByUsername(any())).thenReturn(userEntityOptional);
        UserEntity userEntity = userEntityOptional.get();
        userService.setNotifierService(userEntity.getUsername(), null);
        userEntity.setNotifyType(null);
        verify(userRepository).save(userEntity);
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

    private ContractEntity generateContractEntity() {
        ContractEntity contractEntity = new ContractEntity();
        contractEntity.setId(1L);
        contractEntity.setAddendumNumber(123456789L);
        contractEntity.setCost(123456789L);
        contractEntity.setDateOfNextPayment(LocalDate.now().plusDays(2));
        contractEntity.setExpirationDate(LocalDate.now().minusDays(2));
        contractEntity.setProduct("testProduct");
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

    private List<ContractEntity> createContractEntityList() {
        List<ContractEntity> contractEntities = new ArrayList<>();
        ContractEntity contractEntity = generateContractEntity();
        contractEntity.setClientEntity(generateClientEntity());
        contractEntities.add(contractEntity);
        contractEntities.add(contractEntity);
        return contractEntities;
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

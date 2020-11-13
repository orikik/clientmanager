package com.orikik.clientmanager.service;

import com.orikik.clientmanager.converter.ClientConverter;
import com.orikik.clientmanager.converter.ContractConverter;
import com.orikik.clientmanager.converter.UserConverter;
import com.orikik.clientmanager.dto.ClientDto;
import com.orikik.clientmanager.dto.ContractDto;
import com.orikik.clientmanager.dto.UserDto;
import com.orikik.clientmanager.entity.ClientEntity;
import com.orikik.clientmanager.entity.ContractEntity;
import com.orikik.clientmanager.entity.UserEntity;
import com.orikik.clientmanager.exception.ClientManagerException;
import com.orikik.clientmanager.exception.ErrorCodeEnum;
import com.orikik.clientmanager.repository.ContractRepository;
import com.orikik.clientmanager.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class UserService {
    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ContractRepository contractRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ClientService clientService;


    public UserDto createUser(UserDto userDto) {
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        UserEntity userEntity = userRepository.save(UserConverter.convert(userDto));
        return UserConverter.convert(userEntity);
    }

    public UserDto updateUser(UserDto userDto) {
        UserEntity userEntity = getUserEntity(userDto.getUsername());
        userDto.setId(userEntity.getId());
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        userEntity = userRepository.save(UserConverter.convert(userDto));
        return UserConverter.convert(userEntity);
    }

    public void deleteUser(String username) {
        UserEntity userEntity = getUserEntity(username);
        userRepository.deleteById(userEntity.getId());
    }

    public List<ClientDto> findAllClientsOfUser(String username) {
        UserEntity userEntity = getUserEntity(username);
        List<ContractEntity> contractEntities = getContractEntityListByUserEntity(userEntity);
        Set<ClientEntity> clientEntityHashSet = new HashSet<>();
        for (ContractEntity contractEntity : contractEntities) {
            clientEntityHashSet.add(contractEntity.getClientEntity());
        }
        List<ClientEntity> clientEntityList = new ArrayList<>(clientEntityHashSet);
        return ClientConverter.convert(clientEntityList);
    }

    public List<ContractDto> findAllContractsOfUser(String username) {
        UserEntity userEntity = getUserEntity(username);
        List<ContractEntity> contractEntities = getContractEntityListByUserEntity(userEntity);
        return ContractConverter.convert(contractEntities);
    }

    public List<ContractDto> findAllClientContractsOfUser(String username, Long partnerCode) {
        UserEntity userEntity = getUserEntity(username);
        ClientEntity clientEntity = clientService.getClientEntity(partnerCode);
        Optional<List<ContractEntity>> optionalContractEntityList =
                contractRepository.findByUserEntityAndClientEntity(userEntity, clientEntity);
        if (!optionalContractEntityList.isPresent()) {
            LOG.error("No any contracts username={} partner code ={} not found", username, partnerCode);
            throw new ClientManagerException(ErrorCodeEnum.INTERNAL_SERVER_ERROR,
                    String.format("No contracts with owner username = %s", username));
        }
        return ContractConverter.convert(optionalContractEntityList.get());
    }

    public void setNotifierService(String username, String notifyType) {
        UserEntity userEntity = getUserEntity(username);
        userEntity.setNotifyType(notifyType);
        userRepository.save(userEntity);
    }

    public UserEntity getUserEntity(String username) {
        Optional<UserEntity> userEntityOptional = userRepository.findByUsername(username);
        if (!userEntityOptional.isPresent()) {
            LOG.error("user={} not found", username);
            throw new UsernameNotFoundException("User not found");
        }
        return userEntityOptional.get();
    }

    private List<ContractEntity> getContractEntityListByUserEntity(UserEntity userEntity) {
        Optional<List<ContractEntity>> optionalContractEntities = contractRepository.findByUserEntity(userEntity);
        if (!optionalContractEntities.isPresent()) {
            LOG.error("user={} don't have any contracts", userEntity.getUsername());
            throw new ClientManagerException(ErrorCodeEnum.INTERNAL_SERVER_ERROR,
                    String.format("No contracts with owner username = %s", userEntity.getUsername()));
        }
        return optionalContractEntities.get();
    }
}

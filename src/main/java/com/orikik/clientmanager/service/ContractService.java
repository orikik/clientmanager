package com.orikik.clientmanager.service;

import com.orikik.clientmanager.converter.ContractConverter;
import com.orikik.clientmanager.dto.ContractDto;
import com.orikik.clientmanager.entity.ClientEntity;
import com.orikik.clientmanager.entity.ContractEntity;
import com.orikik.clientmanager.entity.UserEntity;
import com.orikik.clientmanager.exception.ClientManagerException;
import com.orikik.clientmanager.exception.ErrorCodeEnum;
import com.orikik.clientmanager.repository.ClientRepository;
import com.orikik.clientmanager.repository.ContractRepository;
import com.orikik.clientmanager.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class ContractService {
    private static final Logger LOG = LoggerFactory.getLogger(ContractService.class);

    @Autowired
    private ContractRepository contractRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private UserRepository userRepository;

    public ContractDto createContract(ContractDto contractDto, String username, Long partnerCode) {
        Optional<UserEntity> userEntityOptional = userRepository.findByUsername(username);
        Optional<ClientEntity> clientEntityOptional = clientRepository.findByPartnerCode(partnerCode);
        if (!clientEntityOptional.isPresent()) {
            LOG.error("client with partner code={} not found", partnerCode);
            throw new ClientManagerException(ErrorCodeEnum.INTERNAL_SERVER_ERROR,
                    String.format("No client with partner code = %s", partnerCode));
        }
        ContractEntity contractEntity = ContractConverter.convert(contractDto);
        contractEntity.setUserEntity(userEntityOptional.get());
        contractEntity.setClientEntity(clientEntityOptional.get());
        contractEntity = contractRepository.save(contractEntity);
        return ContractConverter.convert(contractEntity);
    }

    public ContractDto updateContract(ContractDto contractDto, String username) {
        if (contractDto.getAddendumNumber() == null) {
            LOG.error("User ={} try create contract without addendum number", username);
            throw new ClientManagerException(ErrorCodeEnum.INTERNAL_SERVER_ERROR,
                    "You forget addendum number");
        }
        ContractEntity contractEntity = getContractEntity(contractDto.getAddendumNumber(), username);
        contractDto.setId(contractEntity.getId());
        contractEntity = contractRepository.save(ContractConverter.convert(contractDto));
        return ContractConverter.convert(contractEntity);
    }

    public void deleteContract(Long addendumNumber, String username) {
        if (addendumNumber == null) {
            LOG.error("User ={} try delete contract without addendum number", username);
            throw new ClientManagerException(ErrorCodeEnum.INTERNAL_SERVER_ERROR,
                    "You forget addendum number");
        }
        ContractEntity contractEntity = getContractEntity(addendumNumber, username);
        contractRepository.deleteById(contractEntity.getId());
    }

    public ContractEntity getContractEntity(Long addendumNumber, String username) {
        Optional<ContractEntity> contractEntityOptional = contractRepository.findByAddendumNumber(addendumNumber);
        if (!contractEntityOptional.isPresent()) {
            LOG.error("contract with addendum number={} not found", addendumNumber);
            throw new ClientManagerException(ErrorCodeEnum.INTERNAL_SERVER_ERROR,
                    String.format("No contract with addendumNumber = %s", addendumNumber));
        }
        ContractEntity contractEntity = contractEntityOptional.get();
        if (!contractEntity.getUserEntity().getUsername().equals(username)) {
            LOG.error("contract with addendum number={} not belong user={}", addendumNumber, username);
            throw new UsernameNotFoundException("User not found");
        }
        return contractEntity;
    }
}

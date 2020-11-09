package com.orikik.clientmanager.service;

import com.orikik.clientmanager.converter.ClientConverter;
import com.orikik.clientmanager.dto.ClientDto;
import com.orikik.clientmanager.dto.ContractDto;
import com.orikik.clientmanager.entity.ClientEntity;
import com.orikik.clientmanager.entity.UserEntity;
import com.orikik.clientmanager.exception.ClientManagerException;
import com.orikik.clientmanager.exception.ErrorCodeEnum;
import com.orikik.clientmanager.repository.ClientRepository;
import com.orikik.clientmanager.repository.ContractRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ClientService {
    private static final Logger LOG = LoggerFactory.getLogger(ClientService.class);
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private ContractRepository contractRepository;
    @Autowired
    private ContractService contractService;
    @Autowired
    private UserService userService;

    public ClientDto createClientAndHisContract(ClientDto clientDto, String username) {
        long partnerCode = clientDto.getPartnerCode();
        Optional<ClientEntity> clientEntityOptional = clientRepository.findByPartnerCode(partnerCode);
        if (clientEntityOptional.isPresent()) {
            LOG.error("client with partner code={} just exist", partnerCode);
            throw new ClientManagerException(ErrorCodeEnum.INTERNAL_SERVER_ERROR,
                    String.format("Client with partner code = %s just exist", partnerCode));
        }
        ClientEntity clientEntity = clientRepository.save(ClientConverter.convert(clientDto));
        List<ContractDto> contractDtoList = clientDto.getContractDtoList();
        List<ContractDto> contractDtos = new ArrayList<>();
        for (ContractDto contractDto : contractDtoList) {
            contractDto = contractService.createContract(contractDto, username, clientEntity.getPartnerCode());
            contractDtos.add(contractDto);
        }
        clientDto = ClientConverter.convert(clientEntity);
        clientDto.setContractDtoList(contractDtos);
        return clientDto;
    }

    public ClientDto updateClient(ClientDto clientDto) {
        ClientEntity clientEntity = getClientEntity(clientDto.getPartnerCode());
        clientDto.setId(clientEntity.getId());
        clientEntity = clientRepository.save(ClientConverter.convert(clientDto));
        return ClientConverter.convert(clientEntity);
    }

    public void deleteClient(Long partnerCode, String username) {
        ClientEntity clientEntity = getClientEntity(partnerCode);
        UserEntity userEntity = userService.getUserEntity(username);
        contractRepository.deleteAllByUserEntityAndClientEntity(userEntity, clientEntity);
    }

    public ClientEntity getClientEntity(Long partnerCode) {
        Optional<ClientEntity> clientEntityOptional = clientRepository.findByPartnerCode(partnerCode);
        if (!clientEntityOptional.isPresent()) {
            LOG.error("client with partner code={} not found", partnerCode);
            throw new ClientManagerException(ErrorCodeEnum.INTERNAL_SERVER_ERROR,
                    String.format("No client with partner code = %s", partnerCode));
        }
        return clientEntityOptional.get();
    }
}

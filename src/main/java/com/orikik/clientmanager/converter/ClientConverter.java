package com.orikik.clientmanager.converter;

import com.orikik.clientmanager.dto.ClientDto;
import com.orikik.clientmanager.entity.ClientEntity;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ClientConverter {
    public static ClientDto convert(ClientEntity clientEntity) {
        if (clientEntity == null) {
            return null;
        }
        ClientDto clientDto = new ClientDto();
        clientDto.setId(clientEntity.getId());
        clientDto.setFirstname(clientEntity.getFirstname());
        clientDto.setLastname(clientEntity.getLastname());
        clientDto.setMiddlename(clientEntity.getMiddlename());
        clientDto.setPartnerCode(clientEntity.getPartnerCode());
        clientDto.setPhoneNumberList(clientEntity.getPhones());
        return clientDto;
    }

    public static ClientEntity convert(ClientDto clientDto) {
        if (clientDto == null) {
            return null;
        }
        ClientEntity clientEntity = new ClientEntity();
        clientEntity.setId(clientDto.getId());
        clientEntity.setFirstname(clientDto.getFirstname());
        clientEntity.setLastname(clientDto.getLastname());
        clientEntity.setMiddlename(clientDto.getMiddlename());
        clientEntity.setPartnerCode(clientDto.getPartnerCode());
        clientEntity.setPhones(clientDto.getPhoneNumberList());
        return clientEntity;
    }

    public static List<ClientDto> convert(List<ClientEntity> contractEntities) {
        if (CollectionUtils.isEmpty(contractEntities)) {
            return Collections.emptyList();
        }
        return contractEntities.stream().map(ClientConverter::convert).collect(Collectors.toList());
    }
}

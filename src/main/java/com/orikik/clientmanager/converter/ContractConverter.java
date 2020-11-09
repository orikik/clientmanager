package com.orikik.clientmanager.converter;

import com.orikik.clientmanager.dto.ContractDto;
import com.orikik.clientmanager.entity.ContractEntity;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ContractConverter {
    public static ContractDto convert(ContractEntity contractEntity) {
        if (contractEntity == null) {
            return null;
        }
        ContractDto contractDto = new ContractDto();
        contractDto.setId(contractEntity.getId());
        contractDto.setAddendumNumber(contractEntity.getAddendumNumber());
        contractDto.setCost(contractEntity.getCost());
        contractDto.setDateOfNextPayment(contractEntity.getDateOfNextPayment());
        contractDto.setExpirationDate(contractEntity.getExpirationDate());
        contractDto.setProduct(contractEntity.getProduct());
        return contractDto;
    }

    public static ContractEntity convert(ContractDto contractDto) {
        if (contractDto == null) {
            return null;
        }
        ContractEntity contractEntity = new ContractEntity();
        contractEntity.setId(contractDto.getId());
        contractEntity.setAddendumNumber(contractDto.getAddendumNumber());
        contractEntity.setCost(contractDto.getCost());
        contractEntity.setDateOfNextPayment(contractDto.getDateOfNextPayment());
        contractEntity.setExpirationDate(contractDto.getExpirationDate());
        contractEntity.setProduct(contractDto.getProduct());
        return contractEntity;
    }

    public static List<ContractDto> convert(List<ContractEntity> contractEntities) {
        if (CollectionUtils.isEmpty(contractEntities)) {
            return Collections.emptyList();
        }
        return contractEntities.stream().map(ContractConverter::convert).collect(Collectors.toList());
    }
}

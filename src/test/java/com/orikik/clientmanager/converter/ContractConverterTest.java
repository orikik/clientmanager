package com.orikik.clientmanager.converter;

import com.orikik.clientmanager.dto.ContractDto;
import com.orikik.clientmanager.entity.ContractEntity;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ContractConverterTest {
    @Test
    public void convertTest_entityToDto() {
        ContractEntity expected = generateContractEntity();
        ContractDto actually = ContractConverter.convert(expected);
        assertContract(actually, expected);
    }

    @Test
    public void convertTest_entityNullToDto() {
        assertNull(ContractConverter.convert((ContractEntity) null));
    }

    @Test
    public void convertTest_dtoToEntity() {
        ContractDto expected = generateContractDto();
        ContractEntity actually = ContractConverter.convert(expected);
        assertContract(expected, actually);
    }

    @Test
    public void convertTest_nullDtoToEntity() {
        assertNull(ContractConverter.convert((ContractDto) null));
    }

    @Test
    public void convertListTest_entitiesToDtos() {
        List<ContractEntity> expected = createContractEntityList();
        List<ContractDto> actually = ContractConverter.convert(expected);
        Assert.assertEquals(2, expected.size());
        Assert.assertEquals(2, actually.size());
        assertContract(actually.get(0), expected.get(0));
        assertContract(actually.get(1), expected.get(1));
    }

    @Test
    public void convertListTest_entitiesNullToDtos() {
        List<ContractDto> result = ContractConverter.convert((List<ContractEntity>) null);
        assertNotNull(result);
        Assert.assertEquals(0, result.size());
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

    private List<ContractEntity> createContractEntityList() {
        List<ContractEntity> contractEntities = new ArrayList<>();
        contractEntities.add(generateContractEntity());
        contractEntities.add(generateContractEntity());
        return contractEntities;
    }

    private void assertContract(ContractDto dto, ContractEntity entity) {
        assertNotNull(dto);
        assertNotNull(entity);
        assertEquals(1, dto.getId().longValue());
        assertEquals(123456789L, dto.getAddendumNumber().longValue());
        assertEquals(123456789L, dto.getCost());
        assertEquals(LocalDate.now().plusDays(2), dto.getDateOfNextPayment());
        assertEquals(LocalDate.now().minusDays(2), dto.getExpirationDate());
        assertEquals("testProduct", dto.getProduct());

        assertEquals(entity.getId(), dto.getId());
        assertEquals(entity.getAddendumNumber(), dto.getAddendumNumber());
        assertEquals(entity.getCost().longValue(), dto.getCost());
        assertEquals(entity.getDateOfNextPayment(), dto.getDateOfNextPayment());
        assertEquals(entity.getExpirationDate(), dto.getExpirationDate());
        assertEquals(entity.getProduct(), dto.getProduct());
    }
}

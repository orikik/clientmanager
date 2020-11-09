package com.orikik.clientmanager.dto;

import java.util.List;

public class ClientDto {
    private Long id;
    private String firstname;
    private String lastname;
    private String middlename;
    private Long partnerCode;
    private List<String> phoneNumberList;
    private List<ContractDto> contractDtoList;

    public List<ContractDto> getContractDtoList() {
        return contractDtoList;
    }

    public void setContractDtoList(List<ContractDto> contractDtoList) {
        this.contractDtoList = contractDtoList;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getMiddlename() {
        return middlename;
    }

    public void setMiddlename(String middlename) {
        this.middlename = middlename;
    }

    public Long getPartnerCode() {
        return partnerCode;
    }

    public void setPartnerCode(Long partnerCode) {
        this.partnerCode = partnerCode;
    }

    public List<String> getPhoneNumberList() {
        return phoneNumberList;
    }

    public void setPhoneNumberList(List<String> phoneNumberList) {
        this.phoneNumberList = phoneNumberList;
    }
}

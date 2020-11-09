package com.orikik.clientmanager.dto;

import java.time.LocalDate;

public class ContractDto {
    private Long id;
    private Long addendumNumber;
    private String product;
    private LocalDate expirationDate;
    private long cost;
    private LocalDate dateOfNextPayment;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAddendumNumber() {
        return addendumNumber;
    }

    public void setAddendumNumber(Long addendumNumber) {
        this.addendumNumber = addendumNumber;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    public long getCost() {
        return cost;
    }

    public void setCost(long cost) {
        this.cost = cost;
    }

    public LocalDate getDateOfNextPayment() {
        return dateOfNextPayment;
    }

    public void setDateOfNextPayment(LocalDate dateOfNextPayment) {
        this.dateOfNextPayment = dateOfNextPayment;
    }
}

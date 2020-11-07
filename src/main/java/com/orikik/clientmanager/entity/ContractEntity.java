package com.orikik.clientmanager.entity;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = ContractEntity.TABLE)
public class ContractEntity {
    public static final String TABLE = "contracts";

    @Id
    @SequenceGenerator(name = "contracts_pk_sequence",
            sequenceName = "contracts_seq",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "contracts_pk_sequence")
    private Long id;
    @Column(name = "addendum_number")
    private Long addendumNumber;
    private String product;
    @Column(name = "expiration_date")
    private LocalDate expirationDate;
    private Long cost;
    @Column(name = "date_of_next_payment")
    private LocalDate dateOfNextPayment;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;
    @ManyToOne
    @JoinColumn(name = "client_id")
    private ClientEntity clientEntity;

    public ClientEntity getClientEntity() {
        return clientEntity;
    }

    public void setClientEntity(ClientEntity clientEntity) {
        this.clientEntity = clientEntity;
    }

    public UserEntity getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

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

    public Long getCost() {
        return cost;
    }

    public void setCost(Long cost) {
        this.cost = cost;
    }

    public LocalDate getDateOfNextPayment() {
        return dateOfNextPayment;
    }

    public void setDateOfNextPayment(LocalDate dateOfNextPayment) {
        this.dateOfNextPayment = dateOfNextPayment;
    }
}

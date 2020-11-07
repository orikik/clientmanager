package com.orikik.clientmanager.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = ClientEntity.TABLE)
public class ClientEntity {
    public static final String TABLE = "clients";

    @Id
    @SequenceGenerator(name = "clients_pk_sequence",
            sequenceName = "clients_seq",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "clients_pk_sequence")
    private Long id;
    private String firstname;
    private String lastname;
    private String middlename;
    @Column(name = "partner_code")
    private Long partnerCode;
    @ElementCollection
    @CollectionTable(name = "client_phone_number", joinColumns = @JoinColumn(name = "client_id"))
    @Column(name = "phone_number")
    private List<String> phones;

    public List<String> getPhones() {
        return phones;
    }

    public void setPhones(List<String> phones) {
        this.phones = phones;
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
}

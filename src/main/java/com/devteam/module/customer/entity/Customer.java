package com.devteam.module.customer.entity;

import com.devteam.core.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "customer")
@Getter
@Setter
public class Customer extends BaseEntity<String> {

    private  String firstName;
    private  String lastName;
    private  String emailAddress;

    @OneToMany(fetch = FetchType.EAGER, orphanRemoval = true, targetEntity = Address.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "customer_id")
    @JsonManagedReference
    private List<Address> addresses = new ArrayList<>();

    public Customer(){}

    public Customer(String id, String firstName, String lastName, String emailAddress, List<Address> addresses) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
        this.addresses = addresses;
    }

    public void setAddresses(List<Address> addresses) {
        if(this.addresses == null) {
            this.addresses = new ArrayList<>();
        }else {
            this.addresses.clear();
        }
        this.addresses.addAll(addresses);
    }
}

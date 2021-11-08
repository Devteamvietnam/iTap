package com.devteam.module.customer.entity;

import com.devteam.config.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;


@Entity
@Table(name = "address")
@Getter
@Setter
public class Address extends BaseEntity {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    private  String streetAddress;
    private  String city;
    private  String stateCode;
    private  String country;
    private  String zipCode;

    @ManyToOne
    @JsonBackReference
    private Customer customer;

    public Address(){}

    public Address(String id, String streetAddress, String city, String stateCode, String country, String zipCode, Customer customer) {
        this.id = id;
        this.streetAddress = streetAddress;
        this.city = city;
        this.stateCode = stateCode;
        this.country = country;
        this.zipCode = zipCode;
        this.customer = customer;
    }
}

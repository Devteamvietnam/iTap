package com.devteam.module.customer.entity;

import com.devteam.config.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "address")
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
}

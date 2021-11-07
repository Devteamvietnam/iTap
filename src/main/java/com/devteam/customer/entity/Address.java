package com.devteam.customer.entity;

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

    private String streetAddress;
    private @NonNull String city;
    private @NonNull String stateCode;
    private @NonNull String country;
    private @NonNull String zipCode;

    @ManyToOne
    @JsonBackReference
    private Customer customer;
}

package com.devteam.module.customer.entity;

import com.devteam.config.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "customer")
public class Customer extends BaseEntity {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;


    private  String firstName;
    private  String lastName;
    private  String emailAddress;

    @OneToMany(fetch = FetchType.EAGER, orphanRemoval = true, targetEntity = Address.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "customer_id")
    @JsonManagedReference
    private List<Address> addresses = new ArrayList<>();

    public void setAddresses(List<Address> addresses) {
        if(this.addresses == null) {
            this.addresses = new ArrayList<>();
        }else {
            this.addresses.clear();
        }
        this.addresses.addAll(addresses);
    }
}

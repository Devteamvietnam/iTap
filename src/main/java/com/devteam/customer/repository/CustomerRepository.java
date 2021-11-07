package com.devteam.customer.repository;

import com.devteam.customer.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {
    List<Customer> findAllByFirstNameContainingOrLastNameContaining(String firstName, String lastName);
}

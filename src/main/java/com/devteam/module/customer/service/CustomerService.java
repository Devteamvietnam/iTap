package com.devteam.module.customer.service;

import com.devteam.module.customer.entity.Customer;
import com.devteam.module.customer.repository.CustomerRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Log4j2
@CacheConfig(cacheNames = "customers")
public class CustomerService {

    //Autowiring the repository
    @Autowired
    private CustomerRepository customerRepository;

    //Get customer by Id
    @Cacheable
    public Optional<Customer> getCustomerById(String customerId) {
        log.info("Fetching customer by id: {}", customerId);
        return customerRepository.findById(customerId);
    }

    //Create the customer
    @Cacheable
    public Customer create(Customer customer) {
        return customerRepository.save(customer);
    }

    //Update the customer
    @Cacheable
    public Customer update(Customer customer) {
        Optional<Customer> optionalCustomer = customerRepository.findById(customer.getId());
        if(optionalCustomer.isEmpty()) {
            throw new RuntimeException("Cannot find the customer by id " + customer.getId());
        }
        Customer existingCustomer = optionalCustomer.get();
        existingCustomer.setAddresses(customer.getAddresses());
        existingCustomer.setFirstName(customer.getFirstName());
        existingCustomer.setLastName(customer.getLastName());
        return customerRepository.save(existingCustomer);
    }

    //Find all customers by name
    public List<Customer> findByName(String name){
        return customerRepository.findAllByFirstNameContainingOrLastNameContaining(name, name);
    }

    //Paging implementation of findAll
    public Page<Customer> findAll(Pageable pageable) {
        return customerRepository.findAll(pageable);
    }
}

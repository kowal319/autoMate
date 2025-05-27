package com.example.demo.service;

import com.example.demo.entity.Customer;

import java.util.List;
import java.util.Optional;

public interface CustomerService {


    Object findById(Long id);
    Optional<Customer> getCustomerById(Long id);
    Customer createCustomer(Customer Customer);
    List<Customer> getAllCustomers();
    Customer updateCustomer(Long id, Customer updateUser);
    String deleteCustomer(Long id);
}


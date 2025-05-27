package com.example.demo.service;

import com.example.demo.dto.CustomerDTO;
import com.example.demo.entity.Customer;

import java.util.List;

public interface CustomerService {


    Object findById(Long id);
    Customer getCustomerById(Long id);
    Customer createCustomer(CustomerDTO dto);

    List<Customer> getAllCustomers();
    Customer updateCustomer(Long id, Customer updateUser);
    String deleteCustomer(Long id);
}


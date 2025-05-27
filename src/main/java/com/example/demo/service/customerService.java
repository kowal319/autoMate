package com.example.demo.service;

import com.example.demo.entity.Customer;

import java.util.List;

public interface customerService {


    Object findById(Long id);
    Customer createAppUser(Customer Customer);
    List<Customer> getAllAppUsers();
    Customer updateCustomer(Long id, Customer updateUser);
    String deleteCustomer(Long id);
}


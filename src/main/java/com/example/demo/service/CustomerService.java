package com.example.demo.service;

import com.example.demo.dto.CustomerDTO;
import com.example.demo.dto.RegistrationDTO;
import com.example.demo.entity.Customer;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Optional;

public interface CustomerService {


    Object findById(Long id);
    Customer getCustomerById(Long id);


    Optional<Customer> findByEmail(String email);

    Optional<Customer> getCurrentUser(Authentication authentication);

    Customer registerCustomer(RegistrationDTO registrationDto);

    Customer saveUserWithRole(CustomerDTO customer, String roleName);

    Customer createCustomer(CustomerDTO dto);

    List<Customer> getAllCustomers();

    Customer updateCustomer(Long id, CustomerDTO updatedCustomerDTO);

    String deleteCustomer(Long id);

    void changePassword(Long id, String newPassword);
}


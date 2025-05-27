package com.example.demo.service.Implementation;

import com.example.demo.entity.Customer;
import com.example.demo.repository.customerRepository;
import com.example.demo.service.customerService;

import java.util.List;
import java.util.Optional;

public class customerServiceImpl implements customerService {

    private customerRepository customerRepository;

    public customerServiceImpl(customerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public Customer findById(Long id) {
        Optional<Customer> appUserOptional = customerRepository.findById(id);
        return appUserOptional.orElse(null);
    }

    @Override
    public Customer createAppUser(Customer Customer) {
        return customerRepository.save(Customer);
    }

    @Override
    public List<Customer> getAllAppUsers() {
        return customerRepository.findAll();
    }

    @Override
    public Customer updateCustomer(Long id, Customer updateUser) {
        return null;
    }

    @Override
    public String deleteCustomer(Long id) {
        Optional<Customer> appUserOptional = customerRepository.findById(id);
        if (appUserOptional.isPresent()) {
            Customer Customer = appUserOptional.get();
            customerRepository.deleteById(id);
            return "User with id : " + id + " has been deleted";
        } else {
            return "User with id: " + id + " not found";
        }
    }
}

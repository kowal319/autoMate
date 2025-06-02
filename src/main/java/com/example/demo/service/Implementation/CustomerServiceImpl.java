package com.example.demo.service.Implementation;

import com.example.demo.dto.CustomerDTO;
import com.example.demo.entity.Customer;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.service.CustomerService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    public CustomerServiceImpl(CustomerRepository customerRepository,
                               PasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Customer findById(Long id) {
        Optional<Customer> appUserOptional = customerRepository.findById(id);
        return appUserOptional.orElse(null);
    }

    @Override
    public Customer getCustomerById(Long id) {
        Optional<Customer> customerOptional = customerRepository.findById(id);
        return customerOptional.orElse(null);
    }

    @Override
    public Customer createCustomer(CustomerDTO dto) {
        Customer customer = new Customer();
        customer.setEmail(dto.getEmail());
        customer.setName(dto.getName());
        customer.setPassword(passwordEncoder.encode(dto.getPassword())); // hashujemy
        return customerRepository.save(customer);
    }
    @Override
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    @Override
    public Customer updateCustomer(Long id, CustomerDTO updatedCustomerDTO) {
        Customer existing = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Nie znaleziono klienta"));

        existing.setName(updatedCustomerDTO.getName());
        existing.setEmail(updatedCustomerDTO.getEmail());
        return customerRepository.save(existing);
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

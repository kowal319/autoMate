package com.example.demo.service.Implementation;

import com.example.demo.dto.CustomerDTO;
import com.example.demo.dto.RegistrationDTO;
import com.example.demo.entity.Customer;
import com.example.demo.entity.Role;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.repository.RoleRepository;
import com.example.demo.service.CustomerService;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;


    public CustomerServiceImpl(CustomerRepository customerRepository,
                               PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
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
    public Optional<Customer> findByEmail(String email) {
        return customerRepository.findByEmail(email);
    }


    @Override
    public Optional<Customer> getCurrentUser(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            String email = authentication.getName();
            return customerRepository.findByEmail(email);
        }
        return null;
    }

    @Override
    public Customer registerCustomer(RegistrationDTO registrationDto) {
        Customer customer = new Customer();
        customer.setEmail(registrationDto.getEmail());
        customer.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
        customer.setName(registrationDto.getName());
        customer.setEnabled(false);


//        // Set default role as "CUSTOMER"
        Role customerRole = roleRepository.findByName("CUSTOMER");
        customer.setRoles(new HashSet<>(Collections.singletonList(customerRole)));
        customerRepository.save(customer);


        return customer;
    }

    @Override
    public Customer saveUserWithRole(CustomerDTO customer, String roleName) {
        // Create the user
        Customer savedCustomer = createCustomer(customer);

        // Find the role by name
        Role role = roleRepository.findByName(roleName);

        // Add the role to the user
        if (role != null) {
            savedCustomer.getRoles().add(role);
            customerRepository.save(savedCustomer);
        } else {
            throw new IllegalArgumentException("Role not found: " + roleName);
        }
        return savedCustomer;
    }

    @Override
    public Customer createCustomer(CustomerDTO dto) {
        Customer customer = new Customer();
        customer.setEmail(dto.getEmail());
        customer.setName(dto.getName());
        customer.setPassword(passwordEncoder.encode(dto.getPassword()));
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
        if (updatedCustomerDTO.getPassword() != null && !updatedCustomerDTO.getPassword().isBlank()) {
            existing.setPassword(passwordEncoder.encode(updatedCustomerDTO.getPassword()));
        }
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
    @Override
    public void changePassword(Long id, String newPassword) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Nie znaleziono klienta"));

        customer.setPassword(passwordEncoder.encode(newPassword));
        customerRepository.save(customer);
    }
}

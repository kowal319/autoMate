package com.example.demo;

import com.example.demo.controller.api.CustomerApiController;
import com.example.demo.dto.CustomerDTO;
import com.example.demo.entity.Customer;
import com.example.demo.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomerApiControllerTest {

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private CustomerApiController customerApiController;

    private Customer customer;
    private CustomerDTO customerDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        customer = new Customer();
        customer.setId(1L);
        customer.setName("Jan");
        customer.setEmail("janek224@gmail.com");

        customerDTO = new CustomerDTO();
        customerDTO.setName("Jan");
        customerDTO.setEmail("janek224@gmail.com");
    }

    @Test
    void getAllCustomers_shouldReturnList() {
        List<Customer> customers = List.of(customer);
        when(customerService.getAllCustomers()).thenReturn(customers);

        ResponseEntity<List<Customer>> response = customerApiController.getAllCustomers();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(customers, response.getBody());
        verify(customerService, times(1)).getAllCustomers();
    }

    @Test
    void getCustomerById_found() {
        when(customerService.getCustomerById(1L)).thenReturn(customer);

        ResponseEntity<Customer> response = customerApiController.getCustomerById(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(customer, response.getBody());
        verify(customerService).getCustomerById(1L);
    }

    @Test
    void getCustomerById_notFound() {
        when(customerService.getCustomerById(2L)).thenReturn(null);

        ResponseEntity<Customer> response = customerApiController.getCustomerById(2L);

        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
        verify(customerService).getCustomerById(2L);
    }

    @Test
    void createCustomer_shouldReturnCreatedCustomer(){
        when(customerService.createCustomer(customerDTO)).thenReturn(customer);

        ResponseEntity<Customer> response = customerApiController.createCustomer(customerDTO);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(customer, response.getBody());
        verify(customerService).createCustomer(customerDTO);
    }

    @Test
    void updateCustomer_shouldReturnUpdatedCustomer(){
        when(customerService.updateCustomer(1L, customerDTO)).thenReturn(customer);

        ResponseEntity<Customer> response = customerApiController.updateCustomer(1L, customerDTO);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(customer, response.getBody());
        verify(customerService).updateCustomer(1L, customerDTO);
    }

    @Test
    void deleteCustomer_shouldReturnNoContent() {
        Long id = 1L;

        ResponseEntity<Void> response = customerApiController.deleteCustomer(id);

        assertEquals(204, response.getStatusCodeValue());
        verify(customerService, times(1)).deleteCustomer(id);
    }

    @Test
    void changePasswordCustomer_wrongConfirmPassword(){
        Long id = 1L;
        String password = "newPassword";
        String confirmPassword = "confirmPassword";

        ResponseEntity<?> response = customerApiController.changePassword(id, password, confirmPassword);        assertEquals(400, response.getStatusCodeValue());
        verify(customerService, never()).changePassword(anyLong(), anyString());
    }

    @Test
    void changePasswordCustomer_correct() {
        Long id = 1L;
        String password = "newPassword";
        String confirmPassword = "newPassword";

        ResponseEntity<?> response = customerApiController.changePassword(id, password, confirmPassword);

        assertEquals(204, response.getStatusCodeValue());

        verify(customerService, times(1)).changePassword(id, password);
    }
}

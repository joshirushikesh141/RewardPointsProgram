package controller;
import com.reward.points.controller.CustomerController;
import com.reward.points.entity.Customer;
import com.reward.points.service.CustomerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CustomerControllerTest {

    @InjectMocks
    private CustomerController customerController;

    @Mock
    private CustomerService customerService;

    @Test
    public void testCustomerRegistration_Success() {
        Customer customer = new Customer();
        customer.setCustomerName("Test Customer");

        when(customerService.saveCustomerDetails(customer)).thenReturn("Customer registered successfully");

        ResponseEntity<String> response = customerController.customerRegistration(customer);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Customer registered successfully", response.getBody());
    }

    @Test
    public void testGetRegisteredCustomerDetailsByCustomerId_Success() {
        Long customerId = 1L;
        Customer customer = new Customer();
        customer.setCustomerId(customerId);
        customer.setCustomerName("Test Customer");

        when(customerService.getRegisteredCustomerDetailsByCustomerId(customerId)).thenReturn(customer);

        ResponseEntity<Customer> response = customerController.getRegisteredCustomerDetailsByCustomerId(customerId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(customer, response.getBody());
    }

    @Test
    public void testGetRegisteredCustomerDetailsByCustomerId_NotFound() {
        Long customerId = 1L;
        when(customerService.getRegisteredCustomerDetailsByCustomerId(customerId)).thenReturn(null);

        ResponseEntity<Customer> response = customerController.getRegisteredCustomerDetailsByCustomerId(customerId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testUpdateCustomerDetails_Success() {
        Long customerId = 1L;
        Customer customer = new Customer();
        customer.setCustomerName("Updated Customer Name");

        Customer existingCustomer = new Customer();
        existingCustomer.setCustomerId(customerId);
        existingCustomer.setCustomerName("Original Name");

        when(customerService.getRegisteredCustomerDetailsByCustomerId(customerId)).thenReturn(existingCustomer);
        when(customerService.saveCustomerDetails(any(Customer.class))).thenReturn("Customer updated successfully");

        ResponseEntity<String> response = customerController.updateCustomerDetails(customer, customerId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Customer updated successfully", response.getBody());
    }

    @Test
    public void testUpdateCustomerDetails_NotFound() {
        Long customerId = 1L;
        Customer customer = new Customer();

        when(customerService.getRegisteredCustomerDetailsByCustomerId(customerId)).thenReturn(null);

        ResponseEntity<String> response = customerController.updateCustomerDetails(customer, customerId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("", response.getBody());

    }

    @Test
    public void testDeleteCustomerDetails_Success() {
        Long customerId = 1L;
        Customer customer = new Customer();
        customer.setCustomerId(customerId);

        when(customerService.getRegisteredCustomerDetailsByCustomerId(customerId)).thenReturn(customer);
        when(customerService.deleteCustomerDetails(customerId)).thenReturn("Customer deleted successfully");

        ResponseEntity<String> response = customerController.deleteCustomerDetails(customerId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Customer deleted successfully", response.getBody());
    }

    @Test
    public void testDeleteCustomerDetails_NotFound() {
        Long customerId = 1L;

        when(customerService.getRegisteredCustomerDetailsByCustomerId(customerId)).thenReturn(null);

        ResponseEntity<String> response = customerController.deleteCustomerDetails(customerId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("", response.getBody());

    }
}
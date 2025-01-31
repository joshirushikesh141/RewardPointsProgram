package service;
import com.reward.points.entity.Customer;
import com.reward.points.exception.CustomerNotFoundException;
import com.reward.points.repository.CustomerRepository;
import com.reward.points.service.CustomerServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceImplTest {

    @InjectMocks
    private CustomerServiceImpl customerService;

    @Mock
    private CustomerRepository customerRepository;

    @Test
    public void testSaveCustomerDetails_Success() {
        Customer customer = new Customer();
        customer.setCustomerName("Test Customer");
        customer.setCustomerId(1L);

        when(customerRepository.save(customer)).thenReturn(customer);

        String result = customerService.saveCustomerDetails(customer);

        assertTrue(result.contains("Customer has been registered/updated successfully!!"));
    }

    @Test
    public void testSaveCustomerDetails_Exception() {
        Customer customer = new Customer();
        when(customerRepository.save(customer)).thenThrow(new RuntimeException("Database error"));

        assertThrows(RuntimeException.class, () -> customerService.saveCustomerDetails(customer));
    }


    @Test
    public void testGetRegisteredCustomerDetailsByCustomerId_Success() {
        Long customerId = 1L;
        Customer customer = new Customer();
        customer.setCustomerId(customerId);
        customer.setCustomerName("Test Customer");

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));

        Customer result = customerService.getRegisteredCustomerDetailsByCustomerId(customerId);

        assertEquals(customer, result);
    }

    @Test
    public void testGetRegisteredCustomerDetailsByCustomerId_NotFound() {
        Long customerId = 1L;
        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> customerService.getRegisteredCustomerDetailsByCustomerId(customerId));

        Throwable cause = thrown.getCause();
        assertTrue(cause instanceof CustomerNotFoundException);
        assertEquals("Customer Not Found, Please Register First!!", cause.getMessage());
    }

    @Test
    public void testGetRegisteredCustomerDetailsByCustomerId_Exception() {
        Long customerId = 1L;
        when(customerRepository.findById(customerId)).thenThrow(new RuntimeException("Database error"));

        assertThrows(RuntimeException.class, () -> customerService.getRegisteredCustomerDetailsByCustomerId(customerId));
    }

    @Test
    public void testDeleteCustomerDetails_Success() {
        Long customerId = 1L;
        Customer customer = new Customer();
        customer.setCustomerId(customerId);

        doNothing().when(customerRepository).deleteById(customerId);

        String result = customerService.deleteCustomerDetails(customerId);

        assertEquals("Customer details has been deleted !!!", result);
    }

    @Test
    public void testGetAllCustomers_Success() {
        Customer customer1 = new Customer();
        customer1.setCustomerId(1L);
        customer1.setCustomerName("Customer 1");

        Customer customer2 = new Customer();
        customer2.setCustomerId(2L);
        customer2.setCustomerName("Customer 2");

        List<Customer> customerList = Arrays.asList(customer1, customer2);
        when(customerRepository.findAll()).thenReturn(customerList);

        List<Customer> result = customerService.getAllCustomers();

        assertEquals(customerList.size(), result.size());
        assertEquals(customer1.getCustomerName(), result.get(0).getCustomerName());
        assertEquals(customer2.getCustomerName(), result.get(1).getCustomerName());
    }

    @Test
    public void testGetAllCustomers_Empty() {
        when(customerRepository.findAll()).thenReturn(List.of());

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> customerService.getAllCustomers());

        Throwable cause = thrown.getCause();
        assertTrue(cause instanceof CustomerNotFoundException);
        assertEquals("No customers found, Please Register First!!", cause.getMessage());
    }


    @Test
    public void testGetAllCustomers_Exception() {
        when(customerRepository.findAll()).thenThrow(new RuntimeException("Database error"));

        assertThrows(RuntimeException.class, () -> customerService.getAllCustomers());
    }
}
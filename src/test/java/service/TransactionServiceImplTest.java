package service;
import com.reward.points.entity.Customer;
import com.reward.points.entity.Transaction;
import com.reward.points.exception.CustomerNotFoundException;
import com.reward.points.exception.TransactionNotFoundException;
import com.reward.points.repository.TransactionRepository;
import com.reward.points.service.CustomerService;
import com.reward.points.service.TransactionServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceImplTest {

    @InjectMocks
    private TransactionServiceImpl transactionService;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private CustomerService customerService;

    @Test
    public void testSaveTransactionDetails_Success() {
        Transaction transaction = new Transaction();
        transaction.setTransactionId(1L);
        transaction.setCustomerId(101L);
        transaction.setTransactionAmount(50.0);


        when(transactionRepository.save(transaction)).thenReturn(transaction);

        String result = transactionService.saveTransactionDetails(transaction);

        assertTrue(result.contains("Transaction completed successfully!!"));
    }

    @Test
    public void testSaveTransactionDetails_Exception() {
        Transaction transaction = new Transaction();
        when(transactionRepository.save(transaction)).thenThrow(new RuntimeException("Runtime Exception"));

        assertThrows(RuntimeException.class, () -> transactionService.saveTransactionDetails(transaction));
    }

    @Test
    public void testSaveAllTransactionDetails_Success() {
        Customer customer1 = new Customer();
        customer1.setCustomerId(101L);
        Customer customer2 = new Customer();
        customer2.setCustomerId(102L);
        List<Customer> customers = Arrays.asList(customer1, customer2);

        Transaction transaction1 = new Transaction();
        transaction1.setCustomerId(101L);
        Transaction transaction2 = new Transaction();
        transaction2.setCustomerId(102L);
        List<Transaction> transactions = Arrays.asList(transaction1, transaction2);

        Set<Long> customerIds = customers.stream().map(Customer::getCustomerId).collect(Collectors.toSet());
        when(customerService.getAllCustomers()).thenReturn(customers);
        when(transactionRepository.saveAll(transactions)).thenReturn(transactions);

        List<Transaction> savedTransactions = transactionService.saveAllTransactionDetails(transactions);

        assertEquals(transactions.size(), savedTransactions.size());
    }

    @Test
    public void testSaveAllTransactionDetails_CustomerNotFound() {
        Customer customer1 = new Customer();
        customer1.setCustomerId(101L);
        List<Customer> customers = Arrays.asList(customer1);

        Transaction transaction1 = new Transaction();
        transaction1.setCustomerId(101L);
        Transaction transaction2 = new Transaction();
        transaction2.setCustomerId(102L);
        List<Transaction> transactions = Arrays.asList(transaction1, transaction2);

        when(customerService.getAllCustomers()).thenReturn(customers);

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> transactionService.saveAllTransactionDetails(transactions));

        Throwable cause = thrown.getCause();
        assertTrue(cause instanceof CustomerNotFoundException);
        assertEquals("Customer with ID : 102 does not exist...please verify all transactions", cause.getMessage());
    }

    @Test
    public void testSaveAllTransactionDetails_Exception() {
        List<Transaction> transactions = Arrays.asList(new Transaction(), new Transaction());
        when(customerService.getAllCustomers()).thenReturn(List.of(new Customer()));
        when(transactionRepository.saveAll(transactions)).thenThrow(new RuntimeException("Runtime Exception"));

        assertThrows(RuntimeException.class, () -> transactionService.saveAllTransactionDetails(transactions));
    }

    @Test
    public void testGetTransactionDetailsByCustomerId_Success() {
        Long customerId = 1L;
        List<Transaction> transactions = Arrays.asList(new Transaction(), new Transaction());
        when(transactionRepository.findByCustomerId(customerId)).thenReturn(transactions);

        List<Transaction> result = transactionService.getTransactionDetailsByCustomerId(customerId);

        assertEquals(transactions, result);
    }

    @Test
    public void testGetTransactionDetailsByCustomerId_NotFound() {
        Long customerId = 1L;
        when(transactionRepository.findByCustomerId(customerId)).thenReturn(List.of());

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> transactionService.getTransactionDetailsByCustomerId(customerId));

        Throwable cause = thrown.getCause();
        assertTrue(cause instanceof TransactionNotFoundException);
        assertEquals("No transactions found for the given customer Id : 1", cause.getMessage());
    }

    @Test
    public void testGetTransactionDetailsByCustomerId_Exception() {
        Long customerId = 1L;
        when(transactionRepository.findByCustomerId(customerId)).thenThrow(new RuntimeException("Runtime Exception"));

        assertThrows(RuntimeException.class, () -> transactionService.getTransactionDetailsByCustomerId(customerId));
    }

    @Test
    public void testGetTransactionDetailsByTransactionId_Success() {
        Long transactionId = 1L;
        Transaction transaction = new Transaction();
        transaction.setTransactionId(transactionId);
        when(transactionRepository.findById(transactionId)).thenReturn(Optional.of(transaction));

        Transaction result = transactionService.getTransactionDetailsByTransactionId(transactionId);

        assertEquals(transaction, result);
    }

    @Test
    public void testGetTransactionDetailsByTransactionId_NotFound() {
        Long transactionId = 1L;
        when(transactionRepository.findById(transactionId)).thenReturn(Optional.empty());

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> transactionService.getTransactionDetailsByTransactionId(transactionId));

        Throwable cause = thrown.getCause();
        assertTrue(cause instanceof TransactionNotFoundException);
        assertEquals("No transactions found for the given transaction Id : 1", cause.getMessage());
    }

    @Test
    public void testGetTransactionDetailsByTransactionId_Exception() {
        Long transactionId = 1L;
        when(transactionRepository.findById(transactionId)).thenThrow(new RuntimeException("Runtime Exception"));

        assertThrows(RuntimeException.class, () -> transactionService.getTransactionDetailsByTransactionId(transactionId));
    }

    @Test
    public void testDeleteTransactionDetails_Success() {
        Long transactionId = 1L;

        doNothing().when(transactionRepository).deleteById(transactionId);

        String result = transactionService.deleteTransactionDetails(transactionId);

        assertEquals("Transaction details has been deleted !!!", result);
    }

    @Test
    public void testDeleteTransactionDetails_Exception() {
        Long transactionId = 1L;
        doThrow(new RuntimeException("Runtime Exception")).when(transactionRepository).deleteById(transactionId);

        assertThrows(RuntimeException.class, () -> transactionService.deleteTransactionDetails(transactionId));
    }
}
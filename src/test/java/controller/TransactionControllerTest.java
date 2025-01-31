package controller;
import com.reward.points.controller.TransactionController;
import com.reward.points.entity.Customer;
import com.reward.points.entity.Transaction;
import com.reward.points.service.CustomerService;
import com.reward.points.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TransactionControllerTest {

    @InjectMocks
    private TransactionController transactionController;

    @Mock
    private TransactionService transactionService;

    @Mock
    private CustomerService customerService;

    @Mock
    private Logger logger;

    @Test
    public void testSaveTransactionDetails_Success() {
        Transaction transaction = new Transaction();
        transaction.setCustomerId(1L);
        transaction.setTransactionAmount(100.0);

        when(customerService.getRegisteredCustomerDetailsByCustomerId(transaction.getCustomerId())).thenReturn(new Customer());
        when(transactionService.saveTransactionDetails(transaction)).thenReturn("Transaction saved successfully");

        ResponseEntity<String> response = transactionController.saveTransactionDetails(transaction);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Transaction saved successfully", response.getBody());
    }

    @Test
    public void testSaveTransactionDetails_CustomerNotFound() {
        Transaction transaction = new Transaction();
        transaction.setCustomerId(1L);
        when(customerService.getRegisteredCustomerDetailsByCustomerId(transaction.getCustomerId())).thenReturn(null);

        transactionController.saveTransactionDetails(transaction);

    }

    @Test
    public void testSaveAllTransactionDetails_Success() {
        List<Transaction> transactions = Arrays.asList(new Transaction(), new Transaction());
        when(transactionService.saveAllTransactionDetails(transactions)).thenReturn(transactions);

        ResponseEntity<List<Transaction>> response = transactionController.saveAllTransactionDetails(transactions);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(transactions, response.getBody());
    }

    @Test
    public void testGetTransactionDetailsByCustomerId_Success() {
        Long customerId = 1L;
        List<Transaction> transactions = Arrays.asList(new Transaction(), new Transaction());
        when(transactionService.getTransactionDetailsByCustomerId(customerId)).thenReturn(transactions);

        ResponseEntity<List<Transaction>> response = transactionController.getTransactionDetailsByCustomerId(customerId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(transactions, response.getBody());
    }

    @Test
    public void testUpdateTransactionDetails_Success() {
        Long transactionId = 1L;
        Transaction transaction = new Transaction();
        transaction.setCustomerId(2L);
        transaction.setTransactionAmount(200.0);

        Transaction existingTransaction = new Transaction();
        existingTransaction.setTransactionId(transactionId);
        existingTransaction.setCustomerId(1L);

        when(transactionService.getTransactionDetailsByTransactionId(transactionId)).thenReturn(existingTransaction);
        when(customerService.getRegisteredCustomerDetailsByCustomerId(transaction.getCustomerId())).thenReturn(new Customer());
        when(transactionService.saveTransactionDetails(any(Transaction.class))).thenReturn("Transaction updated successfully");

        ResponseEntity<String> response = transactionController.updateTransactionDetails(transaction, transactionId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Transaction updated successfully", response.getBody());
    }


    @Test
    public void testDeleteTransactionDetails_Success() {
        Long transactionId = 1L;
        Transaction transaction = new Transaction();
        transaction.setTransactionId(transactionId);

        when(transactionService.getTransactionDetailsByTransactionId(transactionId)).thenReturn(transaction);
        when(transactionService.deleteTransactionDetails(transactionId)).thenReturn("Transaction deleted successfully");

        ResponseEntity<String> response = transactionController.deleteTransactionDetails(transactionId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Transaction deleted successfully", response.getBody());
    }

    @Test
    public void testDeleteTransactionDetails_TransactionNotFound() {
        Long transactionId = 1L;
        when(transactionService.getTransactionDetailsByTransactionId(transactionId)).thenReturn(null);
        
        transactionController.deleteTransactionDetails(transactionId);
    }


}
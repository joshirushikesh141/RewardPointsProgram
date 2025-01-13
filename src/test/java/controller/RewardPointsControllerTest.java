package controller;

import com.reward.points.constants.Constants;
import com.reward.points.controller.RewardPointsController;
import com.reward.points.entity.Customer;
import com.reward.points.entity.Transaction;
import com.reward.points.model.Rewards;
import com.reward.points.service.RewardPointsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class RewardPointsControllerTest {

    @InjectMocks
    private RewardPointsController rewardPointsController;

    @Mock
    private RewardPointsService rewardPointsService;

    private Customer customer;
    private Transaction transaction;

    @BeforeEach
    void setUp() {
        customer = new Customer(1L, "John Doe", "john@example.com");
        transaction = new Transaction(1L, 1L, new Date(), 100.0);
    }

    // Test cases for Customer
    
    @Test
    void testCustomerRegistration_Success() {
        Mockito.when(rewardPointsService.saveCustomerDetails(Mockito.any(Customer.class))).thenReturn("Customer saved successfully");
        ResponseEntity<String> response = rewardPointsController.customerRegistration(customer);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Customer saved successfully", response.getBody());
    }

    @Test
    void testCustomerRegistration_Failure() {
        Mockito.when(rewardPointsService.saveCustomerDetails(Mockito.any(Customer.class)))
               .thenThrow(new RuntimeException("Something went wrong"));
        ResponseEntity<String> response = rewardPointsController.customerRegistration(customer);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(Constants.SomethingWentWrong, response.getBody());
    }

    @Test
    void testGetRegisteredCustomerDetailsByCustomerId_Success() {
        Mockito.when(rewardPointsService.getRegisteredCustomerDetailsByCustomerId(Mockito.anyLong())).thenReturn(customer);
        ResponseEntity<Customer> response = rewardPointsController.getRegisteredCustomerDetailsByCustomerId(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(customer, response.getBody());
    }

    @Test
    void testGetRegisteredCustomerDetailsByCustomerId_Failure() {
        Mockito.when(rewardPointsService.getRegisteredCustomerDetailsByCustomerId(Mockito.anyLong()))
               .thenThrow(new RuntimeException("Customer not found"));
        ResponseEntity<Customer> response = rewardPointsController.getRegisteredCustomerDetailsByCustomerId(999L);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testUpdateCustomerDetails_Success() {
        Customer updatedCustomer = new Customer(1L, "John Smith", "johnsmith@example.com");
        Mockito.when(rewardPointsService.getRegisteredCustomerDetailsByCustomerId(Mockito.anyLong())).thenReturn(customer);
        Mockito.when(rewardPointsService.saveCustomerDetails(Mockito.any(Customer.class))).thenReturn("Customer updated successfully");
        ResponseEntity<String> response = rewardPointsController.updateCustomerDetails(updatedCustomer, 1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Customer updated successfully", response.getBody());
    }

    @Test
    void testUpdateCustomerDetails_Failure() {
        Customer updatedCustomer = new Customer(1L, "John Smith", "johnsmith@example.com");
        Mockito.when(rewardPointsService.getRegisteredCustomerDetailsByCustomerId(Mockito.anyLong()))
               .thenThrow(new RuntimeException("Customer not found"));

        ResponseEntity<String> response = rewardPointsController.updateCustomerDetails(updatedCustomer, 999L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(Constants.SomethingWentWrong, response.getBody());
    }

    @Test
    void testDeleteCustomerDetails_Success() {
        Mockito.when(rewardPointsService.getRegisteredCustomerDetailsByCustomerId(Mockito.anyLong())).thenReturn(customer);
        Mockito.when(rewardPointsService.deleteCustomerDetails(Mockito.anyLong())).thenReturn("Customer deleted successfully");

        ResponseEntity<String> response = rewardPointsController.deleteCustomerDetails(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Customer deleted successfully", response.getBody());
    }

    @Test
    void testDeleteCustomerDetails_Failure() {
        Mockito.when(rewardPointsService.getRegisteredCustomerDetailsByCustomerId(Mockito.anyLong()))
               .thenThrow(new RuntimeException("Customer not found"));

        ResponseEntity<String> response = rewardPointsController.deleteCustomerDetails(999L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(Constants.SomethingWentWrong, response.getBody());
    }

    // Test cases for Transaction
    
    @Test
    void testSaveTransactionDetails_Success() {
        Mockito.when(rewardPointsService.saveTransactionDetails(Mockito.any(Transaction.class))).thenReturn("Transaction saved successfully");

        ResponseEntity<String> response = rewardPointsController.saveTransactionDetails(transaction);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Transaction saved successfully", response.getBody());
    }

    @Test
    void testSaveTransactionDetails_Failure() {
        Mockito.when(rewardPointsService.saveTransactionDetails(Mockito.any(Transaction.class)))
               .thenThrow(new RuntimeException("Something went wrong"));

        ResponseEntity<String> response = rewardPointsController.saveTransactionDetails(transaction);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(Constants.SomethingWentWrong, response.getBody());
    }

    @Test
    void testSaveAllTransactionDetails_Success() {
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);

        Mockito.when(rewardPointsService.saveAllTransactionDetails(Mockito.anyList())).thenReturn(transactions);
        ResponseEntity<List<Transaction>> response = rewardPointsController.saveAllTransactionDetails(transactions);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(transactions, response.getBody());
    }

    @Test
    void testSaveAllTransactionDetails_Failure() {
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);

        Mockito.when(rewardPointsService.saveAllTransactionDetails(Mockito.anyList()))
               .thenThrow(new RuntimeException("Bulk save failed"));
        ResponseEntity<List<Transaction>> response = rewardPointsController.saveAllTransactionDetails(transactions);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody().isEmpty());
    }

    @Test
    void testGetTransactionDetailsByCustomerId_Success() {
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);

        Mockito.when(rewardPointsService.getTransactionDetailsByCustomerId(Mockito.anyLong())).thenReturn(transactions);

        ResponseEntity<List<Transaction>> response = rewardPointsController.getTransactionDetailsByCustomerId(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(transactions, response.getBody());
    }

    @Test
    void testGetTransactionDetailsByCustomerId_Failure() {
        Mockito.when(rewardPointsService.getTransactionDetailsByCustomerId(Mockito.anyLong()))
               .thenThrow(new RuntimeException("No transactions found"));

        ResponseEntity<List<Transaction>> response = rewardPointsController.getTransactionDetailsByCustomerId(999L);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody().isEmpty());
    }

    @Test
    void testUpdateTransactionDetails_Success() {
        Transaction updatedTransaction = new Transaction(1L, 1L, new Date(), 200.0);
        Mockito.when(rewardPointsService.getTransactionDetailsByTransactionId(Mockito.anyLong())).thenReturn(transaction);
        Mockito.when(rewardPointsService.saveTransactionDetails(Mockito.any(Transaction.class))).thenReturn("Transaction updated successfully");

        ResponseEntity<String> response = rewardPointsController.updateTransactionDetails(updatedTransaction, 1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Transaction updated successfully", response.getBody());
    }

    @Test
    void testUpdateTransactionDetails_Failure() {
        Transaction updatedTransaction = new Transaction(1L, 1L, new Date(), 200.0);
        Mockito.when(rewardPointsService.getTransactionDetailsByTransactionId(Mockito.anyLong()))
               .thenThrow(new RuntimeException("Transaction not found"));

        ResponseEntity<String> response = rewardPointsController.updateTransactionDetails(updatedTransaction, 999L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(Constants.SomethingWentWrong, response.getBody());
    }

    @Test
    void testDeleteTransactionDetails_Success() {
        Mockito.when(rewardPointsService.getTransactionDetailsByTransactionId(Mockito.anyLong())).thenReturn(transaction);
        Mockito.when(rewardPointsService.deleteTransactionDetails(Mockito.anyLong())).thenReturn("Transaction deleted successfully");

        ResponseEntity<String> response = rewardPointsController.deleteTransactionDetails(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Transaction deleted successfully", response.getBody());
    }

    @Test
    void testDeleteTransactionDetails_Failure() {
        Mockito.when(rewardPointsService.getTransactionDetailsByTransactionId(Mockito.anyLong()))
               .thenThrow(new RuntimeException("Transaction not found"));

        ResponseEntity<String> response = rewardPointsController.deleteTransactionDetails(999L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(Constants.SomethingWentWrong, response.getBody());
    }

    // Test case for Rewards
    
    @Test
    void testGetRewardsByCustomerId_Success() {
        Rewards rewards = new Rewards();

        Mockito.when(rewardPointsService.getRewardsByCustomerId(Mockito.anyLong())).thenReturn(rewards);
        ResponseEntity<Rewards> response = rewardPointsController.getRewardsByCustomerId(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(rewards, response.getBody());
    }

    @Test
    void testGetRewardsByCustomerId_Failure() {
        Mockito.when(rewardPointsService.getRewardsByCustomerId(Mockito.anyLong()))
               .thenThrow(new RuntimeException("Rewards not found"));

        ResponseEntity<Rewards> response = rewardPointsController.getRewardsByCustomerId(999L);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
    }
}

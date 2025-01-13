package service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.reward.points.entity.Customer;
import com.reward.points.entity.Transaction;
import com.reward.points.exception.CustomerNotFoundException;
import com.reward.points.exception.TransactionNotFoundException;
import com.reward.points.model.Rewards;
import com.reward.points.repository.CustomerRepository;
import com.reward.points.repository.TransactionRepository;
import com.reward.points.service.RewardPointsServiceImpl;


public class RewardPointsServiceImplTest {

    @InjectMocks
    RewardPointsServiceImpl rewardPointsService;
    
    @Mock
    CustomerRepository customerRepository;

    @Mock
    TransactionRepository transactionRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    
    @Test
    void testSaveCustomerDetails() {
        Customer customer = new Customer(1L, "John Doe", "john.doe@example.com");
        Mockito.when(customerRepository.save(Mockito.any(Customer.class))).thenReturn(customer);

        String response = rewardPointsService.saveCustomerDetails(customer);

        assertEquals("Customer has been registered/updated successfully!! with customer ID: 1", response);
    }

    @Test
    void testSaveTransactionDetails() {
        Transaction transaction = new Transaction(1L, 1L, Timestamp.valueOf(LocalDateTime.now()), 150.0);
        Mockito.when(transactionRepository.save(Mockito.any(Transaction.class))).thenReturn(transaction);

        String response = rewardPointsService.saveTransactionDetails(transaction);

        assertEquals("Transaction completed successfully!! with transaction ID: 1", response);
    }

    @Test
    void testSaveAllTransactionDetails() {
        Transaction transaction1 = new Transaction(1L, 1L, Timestamp.valueOf(LocalDateTime.now()), 50.0);
        Transaction transaction2 = new Transaction(2L, 1L, Timestamp.valueOf(LocalDateTime.now()), 200.0);
        List<Transaction> transactions = Arrays.asList(transaction1, transaction2);
        Mockito.when(transactionRepository.saveAll(Mockito.anyList())).thenReturn(transactions);

        List<Transaction> response = rewardPointsService.saveAllTransactionDetails(transactions);

        assertEquals(2, response.size());
        assertEquals(1L, response.get(0).getTransactionId());
        assertEquals(2L, response.get(1).getTransactionId());
    }

    @Test
    void testGetRegisteredCustomerDetailsByCustomerId_Success() {
        Customer customer = new Customer(1L, "John Doe", "john.doe@example.com");
        Mockito.when(customerRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(customer));

        Customer response = rewardPointsService.getRegisteredCustomerDetailsByCustomerId(1L);

        assertNotNull(response);
        assertEquals("John Doe", response.getCustomerName());
    }

    
    @Test
    void testGetRegisteredCustomerDetailsByCustomerId_Failure() {
        Mockito.when(customerRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class, () -> {
            rewardPointsService.getRegisteredCustomerDetailsByCustomerId(999L);
        });
    }

    
    @Test
    void testGetTransactionDetailsByCustomerId() {
        Transaction transaction1 = new Transaction(1L, 1L, Timestamp.valueOf(LocalDateTime.now()), 50.0);
        Transaction transaction2 = new Transaction(2L, 1L, Timestamp.valueOf(LocalDateTime.now()), 150.0);
        List<Transaction> transactions = Arrays.asList(transaction1, transaction2);
        Mockito.when(transactionRepository.findByCustomerId(Mockito.anyLong())).thenReturn(transactions);

        List<Transaction> response = rewardPointsService.getTransactionDetailsByCustomerId(1L);

        assertEquals(2, response.size());
    }

    @Test
    void testDeleteCustomerDetails() {
        Customer customer = new Customer(1L, "John Doe", "john.doe@example.com");
        Mockito.doNothing().when(customerRepository).deleteById(Mockito.anyLong());

        String response = rewardPointsService.deleteCustomerDetails(1L);

        assertEquals("Customer details has been deleted !!!", response);
    }

    
    @Test
    void testGetTransactionDetailsByTransactionId_Success() {
        Transaction transaction = new Transaction(1L, 1L, Timestamp.valueOf(LocalDateTime.now()), 150.0);
        Mockito.when(transactionRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(transaction));

        Transaction response = rewardPointsService.getTransactionDetailsByTransactionId(1L);

        assertNotNull(response);
        assertEquals(150.0, response.getTransactionAmount());
    }

    @Test
    void testGetTransactionDetailsByTransactionId_Failure() {
        Mockito.when(transactionRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        assertThrows(TransactionNotFoundException.class, () -> {
            rewardPointsService.getTransactionDetailsByTransactionId(999L);
        });
    }

    @Test
    void testDeleteTransactionDetails() {
        Mockito.doNothing().when(transactionRepository).deleteById(Mockito.anyLong());

        String response = rewardPointsService.deleteTransactionDetails(1L);

        assertEquals("Transaction details has been deleted !!!", response);
    }

    @Test
    void testGetRewardsByCustomerId_ValidRewards() {
        Long customerId = 1L;
        Timestamp lastMonthTimestamp = Timestamp.valueOf(LocalDateTime.now().minusDays(30));
        Timestamp nowTimestamp = Timestamp.valueOf(LocalDateTime.now());

        List<Transaction> lastMonthTransactions = Arrays.asList(
                new Transaction(1L, customerId, Timestamp.valueOf(LocalDateTime.now().minusDays(5)), 120.0)
        );
        Mockito.when(transactionRepository.findAllByCustomerIdAndTransactionDateBetween(Mockito.anyLong(), Mockito.any(), Mockito.any()))
               .thenReturn(lastMonthTransactions);

        Rewards rewards = rewardPointsService.getRewardsByCustomerId(customerId);

        assertNotNull(rewards);
        assertTrue(rewards.getTotalRewards() > 0);
        assertEquals(customerId, rewards.getCustomerId());
    }

    @Test
    void testGetRewardsByCustomerId_NoTransactions() {
        Long customerId = 1L;
        Mockito.when(transactionRepository.findAllByCustomerIdAndTransactionDateBetween(Mockito.anyLong(), Mockito.any(), Mockito.any()))
               .thenReturn(Collections.emptyList());

        Rewards rewards = rewardPointsService.getRewardsByCustomerId(customerId);

        assertNotNull(rewards);
        assertEquals(0L, rewards.getTotalRewards());
    }

    @Test
    void testCalculateRewards() {
        assertEquals(0L, rewardPointsService.calculateRewards(40.0));
        assertEquals(10L, rewardPointsService.calculateRewards(60.0));
        assertEquals(150L, rewardPointsService.calculateRewards(150.0));
    }

}

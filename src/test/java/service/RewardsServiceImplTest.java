package service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.reward.points.entity.Transaction;
import com.reward.points.model.Rewards;
import com.reward.points.repository.TransactionRepository;
import com.reward.points.service.RewardsServiceImpl;

public class RewardsServiceImplTest {
	@InjectMocks
	RewardsServiceImpl rewardsService;

    @Mock
    TransactionRepository transactionRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
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

        Rewards rewards = rewardsService.getRewardsByCustomerId(customerId);

        assertNotNull(rewards);
        assertTrue(rewards.getTotalRewards() > 0);
        assertEquals(customerId, rewards.getCustomerId());
    }

    @Test
    void testGetRewardsByCustomerId_NoTransactions() {
        Long customerId = 1L;
        Mockito.when(transactionRepository.findAllByCustomerIdAndTransactionDateBetween(Mockito.anyLong(), Mockito.any(), Mockito.any()))
               .thenReturn(Collections.emptyList());

        Rewards rewards = rewardsService.getRewardsByCustomerId(customerId);

        assertNotNull(rewards);
        assertEquals(0L, rewards.getTotalRewards());
    }

    @Test
    void testCalculateRewards() {
        assertEquals(0L, rewardsService.calculateRewards(40.0));
        assertEquals(10L, rewardsService.calculateRewards(60.0));
        assertEquals(150L, rewardsService.calculateRewards(150.0));
    }
}

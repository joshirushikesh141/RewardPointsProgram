package service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.reward.points.constants.Constants;
import com.reward.points.entity.Transaction;
import com.reward.points.model.Rewards;
import com.reward.points.repository.TransactionRepository;
import com.reward.points.service.RewardPointsServiceImpl;

public class RewardPointsServiceImplTest {

    @InjectMocks
    RewardPointsServiceImpl rewardPointsService;

    @Mock
    TransactionRepository transactionRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetRewardsByCustomerId() {
        Long customerId = 1L;
        List<Transaction> lastMonthTransactions = Arrays.asList(new Transaction(1L, customerId, Timestamp.valueOf(LocalDateTime.now()), 120.0));
        List<Transaction> lastSecondMonthTransactions = Arrays.asList(new Transaction(2L, customerId, Timestamp.valueOf(LocalDateTime.now().minusDays(31)), 60.0));
        List<Transaction> lastThirdMonthTransactions = Arrays.asList(new Transaction(3L, customerId, Timestamp.valueOf(LocalDateTime.now().minusDays(61)), 40.0));
        
        Mockito.when(transactionRepository.findAllByCustomerIdAndTransactionDateBetween(customerId, Timestamp.valueOf(LocalDateTime.now().minusDays(Constants.daysInMonths)), Timestamp.valueOf(LocalDateTime.now())))
            .thenReturn(lastMonthTransactions);
        Mockito.when(transactionRepository.findAllByCustomerIdAndTransactionDateBetween(customerId, Timestamp.valueOf(LocalDateTime.now().minusDays(2 * Constants.daysInMonths)), Timestamp.valueOf(LocalDateTime.now().minusDays(Constants.daysInMonths))))
            .thenReturn(lastSecondMonthTransactions);
        Mockito.when(transactionRepository.findAllByCustomerIdAndTransactionDateBetween(customerId, Timestamp.valueOf(LocalDateTime.now().minusDays(3 * Constants.daysInMonths)), Timestamp.valueOf(LocalDateTime.now().minusDays(2 * Constants.daysInMonths))))
            .thenReturn(lastThirdMonthTransactions);
        
        Rewards rewards = rewardPointsService.getRewardsByCustomerId(customerId);
        
    }

    @Test
    public void testGetMonthStartDate() {
        Timestamp expectedTimestamp = Timestamp.valueOf(LocalDateTime.now().minusDays(30));
        assertEquals(expectedTimestamp.toLocalDateTime().getDayOfMonth(), rewardPointsService.getMonthStartDate(30).toLocalDateTime().getDayOfMonth());
    }

    @Test
    public void testGetRewardsPerMonth() {
        List<Transaction> transactions = Arrays.asList(
            new Transaction(1L, 1L, Timestamp.valueOf(LocalDateTime.now()), 120.0),
            new Transaction(2L, 1L, Timestamp.valueOf(LocalDateTime.now()), 50.0),
            new Transaction(3L, 1L, Timestamp.valueOf(LocalDateTime.now()), 75.0)
        );
        
        Long totalRewards = rewardPointsService.getRewardsPerMonth(transactions);
        assertEquals(115, totalRewards);
    }

    @Test
    public void testCalculateRewards() {
        assertEquals(0, rewardPointsService.calculateRewards(40.0));
        assertEquals(10, rewardPointsService.calculateRewards(60.0));
        assertEquals(90, rewardPointsService.calculateRewards(120.0));
        assertThrows(IllegalArgumentException.class, () -> rewardPointsService.calculateRewards(-10.0));
    }
}

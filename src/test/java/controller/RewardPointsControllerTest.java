package controller;

import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.reward.points.controller.RewardPointsController;
import com.reward.points.entity.Customer;
import com.reward.points.model.Rewards;
import com.reward.points.repository.CustomerRepository;
import com.reward.points.service.RewardPointsService;

class RewardPointsControllerTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private RewardPointsService rewardPointsService;

    @InjectMocks
    private RewardPointsController rewardPointsController;
    
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    
    @Test
    public void testSaveCustomerDetails() throws Exception {
        Customer customer = new Customer();
        customer.setCustomerName("Rushikesh Joshi");
        when(customerRepository.save(Mockito.any(Customer.class))).thenReturn(customer);
    }

    @Test
    public void testGetCustomerDetailsByCustomerId() throws Exception {
        Customer customer = new Customer();
        customer.setCustomerName("Rushikesh Joshi");
        when(customerRepository.findById(1L)).thenReturn(java.util.Optional.of(customer));
    }

    @Test
    public void testGetRewardsByCustomerId() throws Exception {
        Rewards rewards = new Rewards();
        rewards.setCustomerId(1L);
        rewards.setLastMonthRewardPoints(100);
        rewards.setLastSecondMonthRewardPoints(150);
        rewards.setLastThirdMonthRewardPoints(200);
        rewards.setTotalRewards(450);
        
        when(customerRepository.findById(1L)).thenReturn(java.util.Optional.of(new Customer()));
        when(rewardPointsService.getRewardsByCustomerId(1L)).thenReturn(rewards);
    }

    @Test
    public void testGetCustomerDetailsByCustomerId_NotFound() throws Exception {
        when(customerRepository.findById(1L)).thenReturn(java.util.Optional.empty());
    }

    @Test
    public void testGetRewardsByCustomerId_NotFound() throws Exception {
        when(customerRepository.findById(1L)).thenReturn(java.util.Optional.empty());
    }

}

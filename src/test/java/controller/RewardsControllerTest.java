package controller;
import com.reward.points.controller.RewardsController;
import com.reward.points.entity.Customer;
import com.reward.points.model.Rewards;
import com.reward.points.service.CustomerService;
import com.reward.points.service.RewardsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RewardsControllerTest {

    @InjectMocks
    private RewardsController rewardsController;

    @Mock
    private RewardsService rewardsService;

    @Mock
    private CustomerService customerService;

    @Mock
    private Logger logger;

    @Test
    public void testGetRewardsByCustomerId_Success() {
        Long customerId = 1L;
        Customer customer = new Customer();
        customer.setCustomerId(customerId);

        Rewards rewards = new Rewards();
        rewards.setCustomerId(customerId);

        when(customerService.getRegisteredCustomerDetailsByCustomerId(customerId)).thenReturn(customer);
        when(rewardsService.getRewardsByCustomerId(customerId)).thenReturn(rewards);

        ResponseEntity<Rewards> response = rewardsController.getRewardsByCustomerId(customerId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(rewards, response.getBody());
    }

    @Test
    public void testGetRewardsByCustomerId_CustomerNotFound() {
        Long customerId = 1L;
        when(customerService.getRegisteredCustomerDetailsByCustomerId(customerId)).thenReturn(null);
            rewardsController.getRewardsByCustomerId(customerId);
    }


    @Test
    public void testGetRewardsByCustomerId_RewardsNotFound() {
        Long customerId = 1L;
        Customer customer = new Customer();
        customer.setCustomerId(customerId);

        when(customerService.getRegisteredCustomerDetailsByCustomerId(customerId)).thenReturn(customer);
        when(rewardsService.getRewardsByCustomerId(customerId)).thenReturn(null);

        ResponseEntity<Rewards> response = rewardsController.getRewardsByCustomerId(customerId);

        assertEquals(HttpStatus.OK, response.getStatusCode()); 
    }

}
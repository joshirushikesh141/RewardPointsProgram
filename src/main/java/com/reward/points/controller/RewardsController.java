package com.reward.points.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.reward.points.entity.Customer;
import com.reward.points.model.Rewards;
import com.reward.points.service.CustomerService;
import com.reward.points.service.RewardsService;

/**
 * RewardsController class for handling rewards related HTTP requests.
 * Provides endpoints for creating, retrieving, updating, and deleting the details.
 * 
 * @see rewardsService
 * @see customerService
 */

@RestController
@RequestMapping("/api/rewards")
public class RewardsController {
		
		@Autowired
		RewardsService rewardsService;
		
		@Autowired
		CustomerService customerService;
		
		private static final Logger logger = LoggerFactory.getLogger(RewardsController.class);
		
		/**
	    * Get Rewards By customerId -- we can get to know the earned rewards by the customers.
	    * @param customerId The customerId received from the path variable.
	    * @return ResponseEntity with a Rewards Object and HTTP status OK if the Rewards details are successfully fetched.
	    */
	    @GetMapping("/rewardsByCustomer/{customerId}")
	    public ResponseEntity<Rewards> getRewardsByCustomerId(@PathVariable Long customerId){
	    	Customer customer = customerService.getRegisteredCustomerDetailsByCustomerId(customerId); // If customer Id not found will through exception
	        logger.info("getRewardsByCustomerId customer {}",customer);
	        
	        Rewards customerRewards = rewardsService.getRewardsByCustomerId(customerId);
	        logger.info("customerRewards {}",customerRewards);
	        
	        return new ResponseEntity<>(customerRewards,HttpStatus.OK);
	    }
}

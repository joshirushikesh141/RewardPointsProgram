package com.reward.points.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.reward.points.entity.Customer;
import com.reward.points.entity.Transaction;
import com.reward.points.model.Rewards;
import com.reward.points.repository.CustomerRepository;
import com.reward.points.repository.TransactionRepository;
import com.reward.points.service.RewardPointsService;

@RestController
@RequestMapping("/api")
public class RewardPointsController {
    @Autowired
    RewardPointsService rewardPointsService;

    @Autowired
    CustomerRepository customerRepository;
    
    @Autowired
    TransactionRepository transactionRepository;
    
    private static final Logger logger = LoggerFactory.getLogger(RewardPointsController.class);
    
    @PostMapping("/saveCustomerDetails")
    public ResponseEntity<Customer> saveCustomerDetails(@RequestBody Customer customer){
    	Customer save = new Customer();
    	try {
    		save  = rewardPointsService.saveCustomerDetails(customer);
    		logger.info("save {}",save);
		} catch (Exception e) {
			logger.error("exception in saveCustomerDetails {}",e);
		}
        return new ResponseEntity<>(save,HttpStatus.OK);
    }
    
    @PutMapping("/saveTransactionDetails")
    public ResponseEntity<Transaction> saveTransactionDetails(@RequestBody Transaction transaction){
    	Transaction save = new Transaction();
    	try {
    		save = rewardPointsService.saveTransactionDetails(transaction);
    		logger.info("save {}",save);
		} catch (Exception e) {
			logger.error("exception in saveTransactionDetails {}",e);
		}
        return new ResponseEntity<>(save,HttpStatus.OK);
    }
    
    @PostMapping("/saveAllTransactionDetails")
    public ResponseEntity<List<Transaction>> saveAllTransactionDetails(@RequestBody List<Transaction> transaction){
    	List<Transaction> save = new ArrayList<>();
    	try {
    		save = rewardPointsService.saveAllTransactionDetails(transaction);
    		logger.info("save {}",save);
		} catch (Exception e) {
			logger.error("exception in saveAllTransactionDetails {}",e);
		}
        return new ResponseEntity<>(save,HttpStatus.OK);
    }
	
    @GetMapping("/getCustomerDetails/{customerId}")
    public ResponseEntity<Customer> getCustomerDetailsByCustomerId(@PathVariable Long customerId){
    	Customer customer = new Customer();
    	try {
    		customer = rewardPointsService.getCustomerDetailsByCustomerId(customerId);
    		 logger.info("getCustomerDetailsByCustomerId customer {}",customer);
		} catch (Exception e) {
			logger.error("exception in getCustomerDetailsByCustomerId {}",e);
		}
        return new ResponseEntity<>(customer,HttpStatus.OK);
    }
    
    @PostMapping("/updateCustomerDetails/{customerId}")
    public void getCustomerDetails(@RequestBody Customer customer,@PathVariable Long customerId){
    	Customer customerDb = rewardPointsService.getCustomerDetailsByCustomerId(customerId);
    	if(customerDb != null) {
    		customerDb.setCustomerName(customer.getCustomerName());
    		rewardPointsService.saveCustomerDetails(customerDb);
    	}
    }
    
    @GetMapping("/getTransactionDetailsByCustomerId/{customerId}")
    public ResponseEntity<List<Transaction>> getTransactionDetailsByCustomerId(@PathVariable Long customerId){
    	 List<Transaction> findByCustomerId = new ArrayList<>();
		 try {
			 findByCustomerId = rewardPointsService.getTransactionDetailsByCustomerId(customerId);
		     logger.info("getTransactionDetailsByCustomerId transaction {}",findByCustomerId);
		} catch (Exception e) {
			logger.error("exception in findByCustomerId {}",e);
		}
        return new ResponseEntity<>(findByCustomerId,HttpStatus.OK);
    }

    @GetMapping("/rewards/{customerId}")
    public ResponseEntity<Rewards> getRewardsByCustomerId(@PathVariable Long customerId){
    	Customer customer = rewardPointsService.getCustomerDetailsByCustomerId(customerId);
        logger.info("getRewardsByCustomerId customer {}",customer);
        
        Rewards customerRewards = new Rewards();
        try {
        	customerRewards = rewardPointsService.getRewardsByCustomerId(customerId);
        	logger.info("customerRewards {}",customerRewards);
		} catch (Exception e) {
			logger.error("exception in getRewardsByCustomerId {}",e);
		}
        return new ResponseEntity<>(customerRewards,HttpStatus.OK);
    }
}

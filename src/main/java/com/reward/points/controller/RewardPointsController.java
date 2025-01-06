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
    		save= customerRepository.save(customer);
    		logger.info("save {}",save);
		} catch (Exception e) {
			logger.error("exception in saveCustomerDetails {}",e);
		}
        return new ResponseEntity<>(save,HttpStatus.OK);
    }
    
    @PostMapping("/saveTransactionDetails")
    public ResponseEntity<Transaction> saveTransactionDetails(@RequestBody Transaction transaction){
    	Transaction save = new Transaction();
    	try {
    		save= transactionRepository.save(transaction);
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
    		save= transactionRepository.saveAll(transaction);
    		logger.info("save {}",save);
		} catch (Exception e) {
			logger.error("exception in saveAllTransactionDetails {}",e);
		}
        return new ResponseEntity<>(save,HttpStatus.OK);
    }
    @GetMapping("/getCustomerDetails/{customerId}")
    public ResponseEntity<Customer> getCustomerDetailsByCustomerId(@PathVariable("customerId") Long customerId){
        Optional<Customer> customer = customerRepository.findById(customerId);
        logger.info("getCustomerDetailsByCustomerId customer {}",customer);
        Customer customerNew = customer.orElseThrow(()-> new RuntimeException("Customer Not Found"));
        return new ResponseEntity<>(customerNew,HttpStatus.OK);
    }
    
    @GetMapping("/getTransactionDetailsByCustomerId/{customerId}")
    public ResponseEntity<List<Transaction>> getTransactionDetailsByCustomerId(@PathVariable("customerId") Long customerId){
        List<Transaction> findByCustomerId = transactionRepository.findByCustomerId(customerId);
        logger.info("getTransactionDetailsByCustomerId transaction {}",findByCustomerId);
        return new ResponseEntity<>(findByCustomerId,HttpStatus.OK);
    }

    @GetMapping("/rewards/{customerId}")
    public ResponseEntity<Rewards> getRewardsByCustomerId(@PathVariable("customerId") Long customerId){
        Optional<Customer> customer = customerRepository.findById(customerId);
        Customer customerNew = customer.orElseThrow(()-> new RuntimeException("Customer Not Found"));
        logger.info("getRewardsByCustomerId customerNew {}",customerNew);
        
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

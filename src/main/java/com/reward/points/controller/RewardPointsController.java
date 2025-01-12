package com.reward.points.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.reward.points.constants.Constants;
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
    
    // Save Customer Details
    @PostMapping("/customerRegistration")
    public ResponseEntity<String> customerRegistration(@RequestBody Customer customer){
    	String save = "";
    	try {
    		 save = rewardPointsService.saveCustomerDetails(customer);
    		logger.info("save {}",save);
		} catch (Exception e) {
			logger.error("exception in customerRegistration {}",e);
			return new ResponseEntity<>(Constants.SomethingWentWrong,HttpStatus.INTERNAL_SERVER_ERROR);
		}
        return new ResponseEntity<>(save,HttpStatus.OK);
    }
    
     // Get Customer Details By customerId
    @GetMapping("/getRegisteredCustomerDetailsById/{customerId}")
    public ResponseEntity<Customer> getRegisteredCustomerDetailsByCustomerId(@PathVariable Long customerId){
    	Customer customer = new Customer();
    	try {
    		customer = rewardPointsService.getRegisteredCustomerDetailsByCustomerId(customerId);
    		logger.info("getRegisteredCustomerDetailsByCustomerId customer {}",customer);
		} catch (Exception e) {
			logger.error("exception in getRegisteredCustomerDetailsByCustomerId {}",e);
			return new ResponseEntity<>(customer,HttpStatus.INTERNAL_SERVER_ERROR);
		}
        return new ResponseEntity<>(customer,HttpStatus.OK);
    }
    
    // Update Customer Details By customerId
    @PutMapping("/updateCustomerDetails/{customerId}")
    public ResponseEntity<String> updateCustomerDetails(@RequestBody Customer customer,@PathVariable Long customerId){
    	Customer customerDb = new Customer();
    	String saveCustomerDetails ="";
    	try {
    		customerDb = rewardPointsService.getRegisteredCustomerDetailsByCustomerId(customerId);
        	if(customerDb != null) {
        		customerDb.setCustomerName(customer.getCustomerName());
        		saveCustomerDetails = rewardPointsService.saveCustomerDetails(customerDb);
        	}
		} catch (Exception e) {
			logger.error("exception in updateCustomerDetails {}",e);
			return new ResponseEntity<>(Constants.SomethingWentWrong,HttpStatus.NOT_FOUND);
		}
    	return new ResponseEntity<>(saveCustomerDetails,HttpStatus.OK);
    }
    
    // Delete Customer Details By customerId
    @DeleteMapping("/deleteCustomerDetails/{customerId}")
    public ResponseEntity<String> deleteCustomerDetails(@PathVariable Long customerId){
    	Customer customerObj = new Customer();
    	String deleteCustomer = "";
    	try {
    		customerObj = rewardPointsService.getRegisteredCustomerDetailsByCustomerId(customerId);
        	if(customerObj != null) {
        		deleteCustomer = rewardPointsService.deleteCustomerDetails(customerId);
        	}
		} catch (Exception e) {
			logger.error("exception in deleteCustomerDetails {}",e);
			return new ResponseEntity<>(Constants.SomethingWentWrong,HttpStatus.NOT_FOUND);
		}
    	return new ResponseEntity<>(deleteCustomer,HttpStatus.OK);
    }
    
    
    
    // Save Transaction Details -- Object
    @PostMapping("/saveTransactionDetails")
    public ResponseEntity<String> saveTransactionDetails(@RequestBody Transaction transaction){
    	String save = "";
    	try {
    		save = rewardPointsService.saveTransactionDetails(transaction);
    		logger.info("save {}",save);
		} catch (Exception e) {
			logger.error("exception in saveTransactionDetails {}",e);
			return new ResponseEntity<>(Constants.SomethingWentWrong,HttpStatus.INTERNAL_SERVER_ERROR);
		}
        return new ResponseEntity<>(save,HttpStatus.OK);
    }
    
    // Save Transaction Details List -- List of Object
    @PostMapping("/saveAllTransactionDetails")
    public ResponseEntity<List<Transaction>> saveAllTransactionDetails(@RequestBody List<Transaction> transaction){
    	List<Transaction> save = new ArrayList<>();
    	try {
    		save = rewardPointsService.saveAllTransactionDetails(transaction);
    		logger.info("save {}",save);
		} catch (Exception e) {
			logger.error("exception in saveAllTransactionDetails {}",e);
			return new ResponseEntity<>(save,HttpStatus.INTERNAL_SERVER_ERROR);
		}
        return new ResponseEntity<>(save,HttpStatus.OK);
    }
    
    // Get Transaction Details By customerId
    @GetMapping("/getTransactionDetailsByCustomerId/{customerId}")
    public ResponseEntity<List<Transaction>> getTransactionDetailsByCustomerId(@PathVariable Long customerId){
    	 List<Transaction> findByCustomerId = new ArrayList<>();
		 try {
			 findByCustomerId = rewardPointsService.getTransactionDetailsByCustomerId(customerId);
		     logger.info("getTransactionDetailsByCustomerId transaction {}",findByCustomerId);
		} catch (Exception e) {
			logger.error("exception in findByCustomerId {}",e);
			return new ResponseEntity<>(findByCustomerId,HttpStatus.INTERNAL_SERVER_ERROR);
		}
        return new ResponseEntity<>(findByCustomerId,HttpStatus.OK);
    }
    
    // Update Transaction Details -- Object
    @PutMapping("/updateTransactionDetails/{transactionId}")
    public ResponseEntity<String> updateTransactionDetails(@RequestBody Transaction transaction,@PathVariable Long transactionId){
    	Transaction transactionObj = new Transaction();
    	String save = "";
    	try {
    		transactionObj = rewardPointsService.getTransactionDetailsByTransactionId(transactionId);
    		if(transactionObj != null) {
    			transactionObj.setCustomerId(transaction.getCustomerId());
    			transactionObj.setTransactionAmount(transaction.getTransactionAmount());
    			transactionObj.setTransactionDate(transaction.getTransactionDate());
	    		save = rewardPointsService.saveTransactionDetails(transactionObj);
	    		logger.info("save {}",save);
    		}
		} catch (Exception e) {
			logger.error("exception in updateTransactionDetails {}",e);
			return new ResponseEntity<>(Constants.SomethingWentWrong,HttpStatus.NOT_FOUND);
		}
        return new ResponseEntity<>(save,HttpStatus.OK);
    }
    
    // Delete Transaction Details By customerId
    @DeleteMapping("/deleteTransactionDetails/{transactionId}")
    public ResponseEntity<String> deleteTransactionDetails(@PathVariable Long transactionId){
    	Transaction transactionObj = new Transaction();
    	String deleteTransaction = "";
    	try {
    		transactionObj = rewardPointsService.getTransactionDetailsByTransactionId(transactionId);
        	if(transactionObj != null) {
        		deleteTransaction = rewardPointsService.deleteTransactionDetails(transactionId);
        	}
		} catch (Exception e) {
			logger.error("exception in deleteTransactionDetails {}",e);
			return new ResponseEntity<>(Constants.SomethingWentWrong,HttpStatus.NOT_FOUND);
		}
    	return new ResponseEntity<>(deleteTransaction,HttpStatus.OK);
    }

    // Get Rewards By customerId
    @GetMapping("/rewards/{customerId}")
    public ResponseEntity<Rewards> getRewardsByCustomerId(@PathVariable Long customerId){
    	Customer customer = rewardPointsService.getRegisteredCustomerDetailsByCustomerId(customerId);
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

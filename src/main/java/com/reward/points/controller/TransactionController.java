package com.reward.points.controller;

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

import com.reward.points.entity.Customer;
import com.reward.points.entity.Transaction;
import com.reward.points.service.CustomerService;
import com.reward.points.service.TransactionService;

/**
 * TransactionController class for handling transactions related HTTP requests.
 * Provides endpoints for creating, retrieving, updating, and deleting the details.
 * 
 * @see transactionService
 * @see customerService
 */

@RestController
@RequestMapping("/api/transaction")
public class TransactionController {
		
		@Autowired
		TransactionService transactionService;
		
		@Autowired
		CustomerService customerService;
	
		private static final Logger logger = LoggerFactory.getLogger(TransactionController.class);
		
		
		/**
	    * Save Transaction Details -- transaction done by customer saved here.
	    * @param transaction The Transaction object received from the request body.
	    * @return ResponseEntity with a success message and HTTP status OK if the transaction details are successfully saved.
	    */
	    @PostMapping("/saveTransactionDetails")
	    public ResponseEntity<String> saveTransactionDetails(@RequestBody Transaction transaction){
	        if(transaction.getTransactionId() == null || transaction.getTransactionId() == 0) {
	        	customerService.getRegisteredCustomerDetailsByCustomerId(transaction.getCustomerId());  // if transaction customerId not found will through exception
		        return new ResponseEntity<>(transactionService.saveTransactionDetails(transaction),HttpStatus.OK);
			}else {
				throw new IllegalArgumentException("Please don't provide transactionId...It will be auto-generated & will provide you the same!!");
			}
	    }

	    
	    /**
	    * Save Transaction Details List -- list of transactions at a time saved here -- this is for application testing purpose.
	    * @param transaction The List of Transaction objects received from the request body.
	    * @return ResponseEntity with a List of Transactions and HTTP status OK if the transaction list details are successfully saved.
	    */
	    @PostMapping("/saveAllTransactionDetails")
	    public ResponseEntity<List<Transaction>> saveAllTransactionDetails(@RequestBody List<Transaction> transaction){
	        return new ResponseEntity<>(transactionService.saveAllTransactionDetails(transaction),HttpStatus.OK);
	    }

	    
	    /**
	    * Get Transaction Details By customerId -- fetch transaction list by customerid.
	    * @param customerId The customerId received from the path variable.
	    * @return ResponseEntity with a transaction list and HTTP status OK if the transaction details are successfully fetched.
	    */
	    @GetMapping("/getTransactionDetailsByCustomerId/{customerId}")
	    public ResponseEntity<List<Transaction>> getTransactionDetailsByCustomerId(@PathVariable Long customerId){
	        return new ResponseEntity<>(transactionService.getTransactionDetailsByCustomerId(customerId),HttpStatus.OK);
	    }


	    /**
	    * Update Transaction Details -- previously done transaction can be updated here.
	    * @param transaction The Transaction object received from the request body.
	    * @param transactionId The transactionId received from the path variable.
	    * @return ResponseEntity with a success message and HTTP status OK if the transaction details are successfully updated.
	    */
	    @PutMapping("/updateTransactionDetails/{transactionId}")
	    public ResponseEntity<String> updateTransactionDetails(@RequestBody Transaction transaction,@PathVariable Long transactionId){
	    	String save = "";
	    	Transaction transactionObj = transactionService.getTransactionDetailsByTransactionId(transactionId); // If transaction Id not found will through exception
	    	logger.info("transactionObj available : {}",transactionObj.getTransactionId());
	    	
	    	Customer isAvailableCustomerId = customerService.getRegisteredCustomerDetailsByCustomerId(transaction.getCustomerId()); // If transaction customer Id not found will through exception
	    	logger.info("isAvailableCustomerId available : {}",isAvailableCustomerId.getCustomerId());

    		if(transactionObj != null) {
    			transactionObj.setCustomerId(transaction.getCustomerId());
    			transactionObj.setTransactionAmount(transaction.getTransactionAmount());
    			transactionObj.setTransactionDate(transaction.getTransactionDate());
	    		save = transactionService.saveTransactionDetails(transactionObj);
	    		logger.info("save {}",save);
    		}
	        return new ResponseEntity<>(save,HttpStatus.OK);
	    }

	    
	    /**
	    * Delete Transaction Details By customerId -- previously done transaction can be deleted here -- not recommended to delete.
	    * @param transactionId The transactionId received from the path variable.
	    * @return ResponseEntity with a success message and HTTP status OK if the transaction details are successfully deleted.
	    */
	    @DeleteMapping("/deleteTransactionDetails/{transactionId}")
	    public ResponseEntity<String> deleteTransactionDetails(@PathVariable Long transactionId){
	    	String deleteTransaction = "";
    		Transaction transactionObj = transactionService.getTransactionDetailsByTransactionId(transactionId); // If transaction Id not found will through exception
        	if(transactionObj != null) {
        		deleteTransaction = transactionService.deleteTransactionDetails(transactionId);
        	}
	    	return new ResponseEntity<>(deleteTransaction,HttpStatus.OK);
	    }
}

package com.reward.points.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.reward.points.entity.Customer;
import com.reward.points.entity.Transaction;
import com.reward.points.exception.CustomerNotFoundException;
import com.reward.points.exception.TransactionNotFoundException;
import com.reward.points.model.Rewards;
import com.reward.points.repository.TransactionRepository;

/**
 * This is an implementation class of transaction service interface.
 * The business logic is written here for transactions.
 *
 * @see Customer
 * @see Transaction
 * @see Rewards
 * @see CustomerService
 * @see TransactionRepository
 */

@Service
public class TransactionServiceImpl implements TransactionService{
	
	@Autowired
	TransactionRepository transactionRepository;
	
	@Autowired
	CustomerService customerService;

	@Override
	public String saveTransactionDetails(Transaction transaction) {
		try {
			Transaction save = transactionRepository.save(transaction);
			return "Transaction completed/updated successfully!! with transaction ID: "+save.getTransactionId();  // transaction updation not recommended 
		} catch (Exception e) {
			 throw new RuntimeException("EXCEPTION: " + e.getMessage(), e);
		}
	}

	@Override
	public List<Transaction> saveAllTransactionDetails(List<Transaction> transaction) {
		try {
			Set<Long> collect = customerService.getAllCustomers().stream().map(Customer::getCustomerId).collect(Collectors.toSet());
			for(Transaction txn: transaction) {
				if(!collect.contains(txn.getCustomerId())){
					throw new CustomerNotFoundException("Customer with ID : " + txn.getCustomerId() + " does not exist...please verify all transactions");
				}
				if(!(txn.getTransactionId() == null || txn.getTransactionId() == 0)){
					throw new IllegalArgumentException("Please don't provide transactionId...It will be auto-generated & will provide you the same!!");
				}
			}
			return transactionRepository.saveAll(transaction);
		} catch (Exception e) {
			throw new RuntimeException("EXCEPTION: " + e.getMessage(), e);
		}
	}
	
	@Override
	public List<Transaction> getTransactionDetailsByCustomerId(Long customerId) {
		try {
			List<Transaction> byCustomerId = transactionRepository.findByCustomerId(customerId);
			if(byCustomerId.isEmpty()) {
				throw new TransactionNotFoundException("No transactions found for the given customer Id : " + customerId);
			}
			return byCustomerId;
		} catch (Exception e) {
			throw new RuntimeException("EXCEPTION: " + e.getMessage(), e);
		}
		
	}
	
	@Override
	public Transaction getTransactionDetailsByTransactionId(Long transactionId) {
		try {
			Transaction transaction = transactionRepository.findById(transactionId)
					.orElseThrow(()-> new TransactionNotFoundException("No transactions found for the given transaction Id : "+transactionId));
			return transaction;
		} catch (Exception e) {
			throw new RuntimeException("EXCEPTION: " + e.getMessage(), e);
		}
	}
	
	@Override
	public String deleteTransactionDetails(Long transactionId) {
		try {
			transactionRepository.deleteById(transactionId);
			return "Transaction details has been deleted !!!";
		} catch (Exception e) {
			throw new RuntimeException("EXCEPTION: " + e.getMessage(), e);
		}
	}
}

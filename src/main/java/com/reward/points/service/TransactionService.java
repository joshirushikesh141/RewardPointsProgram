package com.reward.points.service;

import java.util.List;
import com.reward.points.entity.Transaction;

/**
 * This is a service interface for managing reward points operations.
 * Provides methods for handling transactions.
 *
 * @see Transaction
 */


public interface TransactionService {
	
	public String saveTransactionDetails(Transaction transaction);
	
	public List<Transaction> saveAllTransactionDetails(List<Transaction> transaction);
	
	public List<Transaction> getTransactionDetailsByCustomerId(Long customerId);
	
	public Transaction getTransactionDetailsByTransactionId(Long transactionId);

	public String deleteTransactionDetails(Long transactionId);
}

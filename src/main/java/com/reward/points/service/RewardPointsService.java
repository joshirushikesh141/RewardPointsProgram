package com.reward.points.service;

import java.util.List;

import com.reward.points.entity.Customer;
import com.reward.points.entity.Transaction;
import com.reward.points.model.Rewards;

/**
 * This is a Service interface for managing reward points operations.
 * Provides methods for handling customer details, transactions, and rewards.
 *
 * @see Customer
 * @see Transaction
 * @see Rewards
 */

public interface RewardPointsService {

	public String saveCustomerDetails(Customer customer);
	
	public Customer getRegisteredCustomerDetailsByCustomerId(Long customerId);

	public List<Transaction> getTransactionDetailsByCustomerId(Long customerId);
	
	public Rewards getRewardsByCustomerId(Long customerId);
	
	public String deleteCustomerDetails(Long customerId);

	public String saveTransactionDetails(Transaction transaction);

	public List<Transaction> saveAllTransactionDetails(List<Transaction> transaction);

	public Transaction getTransactionDetailsByTransactionId(Long transactionId);

	public String deleteTransactionDetails(Long transactionId);
	
	public List<Customer> getAllCustomers();
}

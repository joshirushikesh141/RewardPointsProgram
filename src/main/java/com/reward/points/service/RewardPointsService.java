package com.reward.points.service;

import java.util.List;

import com.reward.points.entity.Customer;
import com.reward.points.entity.Transaction;
import com.reward.points.model.Rewards;

public interface RewardPointsService {

	public String saveCustomerDetails(Customer customer);
	
	public Customer getRegisteredCustomerDetailsByCustomerId(Long customerId);

	public List<Transaction> getTransactionDetailsByCustomerId(Long customerId);
	
	public Rewards getRewardsByCustomerId(Long customerId);

	public String saveTransactionDetails(Transaction transaction);

	public List<Transaction> saveAllTransactionDetails(List<Transaction> transaction);

	public String deleteCustomerDetails(Long customerId);

	public Transaction getTransactionDetailsByTransactionId(Long transactionId);

	public String deleteTransactionDetails(Long transactionId);
}

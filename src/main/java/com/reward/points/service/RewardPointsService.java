package com.reward.points.service;

import java.util.List;

import com.reward.points.entity.Customer;
import com.reward.points.entity.Transaction;
import com.reward.points.model.Rewards;

public interface RewardPointsService {

	public Customer saveCustomerDetails(Customer customer);
	
	public Customer getCustomerDetailsByCustomerId(Long customerId);

	public List<Transaction> getTransactionDetailsByCustomerId(Long customerId);
	
	public Rewards getRewardsByCustomerId(Long customerId);

	public Transaction saveTransactionDetails(Transaction transaction);

	public List<Transaction> saveAllTransactionDetails(List<Transaction> transaction);
}

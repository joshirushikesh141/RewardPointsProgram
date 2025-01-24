package com.reward.points.service;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.reward.points.constants.Constants;
import com.reward.points.entity.Customer;
import com.reward.points.entity.Transaction;
import com.reward.points.exception.CustomerNotFoundException;
import com.reward.points.exception.TransactionNotFoundException;
import com.reward.points.model.Rewards;
import com.reward.points.repository.CustomerRepository;
import com.reward.points.repository.TransactionRepository;

/**
 * This is a Implementation of Service interface.
 * The business logic is written here for customer details, transactions, and rewards.
 *
 * @see Customer
 * @see Transaction
 * @see Rewards
 */

@Service
public class RewardPointsServiceImpl implements RewardPointsService {

	@Autowired
	CustomerRepository customerRepository;

	@Autowired
	TransactionRepository transactionRepository;

	private static final Logger logger = LoggerFactory.getLogger(RewardPointsServiceImpl.class);

	@Override
	public String saveCustomerDetails(Customer customer) {
		Customer save = customerRepository.save(customer);
		return "Customer has been registered/updated successfully!! with customer ID: "+save.getCustomerId();
	}

	@Override
	public String saveTransactionDetails(Transaction transaction) {
		Transaction save = transactionRepository.save(transaction);
		return "Transaction completed successfully!! with transaction ID: "+save.getTransactionId();
	}

	@Override
	public List<Transaction> saveAllTransactionDetails(List<Transaction> transaction) {
		List<Transaction> saveAll = transactionRepository.saveAll(transaction);
		return saveAll;
	}

	@Override
	public Customer getRegisteredCustomerDetailsByCustomerId(Long customerId) {
		Optional<Customer> byId = customerRepository.findById(customerId);
		Customer customer = byId.orElseThrow(()-> new CustomerNotFoundException("Customer Not Found"));
		return customer;
	}

	@Override
	public List<Transaction> getTransactionDetailsByCustomerId(Long customerId) {
		List<Transaction> byCustomerId = transactionRepository.findByCustomerId(customerId);
		return byCustomerId;
	}
	
	@Override
	public String deleteCustomerDetails(Long customerId) {
		customerRepository.deleteById(customerId);
		return "Customer details has been deleted !!!";
	}
	
	@Override
	public Transaction getTransactionDetailsByTransactionId(Long transactionId) {
		Optional<Transaction> findById = transactionRepository.findById(transactionId);
		Transaction transaction = findById.orElseThrow(()-> new TransactionNotFoundException("Transaction not found"));
		return transaction;
	}
	
	@Override
	public String deleteTransactionDetails(Long transactionId) {
		transactionRepository.deleteById(transactionId);
		return "Transaction details has been deleted !!!";
	}

	/**
 	* Below method retrieves the rewards of a customer by their ID.
 	*
 	* @param customerId the ID of the customer whose rewards are to be retrieved
 	* @return the rewards object containing details of the customer's rewards
 	*/
	@Override
	public Rewards getRewardsByCustomerId(Long customerId) {

		Timestamp lastMonthTimestamp = getMonthStartDate(Constants.daysInMonths);
		Timestamp lastSecondMonthTimestamp = getMonthStartDate(2 * Constants.daysInMonths);
		Timestamp lastThirdMonthTimestamp = getMonthStartDate(3 * Constants.daysInMonths);
		logger.info("DAYS :: ----- lastMonth :{}, lastSecondMonth :{}, lastThirdMonth :{}", lastMonthTimestamp,
				lastSecondMonthTimestamp, lastThirdMonthTimestamp);

		List<Transaction> lastMonthTransactions = transactionRepository.findAllByCustomerIdAndTransactionDateBetween(
				customerId, lastMonthTimestamp, Timestamp.from(Instant.now()));
		List<Transaction> lastSecondMonthTransactions = transactionRepository
				.findAllByCustomerIdAndTransactionDateBetween(customerId, lastSecondMonthTimestamp, lastMonthTimestamp);
		List<Transaction> lastThirdMonthTransactions = transactionRepository
				.findAllByCustomerIdAndTransactionDateBetween(customerId, lastThirdMonthTimestamp,
						lastSecondMonthTimestamp);
		logger.info("Transactions :: ----- lastMonth :{}, lastSecondMonth :{}, lastThirdMonth :{}",
				lastMonthTransactions, lastSecondMonthTransactions, lastThirdMonthTransactions);

		Long lastMonthRewardPoints = getRewardsPerMonth(lastMonthTransactions);
		Long lastSecondMonthRewardPoints = getRewardsPerMonth(lastSecondMonthTransactions);
		Long lastThirdMonthRewardPoints = getRewardsPerMonth(lastThirdMonthTransactions);
		logger.info("RewardsPerMonth :: ----- lastMonth :{}, lastSecondMonth :{}, lastThirdMonth :{}",
				lastMonthRewardPoints, lastSecondMonthRewardPoints, lastThirdMonthRewardPoints);

		Long totalRewardPoints = lastMonthRewardPoints + lastSecondMonthRewardPoints + lastThirdMonthRewardPoints;
		logger.info("totalRewardPoints: {}", totalRewardPoints);

		Rewards customerRewards = new Rewards();
		customerRewards.setCustomerId(customerId);
		customerRewards.setLastMonthTimeSpan(lastMonthTimestamp+ " to "+Timestamp.from(Instant.now()));
		customerRewards.setLastMonthRewardPoints(lastMonthRewardPoints);
		customerRewards.setLastSecondMonthTimeSpan(lastSecondMonthTimestamp+" to "+lastMonthTimestamp);
		customerRewards.setLastSecondMonthRewardPoints(lastSecondMonthRewardPoints);
		customerRewards.setLastThirdMonthTimeSpan(lastThirdMonthTimestamp+" to "+lastSecondMonthTimestamp);
		customerRewards.setLastThirdMonthRewardPoints(lastThirdMonthRewardPoints);
		customerRewards.setTotalRewards(totalRewardPoints);
		logger.info("customerRewards Final Object: {}", customerRewards);

		return customerRewards;
	}

	/**
	 * Below Method calculates the start date of a month.
	 *
 	* @param days the number of days to subtract to get the start date
 	* @return the timestamp representing the start date of the month
 	*/
	public Timestamp getMonthStartDate(int days) {
		return Timestamp.valueOf(LocalDateTime.now().minusDays(days));
	}

	/**
 	* Below method calculates the rewards for a list of transactions in a month.
 	*
 	* @param transactions is the list of transactions of a perticular month
 	* @return the total rewards for the month
 	*/
	public Long getRewardsPerMonth(List<Transaction> transactions) {
		long totalRewards = 0;
		for (Transaction transaction : transactions) {
			totalRewards += calculateRewards(transaction.getTransactionAmount());
		}
		return totalRewards;
	}

	/**
 	* Below method is to calculate the rewards based on the transaction amount.
 	*
 	* @param transactionAmount the amount of the transaction
 	* @return the calculated rewards for the transaction
 	* @throws IllegalArgumentException if the transaction amount is negative
 	*/
	public Long calculateRewards(Double transactionAmount) {
		if (transactionAmount < 0) {
			throw new IllegalArgumentException("Transaction amount cannot be negative");
		}
		// less then or equal to 50$
		if (transactionAmount <= Constants.firstRewardLimit)
			return 0L;
		// less then or equal to 100$
		if (transactionAmount <= Constants.secondRewardLimit)
			return Math.round(transactionAmount - Constants.firstRewardLimit);
		// greater then then 100$
		return Math.round((transactionAmount - Constants.secondRewardLimit) * 2
				+ (Constants.secondRewardLimit - Constants.firstRewardLimit));
	}

	@Override
	public List<Customer> getAllCustomers() {
		List<Customer> all = customerRepository.findAll();
		return all;
	}
}

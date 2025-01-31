package com.reward.points.service;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.reward.points.constants.Constants;
import com.reward.points.entity.Transaction;
import com.reward.points.model.Rewards;
import com.reward.points.repository.TransactionRepository;

/**
 * This is an implementation class of rewards service interface.
 * The business logic is written here for rewards.
 *
 * @see Transaction
 * @see Rewards
 * @see TransactionRepository
 */

@Service
public class RewardsServiceImpl implements RewardsService {
	
	@Autowired
	TransactionRepository transactionRepository;

	private static final Logger logger = LoggerFactory.getLogger(RewardsServiceImpl.class);
	
	/**
 	* Below method retrieves the rewards of a customer by their ID.
 	* @param customerId the ID of the customer whose rewards are to be retrieved
 	* @return the rewards object containing details of the customer's rewards
 	*/
	@Override
	public Rewards getRewardsByCustomerId(Long customerId) {
		try {
			// Respective Month Start Date
			Timestamp lastMonthStartDate = getMonthStartDate(Constants.daysInMonths);
			Timestamp secondLastMonthStartDate = getMonthStartDate(2 * Constants.daysInMonths);
			Timestamp thirdLastMonthStartDate = getMonthStartDate(3 * Constants.daysInMonths);
			logger.info("DAYS :: ----- lastMonth :{}, lastSecondMonth :{}, lastThirdMonth :{}", lastMonthStartDate,
					secondLastMonthStartDate, thirdLastMonthStartDate);
	
			
			// Respective Month Transaction List Against CustomerId 
			List<Transaction> lastMonthTransactions = transactionRepository.findAllByCustomerIdAndTransactionDateBetween(
					customerId, lastMonthStartDate, Timestamp.from(Instant.now()));
			List<Transaction> secondLastMonthTransactions = transactionRepository
					.findAllByCustomerIdAndTransactionDateBetween(customerId, secondLastMonthStartDate, lastMonthStartDate);
			List<Transaction> thirdLastMonthTransactions = transactionRepository
					.findAllByCustomerIdAndTransactionDateBetween(customerId, thirdLastMonthStartDate,
							secondLastMonthStartDate);
			logger.info("Transactions :: ----- lastMonth :{}, lastSecondMonth :{}, lastThirdMonth :{}",
					lastMonthTransactions, secondLastMonthTransactions, thirdLastMonthTransactions);
	
			
			// Respective Month-wise Rewards
			Long lastMonthRewardPoints = getRewardsPerMonth(lastMonthTransactions);
			Long secondLastMonthRewardPoints = getRewardsPerMonth(secondLastMonthTransactions);
			Long thirdLastMonthRewardPoints = getRewardsPerMonth(thirdLastMonthTransactions);
			logger.info("RewardsPerMonth :: ----- lastMonth :{}, lastSecondMonth :{}, lastThirdMonth :{}",
					lastMonthRewardPoints, secondLastMonthRewardPoints, thirdLastMonthRewardPoints);
	
			
			// Total Rewards Calculated
			Long totalRewardPoints = lastMonthRewardPoints + secondLastMonthRewardPoints + thirdLastMonthRewardPoints;
			logger.info("totalRewardPoints: {}", totalRewardPoints);
	
			
			// Rewards object to show data against customer 
			Rewards customerRewards = new Rewards();
			customerRewards.setCustomerId(customerId);
			customerRewards.setLastMonthTimeSpan(lastMonthStartDate+ " to "+Timestamp.from(Instant.now()));
			customerRewards.setLastMonthRewardPoints(lastMonthRewardPoints);
			customerRewards.setLastSecondMonthTimeSpan(secondLastMonthStartDate+" to "+lastMonthStartDate);
			customerRewards.setLastSecondMonthRewardPoints(secondLastMonthRewardPoints);
			customerRewards.setLastThirdMonthTimeSpan(thirdLastMonthStartDate+" to "+secondLastMonthStartDate);
			customerRewards.setLastThirdMonthRewardPoints(thirdLastMonthRewardPoints);
			customerRewards.setTotalRewards(totalRewardPoints);
			logger.info("customerRewards Final Object: {}", customerRewards);
	
			return customerRewards;
		}catch(Exception e) {
			 throw new RuntimeException("EXCEPTION in getRewardsByCustomerId() : " + e.getMessage(), e);
		}
	}

	
	
	/**
	* Below Method calculates the start date of a month.
 	* @param days the number of days to subtract to get the start date
 	* @return the timestamp representing the start date of the month
 	*/
	public Timestamp getMonthStartDate(int days) {
		return Timestamp.valueOf(LocalDateTime.now().minusDays(days));
	}

	
	
	/**
 	* Below method calculates the rewards for a list of transactions in a month.
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
}

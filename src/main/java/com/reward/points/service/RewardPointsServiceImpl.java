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

@Service
public class RewardPointsServiceImpl implements RewardPointsService{

	@Autowired
	TransactionRepository transactionRepository;
	
	 private static final Logger logger = LoggerFactory.getLogger(RewardPointsServiceImpl.class);

	@Override
	public Rewards getRewardsByCustomerId(Long customerId) {

		Timestamp lastMonthTimestamp = getMonthStartDate(Constants.daysInMonths);
		Timestamp lastSecondMonthTimestamp = getMonthStartDate(2*Constants.daysInMonths);
		Timestamp lastThirdMonthTimestamp = getMonthStartDate(3*Constants.daysInMonths);
		logger.info("DAYS :: ----- lastMonth :{}, lastSecondMonth :{}, lastThirdMonth :{}",lastMonthTimestamp,lastSecondMonthTimestamp,lastThirdMonthTimestamp);

		List<Transaction> lastMonthTransactions = transactionRepository.findAllByCustomerIdAndTransactionDateBetween(customerId, lastMonthTimestamp, Timestamp.from(Instant.now()));
		List<Transaction> lastSecondMonthTransactions = transactionRepository.findAllByCustomerIdAndTransactionDateBetween(customerId, lastSecondMonthTimestamp, lastMonthTimestamp);
		List<Transaction> lastThirdMonthTransactions = transactionRepository.findAllByCustomerIdAndTransactionDateBetween(customerId, lastThirdMonthTimestamp,lastSecondMonthTimestamp);
		logger.info("Transactions :: ----- lastMonth :{}, lastSecondMonth :{}, lastThirdMonth :{}",lastMonthTransactions,lastSecondMonthTransactions,lastThirdMonthTransactions);

		Long lastMonthRewardPoints = getRewardsPerMonth(lastMonthTransactions);
		Long lastSecondMonthRewardPoints = getRewardsPerMonth(lastSecondMonthTransactions);
		Long lastThirdMonthRewardPoints = getRewardsPerMonth(lastThirdMonthTransactions);
		logger.info("RewardsPerMonth :: ----- lastMonth :{}, lastSecondMonth :{}, lastThirdMonth :{}",lastMonthRewardPoints,lastSecondMonthRewardPoints,lastThirdMonthRewardPoints);
		
		Long totalRewardPoints = lastMonthRewardPoints + lastSecondMonthRewardPoints + lastThirdMonthRewardPoints;
		logger.info("totalRewardPoints: {}",totalRewardPoints);
		
		Rewards customerRewards = new Rewards();
		customerRewards.setCustomerId(customerId);
		customerRewards.setLastMonthRewardPoints(lastMonthRewardPoints);
		customerRewards.setLastSecondMonthRewardPoints(lastSecondMonthRewardPoints);
		customerRewards.setLastThirdMonthRewardPoints(lastThirdMonthRewardPoints);
		customerRewards.setTotalRewards(totalRewardPoints);
		logger.info("customerRewards Final Object: {}",customerRewards);
		
		return customerRewards;
	}

	public Timestamp getMonthStartDate(int days) {
		return Timestamp.valueOf(LocalDateTime.now().minusDays(days));
	}
	
	public Long getRewardsPerMonth(List<Transaction> transactions) {
	    long totalRewards = 0;
	    for (Transaction transaction : transactions) {
	        totalRewards += calculateRewards(transaction.getTransactionAmount());
	    }
	    return totalRewards;
	}

	public Long calculateRewards(Double transactionAmount) {
	    if (transactionAmount < 0) {
	        throw new IllegalArgumentException("Transaction amount cannot be negative");
	    } 
		// less then or equal to 50$
	    if (transactionAmount <= Constants.firstRewardLimit) return 0L;
	    // less then or equal to 100$
	    if (transactionAmount <= Constants.secondRewardLimit) return Math.round(transactionAmount - Constants.firstRewardLimit);
	    // greater then then 100$
	    return Math.round((transactionAmount - Constants.secondRewardLimit) * 2 + (Constants.secondRewardLimit - Constants.firstRewardLimit));
	}
	
}

package com.reward.points.service;

import com.reward.points.model.Rewards;

/**
 * This is a rewards service interface for managing reward points operations.
 * Provides methods for handling rewards.
 *
 * @see Rewards
 */

public interface RewardsService {
	
	public Rewards getRewardsByCustomerId(Long customerId);

}

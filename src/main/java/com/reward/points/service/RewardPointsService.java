package com.reward.points.service;

import com.reward.points.model.Rewards;

public interface RewardPointsService {
	public Rewards getRewardsByCustomerId(Long customerId);
}

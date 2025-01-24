package com.reward.points.model;

/**
This class represents a Rewards model with basic attributes. 
*/
public class Rewards {
	private long customerId;
	private String lastMonthTimeSpan;
	private long lastMonthRewardPoints;
	private String lastSecondMonthTimeSpan;
    private long lastSecondMonthRewardPoints;
    private String lastThirdMonthTimeSpan;
    private long lastThirdMonthRewardPoints;
    private long totalRewards;
	public long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}
	public String getLastMonthTimeSpan() {
		return lastMonthTimeSpan;
	}
	public void setLastMonthTimeSpan(String lastMonthTimeSpan) {
		this.lastMonthTimeSpan = lastMonthTimeSpan;
	}
	public long getLastMonthRewardPoints() {
		return lastMonthRewardPoints;
	}
	public void setLastMonthRewardPoints(long lastMonthRewardPoints) {
		this.lastMonthRewardPoints = lastMonthRewardPoints;
	}
	public String getLastSecondMonthTimeSpan() {
		return lastSecondMonthTimeSpan;
	}
	public void setLastSecondMonthTimeSpan(String lastSecondMonthTimeSpan) {
		this.lastSecondMonthTimeSpan = lastSecondMonthTimeSpan;
	}
	public long getLastSecondMonthRewardPoints() {
		return lastSecondMonthRewardPoints;
	}
	public void setLastSecondMonthRewardPoints(long lastSecondMonthRewardPoints) {
		this.lastSecondMonthRewardPoints = lastSecondMonthRewardPoints;
	}
	public String getLastThirdMonthTimeSpan() {
		return lastThirdMonthTimeSpan;
	}
	public void setLastThirdMonthTimeSpan(String lastThirdMonthTimeSpan) {
		this.lastThirdMonthTimeSpan = lastThirdMonthTimeSpan;
	}
	public long getLastThirdMonthRewardPoints() {
		return lastThirdMonthRewardPoints;
	}
	public void setLastThirdMonthRewardPoints(long lastThirdMonthRewardPoints) {
		this.lastThirdMonthRewardPoints = lastThirdMonthRewardPoints;
	}
	public long getTotalRewards() {
		return totalRewards;
	}
	public void setTotalRewards(long totalRewards) {
		this.totalRewards = totalRewards;
	}
	public Rewards() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Rewards(long customerId, String lastMonthTimeSpan, long lastMonthRewardPoints, String lastSecondMonthTimeSpan,
			long lastSecondMonthRewardPoints, String lastThirdMonthTimeSpan, long lastThirdMonthRewardPoints,
			long totalRewards) {
		super();
		this.customerId = customerId;
		this.lastMonthTimeSpan = lastMonthTimeSpan;
		this.lastMonthRewardPoints = lastMonthRewardPoints;
		this.lastSecondMonthTimeSpan = lastSecondMonthTimeSpan;
		this.lastSecondMonthRewardPoints = lastSecondMonthRewardPoints;
		this.lastThirdMonthTimeSpan = lastThirdMonthTimeSpan;
		this.lastThirdMonthRewardPoints = lastThirdMonthRewardPoints;
		this.totalRewards = totalRewards;
	}
	@Override
	public String toString() {
		return "Rewards [customerId=" + customerId + ", lastMonthTimeSpan=" + lastMonthTimeSpan
				+ ", lastMonthRewardPoints=" + lastMonthRewardPoints + ", lastSecondMonthTimeSpan="
				+ lastSecondMonthTimeSpan + ", lastSecondMonthRewardPoints=" + lastSecondMonthRewardPoints
				+ ", lastThirdMonthTimeSpan=" + lastThirdMonthTimeSpan + ", lastThirdMonthRewardPoints="
				+ lastThirdMonthRewardPoints + ", totalRewards=" + totalRewards + "]";
	}
    
}

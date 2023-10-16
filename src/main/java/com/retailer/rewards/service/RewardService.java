package com.retailer.rewards.service;

import com.retailer.rewards.model.Rewards;



public interface RewardService {
    public Rewards getRewardsByCustomerId(Long customerId);
}

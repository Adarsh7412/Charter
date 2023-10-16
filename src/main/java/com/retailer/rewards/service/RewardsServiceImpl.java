package com.retailer.rewards.service;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.retailer.rewards.Enum.GenericConstantEnum;
import com.retailer.rewards.entity.Transaction;
import com.retailer.rewards.model.Rewards;
import com.retailer.rewards.repository.TransactionRepository;

@Service
public class RewardsServiceImpl implements RewardService {

	
	@Autowired
	TransactionRepository transactionRepository;

	public Rewards getRewardsByCustomerId(Long customerId) {

		Timestamp lastMonthTimestamp = getDateBasedOnOffSetDays(GenericConstantEnum.DaysInMonths);
		Timestamp lastSecondMonthTimestamp = getDateBasedOnOffSetDays(2*GenericConstantEnum.DaysInMonths);
		Timestamp lastThirdMonthTimestamp = getDateBasedOnOffSetDays(3*GenericConstantEnum.DaysInMonths);

		List<Transaction> lastMonthTransactions = transactionRepository.findAllByCustomerIdAndTransactionDateBetween(
				customerId, lastMonthTimestamp, Timestamp.from(Instant.now()));
		List<Transaction> lastSecondMonthTransactions = transactionRepository
				.findAllByCustomerIdAndTransactionDateBetween(customerId, lastSecondMonthTimestamp, lastMonthTimestamp);
		List<Transaction> lastThirdMonthTransactions = transactionRepository
				.findAllByCustomerIdAndTransactionDateBetween(customerId, lastThirdMonthTimestamp,
						lastSecondMonthTimestamp);

		Long lastMonthRewardPoints = getRewardsPerMonth(lastMonthTransactions);
		Long lastSecondMonthRewardPoints = getRewardsPerMonth(lastSecondMonthTransactions);
		Long lastThirdMonthRewardPoints = getRewardsPerMonth(lastThirdMonthTransactions);

		Rewards customerRewards = new Rewards();
		customerRewards.setCustomerId(customerId);
		customerRewards.setLastMonthRewardPoints(lastMonthRewardPoints);
		customerRewards.setLastSecondMonthRewardPoints(lastSecondMonthRewardPoints);
		customerRewards.setLastThirdMonthRewardPoints(lastThirdMonthRewardPoints);
		customerRewards.setTotalRewards(lastMonthRewardPoints + lastSecondMonthRewardPoints + lastThirdMonthRewardPoints);

		return customerRewards;

	}

	private Long getRewardsPerMonth(List<Transaction> transactions) {
		return transactions.stream().map(transaction -> calculateRewards(transaction))
				.collect(Collectors.summingLong(r -> r.longValue()));
	}

	private Long calculateRewards(Transaction t) {
		if (t.getTransactionAmount() > GenericConstantEnum.FirstRewardLimit && t.getTransactionAmount() <= GenericConstantEnum.SecondRewardLimit) {
			return Math.round(t.getTransactionAmount() - GenericConstantEnum.FirstRewardLimit);
		} else if (t.getTransactionAmount() > GenericConstantEnum.SecondRewardLimit) {
			return Math.round(t.getTransactionAmount() - GenericConstantEnum.SecondRewardLimit) * 2
					+ (GenericConstantEnum.SecondRewardLimit - GenericConstantEnum.FirstRewardLimit);
		} else
			return 0l;

	}

	public Timestamp getDateBasedOnOffSetDays(int days) {
		return Timestamp.valueOf(LocalDateTime.now().minusDays(days));
	}

}

package com.retailer.rewards.controller;

import com.retailer.rewards.entity.Customer;
import com.retailer.rewards.model.Rewards;
import com.retailer.rewards.repository.CustomerRepository;
import com.retailer.rewards.service.RewardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("customer")
@RestController
public class RewardController {

    @Autowired
    RewardService rewardService;

    @Autowired
    CustomerRepository customerRepository;


    public ResponseEntity<Rewards> getRewardsByCustomerId(@PathVariable("customerId") Long customerId){

        Customer customer = .findById();
        Rewards customerRewards = rewardService.getRewardsByCustomerId(customerId);
        return new ResponseEntity<>(customerRewards, HttpStatus.OK);
    }

}

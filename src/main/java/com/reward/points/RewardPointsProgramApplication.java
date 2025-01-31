package com.reward.points;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(basePackages="com.reward.points.repository")
@EntityScan(basePackages="com.reward.points.entity")
@SpringBootApplication
public class RewardPointsProgramApplication {

	public static void main(String[] args) {
		SpringApplication.run(RewardPointsProgramApplication.class, args);
	}

}

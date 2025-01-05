package com.reward.points.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.reward.points.entity.Customer;
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long>{
}

package com.reward.points.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for performing CRUD operations on the Customer entity.
 * Extends JpaRepository to provide methods for working with Customer.
 * 
 * @see JpaRepository
 * @see Customer
 */

import com.reward.points.entity.Customer;
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long>{
}

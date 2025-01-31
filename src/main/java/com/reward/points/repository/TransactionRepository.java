package com.reward.points.repository;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.reward.points.entity.Transaction;

/**
 * Repository interface for performing CRUD operations on the Transaction entity.
 * Extends JpaRepository to provide methods for working with Transaction.
 * 
 * @see JpaRepository
 * @see Transaction
 */

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
	List<Transaction> findByCustomerId(Long customerId);
	public List<Transaction> findAllByCustomerIdAndTransactionDateBetween(Long customerId, Timestamp from,Timestamp to);
}

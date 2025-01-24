package com.reward.points.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
*This class represents a Transaction entity with basic attributes. 
*/
@Entity
@Table(name = "TRANSACTION")
public class Transaction {
    /**
    transactionId is a unique identifier and primery key
    */
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    @Column(name = "TRANSACTION_ID")
    private Long transactionId;

    /**
    customerId is a ID of the customer and foreignkey of transaction
    */
    @Column(name="CUSTOMER_ID")
    private Long customerId;

    /**
    transactionDate is a date of transaction done
    */
    @Column(name = "TRANSACTION_DATE")
    private Date transactionDate;

    /**
    transactionAmount is an amount of transaction done
    */
    @Column(name = "AMOUNT")
    private Double transactionAmount;

	public Long getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(Long transactionId) {
		this.transactionId = transactionId;
	}
	public Long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}
	public Date getTransactionDate() {
		return transactionDate;
	}
	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}
	public Double getTransactionAmount() {
		return transactionAmount;
	}
	public void setTransactionAmount(Double transactionAmount) {
		this.transactionAmount = transactionAmount;
	}

	public Transaction(Long transactionId, Long customerId, Date transactionDate, Double transactionAmount) {
		super();
		this.transactionId = transactionId;
		this.customerId = customerId;
		this.transactionDate = transactionDate;
		this.transactionAmount = transactionAmount;
	}
	public Transaction() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "Transaction [transactionId=" + transactionId + ", customerId=" + customerId + ", transactionDate="
				+ transactionDate + ", transactionAmount=" + transactionAmount + "]";
	}
    
}

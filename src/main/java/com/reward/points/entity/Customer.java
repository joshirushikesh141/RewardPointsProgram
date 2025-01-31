package com.reward.points.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


/**
*This class represents a Customer entity with basic attributes. 
*/
@Entity
@Table(name = "CUSTOMER")
public class Customer {
    /**
     customerId is a unique identifier and primery key
    */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CUSTOMER_ID")
    private Long customerId;
	
    /**
     customerName is a name of the customer
    */
    @Column(name = "CUSTOMER_NAME")
    private String customerName;

     /**
     emailId is an email of the customer
    */
    @Column(name = "EMAIL_ID")
    private String emailId;

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public Customer() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Customer(Long customerId, String customerName, String emailId) {
		super();
		this.customerId = customerId;
		this.customerName = customerName;
		this.emailId = emailId;
	}

	@Override
	public String toString() {
		return "Customer [customerId=" + customerId + ", customerName=" + customerName + ", emailId=" + emailId + "]";
	}
}

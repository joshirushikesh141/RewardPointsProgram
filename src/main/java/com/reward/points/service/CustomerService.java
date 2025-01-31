package com.reward.points.service;

import java.util.List;

import com.reward.points.entity.Customer;

/**
 * This is a customer service interface for managing reward points operations.
 * Provides methods for handling customer.
 *
 * @see Customer
 */

public interface CustomerService {
	
	public String saveCustomerDetails(Customer customer);
	
	public Customer getRegisteredCustomerDetailsByCustomerId(Long customerId);
	
	public String deleteCustomerDetails(Long customerId);
	
	public List<Customer> getAllCustomers();
}

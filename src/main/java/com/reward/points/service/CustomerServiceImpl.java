package com.reward.points.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.reward.points.entity.Customer;
import com.reward.points.exception.CustomerNotFoundException;
import com.reward.points.repository.CustomerRepository;

/**
 * This is an implementation class of customer service interface.
 * The business logic is written here for customer.
 *
 * @see Customer
 * @see CustomerRepository
 */

@Service
public class CustomerServiceImpl implements CustomerService {
	
	@Autowired
	CustomerRepository customerRepository;

	@Override
	public String saveCustomerDetails(Customer customer) {
		try {
			Customer save = customerRepository.save(customer);
			return "Customer has been registered/updated successfully!! with customer ID: "+save.getCustomerId();
		} catch (Exception e) {
			 throw new RuntimeException("EXCEPTION: " + e.getMessage(), e);
		}
	}

	@Override
	public Customer getRegisteredCustomerDetailsByCustomerId(Long customerId) {
		try {
			Customer customer = customerRepository.findById(customerId)
					.orElseThrow(()-> new CustomerNotFoundException("Customer Not Found, Please Register First!!"));
			return customer;
		} catch (Exception e) {
			 throw new RuntimeException("EXCEPTION: " + e.getMessage(), e);
		}	
	}
	
	@Override
	public String deleteCustomerDetails(Long customerId) {
		try {
			customerRepository.deleteById(customerId);
			return "Customer details has been deleted !!!";
		} catch (Exception e) {
			throw new RuntimeException("EXCEPTION: " + e.getMessage(), e);
		}
		
	}
	
	@Override
	public List<Customer> getAllCustomers() {
		try {
			List<Customer> customerList = customerRepository.findAll();
			if(customerList.isEmpty()) {
				throw new CustomerNotFoundException("No customers found, Please Register First!!");
			}
			return customerList;
		} catch (Exception e) {
			throw new RuntimeException("EXCEPTION: " + e.getMessage(), e);
		}
		
	}
}

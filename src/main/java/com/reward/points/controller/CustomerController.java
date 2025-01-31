package com.reward.points.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.reward.points.entity.Customer;
import com.reward.points.service.CustomerService;

/**
 * CustomerController class for handling customer related HTTP requests.
 * Provides endpoints for creating, retrieving, updating, and deleting the details.
 * 
 * @see customerService
 */

@RestController
@RequestMapping("/api/customer")
public class CustomerController {
	
	@Autowired
	CustomerService customerService;
	
    /**
    * Save Customer Details -- Handles the customer registration process.
    * @param customer The customer object received from the request body.
    * @return ResponseEntity with a success message and HTTP status OK if the customer details are successfully saved.
    */
    @PostMapping("/customerRegistration")
    public ResponseEntity<String> customerRegistration(@RequestBody Customer customer){
    	if(customer.getCustomerId() == null || customer.getCustomerId() == 0) {
    		return new ResponseEntity<>(customerService.saveCustomerDetails(customer),HttpStatus.OK);
		}else {
			throw new IllegalArgumentException("Please don't provide customerId...It will be auto-generated & will provide you the same!!");
		}
    }
	
    /**
    * GetRegistered Customer Details By customerId -- retrives the registered customer data against customer id.
    * @param customerId The customerId received from the path variable.
    * @return ResponseEntity with a customer object and HTTP status OK if the customer details are successfully fetched.
    */
    @GetMapping("/getRegisteredCustomerDetailsById/{customerId}")
    public ResponseEntity<Customer> getRegisteredCustomerDetailsByCustomerId(@PathVariable Long customerId){
        return new ResponseEntity<>(customerService.getRegisteredCustomerDetailsByCustomerId(customerId),HttpStatus.OK);
    }

    /**
    * Update Customer Details By customerId -- previously added customer data can be updated.
    * @param customer The customer object received from the request body.
    * @param customerId The customerId received from the path variable.
    * @return ResponseEntity with a success message and HTTP status OK if the customer details are successfully updated.
    */
    @PutMapping("/updateCustomerDetails/{customerId}")
    public ResponseEntity<String> updateCustomerDetails(@RequestBody Customer customer,@PathVariable Long customerId){
    	String saveCustomerDetails ="";
		Customer customerDb = customerService.getRegisteredCustomerDetailsByCustomerId(customerId); // If customer Id not found will through exception
    	if(customerDb != null) {
    		customerDb.setCustomerName(customer.getCustomerName());
    		saveCustomerDetails = customerService.saveCustomerDetails(customerDb);
    	}
    	return new ResponseEntity<>(saveCustomerDetails,HttpStatus.OK);
    }

    /**
    * Delete Customer Details By customerId -- previously added customer data can be deleted -- not recommended to delete.
    * @param customerId The customerId received from the path variable.
    * @return ResponseEntity with a success message and HTTP status OK if the customer details are successfully deleted.
    */
    @DeleteMapping("/deleteCustomerDetails/{customerId}")
    public ResponseEntity<String> deleteCustomerDetails(@PathVariable Long customerId){
    	String deleteCustomer = "";
		Customer customerObj = customerService.getRegisteredCustomerDetailsByCustomerId(customerId); // If customer Id not found will through exception
    	if(customerObj != null) {
    		deleteCustomer = customerService.deleteCustomerDetails(customerId);
    	}
    	return new ResponseEntity<>(deleteCustomer,HttpStatus.OK);
    }
}

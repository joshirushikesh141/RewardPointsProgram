package com.reward.points.exception;

/**
 * Exception thrown when a customer is not found.
 * This class extends RuntimeException and provides a custom exception message.
 *
 * @see RuntimeException
 */
@SuppressWarnings("serial")
public class CustomerNotFoundException extends RuntimeException{

	public CustomerNotFoundException(String message) {
		super(message);
	}

}

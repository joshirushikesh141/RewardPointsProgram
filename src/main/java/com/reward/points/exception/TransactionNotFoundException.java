package com.reward.points.exception;

/**
 * Exception thrown when a Transaction is not found.
 * This class extends RuntimeException and provides a custom exception message.
 *
 * @see RuntimeException
 */
@SuppressWarnings("serial")
public class TransactionNotFoundException extends RuntimeException {

	public TransactionNotFoundException(String message) {
		super(message);
	}

}

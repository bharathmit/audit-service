package com.audit.app.exception.response;

import lombok.Getter;

public enum ErrorDescription {
	
	TRANSACTION_FAILED("your transaction is failed"),
	TRANSACTION_SUCCESS("your transaction is successful"),
	DATA_ACCESS("Data Access Exception"),
	
	
	
	USER_EXIT("Email Id OR Mobile Number Already Exit"),
	USER_NOT_EXIT("Email Id OR Mobile Number Not Registered"),
	USER_ACCOUNT_ACTIVE("This email already activated"),
	USER_ACCOUNT_BLOCKED("This email id is blocked"),
	
	INVALID_USER("Incorrect UserName"),
	INVALID_PASSWORD("Incorrect Password"),
	MOBILE_EXIT("Mobile Number Already Exit"),
	INVALID_TOKEN("Incoorrect Registration confirmation Token"),
	TIME_OUT("Registration confirmation Token Expiryed"),
	
	;
	
	@Getter
    private String message;

    private ErrorDescription(String message) {
        this.message = message;
    }

}

package com.audit.app.exception.response;

import lombok.Getter;

public enum ErrorDescription {
	
	TRANSACTION_FAILED("your transaction is failed"),
	TRANSACTION_SUCCESS("your transaction is successful"),
	DATA_ACCESS("Data Access Exception"),
	 
	
	
	INVALID_USER("Incorrect UserName"),
	INVALID_PASSWORD("Incorrect Password"),
	USER_EXIT("Email Id OR Mobile Number Already Exit"),
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
